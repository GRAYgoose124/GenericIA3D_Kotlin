package genericIA3D.forces.demo

import genericIA3D.agents.GenericAgent
import genericIA3D.forces.base.ForcesPrimitive
import toxi.geom.Vec3D
import java.util.*

internal class BoidForces() : ForcesPrimitive() {
    private var sep: Vec3D? = null
    private var coh: Vec3D? = null
    private var ali: Vec3D? = null

    private val sepDist: Float = 7000.0f
    private val cohDist: Float = 7500.0f

    private fun separation(agent: GenericAgent, agents: Map<GenericAgent, Float>): Vec3D {
        sep = Vec3D()
        agents.forEach { partner: Map.Entry<GenericAgent, Float> ->
            if (partner.value < sepDist) {
                val interp = agent.position.interpolateTo(partner.key.position, 1 - strengths!![0])
                sep!!.addSelf(interp)
            }
        }
        return sep!!
    }

    private fun cohesion(agent: GenericAgent, agents: Map<GenericAgent, Float>): Vec3D {
        coh = Vec3D()
        agents.forEach { partner: Map.Entry<GenericAgent, Float> ->
            if (partner.value > cohDist) {
                val interp = agent.position.interpolateTo(partner.key.position, 1 - strengths!![1])
                coh!!.addSelf(interp.inverted)
            }
        }
        return coh!!
    }

    private fun align(agent: GenericAgent, agents: Map<GenericAgent, Float>): Vec3D {
        ali = Vec3D()
        agents.forEach { partner: Map.Entry<GenericAgent, Float> ->
            val interp = agent.velocity.interpolateTo(partner.key.velocity, 1 - strengths!![2])
            ali!!.addSelf(interp)
        }
        return ali!!
    }

    init {
        val sepStr = .04f
        val cohStr = .04f
        val aliStr = .05f

        strengths = ArrayList()
        strengths!!.add(sepStr)
        strengths!!.add(cohStr)
        strengths!!.add(aliStr)
        ops = listOf(::separation, ::cohesion, ::align)
    }
}