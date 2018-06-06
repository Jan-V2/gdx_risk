package Gdx_Risk;

import Gdx_Risk.Map.Hex_Coord;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import static Gdx_Risk.Assets.half_hex;
import static Gdx_Risk.Assets.model;
import static Gdx_Risk.Assets.quart_hex;

public class Hex_Intput_Processor implements InputProcessor {

    private Risk_Main risk_main;

    public Hex_Intput_Processor(Risk_Main risk_main) {
        this.risk_main = risk_main;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        //to filter out negative numbers
        if (screenX < 0 || screenY-30 < 0){
            return true;
        }

        Hex_Coord clicked_hex = resolve_hex(screenX,screenY);

        risk_main.mouse_move_handeler(model.getCrd_to_prv().get_prov_id(clicked_hex), screenX, screenY);
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //to filter out negative numbers
        if (screenX < 0 || screenY-30 < 0){
            return true;
        }

        Hex_Coord clicked_hex = resolve_hex(screenX,screenY);
        System.out.println(clicked_hex);

        risk_main.mouse_move_handeler(model.getCrd_to_prv().get_prov_id(clicked_hex), screenX, screenY);
        return true;
    }

    private Hex_Coord resolve_hex(int screenX, int screenY){
        //resolves the hex coordinates from mouse coordinates


        int grid_offsetX = 0; //(scr_width % (half_hex+quart_hex)) / 4;//TODO ofset set to 0 for debugging
        int grid_offsetY = 30; //((scr_height - half_hex) % (hexsize))/ 2;//why do i have to divide by 4?

        screenX = screenX - grid_offsetX;
        screenY = screenY - grid_offsetY; //first resolve column then row. if column is even then -half_hex

        Double tmp1 = (double) (screenX / quart_hex);
        int quarthex_X = tmp1.intValue();
        int hex_grid_X = quarthex_X / 3;//used both as final result and as full_hex_x

        Double tmp2 = (double) (screenY / half_hex + 0.5f);
        int halfhex_Y = tmp2.intValue();
        int hex_grid_Y = halfhex_Y / 2;

        int X_remain;
        int Y_remain;
        if (quarthex_X > 0)	{// to prevent divison / 0
            X_remain = screenX % (quarthex_X*quart_hex);
        } else	{
            X_remain = screenX;
        }

        if (halfhex_Y > 0)	{// to prevent divison / 0
            Y_remain = screenY % (halfhex_Y*half_hex);
        } else	{
            Y_remain = screenY;
        }

        int X_offset;
        int Y_offset;
        //calculates x offset
        if (quarthex_X % 3 > 0)	{//if it is in a straight collumn
            X_offset = 0;
        } else	{
            X_offset = X_grid_offset_calc(X_remain, Y_remain, halfhex_Y, hex_grid_X, quart_hex);
        }

        // calculates y offset
        if (halfhex_Y % 2 == 0)	{ //not sure what this line does, but it was in the origanal code.
            if (quarthex_X % 3 > 0)	{//if it's in a square row
                if (hex_grid_X % 2 == 0)	{
                    Y_offset = 0;
                } else	{
                    Y_offset = -1;
                }
            } else {
                Y_offset = Y_grid_offset_calc (X_remain, Y_remain, hex_grid_X, quart_hex);
            }
        } else	{
            Y_offset = 0;
        }
        hex_grid_Y = hex_grid_Y + Y_offset;
        hex_grid_X = hex_grid_X + X_offset;

        return new Hex_Coord(hex_grid_X, hex_grid_Y);
    }
    private int Y_grid_offset_calc (int X_remain, int Y_remain, int fullhex_X, int quarthex)		{
        int Y_offset;
        if (fullhex_X % 2 == 0)		{//even
            Y_offset = resolve_botL_topR(X_remain, Y_remain, quarthex);
        } else	{
            Y_offset = resolve_topL_BotR (X_remain, Y_remain);
            if (Y_offset == 0)	{//because the method returns the wrong offset but only at this point
                Y_offset = -1;
            } else	{
                Y_offset = 0;
            }
        }
        return Y_offset;
    }
    private int X_grid_offset_calc (int X_remain, int Y_remain, int halfhex_Y, int fullhex_X, int quarthex)	{
        int X_offset;
        if (fullhex_X % 2 == 0)	{//even
            if (halfhex_Y % 2 == 0)	{
                X_offset = resolve_botL_topR(X_remain, Y_remain, quarthex);
            } else	{
                X_offset = resolve_topL_BotR (X_remain, Y_remain);
            }
        } else	{//uneven
            if (halfhex_Y % 2 ==0) {
                X_offset = resolve_topL_BotR (X_remain, Y_remain);

            } else	{
                X_offset = resolve_botL_topR(X_remain, Y_remain, quarthex);

            }
        }
        return X_offset;
    }
    private int resolve_topL_BotR(int X_remain, int Y_remain)	{
        int offset;
        if (X_remain > Y_remain /2 )	{//if it's on the right
            offset = 0;
        } else	{
            offset = -1;
        }
        return offset;
    }
    private int resolve_botL_topR (int X_remain, int Y_remain, int quarthex)	{
        int offset;
        if (quarthex - X_remain > Y_remain /2)	{//if it's on the left
            offset = -1;
        } else	{
            offset = 0;
        }
        return offset;
    }



    //unused
    @Override
    public boolean keyDown(int keycode) {
        //System.out.println(keycode);
        if (keycode == Input.Keys.ENTER){
            //Game.State.Turn_State.advance_turn_state();
        }
        return true;
    }
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }
    @Override
    public boolean keyTyped(char character) {
        return false;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return true;
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }
    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
