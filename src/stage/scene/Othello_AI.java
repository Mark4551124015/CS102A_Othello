package stage.scene;

import graphics.Shape;
import graphics.Image;
import input.Controller;
import main.mainApp;
import net.sf.json.JSONObject;
import newData.Operation;
import newData.Vct;
import newData.intVct;
import object.*;
import object.GUI.PlayerInfoInGame;
import object.inGame.BoardIndex;
import object.inGame.DiskManager;
import object.inGame.DiskManager_AI;
import object.inGame.OperationManager;
import stage.GameStage;

import java.awt.event.KeyEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import static input.Controller.isKeyDown;
import static input.Controller.mouseIsOnboard;
import static java.lang.Thread.sleep;
import static main.mainApp.controller;
import static newData.Operation.Operation_Type.SetDisk;
import static object.Game.gameInfoName;
import static object.Game.gamePath;
import static main.PlayerManager.Competitor;
import static main.PlayerManager.User;
import static object.inGame.DiskManager.isReadyForNextOperation;
import static stage.StageContainer.BoardSize;
import static util.Tools.*;


public class Othello_AI extends OthelloObject implements GameStage {


    private Shape canvas;
    private OthelloObject background;

    public Player AI;
    private AILevel level;
    private ArrayList<Player> playerList;
    private Map<Integer, Player> idMap;

    private OperationManager operationManager;


    private Game game;
    public static boolean isReadyForOperate;
    private BoardIndex boardIndex;
    private PlayerInfoInGame playerInfoUser;
    private PlayerInfoInGame playerInfoCompetitor;
    private GameResult VictoryScene;
    private boolean ExitToLobby;
    private object.EscMenu EscMenu;

    private boolean gameOver;
    private boolean gameOverState;
    private boolean isProcessing;
    double totalTime = 0;
    double AITotalTime =0;




    private OthelloObject Board;

    public Othello_AI(AILevel level) {
        super("Othello_AI");
        this.level = level;
    }

    @Override
    public void init() {
        //背景
        this.background = new OthelloObject("Game_bg", new Image("Game_BackGround"));
        this.addObj(background);
        this.background.setPosition(mainApp.WinSize.x / 2, mainApp.WinSize.y / 2);
        init_AI_Game();
    }

    private void init_AI_Game() {
        AI = new Player("AI","AI", Player.playerType.AI);
        Competitor = AI;

        //棋盘
        this.Board = new OthelloObject("Board", new Image("Board"));
        this.addObj(this.Board);
        this.Board.setPosition(mainApp.WinSize.x / 2, mainApp.WinSize.y / 2);
        this.Board.resizeTo(new Vct(BoardSize, BoardSize));

        //本地游戏管理器
        this.game = new Game(User, AI);
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

        this.VictoryScene = new GameResult(User,Competitor);
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
        this.EscMenu.setPosition(0,0);
        this.EscMenu.setAlpha(0);

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
        return GameStageID.Othello_AI;
    }

