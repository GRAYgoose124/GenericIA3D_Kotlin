package genericIA3D

import genericIA3D.sim.GenericIASimulation
import processing.core.PApplet
import processing.event.MouseEvent

fun main() {
    GenericIA3D.run()
}

class GenericIA3D : PApplet() {
    companion object {
        fun run() {
            val gias = GenericIA3D()
            gias.runSketch()
        }
    }

    private var simulation: GenericIASimulation? = null

    override fun settings() {
        fullScreen(P3D)
        smooth(8)
    }

    override fun setup() {
        val so = SimOptions()
        simulation = GenericIASimulation(so, this)
    }

    override fun draw() {
        background(0)
        simulation!!.drawFrame()
    }

    // Events
    override fun keyPressed() {
        when (key) {
            'r' -> {
                simulation!!.parent.setup()
                simulation!!.lastKeyAction = "Restart"
            }
            'p' -> {
                simulation!!.so.PAUSE = !simulation!!.so.PAUSE
                simulation!!.lastKeyAction = "Pause = " + simulation!!.so.PAUSE
            }
            't' -> {
                simulation!!.so.TRAILS = !simulation!!.so.TRAILS
                simulation!!.lastKeyAction = "Show Trails = " + simulation!!.so.TRAILS
            }
            'v' -> {
                simulation!!.so.SHOW_FORCE_VECTORS = !simulation!!.so.SHOW_FORCE_VECTORS
                simulation!!.lastKeyAction = "Show FVectors = " + simulation!!.so.SHOW_FORCE_VECTORS
            }
            'a' -> {
                simulation!!.so.SHOW_ALL_FORCE_VECTORS = !simulation!!.so.SHOW_ALL_FORCE_VECTORS
                simulation!!.lastKeyAction = "Show All FVectors = " + simulation!!.so.SHOW_ALL_FORCE_VECTORS
            }
            'n' -> {
                simulation!!.so.SHOW_NEIGHBORS = !simulation!!.so.SHOW_NEIGHBORS
                simulation!!.lastKeyAction = "Show Neighbors = " + simulation!!.so.SHOW_NEIGHBORS
            }
            'o' -> {
                simulation!!.so.OBSTACLES = !simulation!!.so.OBSTACLES
                simulation!!.lastKeyAction = "Enable Obstacles = " + simulation!!.so.OBSTACLES
            }
            'd' -> {
                simulation!!.so.DEBUG = !simulation!!.so.DEBUG
                simulation!!.lastKeyAction = "Debug" + simulation!!.so.SHOW_NEIGHBORS
            }
            '1' -> {
                simulation!!.so.FORCES_ON[0] = !simulation!!.so.FORCES_ON[0]
                simulation!!.lastKeyAction = "Force[0] = " + simulation!!.so.FORCES_ON[0]
            }
            '2' -> {
                simulation!!.so.FORCES_ON[1] = !simulation!!.so.FORCES_ON[1]
                simulation!!.lastKeyAction = "Force[1] = " + simulation!!.so.FORCES_ON[1]
            }
            '3' -> {
                simulation!!.so.FORCES_ON[2] = !simulation!!.so.FORCES_ON[2]
                simulation!!.lastKeyAction = "Force[2] = " + simulation!!.so.FORCES_ON[2]
            }
            'f' -> {/*
				if (simulation.so.CAM_TYPE.equals("peasy")) {
					if (simulation.so.FOLLOW) {
						simulation.gfx.peasy_default_camera();
					} else {
						simulation.so.FOLLOW_TOGGLED = true;
					}
					simulation.so.FOLLOW = !simulation.so.FOLLOW;
				}
				break; */
            }
        }
    }

    override fun mouseClicked(event: MouseEvent) {}
    override fun mouseDragged(event: MouseEvent) {}
    override fun mouseMoved(event: MouseEvent) {}
    override fun mouseWheel(event: MouseEvent) {}
}


