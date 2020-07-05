package genericIA3D

import toxi.geom.Vec3D
import java.util.*
import java.util.function.Function

open class Forces {
    @JvmField
    var strengths: MutableList<Float>? = null
    @JvmField
    var ops: MutableList<Function<List<GenericAgent>, Vec3D>>? = null

    constructor() {}
    constructor(strength: Float) {
        strengths = ArrayList()
        strengths!!.add(strength)
        ops = ArrayList()
        ops!!.add(Function { agents: List<GenericAgent> -> position_force(agents) })
    }

    fun position_force(agents: List<GenericAgent>): Vec3D {
        val acceleration = Vec3D()
        acceleration.addSelf(agents[0].position).scale(strengths!![0])
        return acceleration
    }

    companion object {
        @JvmStatic
        @Throws(IndexOutOfBoundsException::class, IllegalArgumentException::class)
        fun <T> tail(xs: List<T>): List<T> {
            return xs.subList(1, xs.size)
        }
    }
}