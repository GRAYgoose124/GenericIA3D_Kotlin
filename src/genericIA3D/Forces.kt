package genericIA3D

import toxi.geom.Vec3D
import java.util.*
import kotlin.reflect.KFunction2

open class Forces {
    var strengths: MutableList<Float>? = null
    var ops: List<KFunction2<GenericAgent, MutableMap<GenericAgent, Float>, Vec3D>>

    constructor() {}
    constructor(strength: Float) {
        strengths = ArrayList()
        strengths!!.add(strength)
    }

    fun positionForce(agent: GenericAgent, agents: MutableMap<GenericAgent, Float>): Vec3D {
        val acceleration = Vec3D()
        acceleration.addSelf(agent.position).scale(strengths!![0])
        return acceleration
    }

    companion object {
        @JvmStatic
        @Throws(IndexOutOfBoundsException::class, IllegalArgumentException::class)
        fun <T> tail(xs: List<T>): List<T> {
            return xs.subList(1, xs.size)
        }
    }

    init {
        ops = listOf(::positionForce)
    }
}
// TODO: Move force strengths from SimOpts to *Forces?