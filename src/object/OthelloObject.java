package object;

import graphics.Image;
import newData.Vct;
import newData.Trans;
import component.Component;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Comparator;



public class OthelloObject {

    protected String id;
    protected boolean Visibility;
    protected Trans trans;
    protected Vct size;
    protected OthelloObject parentObject;
    protected ArrayList<OthelloObject> subObject;
    protected Image image;
    private ArrayList<Component> components;
    protected double alpha;
    private double renderPriority;
    private boolean absoluteTransform;


    public OthelloObject(String id, Image image) {
        //有id
        this.id = id;
        //关系树
        this.parentObject = null;
        this.subObject = new ArrayList<>();
        //动作,位置信息
        this.trans = new Trans();
        this.absoluteTransform = false;
        //可见
        this.Visibility = true;
        //大小
        this.size = null;
        //贴图
        this.image = image;
        if (this.image != null) {
            this.size = this.image.getUnitSize();
        }

        this.renderPriority = 0.0;
        this.alpha = 1.0;

        this.components = new ArrayList<>();
    }


    public OthelloObject(String id) {
       this(id,null);
    }

    //获取特征
    public String getId(){
        return this.id;
    }

    public boolean getVisibility(){
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
        this.parentObject = object;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSize(Vct size) {
        this.size = size;
    }

    public void setSize(double x, double y) {
        this.size = new Vct(x,y);
    }

    public void setColor(Color color) {
        this.image.setColor(color);
    }

    public void setScale(Vct scale) {
        this.trans.scale = scale;
    }

    public void setScale(double x, double y) {
        this.setScale(new Vct(x, y));
    }

    public void setScale(double scalar) {
        this.setScale(new Vct(scalar, scalar));
    }

    public void resizeTo(Vct rect) {
        this.size = rect;
        if (this.image != null)
            this.image.resizeTo(rect);
    }

    public void resizeTo(double x, double y) {
        this.resizeTo(new Vct(x, y));
    }

    //动作特征
    public void move(Vct movement) {
        this.trans.translate(movement);
    }

    public void move(double x, double y) {
        this.move(new Vct(x, y));
    }

    public void setAngle(double angle) {
        this.trans.angle=angle;
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

    public void setAbsoluteTransform(boolean flag) {
        this.absoluteTransform = flag;
    }

    //Sprite 贴图
    public void setSprite(Image image) {
        if (this.image != null) {
            this.image = image;
            if (this.image != null) {
                this.image.resizeTo(this.size);
            }
        } else {
            this.image = image;
            if (this.image != null)
                this.size = this.image.getUnitSize();
        }
    }

    public Image getSprite() {
        return this.image;
    }

    public void addComponent(Component component){
        this.components.add(component);
    }

    public void update(double dt) {
        this.components.removeIf(Component::isDestroy);
        for (Component component : this.components)
            component.update(dt);
        for (OthelloObject gameObject : this.subObject)
            gameObject.update(dt);
    }

    public void setAlpha(double a) {
        this.alpha = a;
        if (this.image != null) {
            this.image.setColor(new Color(255, 255, 255, (int)(Math.round(a * 255))));
        }
    }


    public AffineTransform render(Graphics2D g2d, AffineTransform parentTransform, double alpha) {
        if (!this.getVisibility())
            return null;
        AffineTransform at;
        if (!this.absoluteTransform)
            at = (AffineTransform) parentTransform.clone();
        else
            at = new AffineTransform();
        at = this.trans.concatenate(at);
        alpha *= this.alpha;

        ArrayList<OthelloObject> list = new ArrayList<>();
        for (OthelloObject gameObject : this.subObject) {
            if (gameObject.renderPriority < 0)
                list.add(gameObject);
        }
        list.sort(Comparator.comparingDouble(o -> o.renderPriority));
        for (OthelloObject gameObject : list)
            gameObject.render(g2d, at, alpha);

        if (this.image != null)
            this.image.draw(g2d, at, this.trans.scale, alpha);

        list.clear();
        for (OthelloObject gameObject : this.subObject) {
            if (gameObject.renderPriority >= 0)
                list.add(gameObject);
        }
        list.sort(Comparator.comparingDouble(o -> o.renderPriority));
        for (OthelloObject gameObject : list)
            gameObject.render(g2d, at, alpha);

        return at;
    }

    public AffineTransform render(Graphics2D g2d) {
        this.render(g2d, new AffineTransform(), 1.0);
        return new AffineTransform();
    }

    public AffineTransform getAbsoluteTransform() {
        ArrayList<OthelloObject> path = new ArrayList<>();
        OthelloObject u;
        for (u = this; u != null; u = u.getParentObject()) {
            path.add(u);
        }
        Object[] list = path.toArray();
        AffineTransform res = new AffineTransform();
        for (int i = path.size() - 1; i >= 0; --i) {
            res = ((OthelloObject) list[i]).trans.concatenate(res);
        }
        return res;
    }
}
