import Object.inGame.Disk;
import Object.inGame.*;
import Object.Player;
import Object.Game;
import newData.Trans;
import newData.Vct;
import Object.OthelloObject;
import newData.intVct;


public class MainTest {
    public static void main (String[] args) {

        Player white = new Player("1","FZX");
        white.setColor(1);
        Player black = new Player("2", "JR");
        black.setColor(-1);
        Game TEST = new Game("TEST",white,black);
        for(int i = 0; i< 100; i++) {
            TEST.start();
            TEST.renew();
        }
        }
    }





