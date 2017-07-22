package sampler

import math.Point2f
import math.Point2i

class Sampler {
    val samplesPerPixel: Int = 1
    private var seed: Int = 0
    var currentSampleNb = 0

    fun clone(seed: Int): Sampler {
        //TODO make exact copy
        val sampler = Sampler()
        sampler.seed = seed
        return sampler
    }

    fun startPixel(pixel: Point2i) {
        //TODO
        currentSampleNb = 0
    }

    fun hasNextSample(): Boolean {
        //TODO
        return currentSampleNb < samplesPerPixel
    }

    fun nextSample() {
        //TODO
        currentSampleNb++
    }

    fun  getCameraSample(pixel: Point2i): CameraSample {
        //TODO
        return CameraSample()
    }

}

class CameraSample {
    val posFilm: Point2f = Point2f(0f,0f)

}
