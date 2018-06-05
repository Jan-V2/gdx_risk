package Gdx_Risk;

import Gdx_Risk.Map.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.czyzby.kiwi.util.tuple.immutable.Pair;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Assets {
    //todo get rid of as many as possible
    static int hex_size;
    static int half_hex;
    static int quart_hex;
    static int scr_height;
    static int scr_width;
    static int hexes_per_column;
    static int hexes_per_row;
    static String asset_directory_path;
    static boolean render_infobar_background_bool = false;
    static int test;

    static float sea_color_float[];//background color
    static String[] libgdx_colors;
    static int no_provs;
    //static HashMap<Prov_Id, Prov_Id[]> navtree;

    static int[][] prov_lookup;

    static Render renderer;
    static Map_Model map_model;


    static int[] hex_outline_points;
    static PolygonSprite sea_poly;
    static PolygonSpriteBatch polyBatch;
    static ShapeRenderer shape_renderer;
    static Color sea_hex_color;
    static String map_data_file_path;
    static PolygonSprite[] hex_sprite_array;
    static Pair<Hex_Coord, Integer>[] prov_borders;
    static String[] prov_names;

//todo fix navmap and and border thing also combine them, since they basically do the same thing
//todo fix provid comparision
    static void load()  {

        class Loading{
            private void load_file_paths(){
                //loads file paths
                asset_directory_path = (System.getProperty("user.dir")+"/assets");
                map_data_file_path = asset_directory_path + "/map_data.txt";
            }
            private void load_prov_names() {
                prov_names = new String[]{"Ruthinia", "Austramerica","Chinesia",
                        "Bekistan", "New Chinesia", "Balkania","Germania","Scandiweegia",
                        "New Merica","Isorrowia"};
            }
            private void load_colors(){
                sea_color_float = new float[4];
                sea_color_float [0] =0f;
                sea_color_float [1] =0.47f;
                sea_color_float [2] =0.95f;
                sea_color_float [3] =0.9f;

                sea_hex_color = new Color(sea_color_float[0],sea_color_float[1],sea_color_float[2],sea_color_float[3]);

                libgdx_colors = new String[]{
                        "GOLD", "GOLDENROD", "GRAY", "GREEN", "LIGHT_GRAY", "LIME", "MAGENTA",
                        "MAROON", "NAVY", "OLIVE", "ORANGE", "PINK", "PURPLE", "RED", "ROYAL", "SALMON",
                        "SCARLET", "SKY", "SLATE", "TAN", "TEAL", "VIOLET", "WHITE", "YELLOW"};
                //asset_manager.editor_colors = asset_manager.Editor_color[libgdx_colors.length];
            }
            private void load_graphical_assets(){
                sea_poly = Create_Hex_sprite.polygon_sprite_builder(sea_hex_color);
                hex_outline_points = Create_Hex_sprite.get_hex_outline_points_int();
                polyBatch= new PolygonSpriteBatch();
                shape_renderer = new ShapeRenderer();
                //Prov_edge_verts.build();
            }


        }

        Loading loading = new Loading();
        loading.load_file_paths();
        try {
            IO.load_map_data();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //configdata loaded from io because it's needed to make sprites
        loading.load_colors();
        loading.load_graphical_assets();
        //loading.load_prov_names();
        renderer = new Render();
        map_model = new Map_Model(prov_lookup);
        //navtree=NavTree.build();
    }




    static String get_prov_name(Prov_Id prov_id){
        return prov_names[prov_id.to_int()];
    }

/*

    private static class Prov_edge_verts{
        // Creates an array containing the vertecies of the edges of provinces in the following format: prov_x, prov_y, vert_num
        //TODO could be more efficient. draws a lot of lines twice.
        static void build(){

            ArrayList<Pair<Hex_Coord, Integer>> prov_edge_verts_list = new ArrayList<>();
            for (int prov_y = 0; prov_y < prov_lookup.get_y_len(); prov_y++) {
                for (int prov_x = 0; prov_x < prov_lookup.get_x_len(); prov_x++) {
                    prov_edge_verts_list = check_prov(new Hex_Coord(prov_x, prov_y),prov_edge_verts_list);
                }
            }

            prov_borders = prov_edge_verts_list.toArray(new Pair[0]);
        }

        private static ArrayList<Pair<Hex_Coord, Integer>>
                   check_prov(Hex_Coord c_prov, ArrayList<Pair<Hex_Coord, Integer>> prov_edge_verts_list){
            //currentprov is the prov it's checking the borderSegments of
            //because the id for sea is -1 it should never come up
            //vert_num is the hex vert index in hex_outline_lines

            prov_edge_verts_list = compare_provs(c_prov.transl_y(1), c_prov, prov_edge_verts_list, 4);
            prov_edge_verts_list = compare_provs(c_prov.transl_y(-1), c_prov, prov_edge_verts_list, 1);
            //the part that's different depending on whether the collum is even or not
            if (c_prov.getX() % 2==0) {
                prov_edge_verts_list = compare_provs(c_prov.transl_x(1), c_prov, prov_edge_verts_list, 3);
                prov_edge_verts_list = compare_provs(c_prov.transl_x(-1), c_prov, prov_edge_verts_list, 5);
                prov_edge_verts_list = compare_provs(c_prov.transl(1, -1), c_prov, prov_edge_verts_list,2);
                prov_edge_verts_list = compare_provs(c_prov.transl(-1, -1), c_prov, prov_edge_verts_list,0);
            }else{
                prov_edge_verts_list = compare_provs(c_prov.transl_x(1), c_prov, prov_edge_verts_list, 2);
                prov_edge_verts_list = compare_provs(c_prov.transl_x(-1), c_prov, prov_edge_verts_list, 0);
                prov_edge_verts_list = compare_provs(c_prov.transl(1,1), c_prov, prov_edge_verts_list, 3);
                prov_edge_verts_list = compare_provs(c_prov.transl(-1, 1), c_prov, prov_edge_verts_list, 5);
            }

            return prov_edge_verts_list;
        }

        private static ArrayList<Pair<Hex_Coord, Integer>> compare_provs(Hex_Coord prov, Hex_Coord currentprov
                , ArrayList<Pair<Hex_Coord, Integer>> prov_edge_verts_list, int vert_num){
            if (prov_lookup.get_prov_id(currentprov).to_int()!=-1){
                if (prov.getX()<0|| prov.getX()>prov_lookup.get_y_len()-1|| prov.getY()<0|| prov.getY()>prov_lookup.get_x_len()-1){
                    //if the coordinate is outside of the hex grid and it it's not from a sea prov it automatically adds a line

                    prov_edge_verts_list.add(new Pair<>(currentprov, vert_num));

                }else{
                    if (prov_lookup.get_prov_id(currentprov).id() != prov_lookup.get_prov_id(prov).id()){
                        if (prov_lookup.get_prov_id(prov).to_int() == prov_lookup.get_prov_id(currentprov).to_int()){
                            System.out.println("not working");
                        }
                        prov_edge_verts_list.add(new Pair<>(currentprov, vert_num));
                    }
                }
            }
            return prov_edge_verts_list;
        }
    }

*/

    private static class Create_Hex_sprite{
        public static PolygonSprite polygon_sprite_builder(Color color)		{
            //creates polygon

            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(color);
            pixmap.fill();

            Texture textureSolid = new Texture(pixmap);
            pixmap.dispose();

            PolygonRegion polyReg = new PolygonRegion(new TextureRegion(textureSolid),
                    get_hex_outline_points(), hex_triangle_creator());

            return new PolygonSprite(polyReg);
        }
        static float[] get_hex_outline_points()	{//TODO FIX code
            //origin of hex_polygon is in the bottom left corner of the square
            //array is x1, y1, x2, y2, ect...

            float[] hex_polygon = new float[12];
            hex_polygon[0]= 0;
            hex_polygon[1]= half_hex;
            hex_polygon[2]= quart_hex;
            hex_polygon[3]=	hex_size;
            hex_polygon[4]= half_hex + quart_hex;
            hex_polygon[5]= hex_size;
            hex_polygon[6]= hex_size;
            hex_polygon[7]= half_hex;
            hex_polygon[8]= half_hex + quart_hex;
            hex_polygon[9]=	0;
            hex_polygon[10]= quart_hex;
            hex_polygon[11]= 0;
            return hex_polygon;
        }
        static int[] get_hex_outline_points_int()	{
            int[] ret =  new int[12];
            float[] f_array = get_hex_outline_points();
            for (int i = 0; i <12 ; i++) {
                ret[i] = (int) f_array[i];
            }
            return ret;
        }

        private static short[] hex_triangle_creator() {
            short[] hex_triangles = new short[]{0, 1, 2, 0, 2, 3, 0, 5, 4, 0, 4, 3};
            return hex_triangles;

        }
    }

    static void render_infobar_background(){
        if (render_infobar_background_bool){
            shape_renderer.begin(ShapeRenderer.ShapeType.Filled);
            shape_renderer.setColor(0.36f,0.36f,0.36f,1f);
            shape_renderer.rect(0f,0f,(float)Gdx.graphics.getWidth(),UI.info_bar.info_bar_height);
            shape_renderer.end();
        }
    }


    public static class Util{

        public static int Strip_non_digits_return_int( final CharSequence input ){
            // takes a string, strips the non digests and returns them as an int.
            final StringBuilder sb = new StringBuilder(
                    input.length() );
            for(int i = 0; i < input.length(); i++){
                final char c = input.charAt(i);
                if(c > 47 && c < 58){
                    sb.append(c);
                }
            }
            return Integer.parseInt(sb.toString());
        }

        public static int[] int_arraylist_to_array(ArrayList<Integer> list){
            int[] ints = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                ints[i]= list.get(i);
            }
            return ints;
        }
    }

    private static class IO{
        public static void load_map_data() throws IOException { //main methode that's called to save state to disk
             InputStream inputStream = new FileInputStream(map_data_file_path);
            Reader inputStreamReader = new InputStreamReader(inputStream);

            String file_input = "";
            int data = inputStreamReader.read();
            while(data != -1){
                char the_char = (char) data;
                file_input +=the_char;
                data = inputStreamReader.read();
            }
            inputStreamReader.close();
            List<String> file_input_list = Arrays.asList(file_input.split("\\n"));

            Pair<ArrayList<ArrayList<Integer>>, ArrayList<PolygonSprite>>  parsed = parse_conf_file(file_input_list);

            ArrayList<ArrayList<Integer>> tmp_prov_lookup_list = parsed.getFirst();
            ArrayList<PolygonSprite> tmp_hex_sprite_array = parsed.getSecond();

            no_provs = tmp_hex_sprite_array.size();
            hex_sprite_array = tmp_hex_sprite_array.toArray(new PolygonSprite[0]);


            //converts 2d arraylist to array
            int[][] _prov_lookup = new int[tmp_prov_lookup_list.size()][tmp_prov_lookup_list.get(0).size()];
            for (int i = 0; i <tmp_prov_lookup_list.size() ; i++) {
                _prov_lookup[i]= Util.int_arraylist_to_array(tmp_prov_lookup_list.get(i));
            }

            prov_lookup = _prov_lookup;
        }

        private static Pair<ArrayList<ArrayList<Integer>>, ArrayList<PolygonSprite>>
        parse_conf_file(List<String> file_input_list){

            ArrayList<ArrayList<Integer>> tmp_prov_lookup_list = new ArrayList<>();
            ArrayList<PolygonSprite> tmp_hex_sprite_array = new ArrayList<>();

            boolean reading_screen_data =false;
            boolean reading_color_data =false;
            boolean reading_prov_lookup =false;
            for (String line: file_input_list){
                if(line.contains("/") ){
                    if (!reading_screen_data && !reading_color_data && !reading_prov_lookup){
                        if(line.contains("/SCREENDATA")){
                            reading_screen_data = true;
                        }else if(line.contains("/COLORS")){
                            reading_color_data = true;
                        }else if(line.contains("/PROV_LOOKUP")){
                            reading_prov_lookup = true;
                        }
                    }else if (line.contains("/END")){
                        reading_screen_data =false;
                        reading_color_data =false;
                        reading_prov_lookup =false;
                    }
                }else if (reading_screen_data){
                    //TODO impliment width/height
                    //SCREEN_WITH
                    //SCREEN_HEIGHT
                    if (line.contains("HEX_SIZE")){
                        hex_size = Util.Strip_non_digits_return_int(line);
                        build_config_data();//done from here because it's needed for making hex_sprites
                    }
                }else if (reading_color_data){
                    // Colors stored like this:
                    // province no. 1:0.6901961,0.1882353,0.3764706,1.0
                    String[] line_split = line.split(":");
                    String[] color_values_split = line_split[1].split(",");
                    float[] color_val_float = new float[color_values_split.length];

                    for (int i = 0; i <color_values_split.length ; i++) {
                        color_val_float[i] = Float.parseFloat(color_values_split[i]);
                    }

                    // Turns the rgba float values into color objects.
                    //adds in onto the color array
                    tmp_hex_sprite_array.add(
                            Create_Hex_sprite.polygon_sprite_builder(
                                    new  Color(color_val_float[0], color_val_float[1],
                                            color_val_float[2] ,color_val_float[3])));


                }else if (reading_prov_lookup){
                    String[] tmp_array = line.split(",");

                    ArrayList<Integer> tmp_int_list =new ArrayList<>();
                    for (String prov_lookup_val_string:tmp_array){
                        tmp_int_list.add(Integer.parseInt(prov_lookup_val_string));
                    }
                    tmp_prov_lookup_list.add(tmp_int_list);
                }
            }

            return new Pair<>(tmp_prov_lookup_list, tmp_hex_sprite_array);
        }

        private static void build_config_data (){
            //hexsize from io
            half_hex = hex_size / 2;
            quart_hex = half_hex / 2;
            scr_height = Gdx.graphics.getHeight();
            scr_width = Gdx.graphics.getWidth();
            hexes_per_column = ((scr_height - half_hex)-30 )/ (hex_size);
            hexes_per_row = scr_width/ (half_hex + quart_hex);
        }
    }

}
