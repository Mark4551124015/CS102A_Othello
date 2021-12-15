package main;

import object.GUI.Attention;
import java.awt.*;
import java.util.ArrayList;

public class AttentionManager {
    private static ArrayList<Attention> messages = new ArrayList<>();
    public static void update(double dt) {
        messages.removeIf(Attention::isFinished);
        for (Attention attention : messages)
            attention.update(dt);
    }

    public static void render(Graphics2D g2d) {
        for (Attention attention : messages)
            attention.render(g2d);
    }

}
