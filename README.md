# Generic Intelligent Agents in 3D w/ Kotlin-Processing
This is a project I'm working on to act as a base for more complex intelligent agent interactions in 3D. 
Eventually it will have Variants including neural nets and rts type games.

## jars (Processing reqs)
This project uses javasdk1.8+ and is built on Kotlin. There is currently no gradle right now.
You can attain these through the Processing library installer, the installed libs in the user 
processing folder or find online by searching processing library jars.

    -core.jar (processing core)
    -culebra.jar 
    -toxiclibscore.jar (for toxic graphics, only graphcis currently supported)
    -peasycam.jar 
    -toxiclibs_p5.jar
    -colorutils.jar
    -jogl-all.jar (+your native variant for tag support)
    -gluegen-rt.jar (+your native variant)
    -peasy-math.jar

## Ways to use
You can click pan and drag, double click. It's not very useful at the moment. 
Here is the switch casem which should give you a clue.

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
    }

## TODO
Lots.... Here is a horrible dump im sorry.

    Found 13 TODO items in 10 files
    genericIA3D ( Kotlon .kt classes. )
    .agents.CharacterAgent
    (3, 29) class CharacterAgent() { // TODO: 3rd person select and control
        .GenericAgent
    (28, 90) .normalizeTo(parent_sim.so.MAX_SPEED * forces.strengths!![i]) // TODO: force norming/neighboring is still borked.
    (39, 47) if (parent_sim.so.WRAPPING) wrap() // TODO: move to GIAS?
    (57, 50) val dim = parent_sim.so.DIM.toFloat() // TODO: <=/>= mostly? fixes wrapping neighbors bug
    app.GenericIA3D
    (11, 36) class GenericIA3D : PApplet() { // TODO: functionalize and move to .demo
        .SimOptions.kt
    (20, 12) // TODO: FORCE[] -> FORCE[key]
    (49, 6) } // TODO: change to json format and reduce as much bloat.
    
    forces.base.AIForces
    (3, 21) class AIForces { // TODO: ANNs, GAs, Pheromone systems?
        .FlowForces.kt
    (10, 28) ) : ForcesPrimitive() { // TODO: Make 3D interpolated point vector gradients for spatial forces.
        ForcesPrimitive.kt
    (36, 4) // TODO: Move force strengths from SimOpts to *Forces?
        ObstacleForces.kt
    (3, 27) class ObstacleForces { // TODO: pinned/patrol Attractor and Repulsors as well as function objects
    sim
    
    GenericIASimulation.kt
    (69, 73) so.forcesList.forEach(Consumer { addGroup(so.AGENT_COUNT) }) // TODO: Update to add individual group sizes
    Graphics.kt
    (13, 58) private var parentSim: GenericIASimulation) { // TODO: breakup camera, drawing. Add obstacles/enviro meshes(detection...)