package Gdx_Risk

import Gdx_Risk.Map.Hex_Coord
import Gdx_Risk.Render.*

//add hex_coord -> prov map
//add prov -> player
//add player -> provs
//add dicerolls
//add get renderable hexes
class Map_Model() {
    init{

    }

    inner class Map(){
        init {

        }

    }


    private data class Prov(val sprite_Idx: Sprite_Idx, val hex_coords: Array<Hex_Coord>){
        fun get_new_different_sprite(sprite_Idx: Sprite_Idx): Prov{
            return Prov(sprite_Idx, hex_coords)
        }
    }

}