package genericIA3D.sim

import genericIA3D.app.GenericIA3D
import genericIA3D.app.SimOptions
import genericIA3D.agents.GenericAgent
import genericIA3D.forces.base.FlowForces
import genericIA3D.forces.base.ForcesPrimitive
import toxi.geom.Matrix4x4
import toxi.geom.Vec3D
import java.util.*
import java.util.function.Consumer
import kotlin.collections.ArrayList

class GenericIASimulation internal constructor(
        val so: SimOptions,
        val parent: GenericIA3D
) {
    val groups: MutableList<List<GenericAgent>> = ArrayList()
    private val gfx: Graphics = Graphics(parent, this)
    var lastKeyAction = ""

    // Groups
    fun addGroup(size: Int) {
        val rand = Random()
        val agents: MutableList<GenericAgent> = ArrayList()
        val gradients: FlowForces = FlowForces()

        for (i in 0 until size) {
            val pos = Vec3D(rand.nextFloat() * so.DIM, rand.nextFloat() * so.DIM, rand.nextFloat() * so.DIM)
            val vel = Vec3D(rand.nextFloat() * (so.MAX_SPEED / 2 - so.MAX_SPEED / 2) - so.MAX_SPEED / 2,
                    rand.nextFloat() * (so.MAX_SPEED / 2 - so.MAX_SPEED / 2 - so.MAX_SPEED / 2),
                    rand.nextFloat() * (so.MAX_SPEED / 2 - so.MAX_SPEED / 2 - so.MAX_SPEED / 2))
            val c = GenericAgent(pos, vel, this, gradients)
            agents.add(c)
        }
        groups.add(agents)
    }

    fun removeGroup(index: Int) {
        groups.removeAt(index)
        so.forcesList.removeAt(index)
    }

    private fun updateGroup(group_n: Int, forces: ForcesPrimitive, colour: Matrix4x4) {
        if (!so.PAUSE)
            groups[group_n].parallelStream().forEach {
                it.applyForces(groups[group_n], forces)
            }

        groups[group_n].forEach {
            if (so.WRAPPING) it.wrap() // TODO: move to GIAS?
            gfx.showAgent(it, group_n, colour)
        }
    }

    private fun updateGroups(colour: Matrix4x4) {
        for (i in groups.indices) {
            updateGroup(i, so.forcesList[i], colour)
        }
    }

    fun drawFrame() {
        updateGroups(so.colorMatrix)
        gfx.drawCamera()
    }

    init {
        so.forcesList.forEach(Consumer { addGroup(so.AGENT_COUNT) }) // TODO: Update to add individual group sizes
    }
}