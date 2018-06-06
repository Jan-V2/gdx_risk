package Gdx_Risk;

import Gdx_Risk.Map.Prov_Id;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.util.LmlApplicationListener;
import com.github.czyzby.lml.vis.util.VisLml;

import static Gdx_Risk.Assets.*;

public class Risk_Main extends LmlApplicationListener {

//TODO put all the todos into a .todo file
//TODO make the menu bar a real menu bar

//TODO figure out game screens
//TODO add continents
//TODO add cards
//TODO write load game data
//TODO write turn logic including ui changes
//TODO write victory conditions
//TODO write setup game wizard
//TODO write save game data
//TODO write basic ai?
//TODO increase resolution to 720p
//TODO improve visuals


    @Override
    public void create(){
        super.create();
        load();
        setView(FirstView.class);


        //sets background
        Gdx.gl.glClearColor(sea_color_float[0], sea_color_float[1], sea_color_float[2], sea_color_float[3]);
        //UI.init_UI();
        //Hex_Intput_Processor inputProcessor = new Hex_Intput_Processor(this);
        //InputMultiplexer multiplexer = new InputMultiplexer(getCurrentView().getStage(), inputProcessor);
        Gdx.input.setInputProcessor(getCurrentView().getStage());

    }

    @Override
    protected LmlParser createParser() {
        return VisLml.parser().build();
    }

    @Override
    public void render () {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render(model.get_render_hexes());
        render_infobar_background();
        getCurrentView().render();

/*
        UI.info_bar.update_dialog_timer(Gdx.graphics.getDeltaTime());
        //System.out.println(Gdx.graphics.getFramesPerSecond());
        UI.stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
        UI.stage.draw();
*/

    }

    void mouse_move_handeler(Prov_Id clicked_prov_id, int screenX, int screenY) {
/*        if (clicked_prov_id.to_int() != -1){
            UI.prov_info.setVisible(true);
            UI.set_prov_tooltip(clicked_prov_id,screenX,screenY);
        }else {
            UI.prov_info.setVisible(false);
        }*/
    }

    private void mouse_click_handeler(Prov_Id clicked_prov_id){ //, int screen_x, int screen_y // not used
/*        if (clicked_prov_id.to_int() != -1){
            if (clicked_prov_id!=Game.selected_prov
                    &&Game.Data.who_owns(clicked_prov_id)!=Game.active_player
                    &&Game.Data.is_connected(clicked_prov_id, Game.selected_prov)){//if attack is possible
                UI.call_confirm_attack_window(Game.selected_prov, clicked_prov_id);
            }
            if (Game.Data.who_owns(clicked_prov_id)== Game.active_player){
                Game.selected_prov = clicked_prov_id;
                System.out.print("the selected prov is "+get_prov_name(Game.selected_prov)+"\n");
            }

        }*/


    }
}

