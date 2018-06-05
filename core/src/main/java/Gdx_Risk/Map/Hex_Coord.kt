package Gdx_Risk.Map

import com.github.czyzby.kiwi.util.tuple.immutable.Pair as _Pair

abstract class Abs_Coord(val x: Int, val y: Int) {

    abstract fun transl(x: Int, y: Int): Abs_Coord
    abstract fun transl_x(x: Int): Abs_Coord
    abstract fun transl_y(y: Int): Abs_Coord

    fun to_pair(): _Pair<Int, Int> {
        return _Pair<Int, Int>(this.x, this.y)
    }

    override fun toString(): String {
        return "x:$x y:$y"
    }

    override fun equals(other: Any?): Boolean {
        if (other == null){//todo add typecheck
            return false
        }
        val comp = other as Hex_Coord
        return (comp.hashCode() == this.hashCode())
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
}


class Hex_Coord(x: Int, y: Int) : Abs_Coord(x,y) {
    override fun transl(x: Int, y: Int): Hex_Coord{
        return Hex_Coord(this.x + x, this.y + y)
    }
    override fun transl_x(x: Int): Hex_Coord{
        return Hex_Coord(this.x + x, this.y)
    }
    override fun transl_y(y: Int): Hex_Coord{
        return Hex_Coord(this.x, this.y + y)
    }
}

class Screen_Coord(x: Int, y: Int) : Abs_Coord(x,y) {
    override fun transl(x: Int, y: Int): Screen_Coord{
        return Screen_Coord(this.x + x, this.y + y)
    }
    override fun transl_x(x: Int): Screen_Coord{
        return Screen_Coord(this.x + x, this.y)
    }
    override fun transl_y(y: Int): Screen_Coord{
        return Screen_Coord(this.x, this.y + y)
    }
}

data class Prov(val id: Int,val owner: Int)