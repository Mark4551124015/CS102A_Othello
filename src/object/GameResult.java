package object;

import component.animation.Animator;
import graphics.Image;
import object.GUI.Buttons.NormalButton;

public class GameResult extends OthelloObject{
    private OthelloObject VictoryMenu;
    private OthelloObject DefeatMenu;
    private NormalButton Restart;
    private NormalButton BackToLobby;
    private Animator EndingMenu;
    private Animator EndingMenuA;
    private boolean wantRestart;
    private boolean wantBackToLobby;

    public GameResult(){
        super("GameEnding");

        this.VictoryMenu = new OthelloObject("VictoryMenu");
        this.setSprite(new Image("popo"));
//        this.VictoryMenu.setPosition(MainManuButtonsPivot);
        this.VictoryMenu.setAlpha(0);
        this.DefeatMenu = new OthelloObject("DefeatMenu");
        this.setSprite(new Image("popo"));
//        this.DefeatMenu.setPosition(MainManuButtonsPivot);
        this.DefeatMenu.setAlpha(0);
        this.Restart = new NormalButton("Restart");
        this.BackToLobby = new NormalButton("Back To Lobby");

        this.addObj(this.VictoryMenu);
        this.addObj(this.DefeatMenu);
        this.VictoryMenu.addObj(this.Restart);
        this.DefeatMenu.addObj(this.Restart);
        this.VictoryMenu.addObj(this.BackToLobby);
        this.DefeatMenu.addObj(this.BackToLobby);

        this.Restart.setPosition(50,50);
        this.BackToLobby.setPosition(50,100);

        this.EndingMenu = new Animator(0);
        this.EndingMenuA = new Animator(0);
        this.addComponent(this.EndingMenu);
        this.addComponent(this.EndingMenuA);
    }

    private void VictoryMenu_update(double dt) {
        this.VictoryMenu.setPosition(0,0);
        this.VictoryMenu.setAlpha(this.EndingMenuA.val());
    }

    private void DefeatMenu_update(double dt) {
        this.DefeatMenu.setPosition(0,0);
        this.DefeatMenu.setAlpha(this.EndingMenuA.val());
    }

    private void EndingButton_setActive(boolean flag) {
        this.Restart.setActive(flag);
        this.BackToLobby.setActive(flag);
    }

    public boolean isWantRestart(){
        return this.wantRestart;
    }

    public boolean isWantExitToLobby() {
        return this.wantBackToLobby;
    }

    @Override
    public void update(double dt){
        if(Restart.isClicked()){
            this.wantRestart = true;
        }
        if(BackToLobby.isClicked()){
            this.wantBackToLobby = true;
        }
        super.update(dt);
    }
}
