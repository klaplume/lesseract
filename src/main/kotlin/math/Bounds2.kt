package math

class Bounds2<T>(val xMin: T, val xMax: T, val yMin: T, val yMax: T){

}

typealias Bounds2f = Bounds2<Float>
typealias Bounds2i = Bounds2<Int>