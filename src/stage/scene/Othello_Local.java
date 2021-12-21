package stage.scene;

import component.animation.Animator;
import graphics.Shape;
import graphics.Sprite;
import input.Controller;
import main.mainApp;
import net.sf.json.JSONObject;
import newData.Operation;
import newData.Vct;
import newData.intVct;
import object.*;
import object.GUI.Buttons.MenuButton;
import object.GUI.Buttons.NormalButton;
import object.GUI.Buttons.SelectModeButton;
import object.inGame.BoardIndex;
import object.inGame.OperationManager;
import stage.GameStage;

import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.Map;

import static input.Controller.mouseIsOnboard;
import static main.mainApp.controller;
import static newData.Operation.Operation_Type.SetDisk;
import static object.Game.gameInfoName;
import static object.Game.gamePath;
import static object.Player.playerType.local;
import static object.PlayerManager.Competitor;
import static object.PlayerManager.User;
import static object.inGame.DiskManager.isReadyForNextOperation;
import static stage.StageContainer.BoardSize;
import static util.Tools.getStringFromFile;


public class Othello_Local extends OthelloObject implements GameStage {


    private Shape canvas;
    private OthelloObject background;
//    private OthelloObject playerinfow;
//    private OthelloObject playerinfob;

    private ArrayList<Player> playerList;
    private Map<Integer, Player> idMap;

    private OperationManager operationManager;


    private Game game;
    public static boolean isReadyForOperate;
    private BoardIndex boardIndex;
    private PlayerInfoInGame playerInfow;
    private PlayerInfoInGame playerInfob;


    private boolean gameOver;
    private boolean gameOverState;
    double totalTime = 0;

    private OthelloObject Board;

    public Othello_Local() {
        super("scene_Game");
    }

    @Override
    public void init() {
        //背景
        this.background = new OthelloObject("Game_bg", new Sprite("Game_BackGround"));
        this.addObj(background);
        this.background.setPosition(mainApp.WinSize.x / 2, mainApp.WinSize.y / 2);
        init_local_Game();
    }

    public void init_local_Game() {
        User = new Player("Mark455","Mark455",local);
        Competitor = new Player("JR","JR",local);

        //棋盘
        this.Board = new OthelloObject("Board", new Sprite("Board"));
        this.addObj(this.Board);
        this.Board.setPosition(mainApp.WinSize.x / 2, mainApp.WinSize.y / 2);
        this.Board.resizeTo(new Vct(BoardSize, BoardSize));

        //玩家信息
//        this.playerinfow = new OthelloObject("PlayerInfow",new Sprite("profile"));//white
//        this.addObj(this.playerinfow);
//        this.playerinfow.setPosition(176,200);
//        this.playerinfow.resizeTo(new Vct(160,160));
//        this.playerinfob = new OthelloObject("PlayerInfob",new Sprite("profile"));//black
//        this.addObj(this.playerinfob);
//        this.playerinfob.setPosition(0,100);

        //本地游戏管理器
        this.game = new Game(User, Competitor);
        this.operationManager = new OperationManager(this.game);
        this.Board.addObj(this.game);

        //指针
        this.boardIndex = new BoardIndex();
        this.boardIndex.setVisibility(true);
        this.Board.addObj(this.boardIndex);

        this.playerInfob = new PlayerInfoInGame(this.game.getPlayer(1));
        this.Board.addObj(this.playerInfob);
        this.playerInfob.setPosition(465,-80);


        this.playerInfow = new PlayerInfoInGame(this.game.getPlayer(-1));
        this.Board.addObj(this.playerInfow);
        this.playerInfow.setPosition(-465,-80);

        this.game.start();
        totalTime = 0;
        isReadyForOperate =true;

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
        return GameStageID.Othello_Local;
    }

    @Override
    public void update(double dt) {
        checkMouseOnBoard();
//        System.out.println(mouseBP().c + "" + mouseBP().r);

        this.boardIndex.traceMouse(this.game.getGrid().Disks[mouseBP().c][mouseBP().r].getTrans().position);

        totalTime += dt;
        if (!isReadyForOperate) {
            controller.cleanClick();
        }
        if (isReadyForOperate && isReadyForNextOperation()) {
            OperationCheck();
        }
        super.update(dt);

    }

    public void checkMouseOnBoard() {
        Vct pos = Controller.getMousePos();
        try {
            pos.transform(this.Board.getAbsoluteTransform().createInverse());
        } catch (NoninvertibleTransformException ignored) {}

        intVct intPosition = new intVct((int) Math.floor(pos.x / (BoardSize / 8)) + 4, (int) Math.floor(pos.y / (BoardSize / 8)) + 4);
        if (intPosition.r > 7 || intPosition.r < 0 || intPosition.c > 7 || intPosition.c < 0 ) {
            mouseIsOnboard = false;
        } else {
            mouseIsOnboard = true;
        }
    }

    public void OperationCheck(){
        Player operator = this.game.getPlayer(this.game.getCurrentSide());
        if (controller.isClicked() && mouseIsOnboard){
            this.operationManager.OperationHandler(new Operation(operator.getUsername(), new intVct(mouseBP().c,mouseBP().r), SetDisk));
        }
    }

    public void saveGame(){
        this.game.save();
        this.operationManager.save();
    }

    public void reStart(){
        this.game.renew();
        this.operationManager.renew();
    }

    public boolean loadGame(String gameName){
        String gameInfoJsonStr = getStringFromFile(gamePath+gameName+gameInfoName);
        JSONObject gameInfoJson = JSONObject.fromObject(gameInfoJsonStr);
        if (PlayerManager.User.getUsername() != gameInfoJson.getString("white_Player") )

        if(User.getUsername() != gameInfoJson.getString("white_Player") || User.getUsername() != gameInfoJson.getString("black_Player")){
            return false;
        }
        if(Competitor.getUsername() != gameInfoJson.getString("white_Player") || Competitor.getUsername() != gameInfoJson.getString("black_Player")){
            return false;
        }
        this.operationManager.renew();
        this.game.renew();
        this.game.loadGameInfo(gameInfoJson);
        this.operationManager.loadOperations();
        return true;
    }

    private void cleanInComingOperations() {
        if (!this.operationManager.getIncomingOperations().isEmpty()) {
            isReadyForOperate = false;
            if (((int)totalTime*2) % 1 == 0) {
                this.operationManager.OperationHandler((Operation) this.operationManager.getIncomingOperations().get(0));
                this.operationManager.deleteFirstOperation();
            }
        } else {
            isReadyForOperate = true;
        }
    }
}
