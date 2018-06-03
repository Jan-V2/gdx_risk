package Gdx_Risk;

import Gdx_Risk.Map.Coord;
import Gdx_Risk.Map.Prov;
import Gdx_Risk.Map.Prov_Id;
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
import com.github.czyzby.kiwi.util.tuple.immutable.Triple;
import com.github.czyzby.kiwi.util.tuple.mutable.MutablePair;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Assets {
    static int hex_size;
    static int halfhex;
    static int quarthex;
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
    static HashMap<Integer, Prov_Id[]> navtree;

    static Prov_Lookup prov_lookup;
    static private float[] hex_outline_vert_coords;
    static private PolygonSprite sea_poly;
    static private PolygonSpriteBatch polyBatch;
    static private ShapeRenderer shape_renderer;
    static private Color sea_hex_color;
    static private String map_data_file_path;
    static private PolygonSprite[] prov_sprite_array;
    static private Pair<Coord, Integer>[] prov_edge_verts;
    static private Pair<Pair<Object, Float>, Pair<Object, Float>>[] hex_outline_verts;
    static private String[] prov_names;



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
                hex_outline_vert_coords = Create_Hex_sprite.hex_polygon_vertecies_creator();
                build_hex_outline_verts();
                polyBatch= new PolygonSpriteBatch();
                shape_renderer = new ShapeRenderer();
                Prov_edge_verts.build();
            }
            private void build_hex_outline_verts(){
                //verts are stored like this Pair(Pair(x1,y1),Pair(x2,y2))
                hex_outline_verts =new Pair[hex_outline_vert_coords.length/2];
                for (int i = 0; i < hex_outline_vert_coords.length/2; i++) {

                    MutablePair<Object, Float> x1_y1;
                    MutablePair<Object, Float> x2_y2;
                    int index =i*2;
                    if (i<5) {//to fix out of bounds exeption

                        x1_y1 = new MutablePair<Object, Float>(hex_outline_vert_coords[index],hex_outline_vert_coords[index+1]);
                        x2_y2 = new MutablePair<Object, Float>(hex_outline_vert_coords[index+2],hex_outline_vert_coords[index+3]);
                    }else {
                        x1_y1 = new MutablePair<Object, Float>(hex_outline_vert_coords[index],hex_outline_vert_coords[index+1]);
                        x2_y2 = new MutablePair<Object, Float>(hex_outline_vert_coords[0],hex_outline_vert_coords[1]);
                    }

                    Pair<Pair<Object, Float>, Pair<Object, Float>> vert = new Pair<>(x1_y1.toImmutable(),x2_y2.toImmutable());
                    hex_outline_verts[i]=vert;
                }
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
        loading.load_prov_names();
        navtree=NavTree.build();
    }

    class Prov_Lookup{
        private int[][] prov_lookup;

        Prov_Lookup(int[][] prov_lookup){
            this.prov_lookup = prov_lookup;
        }

        Prov_Id resolve_prov_id(Coord coord){
            //dumps coords if it's outside of the grid
            System.out.println(coord);
            if ( coord.getX()<0|| coord.getY()<0||
                    !(coord.getX() < hexes_per_row)|| !(coord.getY() < hexes_per_column)){
                return new Prov_Id(-1);
            }else {
                return new Prov_Id(prov_lookup[coord.getY()][coord.getX()]);
            }
        }

        int get_y_len(){
            return this.prov_lookup.length;
        }
        int get_x_len(){
            return this.prov_lookup[0].length;
        }
    }

    static String get_prov_name(Prov_Id prov_id){
        return prov_names[prov_id.to_int()];
    }

    private static class NavTree{
        // builds a 2d array where each index is a prov id and contains the provs it's connected to
        // todo turn this into a hashmap
        static Prov_Id[][]  build() {
            //esch index matches a province id
            //each indexindex is a province it's connected to
            int[][] nav_tree = new int[prov_sprite_array.length][];
            ArrayList<Prov_Id> connected_provs = new ArrayList<>();

            // Searches each index in the prov_lookup and looks for neighbours bij comparing prov_ids
            //TODO? this could be a lot more efficient
            //TODO this method does not search the provences on the edge of the grid
            for (int current_prov_id = 0; current_prov_id < prov_sprite_array.length; current_prov_id++) {
                for (int prov_x = 1; prov_x < prov_lookup.get_y_len() - 2; prov_x++) {
                    for (int prov_y = 1; prov_y < prov_lookup.get_x_len() - 2; prov_y++) {
                        connected_provs = check_prov(new Coord(prov_x, prov_y), new Prov_Id(current_prov_id) ,connected_provs);
                    }
                }
                nav_tree[current_prov_id] = Util.int_arraylist_to_array(connected_provs);
                connected_provs.clear();
            }

            //for debugging
            /*
            int prov = 0;
            for (int[] province:nav_tree) {
                System.out.print("prov "+prov+": ");
                for (int connection: province){
                    System.out.print(connection+" ");
                }
                prov++;
                System.out.print("\n");
            }
            System.out.print("\n");*/
            return nav_tree;
        }
        private static ArrayList<Prov_Id> check_prov(Coord prov, Prov_Id currentprov, ArrayList<Prov_Id> connected_provs){
            //currentprov is the prof it's checking the borders of
            //because the id for sea is -1 it should never come up

            if (prov_lookup.resolve_prov_id(prov) == currentprov){
                connected_provs = compare_provs(prov.transl_y(1), currentprov, connected_provs);
                connected_provs = compare_provs(prov.transl_y(-1), currentprov, connected_provs);
                connected_provs = compare_provs(prov.transl_x(1), currentprov, connected_provs);
                connected_provs = compare_provs(prov.transl_x(-1), currentprov, connected_provs);
                //the only part that's different depending on whether the collum is even or not
                if (prov.getX() % 2==0) {
                    connected_provs = compare_provs(prov.transl(1 , -1), currentprov, connected_provs);
                    connected_provs = compare_provs(prov.transl(-1, -1), currentprov, connected_provs);
                }else{
                    connected_provs = compare_provs(prov.transl(1, 1), currentprov, connected_provs);
                    connected_provs = compare_provs(prov.transl(-1,1 ), currentprov, connected_provs);
                }
            }

            return connected_provs;
        }
        private static ArrayList<Prov_Id> compare_provs(Coord prov, Prov_Id current_prov, ArrayList<Prov_Id> connected_provs){
            Prov_Id compare_prov_id = prov_lookup.resolve_prov_id(prov);
            if (compare_prov_id.to_int() != -1){
                if (current_prov != compare_prov_id){
                    if (!connected_provs.contains(compare_prov_id)){
                        connected_provs.add(compare_prov_id);
                    }
                }
            }
            return connected_provs;
        }
    }

    private static class Prov_edge_verts{
        // Creates an array containing the vertecies of the edges of provinces in the following format: prov_x, prov_y, vert_num
        //TODO could be more efficient. draws a lot of lines twice.
        static void build(){

            ArrayList<Pair<Coord, Integer>> prov_edge_verts_list = new ArrayList<>();
            for (int prov_x = 0; prov_x < prov_lookup.length; prov_x++) {
                for (int prov_y = 0; prov_y < prov_lookup[0].length; prov_y++) {
                    check_prov(new Coord(prov_x,prov_y),prov_edge_verts_list);
                }
            }

            prov_edge_verts = prov_edge_verts_list.toArray(new Pair[0]);
        }

        private static ArrayList<Pair<Coord, Integer>>
                   check_prov(Coord c_prov,
                   ArrayList<Pair<Coord, Integer>> prov_edge_verts_list){
            //currentprov is the prof it's checking the borders of
            //because the id for sea is -1 it should never come up
            //vert_num is the hex vert index in hex_outline_verts

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

        private static ArrayList<Pair<Coord, Integer>> compare_provs(Coord prov, Coord currentprov
                , ArrayList<Pair<Coord, Integer>> prov_edge_verts_list, int vert_num){

            if (prov.getX()<0|| prov.getX()>prov_lookup.length-1|| prov.getY()<0|| prov.getY()>prov_lookup[0].length-1){
                //if the coordinate is outside of the hex grid and it it's not from a sea prov it automatically adds a line
                if (prov_lookup.resolve_prov_id(currentprov).to_int()!=-1){
                    prov_edge_verts_list.add(new Pair<>(currentprov, vert_num));
                }
            }else{
                
                if (prov_lookup.resolve_prov_id(currentprov) != prov_lookup.resolve_prov_id(prov)){
                    prov_edge_verts_list.add(new Pair<>(currentprov, vert_num));
                }
            }
            return prov_edge_verts_list;
        }
    }



    private static class Create_Hex_sprite{
        public static PolygonSprite polygon_sprite_builder(Color color)		{
            //creates polygon

            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(color);
            pixmap.fill();

            Texture textureSolid = new Texture(pixmap);
            pixmap.dispose();

            PolygonRegion polyReg = new PolygonRegion(new TextureRegion(textureSolid),
                    hex_polygon_vertecies_creator(), hex_triangle_creator());

            PolygonSprite sprite = new PolygonSprite(polyReg);

            return sprite;
        }
        private static float[] hex_polygon_vertecies_creator()	{
            //origin of hex_polygon is in the bottom left corner of the square
            //array is x1, y1, x2, y2, ect...

            float[] hex_polygon = new float[12];
            hex_polygon[0]= 0;
            hex_polygon[1]=	halfhex;
            hex_polygon[2]= quarthex;
            hex_polygon[3]=	hex_size;
            hex_polygon[4]= halfhex + quarthex;
            hex_polygon[5]= hex_size;
            hex_polygon[6]= hex_size;
            hex_polygon[7]=	halfhex;
            hex_polygon[8]= halfhex + quarthex;
            hex_polygon[9]=	0;
            hex_polygon[10]= quarthex;
            hex_polygon[11]= 0;
            return hex_polygon;
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

    static class HexGrid {
        static boolean did_render = false;
        static void render(){
            //renders hexgrid

            if (!did_render){
                int outline_origin_X = 0; //(scr_width % (halfhex+quarthex)) / 4;//TODO ofset set to 0 for debugging
                int outline_origin_Y = scr_height-hex_size-halfhex-30; //((scr_height - halfhex) % (hexsize))/ 2;//why do i have to divide by 4?
                //-30 to make room for button bar
                //Y asxis flipped to line up with mouse axis

                int drawnhex_origin_Y = outline_origin_Y;

                polyBatch.begin();
                for(int i=0; i<hexes_per_row; i++){
                    if (i % 2 == 0)	{
                        drawnhex_origin_Y += halfhex;
                    }
                    for(int j=0; j<hexes_per_column; j++){
                        if (prov_lookup[j][i] != -1) {//if it's sea
                            PolygonSprite sprite = prov_sprite_array[Game.Data.who_owns(
                                    new Prov_Id( prov_lookup[j][i]))];
                            draw_hex(sprite, outline_origin_X, drawnhex_origin_Y);
                        }
                        drawnhex_origin_Y = drawnhex_origin_Y -hex_size;
                    }
                    outline_origin_X += halfhex+quarthex;
                    drawnhex_origin_Y = outline_origin_Y;
                }
                polyBatch.end();
                draw_borders();
            }
            //did_render = true;
        }

        //TODO add alphablending
        private static void draw_hex(PolygonSprite hex_poly_sprite, int origin_X, int origin_Y){
            //draws the hex
            float alphablending = 0.92f;
            hex_poly_sprite.setPosition(origin_X, origin_Y);

            hex_poly_sprite.draw(polyBatch);
        }

        private static void draw_borders(){
            int hex_width = halfhex +quarthex;
            int hex_height = hex_size;
            int y_offset = scr_height-hex_size-halfhex-30;

            shape_renderer.setAutoShapeType(true);
            shape_renderer.setColor(0f,0f,0f,1);

            shape_renderer.begin();
            for (Triple line: prov_edge_verts){
                float offset_x = (float) ((int)line.getFirst()*hex_width);
                float offset_y = (float)(y_offset - (hex_height*(int)line.getSecond()));
                if ((int)line.getFirst()%2==0){
                    offset_y += halfhex;
                }

                Pair i = hex_outline_verts[(int)line.getThird()];
                Pair j = (Pair)i.getFirst();
                Pair k = (Pair)i.getSecond();

                float x1=(float)j.getFirst()+offset_x;
                float y1 = (float)j.getSecond()+offset_y;
                float x2 = (float)k.getFirst()+offset_x;
                float y2 = (float)k.getSecond()+offset_y;


                shape_renderer.line(x1,y1,x2,y2);
                // this seems the be the frame hog
            }
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
            prov_sprite_array = tmp_hex_sprite_array.toArray(new PolygonSprite[0]);


            //converts 2d arraylist to array
            prov_lookup = new int[tmp_prov_lookup_list.size()][tmp_prov_lookup_list.get(0).size()];
            for (int i = 0; i <tmp_prov_lookup_list.size() ; i++) {
                prov_lookup[i]= Util.int_arraylist_to_array(tmp_prov_lookup_list.get(i));
            }
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
            halfhex = hex_size / 2;
            quarthex = halfhex / 2;
            scr_height = Gdx.graphics.getHeight();
            scr_width = Gdx.graphics.getWidth();
            hexes_per_column = ((scr_height - halfhex)-30 )/ (hex_size);
            hexes_per_row = scr_width/ (halfhex+quarthex);
        }
    }

}
