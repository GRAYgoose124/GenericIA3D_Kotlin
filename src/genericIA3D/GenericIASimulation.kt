package genericIA3D

import toxi.geom.Matrix4x4
import toxi.geom.Vec3D
import java.util.*
import java.util.function.Consumer

class GenericIASimulation internal constructor(
        val so: SimOptions,
        val parent: GenericIA3D) {


    val groups: MutableList<List<GenericAgent>>
    private val gfx: Graphics = Graphics(parent, this)
    var lastKeyAction = ""

    // Groups
    fun addGroup(size: Int) {
        val rand = Random()
        val agents: MutableList<GenericAgent> = ArrayList()
        for (i in 0 until size) {
            val pos = Vec3D(rand.nextFloat() * so.DIM, rand.nextFloat() * so.DIM, rand.nextFloat() * so.DIM)
            val vel = Vec3D(rand.nextFloat() * (so.MAX_SPEED / 2 - so.MAX_SPEED / 2) - so.MAX_SPEED / 2,
                    rand.nextFloat() * (so.MAX_SPEED / 2 - so.MAX_SPEED / 2 - so.MAX_SPEED / 2),
                    rand.nextFloat() * (so.MAX_SPEED / 2 - so.MAX_SPEED / 2 - so.MAX_SPEED / 2))
            val c = GenericAgent(pos, vel, this)
            agents.add(c)
        }
        groups.add(agents)
    }

    fun removeGroup(index: Int) {
        groups.removeAt(index)
        so.forces_list.removeAt(index)
    }

    fun updateGroup(group_n: Int, forces: Forces, gfx_type: String, colour: Matrix4x4) {
        groups[group_n].forEach(Consumer { a: GenericAgent ->
            if (!so.PAUSE) a.apply_forces(groups[group_n], forces, so.WRAPPING)
            gfx.showAgent(a, group_n, gfx_type, colour)
        })
    }

    fun updateGroups(colour: Matrix4x4) {
        for (i in groups.indices) {
            updateGroup(i, so.forces_list[i], so.GFX_TYPE, colour)
        }
    }

    // Graphics
    fun drawFrame() {
        updateGroups(so.colorMatrix)
        gfx.drawCamera()
    }

    init {
        // Init Handlers

        // Init Sim
        groups = ArrayList()
        so.forces_list.forEach(Consumer { addGroup(so.AGENT_COUNT) })
    }
}