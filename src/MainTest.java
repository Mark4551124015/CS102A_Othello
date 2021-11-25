import Object.inGame.Disk;
import Object.inGame.DiskManager;
import newData.Trans;
import newData.Vct;
import Object.OthelloObject;
import newData.intVct;

public class MainTest {
    public static void main (String[] args) {

        DiskManager Grid = new DiskManager();
        Grid.printGrid();

        Grid.roundsTurn(-1);
        Grid.setonDisk(new intVct(2,2));
        Grid.printGrid();

        Grid.roundsTurn(1);
        Grid.setonDisk(new intVct(3,2));
        //FUCK JERRY
        Grid.printGrid();

    }

}

