package genericIA3D

import culebra.viz.Cameras
import processing.core.PApplet
import toxi.geom.Cone
import toxi.geom.Matrix4x4
import toxi.geom.Vec3D
import toxi.processing.ToxiclibsSupport
import java.util.function.Consumer

class Graphics(
        private var parent_proc: PApplet,
        private var parent_sim: GenericIASimulation) {

    // Cameras
    private var cam: Cameras? = null

    // Graphics libs
    private var gfx: ToxiclibsSupport? = null

    fun updateCam(cam_type: String?) {
        when (cam_type) {
            "peasy" -> peasyDefaultCamera()
        }
    }

    fun updateType(gfx_type: String?) {
        when (gfx_type) {
            "toxic" -> gfx = ToxiclibsSupport(parent_proc)
        }
    }

    fun drawCamera() {
        when (parent_sim.so.CAM_TYPE) {
            "peasy" -> peasyDrawCamera()
        }
    }

    // Peasycam
    private fun peasyDrawCamera() {
        val target = peasyCamTarget()
        if (target != null) cam!!.set3DCamera(50.0, 50, 100, target, false)

        // Show HUD
        cam!!.beginHUD()
        parent_proc.textSize(12f)
        parent_proc.fill(255f, 255f, 255f)
        parent_proc.text("FPS " + parent_proc.frameRate, 20f, 20f)
        parent_proc.text(parent_sim.lastKeyAction, 20f, 40f)

        for (i in parent_sim.so.forces_list.indices) {
            parent_proc.text(parent_sim.so.forces_list[i].toString(), 20f, 60 + ((i + 1) * 10).toFloat())
        }
        cam!!.endHUD()
    }

    private fun peasyDefaultCamera() {
        cam = Cameras(parent_proc)
        val lookat = intArrayOf(parent_proc.width / 2,
                                parent_proc.height / 2,
                                parent_sim.so.DIM / 2)

        cam!!.set3DCamera(parent_sim.so.DIM / 10.0f.toDouble(), 0, parent_sim.so.DIM / 2, lookat, true)
    }

    private fun peasyCamTarget(): IntArray? {
        var target: IntArray? = null
        var camTarget: GenericAgent? = null

        // Select follow target and update follow cam if toggled.
        if (parent_sim.so.FOLLOW && parent_sim.so.FOLLOW_TOGGLED) {
            camTarget = parent_sim.groups[0][0]
            parent_sim.so.FOLLOW_TOGGLED = false
        }
        // Camera target tracking. Currently broken, does not follow.
        if (parent_sim.so.FOLLOW)
            target = intArrayOf(camTarget!!.position.x.toInt(),
                                camTarget.position.y.toInt(),
                                camTarget.position.z.toInt())

        return target
    }

    fun showAgent(agent: GenericAgent, group_n: Int, colorMatrix: Matrix4x4) {
        val col = colorMatrix.applyTo(agent.position.add(agent.velocity)
                             .add(agent.acceleration))
        parent_proc.fill(col.x, col.y, col.z)

        if (parent_sim.so.TRAILS) agent.updateTrail(parent_sim.so.TRAIL_SEG_LENGTH)
        if (parent_sim.so.GFX_TYPE == "toxic") toxicShowAgent(agent, col, group_n)
    }

    // Toxic Support
    private fun toxicShowAgent(agent: GenericAgent, colour: Vec3D, group_n: Int) {
        parent_proc.pushMatrix()

        parent_proc.fill(colour.x, colour.y, colour.z)
        parent_proc.stroke(colour.y, colour.z, colour.x)
        gfx!!.cone(Cone(agent.position, agent.velocity,
                0.0f, parent_sim.so.AGENT_SIZE.toFloat(),
                (parent_sim.so.AGENT_SIZE * 4).toFloat()),
                5, false)

        if (parent_sim.so.TRAILS) toxicShowTrail(agent)
        if (parent_sim.so.SHOW_FORCE_VECTORS) toxicShowForceVectors(agent, group_n)

        if (parent_sim.so.SHOW_NEIGHBORS) {
            agent.lastNeighbors.forEach(Consumer { neighbor: GenericAgent ->
                parent_proc.stroke(100f, 50f, 200f)
                gfx!!.line(agent.position, neighbor.position)
            })
        }

        parent_proc.popMatrix()
    }

    private fun toxicShowForceVectors(agent: GenericAgent, group_n: Int) {
        var i = 1
        for (force in agent.lastForces) {
            val colour = Vec3D(force)
            when (i) {
                1 -> colour.x = 255f
                2 -> colour.y = 255f
                3 -> colour.z = 255f
            }
            if (!force.isZeroVector) {
                parent_proc.stroke(colour.x, colour.y, colour.z)
                gfx!!.line(agent.position, agent.position.add(
                            force.normalizeTo(parent_sim.so.FORCE_VECTOR_LENGTH *
                            parent_sim.so.forces_list[group_n].strengths!![i - 1])))
            }
            if (i > 3) {
                i = 1
            } else {
                i++
            }
        }
        parent_proc.stroke(255f, 255f, 255f)
        gfx!!.line(agent.position, agent.position.add(
                agent.acceleration
                        .normalizeTo(parent_sim.so.FORCE_VECTOR_LENGTH)))
        parent_proc.stroke(120f, 120f, 255f)
        gfx!!.line(agent.position, agent.position.add(
                    agent.velocity.normalizeTo(parent_sim.so.FORCE_VECTOR_LENGTH / 2)))
        parent_proc.stroke(0f, 0f, 0f)
    }

    private fun toxicShowTrail(agent: GenericAgent) {
        if (agent.trail.size < 2) {
            return
        }
        var i = 0
        while (i < agent.trail.size - 1) {
            gfx!!.line(agent.trail[i], agent.trail[i + 1])
            i += 1
        }
    }

    init {
        updateCam(parent_sim.so.CAM_TYPE)
        updateType(parent_sim.so.GFX_TYPE)
    }
}