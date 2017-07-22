package integrator

import main.Camera
import main.RayDifferential
import main.Scene
import main.Spectrum
import sampler.Sampler

/**
 * Created by klaplume on 16/07/17.
 */
class WhittedIntegrator(val maxDepth: Int, cam: Camera, sampler: Sampler): SamplerIntegrator(cam, sampler) {

    override fun Li(ray: RayDifferential, scene: Scene, tileSampler: Sampler, depth: Int): Spectrum {
        var L = Spectrum()
        //TODO
        
        return L
    }
}