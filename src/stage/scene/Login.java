package stage.scene;

import object.OthelloObject;
import stage.GameStage;

public class Login extends OthelloObject implements GameStage {
    
    public Login(){
        super("Scene_Login");
    }


    @Override
    public void update(double dt) {

    }



    @Override
    public void init(){

    }






    @Override
    public GameStageID getGameStageID() {
        return GameStageID.Login;
    }

}
