package genericIA3D

import toxi.geom.Vec3D
import java.util.*
import java.util.function.Consumer

class GenericAgent(var position: Vec3D, var velocity: Vec3D, protected val parent_sim: GenericIASimulation) {
    @JvmField
    var acceleration: Vec3D
    @JvmField
    var trail: MutableList<Vec3D>
    @JvmField
    var last_forces: MutableList<Vec3D>
    @JvmField
    var last_neighbors: MutableList<GenericAgent>

    // Forces
    fun apply_forces(group: List<GenericAgent>, forces: Forces, wrapping: Boolean) { // (forces)
        acceleration = Vec3D() // acceleration.scale(0.5f); // TODO: deceleration drag?
        last_forces = ArrayList()
        last_neighbors = neighborhood(group)
        if (last_neighbors.size < 1) return
        for (i in forces.ops!!.indices) {
            if (!parent_sim.so.FORCES_ON[i]) continue
            val force = forces.ops!![i].apply(last_neighbors)
            last_forces.add(force)
            acceleration.addSelf(force)
        }
        velocity.addSelf(acceleration.normalizeTo(parent_sim.so.MAX_SPEED))
        velocity.limit(parent_sim.so.MAX_SPEED)
        position.addSelf(velocity)
        if (wrapping) wrap()
    }

    // Helpers
    private fun neighborhood(group: List<GenericAgent>): MutableList<GenericAgent> {
        val neighbors: MutableList<GenericAgent> = ArrayList()
        neighbors.add(this) // ensure the first item is always the agent being acted on. FIX: HACK ALERT!
        group.forEach(Consumer { neighbor: GenericAgent ->
            val dist_squared = position.distanceToSquared(neighbor.position)
            if (dist_squared < parent_sim.so.NEIGHBORHOOD_SIZE_SQUARED) {
                neighbors.add(neighbor)
            }
        })
        return neighbors
    }

    private fun wrap() {
        var wrapped = false
        val DIM = parent_sim.so.DIM.toFloat()
        if (position.x < -DIM) {
            position.x = DIM
            wrapped = true
        }
        if (position.y < -DIM) {
            position.y = DIM
            wrapped = true
        }
        if (position.z < -DIM) {
            position.z = DIM
            wrapped = true
        }
        if (position.y > DIM) {
            position.y = -DIM
            wrapped = true
        }
        if (position.x > DIM) {
            position.x = -DIM
            wrapped = true
        }
        if (position.z > DIM) {
            position.z = -DIM
            wrapped = true
        }
        if (wrapped) {
            trail.clear()
            trail.add(Vec3D(position))
            last_neighbors.clear()
        }
    }

    fun update_trail(seg_length_squared: Float) {
        if (trail[trail.size - 1].sub(position).magSquared() > seg_length_squared) {
            trail.add(Vec3D(position))
        }
        if (trail.size > 2 * parent_sim.so.MAX_TRAIL_SEGMENTS) {
            trail.removeAt(0)
        }
    }

    init {
        acceleration = Vec3D()
        last_neighbors = ArrayList()
        last_forces = ArrayList()
        trail = ArrayList()
        trail.add(Vec3D(position))
    }
}