package Gdx_Risk.Map

import com.badlogic.gdx.graphics.g2d.PolygonSprite

class Map() {

  def render(): Unit ={

  }
}

class Prov(hexes: Array[Hex]){
  def get_self(): Prov = {
    this.getClass.newInstance()

  }
}


class Hex(/*val coord: Coord,*/ sprite: PolygonSprite)

//todo figure out why i can't instantiate class from withing itself




class Prov_Id(val id:Int) extends AnyVal{
  def to_int(): Int = id

  override def toString: String = {id.toString}
}