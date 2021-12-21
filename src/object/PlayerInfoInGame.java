package object;

import graphics.Sprite;
import graphics.Text;
import javafx.scene.control.Button;
import main.ResourceManager;
import object.GUI.Buttons.NormalButton;

import java.awt.*;

public class PlayerInfoInGame extends OthelloObject{
    private OthelloObject playerProfile;
    private Text player_name;
    private NormalButton Recall;
    private NormalButton Surrender;

    public PlayerInfoInGame(Player player){
        super("User_info");

        this.playerProfile = new OthelloObject("Player Profile");

        this.setSprite(new Sprite("User_info_backGround"));

        int Font_Size = 12;

        ResourceManager.imgs.put(player.getUsername(),ResourceManager.loadImage("save/players/"+player.getUsername()+"/profile.png"));
        this.playerProfile.setSprite(new Sprite(player.getUsername()));

        this.player_name = new Text(player.getUsername()+"_Played", "null", new Font("黑体", Font.PLAIN, Font_Size));
        this.addObj(this.player_name);
        this.addObj(this.playerProfile);


        this.playerProfile.setPosition(0,-80);
        this.playerProfile.resizeTo(100,100);
        this.player_name.setPosition(0,0);
        this.player_name.resizeTo(100, 100);
    }
}
