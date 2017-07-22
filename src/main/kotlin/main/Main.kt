package main

import integrator.Integrator
import integrator.SamplerIntegrator
import integrator.WhittedIntegrator
import kotlinx.coroutines.experimental.runBlocking
import material.DummyMaterial
import sampler.Sampler
import shape.DummyShape

/**
 * Created by klaplume on 15/07/17.
 */

fun main(args: Array<String>) {

    val opt = parseArguments(args)
    val scene = parseScene(opt)
    val integrator = parseIntegrator(opt)

    integrator.render(scene)
}

fun parseIntegrator(opt: Options): Integrator {
    //TODO replace with parsing implementation
    return WhittedIntegrator(5, Camera(), Sampler())
}

fun parseScene(opt: Options): Scene {
    //TODO replace with parsing implementation
    val p = Primitive(DummyShape(), DummyMaterial())
    val lights = arrayOf(Light(), Light())
    return Scene(p, lights)
}

/**
 * Parse arguments and check for mandatory values
 */
fun  parseArguments(args: Array<String>): Options {
    //TODO replace with parsing implementation
    return Options()
}


class Options {
    var  pathScene: String = ""
}