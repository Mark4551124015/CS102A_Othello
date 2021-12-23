package main;


import object.PlayerInfoInGame;
import stage.GameStage;
import stage.GameStage.GameStageID;
import stage.scene.*;
import stage.StageContainer;
import stage.transition.FadeInTransition;
import input.Controller;
import stage.transition.FadeOutTransition;
import stage.transition.OthelloTransition;

import java.awt.*;

import static main.PlayerManager.User;

public class GameManager implements Runnable {
    public static final int FPS = 144;

    private StageContainer stageContainer;
    public static PlayerManager playerManager = new PlayerManager();

    public GameManager() {
        this.stageContainer = new StageContainer();
    }

    public void start() {
        playerManager.readAll();
        User = playerManager.getPlayer("Mark455");
        Thread GMThread = new Thread(this);
        GMThread.start();
    }

    public void update(double dt) {
        this.updateStage();
    }

    private void updateStage() {
        GameStage stage = this.stageContainer.getCurrentStage();
        if (stage.getGameStageID() == GameStageID.Empty) {
            if (((Empty)stage).getTotalTime() >= 0.3 && ResourceManager.getLoadState() >= 1) {
                this.stageContainer.enterStage(new Launching(), null, new FadeInTransition(Color.black, 0.5));
//                AudioManager.PlayWithVolume("intro", 0.1, 0);
            }
        } else if (stage.getGameStageID() == GameStageID.Launching) {
            if (((Launching)stage).getTotalTime() >= 0.6 && ResourceManager.getLoadState() == 2) {
                this.stageContainer.enterStage(new Lobby(), new FadeOutTransition(Color.black, 0.3), new FadeInTransition(Color.black, 0.3));
//                AudioManager.initBGM();
//                AudioManager.playBGM();
            }
        }else if (stage.getGameStageID() == GameStageID.Lobby) {
            if (((Lobby)stage).isExiting())
                System.exit(0);
            if (((Lobby)stage).ChosenLocal()) {
                this.stageContainer.enterStage(new Othello_Local(), new OthelloTransition(1, 0.3, OthelloTransition.TransitionType.OUT), new OthelloTransition(1, 0, OthelloTransition.TransitionType.IN));
            }
            if (((Lobby)stage).ChosenOnline()) {
                this.stageContainer.enterStage(new Matching(), new FadeOutTransition(Color.black, 0.3, 0.3), new FadeInTransition(Color.black, 1, 0));
            }

            if (((Lobby)stage).ChosenAi()) {
                this.stageContainer.enterStage(new Othello_AI(Othello_AI.AILevel.Hard), new FadeOutTransition(Color.black, 0.3, 0.3), new FadeInTransition(Color.black, 1, 0));
            }
            if (((Lobby)stage).ChosenLocal()) {
                this.stageContainer.enterStage(new Othello_Local(), new FadeOutTransition(Color.black, 0.3, 0.3), new FadeInTransition(Color.black, 1, 0));
            }
            if (((Lobby)stage).isHelping()) {
                this.stageContainer.enterStage(new Empty(), new FadeOutTransition(Color.black,0.3,0), new FadeInTransition(Color.black, 0.3, 0));
            }
            if (((Lobby)stage).isOptions()) {
                this.stageContainer.enterStage(new Empty(), new FadeOutTransition(Color.black,0.3,0), new FadeInTransition(Color.black, 0.3, 0));
            }
        }else if (stage.getGameStageID() == GameStageID.Othello_Local) {
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
                dt = 0.002;

            this.update(dt);
            this.stageContainer.update(dt);
            this.stageContainer.repaint();
        }
    }

}
