package genericIA3D

import genericIA3D.forces.demo.BoidForces
import genericIA3D.forces.base.FlowForces
import genericIA3D.forces.base.ForcesPrimitive
import toxi.geom.Matrix4x4
import java.util.*

class SimOptions {
    val DIM = 1500
    val NEIGHBORHOOD_SIZE_SQUARED = 10000.0f
    val AGENT_COUNT = 800
    val AGENT_SIZE = 5
    val MAX_SPEED = 4.0f
    var MAX_TRAIL_SEGMENTS = 25
    val TRAIL_SEG_LENGTH = 200.0f
    val FORCE_VECTOR_LENGTH = 100.0f

    internal enum class CommandKeys {
        // TODO: FORCE[] -> FORCE[key]
        PAUSE, DEBUG, WRAPPING, FOLLOW, OBSTACLES, TRAILS, SHOW_NEIGHBORS, SHOW_FORCE_VECTORS
    }

    var FORCES_ON = booleanArrayOf(true, true, true)
    var forcesList: MutableList<ForcesPrimitive> = ArrayList()
    var PAUSE = false
    var DEBUG = false
    var WRAPPING = true
    var FOLLOW = false
    var FOLLOW_TOGGLED = false
    var OBSTACLES = false
    var TRAILS = false
    var SHOW_NEIGHBORS = false
    var SHOW_FORCE_VECTORS = true
    var SHOW_ALL_FORCE_VECTORS = false
    var GFX_TYPE = "toxic"
    var CAM_TYPE = "peasy"
    val colorMatrix = Matrix4x4().scale(255f / (DIM * 2).toDouble())
            .translate(DIM.toDouble(), DIM.toDouble(), DIM.toDouble())!!

    init {
        val bf = BoidForces()
        forcesList.add(bf)

        val ef = FlowForces()
        //val f = Forces(MAX_SPEED)
        //forces_list.add(f)
    }
}