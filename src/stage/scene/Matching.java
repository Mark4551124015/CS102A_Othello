package stage.scene;

import graphics.Shape;
import graphics.Image;
import input.Controller;
import main.mainApp;
import newData.Vct;
import object.GUI.Buttons.NormalButton;
import object.OthelloObject;
import object.Player;
import object.GUI.PlayerInfo;
import object.inGame.BoardIndex;
import object.inGame.DiskManager;
import util.FontLib;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

import static main.PlayerManager.User;


public class Matching extends OthelloObject implements stage.GameStage {

    private Shape canvas;
    private OthelloObject background;

    private ArrayList<Player> playerList;
    private Map<Integer, Player> idMap;


    private DiskManager diskManager;
    private Controller controller;
    private BoardIndex boardIndex;

    public static boolean mouseIsOnboard;

    private boolean gameOver;
    private boolean gameOverState;



    private OthelloObject Left;
    private OthelloObject Right;
    private NormalButton Ready;

    private OthelloObject User_Info;
    private OthelloObject AnotherPlayer_Info;

    public Matching() {
        super("scene_Matching");
    }

    @Override
    public void init() {
        Font font = FontLib.GetNormalButtonFont(4);


        //背景
        this.background = new OthelloObject("Matching_bg", new Image("Matching_bg"));
        this.addObj(this.background);
        this.background.setPosition(mainApp.WinSize.x / 2, mainApp.WinSize.y / 2);
        //
        this.Ready = new NormalButton("Ready_Button");
        this.Ready.setText(" Ready");
        this.Ready.setTextColor(new Color(212, 212, 212));
        this.background.addObj(this.Ready);

        this.Ready.setPosition(0,0.375*mainApp.Height);


        init_left();
        init_right();
    }



    public void init_left() {
        //左侧面板
        this.Left = new OthelloObject("Left_Panel", new Image("Left_Panel"));
        this.background.addObj(this.Left);
        this.Left.resizeTo(new Vct(mainApp.Width, mainApp.Height/2));
        this.Left.setPosition(0, 0);



        this.User_Info = new PlayerInfo(User);
        this.Left.addObj(this.User_Info);

        this.User_Info.setPosition(-mainApp.Width/4,0);


    }
    public void init_right() {
        //右侧面板
        this.Right = new OthelloObject("Right_Panel", new Image("Right_Panel"));
        this.background.addObj(this.Right);
        this.Right.resizeTo(new Vct(mainApp.Width, mainApp.Height/2));
        this.Right.setPosition(0, 0);
    }



    @Override
    public GameStageID getGameStageID() {
        return GameStageID.Matching;
    }

    @Override
    public void update(double dt) {
        if (this.Ready.isClicked()) {
            User.setReady(!User.getReady());
        }
//        System.out.println(mouseBP().c + "" + mouseBP().r);
        super.update(dt);
    }

}
