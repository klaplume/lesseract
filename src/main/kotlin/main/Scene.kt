package main

/**
 * Created by klaplume on 15/07/17.
 */
class Scene(val aggregate: Primitive, val lights: Array<Light>) {

    fun intersect(ray: Ray, si: SurfaceInteraction): Boolean {
        return false
    }

    fun intersectP(ray: Ray): Boolean {
        return false
    }
}

