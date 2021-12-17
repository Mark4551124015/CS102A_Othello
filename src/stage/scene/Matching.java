package stage.scene;

import graphics.Shape;
import graphics.Sprite;
import input.Controller;
import main.mainApp;
import newData.Vct;
import newData.intVct;
import object.GUI.Buttons.NormalButton;
import object.OthelloObject;
import object.Player;
import object.PlayerInfo;
import object.inGame.BoardIndex;
import object.inGame.DiskManager;
import util.FontLibrary;

import java.awt.*;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.Map;

import static object.PlayerManager.User;


public class Matching extends OthelloObject implements stage.GameStage {
    public static final double BoardSize = mainApp.Height * 0.65;
    public static final double DiskSize = (BoardSize - 10) / 8;

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
        Font font = FontLibrary.GetNormalButtonFont(4);


        //背景
        this.background = new OthelloObject("Matching_bg", new Sprite("Matching_bg"));
        this.addObj(this.background);
        this.background.setPosition(mainApp.WinSize.x / 2, mainApp.WinSize.y / 2);
        //
        this.Ready = new NormalButton("Ready_Button", new Sprite("NormalButton"));
        this.Ready.resizeTo(mainApp.Width/9+10, mainApp.Height/9-5);
        this.Ready.setText(" Ready");
        this.Ready.setTextColor(new Color(212, 212, 212));
        this.background.addObj(this.Ready);

        this.Ready.setPosition(0,0.375*mainApp.Height);


        init_left();
        init_right();
    }



    public void init_left() {
        //左侧面板
        this.Left = new OthelloObject("Left_Panel", new Sprite("Left_Panel"));
        this.background.addObj(this.Left);
        this.Left.resizeTo(new Vct(mainApp.Width, mainApp.Height/2));
        this.Left.setPosition(0, 0);



        this.User_Info = new PlayerInfo(User);
        this.Left.addObj(this.User_Info);

        this.User_Info.setPosition(-mainApp.Width/4,0);


    }
    public void init_right() {
        //右侧面板
        this.Right = new OthelloObject("Right_Panel", new Sprite("Right_Panel"));
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
            if (!User.getReady()) {
                User.setReady(true);
            } else {
                User.setReady(false);
            }
        }
//        System.out.println(mouseBP().c + "" + mouseBP().r);
        super.update(dt);
    }

}
