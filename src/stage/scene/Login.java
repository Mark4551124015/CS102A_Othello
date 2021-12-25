package stage.scene;

import graphics.Image;
import main.mainApp;
import newData.Vct;
import object.GUI.Buttons.NormalButton;
import object.GUI.InputBox;
import object.OthelloObject;
import stage.GameStage;

public class Login extends OthelloObject implements GameStage {
    
    public Login(){
        super("Scene_Login");
    }

    private OthelloObject bg;
    private OthelloObject loginBox;
    private NormalButton SignIn;
    private InputBox UsernameInput;
    @Override
    public void update(double dt) {
        super.update(dt);
    }

    @Override
    public void init(){
        this.bg = new OthelloObject("Login_BG", new Image("LoginBG"));
        this.addObj(this.bg);
        this.bg.setPosition(mainApp.Width/2,mainApp.Height/2);
        this.loginBox = new OthelloObject("LoginBox", new Image("popo"));
        this.loginBox.resizeTo(mainApp.Width/1.7, mainApp.Height/1.7);
        this.SignIn = new NormalButton("Sign In");
        this.SignIn.setPosition(0,100);
        this.loginBox.addObj(this.SignIn);
        this.UsernameInput = new InputBox(new Vct(320,60),"Username",10);
        this.loginBox.addObj(this.UsernameInput);
        this.bg.addObj(this.loginBox);

    }

    @Override
    public GameStageID getGameStageID() {
        return GameStageID.Login;
    }

}
