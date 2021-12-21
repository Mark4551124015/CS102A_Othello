package object;

import graphics.Sprite;
import graphics.Text;
import main.ResourceManager;
import newData.Vct;

import java.awt.*;



public class PlayerInfo extends OthelloObject{

    private OthelloObject Player_profile;
    private OthelloObject Player_Ready;
    private Player player;
    private Text Player_Name;
    private Text Player_Played;
    private Text Player_WinRate;
    private static final int Letter_Space = 30;
    private static Vct UserInfoSize = new Vct(240,360);

    public PlayerInfo(Player player) {
        super("User_info");
        this.player = player;
        this.setSprite(new Sprite("User_info_backGround"));

        int Font_Size = 12;

        this.Player_Ready = new OthelloObject( "isReady", new Sprite("Ready"));

        ResourceManager.imgs.put(player.getUsername(),ResourceManager.loadImage("save/players/"+player.getUsername()+"/profile.png"));

        try {
            this.Player_profile = new OthelloObject(player.getUsername() + "_profile", new Sprite(player.getUsername()));
        } catch (Throwable e){
            this.Player_profile = new OthelloObject(player.getUsername() + "_profile");
        }
        this.Player_Name = new Text(player.getUsername()+"_Name", player.getName().toString(), new Font("黑体", Font.PLAIN, Font_Size ));
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
        this.Player_Ready.setScale(UserInfoSize.x/this.Player_Ready.sprite.getUnitSize().x);
        this.Player_Name.setPosition(0,20);
        this.Player_Played.setPosition(0,this.Player_Name.trans.position.y + Font_Size + Letter_Space);
        this.Player_WinRate.setPosition(0,this.Player_Played.trans.position.y + Font_Size + Letter_Space);
        this.Player_Ready.setPosition(0,this.Player_WinRate.trans.position.y + Font_Size + Letter_Space);
    }

    @Override
    public void update(double dt) {
        if (this.player.getReady()) {
            this.Player_Ready.setVisibility(true);
        } else {
            this.Player_Ready.setVisibility(false);
        }
        super.update(dt);
    }



}
