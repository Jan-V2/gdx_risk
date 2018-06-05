package Gdx_Risk.Map;

public class Prov_Lookup{
    private int[][] prov_lookup;
    private int hexes_per_column;
    private int hexes_per_row;

    public Prov_Lookup(int[][] prov_lookup, int hexes_per_row, int hexes_per_column){
        this.prov_lookup = prov_lookup;
        this.hexes_per_column = hexes_per_column;
        this.hexes_per_row = hexes_per_row;
    }

    public Prov_Id resolve_prov_id(Hex_Coord hexCoord){
        //dumps coords if it's outside of the grid
        if ( hexCoord.getX()<0|| hexCoord.getY()<0||
                !(hexCoord.getX() < hexes_per_row)|| !(hexCoord.getY() < hexes_per_column)){
            return new Prov_Id(-1);
        }else {
            return new Prov_Id(prov_lookup[hexCoord.getY()][hexCoord.getX()]);
        }
    }

}