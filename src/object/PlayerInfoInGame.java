package object;

import graphics.Sprite;
import graphics.Text;
import javafx.scene.control.Button;
import main.ResourceManager;
import object.GUI.Buttons.MenuButton;
import object.GUI.Buttons.NormalButton;
import util.FontLibrary;

import java.awt.*;

public class PlayerInfoInGame extends OthelloObject{
    private OthelloObject playerProfile;
    private Text player_name;
    private NormalButton Recall;
    private NormalButton Surrender;
    private boolean wantRecall;
    private boolean wantSurrender;

    public PlayerInfoInGame(Player player){
        super("User_info"+player.getUsername());

        this.playerProfile = new OthelloObject("Player Profile");
        this.setSprite(new Sprite("User_info_backGround"));
        this.Recall = new NormalButton("Recall");
        this.Surrender = new NormalButton("Surrender");
        int Font_Size = 18;

        Font font = FontLibrary.GetMenuButtonFont(10);
        this.Surrender.setFont(font);
        this.Recall.setFont(font);

        ResourceManager.imgs.put(player.getUsername(),ResourceManager.loadImage("save/players/"+player.getUsername()+"/profile.png"));
        System.out.print("Loaded");
        this.playerProfile.setSprite(new Sprite(player.getUsername()));

        this.player_name = new Text(player.getUsername()+"_Played", player.getName(), new Font("黑体", Font.PLAIN, Font_Size));
        this.addObj(this.player_name);
        this.addObj(this.playerProfile);
        this.addObj(this.Recall);
        this.addObj(this.Surrender);

        this.playerProfile.setPosition(0,-80);
        this.playerProfile.resizeTo(100,100);
        this.player_name.setPosition(0,15);
//        this.player_name.resizeTo(100, 40);
        this.Recall.setPosition(0,80);
        this.Recall.resizeTo(120, 50);
        this.Surrender.setPosition(0,150);
        this.Surrender.resizeTo(120,50 );
    }

    public boolean isWantRecall(){
        return this.wantRecall;
    }

    public boolean isWantSurrender(){
        return this.wantSurrender;
    }

    public void setSurrender(boolean a){
        this.wantSurrender = a;
    }

    public void setRecall(boolean a){
        this.wantRecall = a;
    }

    @Override
    public void update(double dt){
        if(Recall.isClicked()){
            this.wantRecall = true;
        }
        if(Surrender.isClicked()){
            this.wantSurrender = true;
        }
        super.update(dt);
    }
}
