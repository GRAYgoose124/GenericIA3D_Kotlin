package genericIA3D.agents

import genericIA3D.sim.GenericIASimulation
import genericIA3D.forces.base.FlowForces
import genericIA3D.forces.base.ForcesPrimitive
import toxi.geom.Vec3D
import kotlin.collections.ArrayList

open class GenericAgent(var position: Vec3D,
                        var velocity: Vec3D,
                        private val parent_sim: GenericIASimulation, var forceNeighbors: ForcesPrimitive) {
    var acceleration: Vec3D
    var trail: MutableList<Vec3D>
    var lastForces: MutableList<Vec3D>
    var lastNeighbors: MutableMap<GenericAgent, Float>
    var wrapped = false

    // Forces
    fun applyForces(group: List<GenericAgent>, forces: ForcesPrimitive) { // (forces)
        acceleration = Vec3D()
        lastForces = ArrayList()
        lastNeighbors = neighborhood(group)

        if (lastNeighbors.isNotEmpty())
            for (i in forces.ops.indices) {
                if (!parent_sim.so.FORCES_ON[i]) continue
                val force = forces.ops[i](this, lastNeighbors)
                        .scale(1f / lastNeighbors.size)
                        .normalizeTo(parent_sim.so.MAX_SPEED * forces.strengths!![i]) // TODO: force norming/neighboring is still borked.
                lastForces.add(force)
                acceleration.addSelf(force)
            }

        velocity.addSelf(acceleration)
        velocity.normalizeTo(parent_sim.so.MAX_SPEED)

        position.addSelf(velocity)

        if (parent_sim.so.TRAILS) updateTrail()
    }

    // Helpers
    private fun updateTrail() {
        if (trail[trail.size - 1].sub(position).magSquared() > parent_sim.so.TRAIL_SEG_LENGTH)
            trail.add(Vec3D(position))

        if (trail.size > 2 * parent_sim.so.MAX_TRAIL_SEGMENTS)
            trail.removeAt(0)
    }

    private fun neighborhood(group: List<GenericAgent>): MutableMap<GenericAgent, Float> {
        val neighbors: MutableMap<GenericAgent, Float> = mutableMapOf()

        group.forEach { neighbor: GenericAgent ->
            val distSquared = position.distanceToSquared(neighbor.position)
            if (distSquared < parent_sim.so.NEIGHBORHOOD_SIZE_SQUARED) {
                neighbors[neighbor] = distSquared
            }
        }
        return neighbors
    }

    fun wrap() {
        val dim = parent_sim.so.DIM.toFloat() // TODO: <=/>= mostly? fixes wrapping neighbors bug

        wrapped = true
        when {
            position.x < -dim -> position.x = dim
            position.y < -dim -> position.y = dim
            position.z < -dim -> position.z = dim
            position.y > dim -> position.y = -dim
            position.x > dim -> position.x = -dim
            position.z > dim -> position.z = -dim
            else -> wrapped = false
        }

        if (wrapped) {
            trail.clear()
            trail.add(Vec3D(position))
        }
    }

    init {
        acceleration = Vec3D()
        lastNeighbors = mutableMapOf<GenericAgent, Float>()
        lastForces = ArrayList()

        trail = ArrayList()
        trail.add(Vec3D(position))
    }
}