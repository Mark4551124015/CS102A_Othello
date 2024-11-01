package main;



import stage.GameStage;
import stage.GameStage.GameStageID;
import stage.scene.*;
import stage.StageContainer;
import stage.transition.FadeTransition;
import input.Controller;
import stage.transition.OthelloTransition;

import java.awt.*;

import static stage.transition.FadeTransition.FadeType.IN;
import static stage.transition.FadeTransition.FadeType.OUT;

public class GameManager implements Runnable {
    public static final int FPS = 144;

    private final StageContainer stageContainer;
    public static PlayerManager playerManager = new PlayerManager();

    public GameManager() {
        this.stageContainer = new StageContainer();
    }

    public void start() {
        playerManager.readAll();
        Thread GMThread = new Thread(this);
        GMThread.start();

//        OthelloServer Server = new OthelloServer(8888);
//        Server.init();


    }

    public void update(double dt) {
        this.updateStage();
    }

    private void updateStage() {
        GameStage stage = this.stageContainer.getCurrentStage();
        if (stage.getGameStageID() == GameStageID.Empty) {
            if (((Empty)stage).getTotalTime() >= 1 && ResourceManager.getLoadState() >= 1) {
                this.stageContainer.enterStage(new Launching(), null, new FadeTransition(Color.black, 0.5, 0, IN));
//                AudioManager.PlayWithVolume("intro", 0.1, 0);
            }
        } else if (stage.getGameStageID() == GameStageID.Launching) {
            if (((Launching)stage).getTotalTime() >= 2 && ResourceManager.getLoadState() == 2) {
                this.stageContainer.enterStage(new Login(), new FadeTransition(Color.black, 0.5,0,OUT), new FadeTransition(Color.black, 0.5,0,IN));
//                AudioManager.initBGM();
//                AudioManager.playBGM();
            }
        }else if (stage.getGameStageID() == GameStageID.Login) {
            if (((Login) stage).isLogged()) {
                this.stageContainer.enterStage(new Lobby(), new OthelloTransition(1, 0.3, OthelloTransition.TransitionType.OUT), new OthelloTransition(1, 0.3, OthelloTransition.TransitionType.IN));
            }
            } else if (stage.getGameStageID() == GameStageID.Lobby) {
            if (((Lobby)stage).isExiting())
                System.exit(0);
            if (((Lobby)stage).ChosenLocal()) {
                this.stageContainer.enterStage(new Othello_Local(), new OthelloTransition(1, 0.3, OthelloTransition.TransitionType.OUT), new OthelloTransition(1, 0.3, OthelloTransition.TransitionType.IN));
            }
            if (((Lobby)stage).ChosenOnline()) {
                this.stageContainer.enterStage(new Matching(), new FadeTransition(Color.black, 0.3, 0.3,OUT), new FadeTransition(Color.black, 1, 0, IN));
            }
            if (((Lobby)stage).ChosenAi() && ((Lobby)stage).getAi() == 1) {
                this.stageContainer.enterStage(new Othello_AI(Othello_AI.AILevel.Easy),new OthelloTransition(1, 0.3, OthelloTransition.TransitionType.OUT), new OthelloTransition(1, 0.3, OthelloTransition.TransitionType.IN));
            }
            if (((Lobby)stage).ChosenAi() && ((Lobby)stage).getAi() == 2) {
                this.stageContainer.enterStage(new Othello_AI(Othello_AI.AILevel.Normal),new OthelloTransition(1, 0.3, OthelloTransition.TransitionType.OUT), new OthelloTransition(1, 0.3, OthelloTransition.TransitionType.IN));
            }
            if (((Lobby)stage).ChosenAi() && ((Lobby)stage).getAi() == 3) {
                this.stageContainer.enterStage(new Othello_AI(Othello_AI.AILevel.Hard),new OthelloTransition(1, 0.3, OthelloTransition.TransitionType.OUT), new OthelloTransition(1, 0.3, OthelloTransition.TransitionType.IN));
            }
            if (((Lobby)stage).isHelping()) {
                this.stageContainer.enterStage(new Empty(), new FadeTransition(Color.black,0.3,0,OUT), new FadeTransition(Color.black, 0.3, 0, IN));
            }
            if (((Lobby)stage).isOptions()) {
                this.stageContainer.enterStage(new Empty(), new FadeTransition(Color.black,0.3,0,OUT), new FadeTransition(Color.black, 0.3, 0, IN));
            }
        }else if (stage.getGameStageID() == GameStageID.Othello_Local) {
            if(((Othello_Local)stage).isExitToLobby()){
                this.stageContainer.enterStage(new Lobby(), new OthelloTransition(1, 0.3, OthelloTransition.TransitionType.OUT), new OthelloTransition(1, 0.3, OthelloTransition.TransitionType.IN));
            }
        }
    }

    public StageContainer getStageContainer() {
        return this.stageContainer;
    }

    @Override
    public void run() {
        long stdWait = (long) Math.floor(1000.0 / FPS);
        long lastTime = System.currentTimeMillis();
        while (true) {
            long currTime = System.currentTimeMillis();
            long deltaTime = currTime - lastTime;
            if (deltaTime < stdWait) {
                try {
                    Thread.sleep(stdWait - deltaTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            deltaTime = System.currentTimeMillis() - lastTime;
            double dt = (double)deltaTime / 1000;
            lastTime = System.currentTimeMillis();

            if (Controller.isKeyDown('m'))
                dt = 0.003;

            this.update(dt);
            this.stageContainer.update(dt);
            this.stageContainer.repaint();
        }

    }

}
