package graphics;

import main.ResourceManager;
import newData.Vct;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;

public class Sprite {

    private int width, height;
    private Vct baseScale;
    private Vct unitSize;
    private Color color;
    private BufferedImage img;

    protected boolean visibility;


    public Sprite(String imgName) {
        this.color = new Color(255, 255, 255, 255);
        this.baseScale = new Vct(1.0,1.0);
        this.visibility = true;
        this.setImage(imgName);
    }


    public Sprite(BufferedImage img) {
        this.color = new Color(255, 255, 255, 255);
        this.baseScale = new Vct(1.0, 1.0);
        this.visibility = true;
        this.setImage(img);
    }


    public Sprite(String imgName, Vct minSize) {
        this(imgName);
        if (this.width < minSize.x || this.height < minSize.y) {
            double scale = Math.max(minSize.x / this.width, minSize.y / this.height);
            this.resizeTo(this.width * scale, this.height * scale);
        }
    }


    //运行
    public void draw(Graphics2D g2d, AffineTransform at, Vct scale, double setAlpha) {
        if (!this.visibility)
            return;
        AffineTransform localAt = (AffineTransform) at.clone();
        localAt.scale(this.baseScale.x * scale.x, this.baseScale.y * scale.y);
        localAt.translate(- this.width / 2.0, - this.height / 2.0);
        g2d.setComposite(AlphaComposite.SrcOver.derive((float)(this.color.getAlpha() * setAlpha / 255.0)));
        g2d.drawImage(this.img, localAt, null);
    }


    //


    //返回
    public Vct getUnitSize() {
        return new Vct(this.unitSize.x, this.unitSize.y);
    }



    //设置图像
    public void setImage(BufferedImage img) {
        this.img = img;
        this.width = this.img.getWidth(null);
        this.height = this.img.getHeight(null);
        this.unitSize = new Vct(this.width, this.height);
    }

    public void setImage(String imgName) {
        this.setImage(ResourceManager.getImage(imgName));
    }

    //设置
    public void setColor(Color color) {
        this.color = color;
    }

    public void setVisibility(boolean flag) {
        this.visibility=flag;
    }



    public void resizeTo(Vct rect) {
        this.unitSize = rect;
        this.baseScale.x = rect.x / this.width;
        this.baseScale.y = rect.y / this.height;
    }

    public void resizeTo(double width, double height) {
        this.resizeTo(new Vct(width,height));
    }

    public Sprite subImage(Vct pos, Vct size) {
        Sprite res = new Sprite(this.img.getSubimage(
                (int)Math.round(pos.x / this.baseScale.x),
                (int)Math.round(pos.y / this.baseScale.y),
                (int)Math.round(size.x / this.baseScale.x),
                (int)Math.round(size.y / this.baseScale.y)));
        res.resizeTo(size);
        return res;
    }



}
