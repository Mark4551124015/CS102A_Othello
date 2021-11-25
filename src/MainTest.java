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
        Grid.setDisk(new intVct(2,2),-1);
        Grid.printGrid();

        Grid.roundsTurn(1);
        Grid.setDisk(new intVct(3,2), 1);
        //FUCK JERRY
        Grid.printGrid();

    }

}

