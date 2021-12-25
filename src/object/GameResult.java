package object;

import component.animation.Animator;
import graphics.Image;
import graphics.Text;
import main.ResourceManager;
import object.GUI.Buttons.NormalButton;

import java.awt.*;

import static main.PlayerManager.Competitor;
import static main.PlayerManager.User;


public class GameResult extends OthelloObject{
    private OthelloObject VictoryMenu;
    private OthelloObject DefeatMenu;
    private NormalButton Restart;
    private NormalButton BackToLobby;
    private Animator EndingMenu;
    private Animator EndingMenuA;
    private OthelloObject UserProfile;
    private OthelloObject CompetitorProfile;
    private OthelloObject White;
    private OthelloObject Black;
    private int WhiteCount;
    private int BlackCount;
    private Text WhiteCnt;
    private Text BlackCnt;
    private boolean wantRestart;
    private boolean wantBackToLobby;

    public GameResult(Player User,Player Competitor){
        super("GameEnding");

        this.VictoryMenu = new OthelloObject("VictoryMenu");
        this.setImage(new Image("User_info_backGround"));
        this.setSize(850,500);

        //        this.VictoryMenu.setPosition(MainManuButtonsPivot);
        this.VictoryMenu.setAlpha(0);
        this.DefeatMenu = new OthelloObject("DefeatMenu");
        this.setImage(new Image("User_info_backGround"));

        //        this.DefeatMenu.setPosition(MainManuButtonsPivot);
        this.DefeatMenu.setAlpha(0);
        this.Restart = new NormalButton("Restart");
        this.Restart.setActive(false);
        this.BackToLobby = new NormalButton("Back To Lobby");
        this.BackToLobby.setActive(false);
        this.Restart.setPosition(-250,170);
        this.BackToLobby.setPosition(250,170);
//        if(Restart.isClicked()){
//            System.out.println("111");
//        }

        this.UserProfile = new OthelloObject("UserProfile");
        ResourceManager.imgs.put(User.getUsername(),ResourceManager.loadImage("save/players/"+User.getUsername()+"/profile.png"));
        this.UserProfile.setImage(new Image(User.getUsername()));
        this.UserProfile.resizeTo(120,120);
        this.UserProfile.setPosition(-250,-140);

        this.CompetitorProfile = new OthelloObject("CompetitorProfile");
        ResourceManager.imgs.put(Competitor.getUsername(),ResourceManager.loadImage("save/players/"+Competitor.getUsername()+"/profile.png"));
        this.CompetitorProfile.setImage(new Image(Competitor.getUsername()));
        this.CompetitorProfile.resizeTo(120,120);
        this.CompetitorProfile.setPosition(250,-140);

        this.White = new OthelloObject("White");
        this.White.setImage(new Image("White_Disk"));
        this.White.resizeTo(150,150);
        this.White.setPosition(-250,0);

        this.Black = new OthelloObject("Black");
        this.Black.setImage(new Image("Black_Disk"));
        this.Black.resizeTo(150,150);
        this.Black.setPosition(250,0);

        this.WhiteCnt = new Text("WhiteCnt",toString(WhiteCount), new Font("黑体", Font.PLAIN,30));
        this.WhiteCnt.setPosition(-250,70);
        this.BlackCnt = new Text("BlackCnt",toString(BlackCount), new Font("黑体", Font.PLAIN,30));
        this.BlackCnt.setPosition(250,70);

        this.addObj(this.VictoryMenu);
        this.addObj(this.DefeatMenu);
        this.addObj(this.Restart);
        this.addObj(this.BackToLobby);
        this.addObj(this.UserProfile);
        this.addObj(this.CompetitorProfile);
        this.addObj(this.White);
        this.addObj(this.Black);
        this.addObj(this.WhiteCnt);
        this.addObj(this.BlackCnt);


        this.EndingMenu = new Animator(0);
        this.EndingMenuA = new Animator(0);
        this.addComponent(this.EndingMenu);
        this.addComponent(this.EndingMenuA);
    }



    private String toString(int Count) {
        return String.valueOf(Count);
    }

    public void setVictoryMenu(int a){
        this.VictoryMenu.setAlpha(a);
        this.UserProfile.resizeTo(150,150);
        if (User.getColor() == 1) {
            this.BlackCount = new Game(User,Competitor).getBlackCnt();
            this.WhiteCount = new Game(User,Competitor).getWhiteCnt();
            this.UserProfile.setPosition(-250,-140);
            this.CompetitorProfile.setPosition(250,-140);
        } else if (User.getColor() == -1) {
            this.BlackCount = new Game(Competitor,User).getBlackCnt();
            this.WhiteCount = new Game(Competitor,User).getWhiteCnt();
            this.UserProfile.setPosition(250,-140);
            this.CompetitorProfile.setPosition(-250,-140);
        }
    }

    public void setDefeatMenu(int a){
        this.DefeatMenu.setAlpha(a);
        this.CompetitorProfile.resizeTo(150,150);
        if (User.getColor() == 1) {
            this.BlackCount = new Game(User,Competitor).getBlackCnt();
            this.WhiteCount = new Game(User,Competitor).getWhiteCnt();
            this.UserProfile.setPosition(-250,-140);
            this.CompetitorProfile.setPosition(250,-140);
        } else if (User.getColor() == -1) {
            this.BlackCount = new Game(Competitor,User).getBlackCnt();
            this.WhiteCount = new Game(Competitor,User).getWhiteCnt();
            this.UserProfile.setPosition(250,-140);
            this.CompetitorProfile.setPosition(-250,-140);
        }
    }

    private void VictoryMenu_update(double dt) {
        this.VictoryMenu.setPosition(0,0);
        this.VictoryMenu.setAlpha(this.EndingMenuA.val());
    }

    private void DefeatMenu_update(double dt) {
        this.DefeatMenu.setPosition(0,0);
        this.DefeatMenu.setAlpha(this.EndingMenuA.val());
    }

    public void EndingButton_setActive(boolean flag) {
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
        if(this.Restart.isClicked()){
            this.wantRestart = true;
        }
        if(this.BackToLobby.isClicked()){
            this.wantBackToLobby = true;
        }
        super.update(dt);
    }
}
