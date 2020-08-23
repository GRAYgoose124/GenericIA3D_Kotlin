package genericIA3D.forces.base

import genericIA3D.agents.GenericAgent
import toxi.geom.Vec3D
import kotlin.reflect.KFunction2


class FlowForces  : ForcesPrimitive() { // TODO: Make 3D interpolated point vector gradients for spatial forces. flowfields
    var points: MutableMap<Vec3D, KFunction2<GenericAgent, MutableMap<GenericAgent, Float>, Vec3D>> = mutableMapOf()

    init {

    }
}
