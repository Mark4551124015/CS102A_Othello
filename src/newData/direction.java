package newData;

import java.util.ArrayList;

public class direction {
    public static ArrayList<intVct> list = new ArrayList<>();
    intVct up = new intVct(-1,0);
    intVct upLeft    = new intVct(-1,-1);
    intVct left      = new intVct(0,-1);
    intVct leftDown  = new intVct(1,-1);
    intVct down      = new intVct(1,0);
    intVct downRight = new intVct(1,1);
    intVct right     = new intVct(0,1);
    intVct rightUp   = new intVct(-1,1);
    
    public intVct up() {
        return up;
    }
    public intVct UpLeft(){
        return upLeft;
    }
    public intVct left(){
        return left;
    }
    public intVct leftDown(){
        return leftDown;
    }
    public intVct down() {
        return down;
    }
    public intVct downRight(){
        return downRight;
    }
    public intVct right(){
        return right;
    }
    public intVct rightUp(){
        return rightUp;
    }
    
    public direction() {
        list.add(up);
        list.add(upLeft);
        list.add(left);
        list.add(leftDown);
        list.add(down);
        list.add(downRight);
        list.add(right);
        list.add(rightUp);
    }
    
    
    
        
}
