package genericIA3D

import toxi.geom.Matrix4x4
import java.util.*

class SimOptions {
    val DIM = 1000
    val NEIGHBORHOOD_SIZE_SQUARED = DIM * 13.0f
    val AGENT_COUNT = DIM
    val AGENT_SIZE = 5
    val MAX_SPEED = 4.0f
    var MAX_TRAIL_SEGMENTS = 20
    val TRAIL_SEG_LENGTH = 35.0f
    val FORCE_VECTOR_LENGTH = 70.0f
    var SEP_STR = .5f
    var COH_STR = .5f
    var ALI_STR = .5f

    internal enum class CommandKeys {
        // TODO: FORCE[] -> FORCE[key]
        PAUSE, DEBUG, WRAPPING, FOLLOW, OBSTACLES, TRAILS, SHOW_NEIGHBORS, SHOW_FORCE_VECTORS
    }

    var FORCES_ON = booleanArrayOf(true, true, true)
    var forces_list: MutableList<Forces> = ArrayList()
    var PAUSE = false
    var DEBUG = false
    var WRAPPING = true
    var FOLLOW = false
    var FOLLOW_TOGGLED = false
    var OBSTACLES = false
    var TRAILS = false
    var SHOW_NEIGHBORS = false
    var SHOW_FORCE_VECTORS = true
    var GFX_TYPE = "toxic"
    var CAM_TYPE = "peasy"
    val colorMatrix = Matrix4x4().scale(255f / (DIM * 2).toDouble())
                                 .translate(DIM.toDouble(), DIM.toDouble(), DIM.toDouble())

    init {
        val bf = BoidForces(SEP_STR, COH_STR, ALI_STR)
        forces_list.add(bf)

        // val f = new Forces(MAX_SPEED)
        // forces_list.add(f)
    }
}