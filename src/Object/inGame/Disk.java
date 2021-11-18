package Object.inGame;
import Object.OthelloObject;
import newData.Vct;
import newData.intVct;

public class Disk extends OthelloObject {
    private int status;
    private boolean onBoard;
    private intVct bP;

    private double boardSize = Board.getBoardSize();

    public Disk(intVct bP, int count) {
        super("Disk_" + count);
        this.onBoard = false;
        this.size = new Vct(boardSize/8.1 , boardSize/8.1);
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

    public boolean isonBoard() {
        return this.onBoard;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
