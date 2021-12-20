//Fullname: BoardPosition

package newData;


import static stage.StageContainer.BoardSize;

public class intVct {
    public int r, c;

    public intVct(int row, int column){
        this.r = row;
        this.c = column;
    }

    public Vct toPosition() {
        return new Vct(((this.c+1)*BoardSize/8-BoardSize/16)-BoardSize/2,((this.r+1)*BoardSize/8-BoardSize/16)-BoardSize/2);
    }

    public intVct() {
        this.r = this.c = 0;
    }

}
