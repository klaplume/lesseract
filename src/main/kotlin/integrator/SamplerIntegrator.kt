package integrator

import math.Bounds2i
import math.Point2i
import sampler.Sampler
import kotlinx.coroutines.experimental.*
import main.*

abstract class SamplerIntegrator(val cam: Camera, val sampler: Sampler) : Integrator {

    var scene: Scene? = null

    override fun render(scene: Scene) {
        preprocess(scene, this.sampler)

        this.scene = scene
        val tiles = computeNbTiles(cam.film.getSampleBounds(), cam.film.tileSize)
        runBlocking {
            val jobs = mutableListOf<Job>()
            for (y in 0..tiles.y-1) {
                for (x in 0..tiles.x-1) {
                    val tile = Point2i(x, y)
                    val job = launch(CommonPool) {
                        renderTile(tile, tiles)
                    }
                    jobs.add(job)
                }
            }
            jobs.forEach({ it.join() })
        }
        cam.film.writeImage()
    }

    private suspend fun renderTile(tile: Point2i, tiles: Point2i) {

        println("Rendering tile (${tile.x}, ${tile.y})")
        val seed = tile.y * tiles.x + tile.x
        val tileSampler = sampler.clone(seed)

        val tileBounds = cam.film.computeTileBounds(tile, cam.film.getSampleBounds())
        //println("tile (${tile.x}; ${tile.y}) bounds x=(${tileBounds.xMin}; ${tileBounds.xMax}) y=(${tileBounds.yMin}; ${tileBounds.yMax})")
        val frac = seed.toFloat() / (tiles.x * tiles.y)
        val filmTile = cam.film.getFilmTile(tileBounds, frac)
        for(i in 0..filmTile.pixels.size-1){
            val pixel = filmTile.pixels[i]
            pixel.x = i % cam.film.tileSize + tileBounds.xMin
            pixel.y = i / cam.film.tileSize + tileBounds.yMin

            tileSampler.startPixel(pixel)
            while(tileSampler.hasNextSample()){
                tileSampler.nextSample()
                val cameraSample = tileSampler.getCameraSample(pixel)
                //Generate ray
                val rayDiffData: RayDifferentialData = cam.generateRayDifferential(cameraSample)
                val ray: RayDifferential = rayDiffData.ray
                val rayWeight: Float = rayDiffData.rayWeight
                ray.scaleDifferentials(1 / Math.sqrt(tileSampler.samplesPerPixel.toDouble()))
                //Evaluate radiance along ray
                var L: Spectrum = Spectrum()
                if(scene != null){ //TODO possible to avoid this?
                    if(rayWeight > 0) {
                        L = Li(ray, scene!!, tileSampler)
                        checkLValue(L)
                    }
                }
                //Add contribution to image
                filmTile.addSample(cameraSample.posFilm, L, rayWeight)
                //Free data structures
            }
        }
        cam.film.mergeFilmTile(filmTile)
    }

    private fun checkLValue(l: Spectrum) {
        //TODO check for valid values
    }

    private fun computeNbTiles(sampleBounds: Bounds2i, tileSize: Int): Point2i {
        //TODO compute
        var p = Point2i(Math.ceil(((sampleBounds.xMax+1-sampleBounds.xMin).toDouble() / tileSize)).toInt(),
                Math.ceil(((sampleBounds.yMax+1-sampleBounds.yMin).toDouble() / tileSize)).toInt())
        return p
    }

    fun preprocess(scene: Scene, sampler: Sampler) {

    }

    abstract fun Li(ray: RayDifferential, scene: Scene, tileSampler: Sampler, depth: Int = 0): Spectrum
}

