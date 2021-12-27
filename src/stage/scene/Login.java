package stage.scene;

import graphics.Image;
import main.PlayerManager;
import main.mainApp;
import newData.Vct;
import object.GUI.Buttons.NormalButton;
import object.GUI.InputBox;
import object.GUI.Menu.ChoosingMenu;
import object.OthelloObject;
import stage.GameStage;

import static main.GameManager.playerManager;

public class Login extends OthelloObject implements GameStage {
    
    public Login(){
        super("Scene_Login");
    }

    private OthelloObject bg;
    private OthelloObject loginBox;
    private NormalButton New;
    private NormalButton Delete;
    private InputBox UsernameInput;
    private ChoosingMenu choosingMenu;
    private boolean logged;
    @Override
    public void update(double dt) {
        super.update(dt);
//        if (this.UsernameInput.isSubmitted()) {
//            try {
//                Player User = playerManager.getPlayer(this.UsernameInput.getResult());
//                this.logged = true;
//            }catch (Exception e) {
//                AttentionManager.showWarnMessage("Invalid Player");
//                this.UsernameInput.setSubmitted(false);
//            }
//        }
        this.createCheck();
        this.deleteCheck();
        if (this.choosingMenu.isSubmitted()) {
            try {
                PlayerManager.User = playerManager.getPlayer(this.choosingMenu.getResult());
                this.logged = true;
            }catch (Exception e) {
                System.out.println("Invalid Player");
                this.choosingMenu.setSubmitted(false);
            }
        }
    }

    @Override
    public void init(){
        this.bg = new OthelloObject("Login_BG", new Image("LoginBG"));
        this.addObj(this.bg);
        this.bg.setPosition(mainApp.Width/2,mainApp.Height/2);
        this.loginBox = new OthelloObject("LoginBox", new Image("LoginBox"));
        this.loginBox.resizeTo(mainApp.Width/1.7, mainApp.Height/2);
        this.New = new NormalButton("New");
        this.Delete = new NormalButton("Del");

        this.New.setPosition(90,130);
        this.Delete.setPosition(220,130);

        this.loginBox.addObj(this.New);
        this.loginBox.addObj(this.Delete);

        this.UsernameInput = new InputBox(new Vct(320,60),"Username",10);
        this.loginBox.addObj(this.UsernameInput);
        this.bg.addObj(this.loginBox);
        this.logged = false;
        this.choosingMenu = new ChoosingMenu(playerManager.getPlayersName(), "Choose User");
        this.choosingMenu.setPosition(0,-5);
        this.loginBox.addObj(this.choosingMenu);
        this.loginBox.setPosition(0,100);
        this.UsernameInput.setPosition(-120,130);
        this.New.setActive(true);
        this.Delete.setActive(true);

    }

    @Override
    public GameStageID getGameStageID() {
        return GameStageID.Login;
    }

    public void createCheck(){
        if (this.New.isClicked()) {
            if (this.UsernameInput.getResult()!=null) {
                String name = this.UsernameInput.getResult();
                playerManager.createPlayer(name,name);
            }
            this.choosingMenu.renew(playerManager.getPlayersName());

        }
    }

    public void deleteCheck(){
        if (this.Delete.isClicked()) {
            if (this.UsernameInput.getResult()!=null) {
                String name = this.UsernameInput.getResult();
                playerManager.deletePlayer(playerManager.getPlayer(name));
            }
            this.choosingMenu.renew(playerManager.getPlayersName());

        }
    }


    public boolean isLogged(){
        return this.logged;
    }
}
