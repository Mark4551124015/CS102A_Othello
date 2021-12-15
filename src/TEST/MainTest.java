package TEST;

import object.Player;
import object.Game;

import static object.Player.playerType.local;


public class MainTest {
    public static void main (String[] args) {
        Player white = new Player("1","FZX", local);
        white.setColor(1);
        Player black = new Player("2", "JR", local);
        black.setColor(-1);
        Game TEST = new Game("TEST",white,black);

        for(int i = 0; i< 100; i++) {
            TEST.start();
//            if(TEST.getWinner() != null) {
//                if (TEST.getWinner().getId() == white.getId()) {
//                    white.winCntplus(1);
//                } else if (TEST.getWinner().getId() == black.getId()) {
//                    black.winCntplus(1);
//                }
//            }
            TEST.renew();
        }

        System.out.println("White winned :"+white.getWinCnt());
        System.out.println("Black winned :"+black.getWinCnt());
        System.out.print("Draw: " + (100 - black.getWinCnt() - white.getWinCnt()));

        }
    }





