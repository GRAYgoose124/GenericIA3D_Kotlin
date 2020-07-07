package genericIA3D

import toxi.geom.Vec3D
import java.util.*
import java.util.function.Consumer

internal class BoidForces(sep_str: Float, coh_str: Float, ali_str: Float) : Forces() {
    var sep: Vec3D? = null
    var coh: Vec3D? = null
    var ali: Vec3D? = null

    private val sepDist: Float = 3000.0f
    private val cohDist: Float = 5000.0f

    private fun separation(agent: GenericAgent, agents: Map<GenericAgent, Float>): Vec3D {
        sep = Vec3D()
        agents.forEach { partner: Map.Entry<GenericAgent, Float> ->
            if (partner.value < sepDist) {
                val interp = agent.position.interpolateTo(partner.key.position, strengths!![0])
                sep!!.addSelf(interp)
            }
        }
        return sep!!
    }

    private fun cohesion(agent: GenericAgent, agents: Map<GenericAgent, Float>): Vec3D {
        coh = Vec3D()
        agents.forEach { partner: Map.Entry<GenericAgent, Float> ->
            if (partner.value > cohDist) {
                val interp = agent.position.interpolateTo(partner.key.position, strengths!![1])
                coh!!.addSelf(interp.inverted)
            }
        }
        return coh!!
    }

    private fun align(agent: GenericAgent, agents: Map<GenericAgent, Float>): Vec3D {
        ali = Vec3D()
        agents.forEach { partner: Map.Entry<GenericAgent, Float> ->
            val interp = agent.velocity.interpolateTo(partner.key.velocity, strengths!![1])
            ali!!.addSelf(interp)
        }
        return ali!!
    }

    init {
        strengths = ArrayList()
        strengths!!.add(sep_str)
        strengths!!.add(coh_str)
        strengths!!.add(ali_str)
        ops = listOf(::separation, ::cohesion, ::align)
    }
}