package Gdx_Risk

import Gdx_Risk.Assets.*
import Gdx_Risk.Map.Hex_Coord
import Gdx_Risk.Map.Screen_Coord
import com.badlogic.gdx.graphics.g2d.PolygonSprite

class Render() {

    fun update_assets(){

    }

    fun render(hexes: Array<Render_Hex>) {
        //-30 to make room for button bar
        var outline_origin_X = 0 //TODO set to 0 for debugging
        val outline_origin_Y = scr_height - hex_size - half_hex - 30

        var render_field_origin = Screen_Coord(0, 30)//TODO x set to 0 for debugging

        val hex_width = half_hex + quart_hex //todo refactor out?

        shape_renderer.begin()
        polyBatch.begin()

        hexes.forEach {
            var hex_origin = render_field_origin
            hex_origin = hex_origin.transl(
                    it.hex_coord.x * hex_width,
                    it.hex_coord.y * hex_size
            )
            if (it.hex_coord.x % 2 == 0){
                hex_origin = hex_origin.transl_y(half_hex)
            }
            val sprite = hex_sprite_array[it.sprite.idx]
            sprite.setPosition(hex_origin.x.toFloat(), hex_origin.y.toFloat())
            sprite.draw(polyBatch)

            if (it.has_borders){
                it.borders!!.forEach {
                    shape_renderer.line(
                            hex_origin.x.toFloat() + it.line_start.x,
                            hex_origin.y.toFloat() + it.line_start.y,
                            hex_origin.x.toFloat() + it.line_end.x,
                            hex_origin.y.toFloat()+ it.line_end.y
                    )
                }
            }
        }
        polyBatch.end()
        shape_renderer.end()
    }





    data class Render_Hex(val hex_coord: Hex_Coord, val sprite: Sprite_Idx){
        private var _has_borders = false
        val has_borders: Boolean get() = _has_borders

        private var _borders: Array<Hex_Border>? = null
        val borders : Array<Hex_Border>? get() = _borders

        constructor(hexCoord: Hex_Coord, sprite: Sprite_Idx, borders: Array<Hex_Border>):this(hexCoord, sprite){
            this._borders = borders
            this._has_borders = true
        }

    }
    data class Hex_Border(val line_start: Screen_Coord, val line_end: Screen_Coord)
    data class Sprite_Idx(val idx:Int)

}