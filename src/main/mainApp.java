package main;

import input.Controller;
import javafx.embed.swing.JFXPanel;
import newData.Vct;

import javax.swing.*;

public class mainApp extends JFrame {
    public static final int Width = 1280;
    public static final int Height = 720;
    public static final Vct WinSize = new Vct(Width, Height);
    public static GameManager gm;
    public static Controller controller;

    public mainApp() {
        this.setTitle("Othello");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void init() {
        this.add(gm.getStageContainer());
        this.pack();
        this.setLocationRelativeTo(null);
        this.addKeyListener(controller);
        this.addMouseListener(controller);
        this.addMouseMotionListener(controller);
        this.addMouseWheelListener(controller);
        SwingUtilities.invokeLater(JFXPanel::new);
    }

    public static void main(String[] args) {
        ResourceManager.init();
        mainApp app = new mainApp();
        gm = new GameManager();
        controller = new Controller();
        gm.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                super.run();
                gm.playerManager.save();
            }
        });

        app.init();
        app.setVisible(true);
    }




}