    @Override
    public void update(double dt) {
        checkMouseOnBoard();

        this.boardIndex.traceMouse(this.game.getGrid().Disks[mouseBP().c][mouseBP().r].getTrans().position);

        totalTime += dt;
        AITotalTime += dt;

        if (!isReadyForOperate) {
            controller.cleanClick();
        }
        if (isReadyForOperate && isReadyForNextOperation()) {
            OperationCheck();
        }

        if(this.VictoryScene.isWantRestart()){
            reStart();
            this.VictoryScene.setAlpha(0);
            this.VictoryScene.EndingButton_setActive(false);
            this.VictoryScene.setRestart(false);
        }

        if(this.VictoryScene.isWantExitToLobby()){
            this.ExitToLobby = true;
            System.out.println("dian");
        }

        if(game.gameEnd()){
            this.VictoryScene.setVictoryMenu(1);
            this.VictoryScene.setAlpha(1);
            this.VictoryScene.EndingButton_setActive(true);
        }

        if(playerInfoUser.isWantSurrender() || playerInfoCompetitor.isWantSurrender()){
            game.gameEnd();
        }

        if(isKeyDown(KeyEvent.VK_ESCAPE)){
            this.EscMenu.setAlpha(1);
            this.EscMenu.setEscMenuActive(true);
            this.Board.setPosition(10000,10000);
        }

        if(EscMenu.isWantBack()){
            this.EscMenu.setAlpha(0);
            this.EscMenu.setEscMenuActive(false);
            this.Board.setPosition(mainApp.WinSize.x / 2, mainApp.WinSize.y / 2);
            this.EscMenu.setBack(false);
        }

        if(EscMenu.isWantRestart()){
            reStart();
            this.EscMenu.setRestart(false);
            this.EscMenu.setAlpha(0);
            this.EscMenu.setEscMenuActive(false);
            this.Board.setPosition(mainApp.WinSize.x / 2, mainApp.WinSize.y / 2);
        }

        if(EscMenu.isWantSave()){
            saveGame();
            this.EscMenu.setEscSave(false);
            this.EscMenu.setAlpha(0);
            this.EscMenu.setEscMenuActive(false);
            this.Board.setPosition(mainApp.WinSize.x / 2, mainApp.WinSize.y / 2);
        }

        this.AIProcess();
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
        if (controller.isClicked() && mouseIsOnboard){
            this.operationManager.OperationHandler(new Operation(User.getUsername(), new intVct(mouseBP().c,mouseBP().r), SetDisk));
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
            if (totalTime > 0.4) {
                totalTime = 0;
                this.operationManager.OperationHandler((Operation) this.operationManager.getIncomingOperations().get(0));
                this.operationManager.deleteFirstOperation();
            }
        } else {
            isReadyForOperate = true;
        }
    }

