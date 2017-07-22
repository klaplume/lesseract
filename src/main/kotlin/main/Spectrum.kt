package main

import math.Point3f

class Spectrum {
    var x: Float = 0.0f
    var y: Float = 0.0f
    var z: Float = 0.0f

    fun  toXYZ(): Point3f {
        return Point3f(x, y, z)
    }

}
