package Object.inGame;

import Object.OthelloObject;
import newData.*;

public class Board extends OthelloObject {
    private Vct positon;
    private static double boardSize;

    private Board() {
        super("Board");
        this.positon = new Vct(0,0);
    }

    public static double getBoardSize() {
        return boardSize;
    }
}
