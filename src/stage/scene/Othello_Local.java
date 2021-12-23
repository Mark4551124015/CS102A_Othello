package stage.scene;

import graphics.Shape;
import graphics.Image;
import input.Controller;
import main.mainApp;
import net.sf.json.JSONObject;
import newData.Operation;
import newData.Vct;
import newData.intVct;

import object.GUI.Buttons.NormalButton;
import object.GUI.PlayerInfoInGame;
import object.Game;
import object.OthelloObject;
import object.Player;

import object.*;

import object.inGame.BoardIndex;
import object.inGame.OperationManager;
import stage.GameStage;

import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import static input.Controller.mouseIsOnboard;
import static main.mainApp.controller;
import static newData.Operation.Operation_Type.SetDisk;
import static object.Game.gameInfoName;
import static object.Game.gamePath;
import static object.Player.playerType.local;
import static main.PlayerManager.Competitor;
import static main.PlayerManager.User;
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
    private PlayerInfoInGame playerInfoUser;
    private PlayerInfoInGame playerInfoCompetitor;
    private GameResult VictoryScene;
//    private GameResult DefearScene;
    private boolean ExitToLobby;
    private NormalButton Back;
    private boolean wantBack;


    private boolean gameOver;
    private boolean gameOverState;
    double totalTime = 0;

    private OthelloObject Board;

    public Othello_Local() {
        super("Othello_Local");
    }

    @Override
    public void init() {
        //背景
        this.background = new OthelloObject("Game_bg", new Image("Game_BackGround"));
        this.addObj(background);
        this.background.setPosition(mainApp.WinSize.x / 2, mainApp.WinSize.y / 2);
        init_local_Game();
    }

    public void init_local_Game() {
        User = new Player("Mark455","Mark455",local);
        Competitor = new Player("Jerry","Jerry",local);

        //棋盘
        this.Board = new OthelloObject("Board", new Image("Board"));
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

        this.playerInfoCompetitor = new PlayerInfoInGame(Competitor);
        this.Board.addObj(this.playerInfoCompetitor);

        this.playerInfoUser = new PlayerInfoInGame(User);
        this.Board.addObj(this.playerInfoUser);

        if (User.getColor() == 1) {
            this.playerInfoUser.setPosition(-465,-80);
            this.playerInfoCompetitor.setPosition(465,-80);
        } else if (User.getColor() == -1) {
            this.playerInfoUser.setPosition(465,-80);
            this.playerInfoCompetitor.setPosition(-465,-80);
        }

        this.VictoryScene = new GameResult();
        this.Board.addObj(this.VictoryScene);
        this.VictoryScene.setAlpha(0);
        this.VictoryScene.setPosition(10000,10000);
//        this.DefearScene = new GameResult();
//        this.Board.addObj(this.DefearScene);
//        this.DefearScene.setAlpha(0);
//        this.DefearScene.setPosition(0,0);

        this.game.start();
        totalTime = 0;
        isReadyForOperate =true;

        this.Back = new NormalButton("Back");
        this.Board.addObj(this.Back);
        this.Back.setPosition(-1000,500);
        this.Back.resizeTo(120,50);
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
            SetDiskCheck();
        }
        super.update(dt);



        if(this.VictoryScene.isWantRestart()){
            reStart();
        }
        if(this.VictoryScene.isWantExitToLobby()){
            this.ExitToLobby = true;
            System.out.println("dian");
        }

        if(this.Back.isClicked()){
            this.wantBack = true;
        }


        recallCheck();

    }

    public void checkMouseOnBoard() {
        Vct pos = Controller.getMousePos();
        try {
            pos.transform(this.Board.getAbsoluteTransform().createInverse());
        } catch (NoninvertibleTransformException ignored) {}

        intVct intPosition = new intVct((int) Math.floor(pos.y / (BoardSize / 8)) + 4, (int) Math.floor(pos.x / (BoardSize / 8)) + 4);
        if (intPosition.r > 7 || intPosition.r < 0 || intPosition.c > 7 || intPosition.c < 0 ) {
            mouseIsOnboard = false;
        } else {
            mouseIsOnboard = true;
        }
    }

    public void SetDiskCheck(){
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
        if (!Objects.equals(User.getUsername(), gameInfoJson.getString("white_Player")))

        if(!Objects.equals(User.getUsername(), gameInfoJson.getString("white_Player")) || !Objects.equals(User.getUsername(), gameInfoJson.getString("black_Player"))){
            return false;
        }
        if(!Objects.equals(Competitor.getUsername(), gameInfoJson.getString("white_Player")) || !Objects.equals(Competitor.getUsername(), gameInfoJson.getString("black_Player"))){
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
            if (totalTime*2>1.2) {
                totalTime = 0;
                this.operationManager.OperationHandler((Operation) this.operationManager.getIncomingOperations().get(0));
                this.operationManager.deleteFirstOperation();
            }
        } else {
            isReadyForOperate = true;
            totalTime = 0;
        }
    }

    public boolean isExitToLobby(){
        return this.ExitToLobby;
    }


    public boolean isWantBack(){
        return this.wantBack;
    }

    public void recallCheck() {
        if(this.playerInfoCompetitor.isWantSurrender()){
            this.game.setWinner(User);
        }
        if(this.playerInfoUser.isWantSurrender()){
            this.game.setWinner(Competitor);
        }

        if(this.playerInfoUser.isWantRecall()){
            this.game.playerRecall(User);
            this.playerInfoUser.setRecalled(User.getReCalledTime());
            this.playerInfoUser.setRecall(false);
            this.playerInfoUser.setRecall(false);
            this.game.setHinted(false);
        }
        if(this.playerInfoCompetitor.isWantRecall()){
            this.game.playerRecall(Competitor);
            this.playerInfoCompetitor.setRecalled(Competitor.getReCalledTime());
            this.playerInfoCompetitor.setRecall(false);
            this.playerInfoCompetitor.setRecall(false);
            this.game.setHinted(false);
        }
    }

}
