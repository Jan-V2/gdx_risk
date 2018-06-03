package Gdx_Risk.Map

import com.github.czyzby.kiwi.util.tuple.immutable.Pair as _Pair

class Coord(val x: Int, val y: Int) {
    fun transl(x: Int, y: Int): Coord{
        return Coord(this.x + x, this.y + y)
    }

    fun transl_x(x: Int): Coord{
        return Coord(this.x + x, this.y)
    }
    fun transl_y(y: Int): Coord{
        return Coord(this.x, this.y + y)
    }

    fun to_pair(): _Pair<Int, Int> {
        return _Pair<Int, Int>(this.x, this.y)
    }

    override fun toString(): String {
        return "x: $x y: $y"
    }
}