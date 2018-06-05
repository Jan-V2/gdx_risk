package Gdx_Risk

import Gdx_Risk.Assets.*
import Gdx_Risk.Map.Hex_Coord
import Gdx_Risk.Map.Screen_Coord

class Render() {

    fun update_assets(){

    }

    fun render(hexes: Array<Render_Hex>) {
        // draws the y axis bot to top so it matches the coord map
        val render_field_origin = Screen_Coord(0, scr_height - hex_size - half_hex - 30)//-30 to make room for button bar

        val hex_width = half_hex + quart_hex //todo refactor out?

        shape_renderer.setAutoShapeType(true)
        shape_renderer.setColor(0f,0f,0f,1f)

        shape_renderer.begin()
        polyBatch.begin()

        hexes.forEach {
            var hex_origin = render_field_origin
            hex_origin = hex_origin.transl(
                    it.hex_coord.x * hex_width,
                    -(it.hex_coord.y * hex_size)
            )
            if (it.hex_coord.x % 2 == 0){
                hex_origin = hex_origin.transl_y(half_hex)
            }
            val sprite = hex_sprite_array[it.sprite.idx]
            sprite.setPosition(hex_origin.x.toFloat(), hex_origin.y.toFloat())
            sprite.draw(polyBatch)

            if (it.has_borders){
                it.borderSegments!!.forEach {
                    val coords = it.get_line()
                    shape_renderer.line(
                            hex_origin.x.toFloat() + coords.first.x,
                            hex_origin.y.toFloat() + coords.first.y,
                            hex_origin.x.toFloat() + coords.second.x,
                            hex_origin.y.toFloat()+ coords.second.y
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

        private var _borderSegments: Array<Hex_Border_Segment>? = null
        val borderSegments : Array<Hex_Border_Segment>? get() = _borderSegments

        constructor(hexCoord: Hex_Coord, sprite: Sprite_Idx, borderSegments: Array<Hex_Border_Segment>):this(hexCoord, sprite){
            this._borderSegments = borderSegments
            this._has_borders = true
        }

    }
    data class Hex_Border_Segment(val line_number:Int){
        fun get_line():Pair<Screen_Coord, Screen_Coord>{
            val idx = line_number * 2
            return if (line_number < 5){
                Pair(
                        Screen_Coord(
                                hex_outline_points[idx],
                                hex_outline_points[idx+1]
                        ),
                        Screen_Coord(
                                hex_outline_points[idx+2],
                                hex_outline_points[idx+3]
                        )
                )
            }else{
                Pair(
                        Screen_Coord(
                                hex_outline_points[idx],
                                hex_outline_points[idx+1]
                        ),
                        Screen_Coord(
                                hex_outline_points[0],
                                hex_outline_points[1]
                        )
                )
            }
        }
    }
    data class Sprite_Idx(val idx:Int)

}