    private void AIProcess() {
        if (this.isProcessing || this.game.getCurrentSide() != AI.getColor()) {
            AITotalTime = 0;
            return;
        }

        if (!this.isProcessing && isReadyForNextOperation() && AITotalTime>0.1) {
            this.isProcessing = true;
        } else {
            return;
        }

        if (this.level == AILevel.Easy) {

            DiskManager_AI AIDisks = new DiskManager_AI();
            AIDisks.loadDisks(this.game.getGrid().Disks);
            ArrayList<Integer> flipPoints = new ArrayList<>();
            ArrayList<Integer> UserValidPoints = new ArrayList<>();
            ArrayList<Integer> Final = new ArrayList<>();


            ArrayList<intVct> load =  this.game.getGrid().validBP(AI);
            ArrayList<intVct> load_User;


            for (int i=0; i < load.size(); i++) {
                int point = 0;
                intVct index = load.get(i);
                point += this.game.getGrid().check(index,AI, DiskManager.checkPurpose.Flip).size() * 10;
                if (load.get(i).r == 0 || load.get(i).r == 7 || load.get(i).c == 0 || load.get(i).c == 7) {
                    point += 100;
                }
                if (load.get(i).c == 0 || load.get(i).c == 7) {
                    point += 100;
                }

                flipPoints.add(point);
            }

            for (int i=0; i < load.size(); i++) {
                AIDisks.SetDisk(load.get(i),AI);
                int point = 0;
                if (AIDisks.checkWinner(User, AI)!=null) {
                    if (AIDisks.checkWinner(User, AI).getUsername() == AI.getUsername()) {
                        point += 1000;
                    }
                    if (AIDisks.checkWinner(User, AI).getUsername() == User.getUsername()) {
                        point -= 1000;
                    }
                }
                load_User = AIDisks.validBP(User);

                for (int a=0; a < load_User.size(); a++) {
                    intVct index = load_User.get(a);
                    point += this.game.getGrid().check(index,User, DiskManager.checkPurpose.Flip).size() * -1;
                    if (load_User.get(a).r == 0 || load_User.get(a).r == 7 ) {
                        point -= 100;
                    }
                    if (load_User.get(a).c == 0 || load_User.get(a).c == 7 ) {
                        point -= 100;
                    }

                }



                point += AIDisks.validBP(User).size()*(-10);
                UserValidPoints.add(point);
                AIDisks.recallLastDisk();
            }

            for (int i=0; i < load.size(); i++) {
                Final.add(flipPoints.get(i)+UserValidPoints.get(i));
            }

            intVct bP = load.get(getSmallestIndex(Final));

            if (this.game.getCurrentSide() == AI.getColor() && isReadyForNextOperation()) {
                this.operationManager.OperationHandler(new Operation("AI",bP, SetDisk));
            }
            this.isProcessing = false;
        }


        if (this.level == AILevel.Normal) {
            ArrayList<Integer> Points = new ArrayList<>();
            ArrayList<intVct> load =  this.game.getGrid().validBP(AI);
            for (int i=0; i < load.size(); i++) {
                intVct index = load.get(i);
                Points.add((this.game.getGrid().check(index,AI, DiskManager.checkPurpose.Flip).size() * 10));
            }
            intVct bP = load.get(getGreatestIndex(Points));
            if (this.game.getCurrentSide() == AI.getColor() && isReadyForNextOperation()) {
                this.operationManager.OperationHandler(new Operation("AI",bP, SetDisk));
            }
            this.isProcessing = false;
        }


        if (this.level == AILevel.Hard) {
            DiskManager_AI AIDisks = new DiskManager_AI();
            AIDisks.loadDisks(this.game.getGrid().Disks);
            ArrayList<Integer> flipPoints = new ArrayList<>();
            ArrayList<Integer> UserValidPoints = new ArrayList<>();
            ArrayList<Integer> Final = new ArrayList<>();


            ArrayList<intVct> load =  this.game.getGrid().validBP(AI);
            ArrayList<intVct> load_User;


            for (int i=0; i < load.size(); i++) {
                int point = 0;
                intVct index = load.get(i);
                point += this.game.getGrid().check(index,AI, DiskManager.checkPurpose.Flip).size() * 10;
                if (load.get(i).r == 0 || load.get(i).r == 7 || load.get(i).c == 0 || load.get(i).c == 7) {
                    point += 100;
                }
                if (load.get(i).c == 0 || load.get(i).c == 7) {
                    point += 100;
                }

                flipPoints.add(point);
            }

            for (int i=0; i < load.size(); i++) {
                AIDisks.SetDisk(load.get(i),AI);
                int point = 0;
                if (AIDisks.checkWinner(User, AI)!=null) {
                    if (AIDisks.checkWinner(User, AI).getUsername() == AI.getUsername()) {
                        point += 1000;
                    }
                    if (AIDisks.checkWinner(User, AI).getUsername() == User.getUsername()) {
                        point -= 1000;
                    }
                }
                load_User = AIDisks.validBP(User);

                for (int a=0; a < load_User.size(); a++) {
                    intVct index = load_User.get(a);
                    point += this.game.getGrid().check(index,User, DiskManager.checkPurpose.Flip).size() * -1;
                    if (load_User.get(a).r == 0 || load_User.get(a).r == 7 ) {
                        point -= 100;
                    }
                    if (load_User.get(a).c == 0 || load_User.get(a).c == 7 ) {
                        point -= 100;
                    }

                }



                point += AIDisks.validBP(User).size()*(-10);
                UserValidPoints.add(point);
                AIDisks.recallLastDisk();
            }

            for (int i=0; i < load.size(); i++) {
                Final.add(flipPoints.get(i)+UserValidPoints.get(i));
            }

            intVct bP = load.get(getGreatestIndex(Final));

            if (this.game.getCurrentSide() == AI.getColor() && isReadyForNextOperation()) {
                this.operationManager.OperationHandler(new Operation("AI",bP, SetDisk));
            }
            this.isProcessing = false;
        }




    }

    public boolean isExitToLobby(){
        return this.ExitToLobby;
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


    public enum AILevel{
        Easy,Normal,Hard
    }






}
