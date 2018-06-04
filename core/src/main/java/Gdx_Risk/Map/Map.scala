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


class Hex(/*val hex_coord: Hex_Coord,*/ sprite: PolygonSprite)

//todo figure out why i can't instantiate class from withing itself




case class Prov_Id(id:Int)  {
  def to_int(): Int = id
  override def toString: String = {id.toString}

  override def hashCode(): Int = id.hashCode()
  override def equals(o: scala.Any): Boolean = id.equals(o)
}
