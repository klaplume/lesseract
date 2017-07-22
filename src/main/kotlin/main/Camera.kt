package main

import math.Bounds2f
import math.Bounds2i
import math.Point2f
import math.Point2i
import sampler.CameraSample
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 * Created by klaplume on 15/07/17.
 */
class Camera {
    val film = Film(Defaults.DEFALUT_RESOLUTION,
            Bounds2f(0f, 1f, 0f, 1f),
            30f,
            "/home/klaplume/Pictures/default.png",
            1f)

    fun generateRayDifferential(cameraSample: CameraSample): RayDifferentialData {
        //TODO
        return RayDifferentialData(RayDifferential(), 1f)
    }
}

class Film(val resolution: Point2i, var cropWindow: Bounds2f, val diagonal: Float, val fileName: String, val scale: Float) {

    val pixels = Array<Pixel>(resolution.x*resolution.y) { i -> Pixel() }
    val tileSize = Defaults.DEFAULT_TILE_SIZE
    val croppedPixelBounds = Bounds2i(
                Math.ceil(((resolution.x-1) * cropWindow.xMin).toDouble()).toInt(),
                Math.ceil(((resolution.x-1) * cropWindow.xMax).toDouble()).toInt(),
                Math.ceil(((resolution.y-1) * cropWindow.yMin).toDouble()).toInt(),
                Math.ceil(((resolution.y-1) * cropWindow.yMax).toDouble()).toInt()
            )

    fun computeTileBounds(tile: Point2i, sampleBounds: Bounds2i): Bounds2i {
        val x0 = sampleBounds.xMin + tile.x * tileSize
        val x1 = Math.min(x0 + tileSize -1, sampleBounds.xMax)
        val y0 = sampleBounds.yMin + tile.y * tileSize
        val y1 = Math.min(y0 + tileSize -1, sampleBounds.yMax)
        return Bounds2i(x0, x1, y0, y1)
    }

    fun getFilmTile(tileBounds: Bounds2i, frac: Float): FilmTile {
        var tile = FilmTile(tileBounds, frac)
        return tile
    }

    fun getSampleBounds(): Bounds2i {
        //TODO incorrect, needs to extends a little further
        return croppedPixelBounds
    }

    fun mergeFilmTile(filmTile: FilmTile) {
        //TODO
        val tileBounds = filmTile.pixelBounds
        for(y in tileBounds.yMin..tileBounds.yMax){
            for(x in tileBounds.xMin..tileBounds.xMax){
                if(y == 350 && x == 20){
                    println()
                }
                val pixel = getPixel(x, y)
                val tilePixel = filmTile.getPixel(x, y)
                //Merge
                var xyzColor = tilePixel.contribSum.toXYZ()
                pixel.x = xyzColor.x
                pixel.y = xyzColor.y
                pixel.z = xyzColor.z
            }
        }

    }

    private fun getPixel(x: Int, y: Int): Pixel {
        return pixels[y * resolution.x + x]
    }

    fun writeImage() {
        //TODO
        var image = BufferedImage(resolution.x, resolution.y, BufferedImage.TYPE_INT_RGB)
        for(y in 0..resolution.y-1){
            for(x in 0..resolution.x-1){
                val p = pixels[y * resolution.x + x]
                val r = p.x*255
                val g = p.y*255
                val b = p.z*255
                val color = Color(r.toInt(), g.toInt(), b.toInt())
                image.setRGB(x, y, color.rgb)
            }
        }
        var res = ImageIO.write(image, "png", File(fileName))
    }
}

class FilmTile(val pixelBounds: Bounds2i, frac: Float) {
//class FilmTile(val pixelBounds: Bounds2i) {

    var pixels = Array<Point2i>((pixelBounds.xMax+1 - pixelBounds.xMin) * (pixelBounds.yMax+1 - pixelBounds.yMin)){ i -> Point2i(0, 0) }
    var pixels2 = Array((pixelBounds.xMax+1 - pixelBounds.xMin) * (pixelBounds.yMax+1 - pixelBounds.yMin)){
        val p = FilmTilePixel()
        p.contribSum.x = frac
        p.contribSum.y = frac
        p.contribSum.z = frac
        p
    }
    //var pxCol = 0f

    fun addSample(pFilm: Point2f, L: Spectrum, rayWeight: Float){
        //TODO needs to be implemented
        /*for(i in pixels2){
            i.contribSum.x = pxCol
            i.contribSum.y = pxCol
            i.contribSum.z = pxCol
        }*/
    }

    fun getPixel(x: Int, y: Int): FilmTilePixel {
        val width = pixelBounds.xMax - pixelBounds.xMin
        val offset = (x - pixelBounds.xMin) + (y - pixelBounds.yMin) * width
        return pixels2[offset]
    }
}

class Pixel {
    var x: Float = 0f
    var y: Float = 0f
    var z: Float = 0f
}

class FilmTilePixel {
    var contribSum: Spectrum = Spectrum()
    var filterWeightedSum: Float = 0.0f
}