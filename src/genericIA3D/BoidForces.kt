package genericIA3D

import toxi.geom.Vec3D
import java.util.*
import java.util.function.Consumer
import java.util.function.Function

internal class BoidForces(sep_str: Float, coh_str: Float, ali_str: Float) : Forces() {
    private var separation: Vec3D? = null
    private var cohesion: Vec3D? = null
    private var alignment: Vec3D? = null

    private fun separation(agents: List<GenericAgent>): Vec3D {
        separation = Vec3D()
        tail(agents).forEach(Consumer { partner: GenericAgent ->
            val interp = agents[0].position.interpolateTo(partner.position, strengths!![0])
            separation!!.addSelf(interp)
        })
        return separation!!
    }

    private fun cohesion(agents: List<GenericAgent>): Vec3D {
        cohesion = Vec3D()
        tail(agents).forEach(Consumer { partner: GenericAgent ->
            val interp = agents[0].position.interpolateTo(partner.position, strengths!![1])
            cohesion!!.addSelf(interp.inverted)
        })
        return cohesion!!
    }

    private fun align(agents: List<GenericAgent>): Vec3D {
        alignment = Vec3D()
        tail(agents).forEach(Consumer { partner: GenericAgent ->
            val interp = agents[0].velocity.interpolateTo(partner.velocity, strengths!![1])
            alignment!!.addSelf(interp)
        })
        return alignment!!
    }

    init {
        strengths = ArrayList<Float>()
        strengths!!.add(sep_str)
        strengths!!.add(coh_str)
        strengths!!.add(ali_str)
        ops = ArrayList()
        ops!!.add(Function { agents: List<GenericAgent> -> separation(agents) })
        ops!!.add(Function { agents: List<GenericAgent> -> cohesion(agents) })
        ops!!.add(Function { agents: List<GenericAgent> -> align(agents) })
    }
}