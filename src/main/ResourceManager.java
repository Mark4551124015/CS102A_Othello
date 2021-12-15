package main;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import util.ErrorMsg;
import javafx.util.Pair;

import javax.imageio.ImageIO;


public class ResourceManager {
//    public static final int MineBackgroundNum = 4;

    private static ArrayList<Pair<Pair<String, String>, Color>> basicList = new ArrayList<>();
    private static ArrayList<Pair<Pair<String, String>, Color>> fullList = new ArrayList<>();
    private static ArrayList<Pair<String, String>> soundList = new ArrayList<>();
    private static Map<String, BufferedImage> imgs = new HashMap<>();
    private static boolean basicLoaded;
    private static boolean fullyLoaded;

    private static class LoadingThread implements Runnable {

        private static void load(ArrayList<Pair<Pair<String, String>, Color>> basicList, ArrayList<Pair<Pair<String, String>, Color>> fullList) {
            for (Pair<Pair<String, String>, Color> pair : basicList)
                imgs.put(pair.getKey().getKey(), loadImage("Resources/" + pair.getKey().getValue(), pair.getValue()));
            for (Pair<String, String> pair : soundList) {
                AudioManager.load(pair.getKey(), "Resources/" + pair.getValue());
            }
            basicLoaded = true;
            for (Pair<Pair<String, String>, Color> pair : fullList) {
                imgs.put(pair.getKey().getKey(), loadImage("Resources/" + pair.getKey().getValue(), pair.getValue()));
                System.out.println("Finish loading " + pair.getKey().getKey());
            }
            fullyLoaded = true;
        }

        @Override
        public void run() {
            basicLoaded = fullyLoaded = false;
            load(basicList, fullList);
        }

    }





    private static void addList(ArrayList<Pair<Pair<String, String>, Color>> list, String textureName, String imgPath) {
        addList(list, textureName, imgPath, Color.white);
    }

    private static void addList(ArrayList<Pair<Pair<String, String>, Color>> list, String textureName, String imgPath, Color color) {
        list.add(new Pair<>(new Pair<>(textureName, imgPath), color));
    }
    private static void addSoundList(ArrayList<Pair<String, String>> list, String soundName, String soundPath) {
            list.add(new Pair<>(soundName, soundPath));
        }

    public static void init() {
        addList(fullList, "background", "background.jpg");
        addList(fullList, "popo","popo.png");
        addList(fullList, "Black_Disk","Black_Disk.png");
        addList(fullList, "White_Disk","White_Disk.png");
        addList(fullList, "Board","Board.png");
        addList(fullList, "Game_BackGround", "background.jpg");
        addList(fullList, "BoardIndex", "BoardIndex.png");



        Thread loadingThread = new Thread(new LoadingThread());
        loadingThread.start();
    }

    public static int getLoadState() {
        if (!basicLoaded && !fullyLoaded)
            return 0;
        if (basicLoaded && !fullyLoaded)
            return 1;
        return 2;
    }

    private static BufferedImage loadImage(String imgPath) {
        return loadImage(imgPath, Color.white);
    }

    private static BufferedImage loadImage(String imgPath, Color color) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new FileInputStream(imgPath));
        } catch (IOException e) {
            ErrorMsg.error(String.format("File error: %s", e.getMessage()));
        }
        BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = res.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        float[] scale = {color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f};
        RescaleOp rop = new RescaleOp(scale, new float[4], null);
        return rop.filter(res, null);
    }

    public static BufferedImage getImage(String name) {
        return imgs.get(name);
    }
    }

