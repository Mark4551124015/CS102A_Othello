package object.inGame;
import object.OthelloObject;
import newData.Vct;
import newData.intVct;

import static stage.StageContainer.BoardSize;

public class Disk extends OthelloObject {
    private int status;
    private boolean onBoard;
    private intVct bP;


    public Disk(intVct bP, int count) {
        super("Disk_" + count);
        this.onBoard = false;
        this.bP = bP;
        this.status = 0;
        this.Visibility = false;

    }

//    public Disk(Vct position, int status) {
//        super("Disk_inHand");
//        this.onBoard = false;
//        this.size = new Vct(boardSize/8.1 , boardSize/8.1);
//        this.status = status;
//    }

    public void flip() {
        this.status *= -1;
    }

    public void setOnBoard() {
        this.onBoard = true;
        this.setVisibility(true);
    }

    public boolean isOnBoard() {
        return this.onBoard;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
