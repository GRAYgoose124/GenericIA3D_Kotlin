package genericIA3D.agents

import genericIA3D.sim.GenericIASimulation
import genericIA3D.forces.base.FlowForces
import genericIA3D.forces.base.ForcesPrimitive
import toxi.geom.Vec3D
import kotlin.collections.ArrayList

open class GenericAgent(var position: Vec3D,
                        var velocity: Vec3D,
                        protected val parent_sim: GenericIASimulation, var forceNeighbors: ForcesPrimitive) {
    var acceleration: Vec3D
    var trail: MutableList<Vec3D>
    var lastForces: MutableList<Vec3D>
    var lastNeighbors: MutableMap<GenericAgent, Float>

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
        if (parent_sim.so.WRAPPING) wrap() // TODO: move to GIAS?
    }

    // Helpers
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

    private fun wrap() {
        var wrapped = false
        val dim = parent_sim.so.DIM.toFloat() // TODO: <=/>= mostly? fixes wrapping neighbors bug

        if (position.x < -dim) { // But now trails disappear at edges and they still want to gather at dim edges/ center.
            position.x = dim
            wrapped = true }
        if (position.y < -dim) {
            position.y = dim
            wrapped = true }
        if (position.z < -dim) {
            position.z = dim
            wrapped = true }
        if (position.y > dim) {
            position.y = -dim
            wrapped = true }
        if (position.x > dim) {
            position.x = -dim
            wrapped = true }
        if (position.z > dim) {
            position.z = -dim
            wrapped = true }

        if (wrapped) {
            trail.clear()
            trail.add(Vec3D(position))
            lastNeighbors.clear() }
    }

    private fun updateTrail() {
        if (trail[trail.size - 1].sub(position).magSquared() > parent_sim.so.TRAIL_SEG_LENGTH) {
            trail.add(Vec3D(position))
        }
        if (trail.size > 2 * parent_sim.so.MAX_TRAIL_SEGMENTS) {
            trail.removeAt(0)
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