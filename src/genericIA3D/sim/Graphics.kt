package genericIA3D.sim

import culebra.viz.Cameras
import genericIA3D.agents.GenericAgent
import processing.core.PApplet
import toxi.geom.Cone
import toxi.geom.Matrix4x4
import toxi.geom.Vec3D
import toxi.processing.ToxiclibsSupport

class Graphics(
        private var parentProc: PApplet,
        private var parentSim: GenericIASimulation) { // TODO: breakup camera, drawing. Add obstacles/enviro meshes(detection...)

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
            "toxic" -> gfx = ToxiclibsSupport(parentProc)
        }
    }

    fun drawCamera() {
        when (parentSim.so.CAM_TYPE) {
            "peasy" -> peasyDrawCamera()
        }
    }

    // Peasycam
    private fun peasyDrawCamera() {
        val target = peasyCamTarget()
        if (target != null) cam!!.set3DCamera(50.0, 50, 100, target, false)

        // Show HUD
        cam!!.beginHUD()
        parentProc.textSize(12f)
        parentProc.fill(255f, 255f, 255f)
        parentProc.text("FPS " + parentProc.frameRate, 20f, 20f)
        parentProc.text(parentSim.lastKeyAction, 20f, 40f)

        for (i in parentSim.so.forcesList.indices) {
            parentProc.text(parentSim.so.forcesList[i].toString(), 20f, 60 + ((i + 1) * 10).toFloat())
        }
        cam!!.endHUD()
    }

    private fun peasyDefaultCamera() {
        cam = Cameras(parentProc)
        val lookat = intArrayOf(parentProc.width / 2,
                                parentProc.height / 2,
                                parentSim.so.DIM / 2)

        cam!!.set3DCamera(parentSim.so.DIM / 10.0f.toDouble(), 0, parentSim.so.DIM / 2, lookat, true)
    }

    private fun peasyCamTarget(): IntArray? {
        var target: IntArray? = null
        var camTarget: GenericAgent? = null

        // Select follow target and update follow cam if toggled.
        if (parentSim.so.FOLLOW && parentSim.so.FOLLOW_TOGGLED) {
            camTarget = parentSim.groups[0][0]
            parentSim.so.FOLLOW_TOGGLED = false
        }
        // Camera target tracking. Currently broken, does not follow.
        if (parentSim.so.FOLLOW)
            target = intArrayOf(camTarget!!.position.x.toInt(),
                                camTarget.position.y.toInt(),
                                camTarget.position.z.toInt())

        return target
    }

    fun showAgent(agent: GenericAgent, group_n: Int, colorMatrix: Matrix4x4) {
        val col = colorMatrix.applyTo(agent.position.add(agent.velocity)
                             .add(agent.acceleration))
        parentProc.fill(col.x, col.y, col.z)

        if (parentSim.so.GFX_TYPE == "toxic") toxicShowAgent(agent, col, group_n)
    }

    // Toxic Support
    private fun toxicShowAgent(agent: GenericAgent, colour: Vec3D, group_n: Int) {
        parentProc.pushMatrix()

        parentProc.fill(colour.x, colour.y, colour.z)
        parentProc.stroke(colour.y, colour.z, colour.x)
        gfx!!.cone(Cone(agent.position, agent.velocity,
                0.0f, parentSim.so.AGENT_SIZE.toFloat(),
                (parentSim.so.AGENT_SIZE * 4).toFloat()),
                3, false)

        if (parentSim.so.TRAILS) toxicShowTrail(agent)
        if (parentSim.so.SHOW_NEIGHBORS) toxicShowNeighbors(agent)
        if (parentSim.so.SHOW_FORCE_VECTORS) toxicShowForceVectors(agent, group_n)


        parentProc.popMatrix()
    }

    private fun toxicShowNeighbors(agent: GenericAgent) {
        parentProc.stroke(100f, 50f, 200f)
        agent.lastNeighbors.keys.forEach { neighbor: GenericAgent ->
            if (!agent.wrapped and !neighbor.wrapped)
                gfx!!.line(agent.position, neighbor.position)
        }

    }

    private fun toxicShowForceVectors(agent: GenericAgent, group_n: Int) {
        var i = 1

        agent.lastForces.forEach { force ->
            val colour = Vec3D(force)
            when (i) {
                1 -> colour.x = 255f
                2 -> colour.y = 255f
                3 -> colour.z = 255f
                4 -> {colour.x = 255f; colour.y = 0f}
                5 -> {colour.x = 255f; colour.z = 255f}
                6 -> {colour.y = 255f; colour.z = 255f}
            }

            if (!force.isZeroVector) { // parent_sim.so.SHOW_ALL_FORCE_VECTORS == true or somehow breaks!?! wtf?
                parentProc.stroke(colour.x, colour.y, colour.z)
                gfx!!.line(agent.position, agent.position.add(
                            force.copy().normalizeTo(parentSim.so.FORCE_VECTOR_LENGTH *
                            parentSim.so.forcesList[group_n].strengths!![i - 1])))
            }
            if (i >= parentSim.so.forcesList[group_n].strengths!!.size) {
                i = 1
            } else {
                i++
            }
        }

        parentProc.stroke(255f, 255f, 255f)
        gfx!!.line(agent.position, agent.position.add(
                agent.acceleration.copy()
                        .normalizeTo(parentSim.so.FORCE_VECTOR_LENGTH * parentSim.so.forcesList[group_n].strengths!!.sum())))

        parentProc.stroke(120f, 120f, 255f)
        gfx!!.line(agent.position, agent.position.add(
                    agent.velocity.copy()
                            .normalizeTo(parentSim.so.FORCE_VECTOR_LENGTH / 2)))
    }

    private fun toxicShowTrail(agent: GenericAgent) {
        if (agent.trail.size < 2) { return }

        for (i in 0 until (agent.trail.size - 1)) {
            gfx!!.line(agent.trail[i], agent.trail[i + 1])
        }
    }

    init {
        updateCam(parentSim.so.CAM_TYPE)
        updateType(parentSim.so.GFX_TYPE)
    }
}