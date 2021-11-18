package Object;

import newData.Vct;
import newData.Trans;

import java.util.ArrayList;

public class OthelloObject {

    protected String id;
    protected boolean Visibility;
    protected Trans trans;
    protected Vct size;
    protected OthelloObject parentObject;
    protected ArrayList<OthelloObject> subObject;


    public OthelloObject(String id) {
        //有id
        this.id = id;
        //关系树
        this.parentObject = null;
        this.subObject = new ArrayList<>();
        //动作,位置信息
        this.trans = new Trans();
        //可见
        this.Visibility = false;
        //大小
        this.size = null;

    }

    //获取特征
    public String getId(){
        return this.id;
    }

    public boolean getVisbility(){
        return this.Visibility;
    }

    public Trans getTrans() {
        return this.trans;
    }

    public Vct getSize() { return this.size; }

    public OthelloObject getParentObject() { return this.parentObject; }

    public ArrayList<OthelloObject> getSubObject() {
        return subObject;
    }

    //更改特征
    public void setVisibility(boolean bool) { this.Visibility = bool; }

    public void setPosition(Vct position) {
        this.trans.position = position;
    }

    public void setPosition(double x, double y) {
        this.setPosition(new Vct(x, y));
    }

    public void setParentObject(OthelloObject object) {
        object.addObj(this);
    }

    //动作特征
    public void move(Vct movement) {
        this.trans.translate(movement);
    }

    public void move(double x, double y) {
        this.move(new Vct(x, y));
    }

    public void setAngle(double angle) {
        this.trans.angle(angle);
    }

    public void rotate(double angle) {
        this.trans.rotate(angle);
    }

    //关系树处理
    public boolean addObj(OthelloObject object) {
        if (object == null || object.getParentObject() != null ) {
            return false;
        }
        for (OthelloObject check : this.getSubObject()) {
            if ( check.getId() == object.getId()) {
                return false;
            }
        }
        this.subObject.add(object);
        object.setParentObject(this);
        return true;
    }

    public OthelloObject findSubObj(String id) {
        for (OthelloObject object : this.subObject) {
            if (object.getId().equals(id)) {
                return object;
            }
        }
        return null;
    }

    public boolean deleteSubObj(String id) {
        int i = 0;
        for (OthelloObject check : this.getSubObject()) {
            if ( check.getId().equals(id)) {
                this.subObject.remove(i);
                return true;
            }
            ++i;
        }
        return false;
    }

    public void detachParentObj() {
        if (this.parentObject != null) {
            this.parentObject.deleteSubObj(this.id);
        }
        this.parentObject = null;
    }

    public void detachSubObj() {
        for (OthelloObject check : this.getSubObject()) {
            check.setParentObject(null);
        }
        this.subObject.clear();
    }

    //


}
