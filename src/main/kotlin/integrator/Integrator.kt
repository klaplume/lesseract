package integrator

import main.Scene

interface Integrator {

    fun render(scene: Scene)
}