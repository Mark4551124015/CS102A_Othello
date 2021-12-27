package stage.scene;

import graphics.Image;
import graphics.Shape;
import input.Controller;
import main.mainApp;
import net.sf.json.JSONObject;
import newData.Operation;
import newData.Vct;
import newData.intVct;
import object.*;
import object.GUI.Buttons.NormalButton;
import object.GUI.InputBox;
import object.GUI.Menu.EscMenu;
import object.GUI.PlayerInfoInGame;
import object.inGame.BoardIndex;
import object.inGame.OperationManager;
import stage.GameStage;


import java.awt.event.KeyEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.util.Objects;

import static input.Controller.isKeyDown;
import static input.Controller.mouseIsOnboard;
import static main.PlayerManager.Competitor;
import static main.PlayerManager.User;
import static main.mainApp.controller;
import static newData.Operation.Operation_Type.*;
import static object.Game.gameInfoName;
import static object.Game.gamePath;
import static object.inGame.DiskManager.isReadyForNextOperation;
import static stage.StageContainer.BoardSize;
import static util.Tools.getStringFromFile;


public class Othello_Local extends OthelloObject implements GameStage {


    private Shape canvas;
    private OthelloObject background;

    private OperationManager operationManager;


    private Game game;
    public static boolean isReadyForOperate;
    private BoardIndex boardIndex;
    private PlayerInfoInGame playerInfoUser;
    private PlayerInfoInGame playerInfoCompetitor;
    private GameResult VictoryScene;
///    private GameResult DefearScene;
    private boolean ExitToLobby;
//    private NormalButton Restart;
    private object.GUI.Menu.EscMenu EscMenu;
    private boolean isReadyForNextCode;
    private boolean isReadyForNextEsc;

    private OthelloObject GameLoader;
    private InputBox gameNameInput;
    private NormalButton Save;
    private NormalButton Load;

    private int KeysCnt;
    private double TheWorld;
    private double CoolDown;
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


        this.VictoryScene = new GameResult(User,Competitor,this.game);
        this.Board.addObj(this.VictoryScene);
        this.VictoryScene.setAlpha(0);
//        this.VictoryScene.setPosition(10000,10000);
//        this.DefearScene = new GameResult();
//        this.Board.addObj(this.DefearScene);
//        this.DefearScene.setAlpha(0);
//        this.DefearScene.setPosition(0,0);
        this.VictoryScene.setPosition(0,0);
        this.VictoryScene.EndingButton_setActive(false);

        this.EscMenu = new EscMenu();
        this.background.addObj(this.EscMenu);
        this.EscMenu.setPosition(320,0);
        this.EscMenu.setVisibility(false);

        this.GameLoader = new OthelloObject("GameLoader");
        this.background.addObj(this.GameLoader);
        this.GameLoader.setPosition(0,0);
        this.gameNameInput = new InputBox(new Vct(250, 60), "GameName",10);
        this.gameNameInput.setActive(false);
        this.Save = new NormalButton("Save");
        this.Load = new NormalButton("Load");
        this.GameLoader.addObj(this.Save);
        this.GameLoader.addObj(this.Load);
        this.Save.setPosition(-60,80);
        this.Load.setPosition(60,80);

        this.GameLoader.addObj(this.gameNameInput);
        this.gameNameInput.setPosition(0,0);
        this.GameLoader.setVisibility(false);


        this.game.start();
        totalTime = 0;
        isReadyForOperate =true;
        isReadyForNextEsc=true;
        this.KeysCnt = 0;
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
        if (this.game.getGameEndingState()==0) {
            checkCheat(dt);
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

            if (isKeyDown(KeyEvent.VK_ESCAPE) && isReadyForNextEsc) {
                isReadyForNextEsc=false;
                if (!this.EscMenu.isVisible()) {
                    System.out.println("esc");
                    this.EscMenu.setVisibility(true);
                    this.EscMenu.setEscMenuActive(true);
                    this.GameLoader.setVisibility(true);
                    this.setGameLoaderActive(true);
                    this.Board.setPosition(10000, 10000);
                }else if (this.EscMenu.isVisible()) {
                    System.out.println("esced");
                    this.EscMenu.setVisibility(false);
                    this.EscMenu.setEscMenuActive(false);
                    this.GameLoader.setVisibility(false);
                    this.setGameLoaderActive(false);
                    this.Board.setPosition(mainApp.WinSize.x / 2, mainApp.WinSize.y / 2);
                }
            }
            if (!isKeyDown(KeyEvent.VK_ESCAPE) && !isReadyForNextEsc) {
                isReadyForNextEsc=true;
            }

            if (EscMenu.isWantBack()) {
                this.EscMenu.setVisibility(false);
                this.EscMenu.setEscMenuActive(false);
                this.Board.setPosition(mainApp.WinSize.x / 2, mainApp.WinSize.y / 2);
                this.EscMenu.setQuit(false);
                this.ExitToLobby = true;
                this.setGameLoaderActive(false);
            }

            if (EscMenu.isWantRestart()) {
                this.reStart();
                this.EscMenu.setRestart(false);
                this.EscMenu.setVisibility(false);
                this.EscMenu.setEscMenuActive(false);
                this.Board.setPosition(mainApp.WinSize.x / 2, mainApp.WinSize.y / 2);
            }

            if (EscMenu.isWantSave()) {
                this.saveGame();
                this.EscMenu.setEscSave(false);
                this.EscMenu.setAlpha(0);
                this.EscMenu.setEscMenuActive(false);
                this.Board.setPosition(mainApp.WinSize.x / 2, mainApp.WinSize.y / 2);
            }

            if (EscMenu.isWantReload()) {
                this.EscMenu.setPosition(320, 0);
                this.EscMenu.setLoad(false);
                this.GameLoader.setVisibility(true);
                this.setGameLoaderActive(true);
            }
            if (this.Save.isClicked()) {
                if (this.gameNameInput.getResult()!=null) {
                    this.game.setName(this.gameNameInput.getResult());
                    this.saveGame();
                }
            }
            if (this.Load.isClicked()) {
                if (this.gameNameInput.getResult()!=null) {
                    this.loadGame(this.gameNameInput.getResult());
                }
            }


            this.cleanInComingOperations();
            this.recallCheck();
            this.surrenderCheck();
        }
        if (this.VictoryScene.isWantRestart()) {
            this.reStart();
            this.VictoryScene.setAlpha(0);
            this.VictoryScene.EndingButton_setActive(false);
            this.VictoryScene.setRestart(false);
        }

