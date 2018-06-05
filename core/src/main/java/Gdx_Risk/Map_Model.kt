package Gdx_Risk

import Gdx_Risk.Map.Hex_Coord
import Gdx_Risk.Map.Prov_Id
import Gdx_Risk.Map.Screen_Coord
import Gdx_Risk.Render.*
import Gdx_Risk.Assets.*
//add hex_coord -> prov map
//add prov -> player
//add player -> provs
//add dicerolls
//add get renderable hexes


class Map_Model(prov_lookup: Array<Array<Int>>) {
    constructor(prov_lookup: Array<IntArray>) : this(
            prov_lookup.map {
                it.map { it }.toTypedArray()
            }.toTypedArray()
    )


    public val crd_to_prv = Coord_To_Prov(prov_lookup)
    init{

    }

    inner class Map(){
        init {

        }

    }

    fun temp_get_render_hexes(): Array<Render_Hex>{

        fun get_borders(coord: Hex_Coord):Array<Hex_Border_Segment>{
            fun get_line_num(_i: Int): Int{
                val magic_vert_numbers = Pair(
                        //magic numbers that represent line along the edge of the hexes
                        arrayOf(3,5,4,1,2,0),
                        arrayOf(2,0,4,1,3,5)
                )

                return if (coord.x % 2 == 0){
                    magic_vert_numbers.first[_i]
                }else{
                    magic_vert_numbers.second[_i]
                }
            }

            val ret = ArrayList<Hex_Border_Segment>()
            val this_prov_id = crd_to_prv.get_prov_id(coord)
            val around_coord = crd_to_prv.get_around_coord(coord)
            for (i in 0 until 6){
                    if (around_coord[i] != null) {
                        if (crd_to_prv.get_prov_id(around_coord[i]!!).id() != this_prov_id.id()) {
                            ret.add(Hex_Border_Segment(get_line_num(i)))
                        }
                    } else {
                        ret.add(Hex_Border_Segment(get_line_num(i)))
                    }

            }
            return ret.toTypedArray()
        }

        val ret  =  arrayListOf<Render_Hex>()
        crd_to_prv.get_all_coords()
                .filter { crd_to_prv.get_prov_id(it).id() != -1 }.forEach {
                        val borders = get_borders(it)
                        if (borders.isEmpty()){
                            ret.add(
                                    Render_Hex(it, Sprite_Idx(crd_to_prv.get_prov_id(it).id()))
                            )
                        }else{
                            ret.add(
                                    Render_Hex(it, Sprite_Idx(crd_to_prv.get_prov_id(it).id()),borders)
                            )
                        }

                }
        return ret.toTypedArray()
    }

    private inner class Nav_Tree{
        val tree = HashMap<Prov_Id,ArrayList<Prov_Id>>()
        init {
            fun add( from: Prov_Id, to: Prov_Id){
                val from_tree = tree[from]
                if (from_tree != null){
                    if(!from_tree.contains(to)) {
                        from_tree.add(to)
                        tree[from] = from_tree
                    }
                }else{
                    tree[from] = arrayListOf(to)
                }
            }

            crd_to_prv.get_all_coords().forEach {
                val id_old = crd_to_prv.get_prov_id(it)
                if( id_old.id() != -1){
                    crd_to_prv.get_around_coord(it)
                            .filter { it != null }.filter { crd_to_prv.get_prov_id(it!!).id() != -1 }
                            .forEach {
                                val id_new = crd_to_prv.get_prov_id(it!!)
                                add(id_old, id_new)
                                add(id_new, id_old)
                            }
                }
            }
        }

    }

    class Coord_To_Prov(private val prov_lookup: Array<Array<Int>>){

        fun get_prov_id(hex_coord: Hex_Coord): Prov_Id {
            //dumps coords if it's outside of the grid
            return if (!coord_is_safe(hex_coord)) {
                Prov_Id(-1)
            } else {
                if (hex_coord.x == -1 || hex_coord.y == -1){
                    System.out.println("");
                }
                Prov_Id(prov_lookup[hex_coord.y][hex_coord.x])
            }
        }

        fun get_around_coord(coord: Hex_Coord): Array<Hex_Coord?>{
            val ret = arrayListOf<Hex_Coord?>()
            fun add(_coord: Hex_Coord){
                if (coord_is_safe(_coord)){
                    ret.add(_coord)
                }else{
                    ret.add(null)
                }
            }
            val magic_vert_numbers = Pair(
                    //magic numbers that represent line along the edge of the hexes
                    arrayOf(4,1,2,0,3,5),
                    arrayOf(4,1,3,5,2,0)
            )
            add(coord.transl_x(1))
            add(coord.transl_x(-1))
            add(coord.transl_y(1))
            add(coord.transl_y(-1))
            if (coord.x % 2 == 0){
                add(coord.transl(1, -1))
                add(coord.transl(-1,-1))
            }else{
                add(coord.transl(1,1))
                add(coord.transl(-1,1))
            }
            return ret.toTypedArray()
        }

        fun get_all_coords(): Array<Hex_Coord>{
            val ret = arrayListOf<Hex_Coord>()
            for (y in 0 until prov_lookup.size){
                for (x in 0 until prov_lookup[0].size){
                    ret.add(Hex_Coord(x , y))
                }
            }
            return ret.toTypedArray()
        }

        private fun coord_is_safe(hex_coord: Hex_Coord): Boolean{
            return (hex_coord.x > -1 && hex_coord.x < prov_lookup[0].size &&
                    hex_coord.y > -1 && hex_coord.y < prov_lookup.size)
        }
    }




    private data class Prov(val sprite_Idx: Sprite_Idx, val hex_coords: Array<Hex_Coord>){
        fun get_new_different_sprite(sprite_Idx: Sprite_Idx): Prov{
            return Prov(sprite_Idx, hex_coords)
        }
    }

}