package object.GUI;

import graphics.Image;
import graphics.Text;
import main.ResourceManager;
import newData.Vct;
import object.OthelloObject;
import object.Player;

import java.awt.*;



public class PlayerInfo extends OthelloObject {

    private OthelloObject Player_profile;
    private final OthelloObject Player_Ready;
    private final Player player;
    private final Text Player_Name;
    private final Text Player_Played;
    private final Text Player_WinRate;
    private static final int Letter_Space = 30;
    private static final Vct UserInfoSize = new Vct(240,360);

    public PlayerInfo(Player player) {
        super("Player_info_"+player.getUsername());
        this.player = player;
        this.setImage(new Image("User_info_backGround"));

        int Font_Size = 12;

        this.Player_Ready = new OthelloObject( "isReady", new Image("Ready"));

        ResourceManager.imgs.put(player.getUsername(),ResourceManager.loadImage("save/players/"+player.getUsername()+"/profile.png"));

        try {
            this.Player_profile = new OthelloObject(player.getUsername() + "_profile", new Image(player.getUsername()));
        } catch (Throwable e){
            this.Player_profile = new OthelloObject(player.getUsername() + "_profile");
        }
        this.Player_Name = new Text(player.getUsername()+"_Name", player.getName(), new Font("黑体", Font.PLAIN, Font_Size ));
        String winRate;
        if (player.getPlayed() == 0) {
            winRate = "0";
        } else {
            winRate = (player.getWinCnt()/player.getPlayed()) + "";
        }
        this.Player_Played = new Text(player.getUsername()+"_Played", "Played : "+player.getPlayed(), new Font("黑体", Font.PLAIN, Font_Size));

        this.Player_WinRate = new Text(player.getUsername()+"_WinRate", "Win Rate: "+winRate, new Font("黑体", Font.PLAIN, Font_Size));

        this.addObj(this.Player_profile);
        this.addObj(this.Player_Name);
        this.addObj(this.Player_Played);
        this.addObj(this.Player_WinRate);
        this.addObj(this.Player_Ready);
        this.Player_Ready.setVisibility(false);

        this.Player_profile.setPosition(0,-75);
        this.Player_profile.resizeTo(UserInfoSize.x/2, UserInfoSize.x/2);
        this.Player_Ready.setScale(UserInfoSize.x/this.Player_Ready.getImage().getUnitSize().x);
        this.Player_Name.setPosition(0,20);
        this.Player_Played.setPosition(0,this.Player_Name.getTrans().position.y + Font_Size + Letter_Space);
        this.Player_WinRate.setPosition(0,this.Player_Played.getTrans().position.y + Font_Size + Letter_Space);
        this.Player_Ready.setPosition(0,this.Player_WinRate.getTrans().position.y + Font_Size + Letter_Space);
    }

    @Override
    public void update(double dt) {
        this.Player_Ready.setVisibility(this.player.getReady());
        super.update(dt);
    }



}