        if (this.VictoryScene.isWantExitToLobby()) {
            this.ExitToLobby = true;
        }
        this.gameEnding();
        super.update(dt);

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
            if (this.game.getPlayer(this.game.getCurrentSide()).isCheating()) {
                this.operationManager.OperationHandler(new Operation(operator.getUsername(), new intVct(mouseBP().c, mouseBP().r), MadeInHeaven));
            } else {
                this.operationManager.OperationHandler(new Operation(operator.getUsername(), new intVct(mouseBP().c, mouseBP().r), SetDisk));
            }


        }
    }

    public void saveGame(){
        this.game.save();
        this.operationManager.save();
    }

    public void reStart(){
        this.game.renew();
        this.operationManager.renew();
        this.game.setGameEndingState(0);
    }

    public void setGameLoaderActive(boolean flag){
        this.gameNameInput.setActive(flag);
        this.Save.setActive(flag);
        this.Load.setActive(flag);
    }

    public void loadGame(String gameName){
        String gameInfoJsonStr = getStringFromFile(gamePath+gameName+"/"+gameInfoName);
        JSONObject gameInfoJson = JSONObject.fromObject(gameInfoJsonStr);
        if(!Objects.equals(User.getUsername(), gameInfoJson.getString("white_Player")) && !Objects.equals(User.getUsername(), gameInfoJson.getString("black_Player"))){
            return;
        }
        if(!Objects.equals(Competitor.getUsername(), gameInfoJson.getString("black_Player")) && !Objects.equals(Competitor.getUsername(), gameInfoJson.getString("white_Player"))){
            return;
        }
        this.operationManager.renew();
        this.game.renew();
        this.game.loadGameInfo(gameInfoJson);
        this.game.setCurrentPlayer(1);
        this.operationManager.loadOperations();
        System.out.println("Loaded");
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

    public void recallCheck() {
        if(this.playerInfoUser.isWantRecall()){
            operationManager.OperationHandler(new Operation(User.getUsername(),new intVct(),Recall));
            this.playerInfoUser.setRecall(false);
        }
        if(this.playerInfoCompetitor.isWantRecall()){
            operationManager.OperationHandler(new Operation(Competitor.getUsername(),new intVct(),Recall));
            this.playerInfoCompetitor.setRecall(false);
        }
    }
    public void surrenderCheck(){
        if(this.playerInfoCompetitor.isWantSurrender()){
            this.operationManager.OperationHandler(new Operation(User.getUsername(), new intVct(mouseBP().c,mouseBP().r), Surrender));
            this.playerInfoCompetitor.setSurrender(false);
        }
        if(this.playerInfoUser.isWantSurrender()){
            this.operationManager.OperationHandler(new Operation(Competitor.getUsername(), new intVct(mouseBP().c,mouseBP().r), Surrender));
            this.playerInfoUser.setSurrender(false);

        }
    }

    public void gameEnding(){
        if (this.game.getGameEndingState()==1 && this.game.getWinner() != null) {
            this.VictoryScene.setVictoryMenu(1);
            this.VictoryScene.setAlpha(1);
            this.VictoryScene.EndingButton_setActive(true);
            this.game.setGameEndingState(2);
            this.game.getPlayer(1).played();
            this.game.getPlayer(-1).played();
            if(this.game.getWinner()!=null) {
                this.game.getWinner().winCntPlus(1);
            }
        }

        if (this.game.getGameEndingState()==1 && this.game.getWinner() == null) {
            this.game.checkGameEnd();
            this.VictoryScene.setVictoryMenu(1);
            this.VictoryScene.setAlpha(1);
            this.VictoryScene.EndingButton_setActive(true);
            this.game.setGameEndingState(2);
            this.game.getPlayer(1).played();
            this.game.getPlayer(-1).played();
            if(this.game.getWinner()!=null) {
                this.game.getWinner().winCntPlus(1);
            }
            System.out.println("end");
        }




    }



    public void checkCheat(double dt){
        if (isKeyDown(KeyEvent.VK_UP) && this.KeysCnt == 0){
            this.CoolDown=0;
            this.KeysCnt=1;
            this.isReadyForNextCode = false;
        }
        if (!isKeyDown(KeyEvent.VK_UP) && this.KeysCnt == 1) {
            this.isReadyForNextCode = true;
        }
        if (isKeyDown(KeyEvent.VK_UP) && this.KeysCnt == 1 && this.isReadyForNextCode){
            this.KeysCnt=2;
            this.isReadyForNextCode = false;
        }
        if (!isKeyDown(KeyEvent.VK_UP) && this.KeysCnt == 2) {
            this.isReadyForNextCode = true;
        }
        if (isKeyDown(KeyEvent.VK_DOWN) && this.KeysCnt == 2 && this.isReadyForNextCode){
            this.KeysCnt=3;
            this.isReadyForNextCode = false;
        }
        if (!isKeyDown(KeyEvent.VK_DOWN) && this.KeysCnt == 3) {
            this.isReadyForNextCode = true;
        }
        if (isKeyDown(KeyEvent.VK_DOWN) && this.KeysCnt == 3 && this.isReadyForNextCode){
            this.KeysCnt=4;
            this.isReadyForNextCode = false;

        }
        if (!isKeyDown(KeyEvent.VK_DOWN) && this.KeysCnt == 4) {
            this.isReadyForNextCode = true;
        }
        if (isKeyDown(KeyEvent.VK_LEFT) && this.KeysCnt == 4 && this.isReadyForNextCode){
            this.KeysCnt=5;
            this.isReadyForNextCode = false;
        }
        if (!isKeyDown(KeyEvent.VK_LEFT) && this.KeysCnt == 5) {
            this.isReadyForNextCode = true;
        }
        if (isKeyDown(KeyEvent.VK_RIGHT) && this.KeysCnt == 5 && this.isReadyForNextCode){
            this.KeysCnt=6;
            this.isReadyForNextCode = false;
        }
        if (!isKeyDown(KeyEvent.VK_RIGHT) && this.KeysCnt == 6) {
            this.isReadyForNextCode = true;
        }
        if (isKeyDown(KeyEvent.VK_LEFT) && this.KeysCnt == 6 && this.isReadyForNextCode){
            this.KeysCnt=7;
            this.isReadyForNextCode = false;
        }
        if (!isKeyDown(KeyEvent.VK_LEFT) && this.KeysCnt == 7) {
            this.isReadyForNextCode = true;
        }
        if (isKeyDown(KeyEvent.VK_RIGHT) && this.KeysCnt == 7 && this.isReadyForNextCode){
            this.KeysCnt=8;
            this.isReadyForNextCode = false;
        }
        if (!isKeyDown(KeyEvent.VK_RIGHT) && this.KeysCnt == 8) {
            this.isReadyForNextCode = true;
        }
        if (isKeyDown(KeyEvent.VK_B) && this.KeysCnt == 8 && this.isReadyForNextCode){
            this.KeysCnt=9;
            this.isReadyForNextCode = false;
        }
        if (!isKeyDown(KeyEvent.VK_B) && this.KeysCnt == 9) {
            this.isReadyForNextCode = true;
        }
        if (isKeyDown(KeyEvent.VK_A) && this.KeysCnt == 9 && this.isReadyForNextCode){
            this.KeysCnt=10;
            this.isReadyForNextCode = false;
        }
        if (!isKeyDown(KeyEvent.VK_A) && this.KeysCnt == 10) {
            this.isReadyForNextCode = true;
        }
        if (isKeyDown(KeyEvent.VK_B) && this.KeysCnt == 10 && this.isReadyForNextCode){
            this.KeysCnt=11;
            this.isReadyForNextCode = false;
        }
        if (!isKeyDown(KeyEvent.VK_B) && this.KeysCnt == 11) {
            this.isReadyForNextCode = true;
        }
        if (isKeyDown(KeyEvent.VK_A) && this.KeysCnt == 11 && this.isReadyForNextCode){
            this.KeysCnt=12;
            this.isReadyForNextCode = false;
        }
        if (!isKeyDown(KeyEvent.VK_A) && this.KeysCnt == 12) {
            this.isReadyForNextCode = true;
        }

        if (this.KeysCnt != 0) {
            this.CoolDown+=dt;
            if(this.CoolDown>=10){
                this.KeysCnt = 0;
            }
        }
        if (this.KeysCnt == 12 && this.isReadyForNextCode) {
            this.game.getPlayer(this.game.getCurrentSide()).setCheating(true);
            this.TheWorld += dt;
            System.out.println("砸 瓦鲁多"+this.TheWorld);
            if (this.TheWorld >= 5) {
                this.KeysCnt = 0;
                this.TheWorld = 0;
                this.game.getPlayer(this.game.getCurrentSide()).setCheating(false);
            }
        }



    }
}
