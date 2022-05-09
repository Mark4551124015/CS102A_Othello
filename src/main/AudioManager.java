package main;
import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
public class AudioManager extends Application {

    private static Map<String, Media> ResourceMap = new HashMap<>();
    private static ReentrantLock lock = new ReentrantLock();
    private static MediaPlayer bgmPlayer;


    public static void load(String name, String audioPath) {
        if (ResourceMap.containsKey(name))
            return;
        Media sound = new Media(new File(audioPath).toURI().toString());
        ResourceMap.put(name, sound);
    }

    public static void Play(String name, double delay) {
        Play(name, 1.0, delay);
    }

    public static void Play(String name, double volume, double delay) {
        if (ResourceMap.containsKey(name)) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        Thread.sleep(Math.round(delay * 1000), 0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!lock.isLocked()) {
                        lock.lock();
                        MediaPlayer mp = new MediaPlayer(ResourceMap.get(name));
                        mp.setVolume(volume);
                        mp.play();
                        lock.unlock();
                    }
                }
            };
            thread.start();
        }
    }

    public static void Play(String name) {
        Play(name, 0);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    public static void initBGM() {
        bgmPlayer = new MediaPlayer(ResourceMap.get("bgm"));
        bgmPlayer.setVolume(0.1);
        bgmPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                bgmPlayer.seek(Duration.ZERO);
            }
        });
    }

    public static void playBGM() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(9000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                bgmPlayer.play();
            }
        });
        thread.start();
    }

    public static void stopBGM() {
        bgmPlayer.stop();
    }
}
