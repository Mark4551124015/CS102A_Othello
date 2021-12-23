package stage.scene;

import graphics.Shape;
import graphics.Sprite;
import input.Controller;
import main.mainApp;
import network.Client_Room;
import newData.*;

import object.OthelloObject;
import object.Player;
import object.inGame.BoardIndex;
import object.inGame.DiskManager;

import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.Map;

import static stage.StageContainer.BoardSize;


public class Othello_Online extends OthelloObject implements stage.GameStage {
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



    private OthelloObject Board;

    public Othello_Online(Client_Room room) {
        super("Othello_Online");
    }

    @Override
    public void init() {
        //背景
        this.background = new OthelloObject("Game_bg", new Sprite("Game_BackGround"));
        this.addObj(background);
        this.background.setPosition(mainApp.WinSize.x / 2, mainApp.WinSize.y / 2);

        init_Game();
    }


    public void init_Game() {
        //棋盘
        this.Board = new OthelloObject("Board", new Sprite("Board"));
        this.background.addObj(this.Board);
        this.Board.resizeTo(new Vct(BoardSize, BoardSize));
        this.Board.setPosition(0, 0);

        //棋子管理器
        this.diskManager = new DiskManager();
        this.Board.addObj(diskManager);

        //指针
        this.boardIndex = new BoardIndex();
        this.boardIndex.setVisibility(true);
        this.Board.addObj(this.boardIndex);
    }

    public intVct mouseBP() {
        Vct pos = Controller.getMousePos();
        try {
            pos.transform(this.Board.getAbsoluteTransform().createInverse());
        } catch (NoninvertibleTransformException ignored) {
        }
        intVct intPosition = new intVct((int) Math.floor(pos.x / (BoardSize / 8)) + 4, (int) Math.floor(pos.y / (BoardSize / 8)) + 4);

        if (intPosition.c > 7) {
            intPosition.c = 7;
        }
        if (intPosition.c < 0) {
            intPosition.c = 0;
        }
        if (intPosition.r > 7) {
            intPosition.r = 7;
        }
        if (intPosition.r < 0) {
            intPosition.r = 0;
        }
        return intPosition;
    }

    @Override
    public GameStageID getGameStageID() {
        return GameStageID.Othello;
    }

    @Override
    public void update(double dt) {
        checkMouseOnBoard();
//        System.out.println(mouseBP().c + "" + mouseBP().r);

        this.boardIndex.traceMouse(this.diskManager.Disks[mouseBP().c][mouseBP().r].getTrans().position);

        super.update(dt);
    }

    public void checkMouseOnBoard() {
        Vct pos = Controller.getMousePos();
        try {
            pos.transform(this.Board.getAbsoluteTransform().createInverse());
        } catch (NoninvertibleTransformException ignored) {
        }

        intVct intPosition = new intVct((int) Math.floor(pos.x / (BoardSize / 8)) + 4, (int) Math.floor(pos.y / (BoardSize / 8)) + 4);

        if (intPosition.r > 7 || intPosition.r < 0 || intPosition.c > 7 || intPosition.c < 0 ) {
            mouseIsOnboard = false;
        } else {
            mouseIsOnboard = true;
        }
    }
}
