package main;


import javafx.stage.Stage;
import object.Game;
import object.Player;
import object.PlayerManager;

import static object.Player.playerType.local;
import static object.PlayerManager.User;

import stage.GameStage;
import stage.GameStage.GameStageID;
import stage.scene.*;
import stage.StageContainer;
import stage.transition.FadeInTransition;
import input.Controller;
import stage.transition.FadeOutTransition;
import java.awt.*;

public class GameManager implements Runnable {
    public static final int FPS = 144;

    private StageContainer stageContainer;
    private PlayerManager playerManager = new PlayerManager();

    public GameManager() {
        this.stageContainer = new StageContainer();
    }

    public void start() {
        User = new Player("mark455","Mark455", local);
        Thread GMThread = new Thread(this);
        GMThread.start();
    }

    public void update(double dt) {
        this.updateStage();
    }

    private void updateStage() {
        GameStage stage = this.stageContainer.getCurrentStage();
        if (stage.getGameStageID() == GameStageID.Empty) {
            if (((Empty)stage).getTotalTime() >= 0.5 && ResourceManager.getLoadState() >= 1) {
                this.stageContainer.enterStage(new Launching(), null, new FadeInTransition(Color.black, 0.5));
//                AudioManager.PlayWithVolume("intro", 0.1, 0);
            }
        } else if (stage.getGameStageID() == GameStageID.Launching) {
            if (((Launching)stage).getTotalTime() >= 2 && ResourceManager.getLoadState() == 2) {
                this.stageContainer.enterStage(new Lobby(), new FadeOutTransition(Color.black, 1), new FadeInTransition(Color.black, 0.5));
//                AudioManager.initBGM();
//                AudioManager.playBGM();
            }
        }else if (stage.getGameStageID() == GameStageID.Lobby) {
            if (((Lobby)stage).isExiting())
                System.exit(0);
            if (((Lobby)stage).isReadyForGame()) {
                this.stageContainer.enterStage(new Lobby(), new FadeOutTransition(Color.black, 1, 0.25), new FadeInTransition(Color.black, 1, 0));
            }
            if (((Lobby)stage).isHelping()) {
                this.stageContainer.enterStage(new Empty(), new FadeOutTransition(Color.black,1,0.25), new FadeInTransition(Color.black, 1, 0));
            }
            if (((Lobby)stage).isOptions()) {
                this.stageContainer.enterStage(new Empty(), new FadeOutTransition(Color.black,1,0.25), new FadeInTransition(Color.black, 1, 0));
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
                dt = 0.002;

            this.update(dt);
            this.stageContainer.update(dt);
            this.stageContainer.repaint();
        }
    }

}
