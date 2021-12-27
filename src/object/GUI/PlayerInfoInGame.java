package object.GUI;

import graphics.Image;
import graphics.Text;
import main.ResourceManager;
import object.GUI.Buttons.NormalButton;
import object.OthelloObject;
import object.Player;
import util.FontLib;

import java.awt.*;

public class PlayerInfoInGame extends OthelloObject {
    private OthelloObject playerProfile;
    private Text player_name;
    private Player player;
    private NormalButton Recall;
    private NormalButton Surrender;
    private boolean wantRecall = false;
    private boolean wantSurrender= false;
    private OthelloObject Recall1;
    private OthelloObject Recall2;
    private OthelloObject Recall3;
    private final static int RecallCntSize = 30;
    private final static int RecallCntSpace = 35;
    private final static int RecallCntFirstSpace = 75;
    private int Recalled;



    public PlayerInfoInGame(Player player){
        super("User_inGame_info"+player.getUsername());

        this.playerProfile = new OthelloObject("Player Profile");
        this.setImage(new Image("User_info_backGround"));
        this.Recall = new NormalButton("Recall");
        this.Surrender = new NormalButton("Surrender");
        int Font_Size = 16;

        this.player = player;
        Font font = FontLib.GetMenuButtonFont(10);
        this.Surrender.setFont(font);
        this.Recall.setFont(font);

        this.Recall.setActive(true);

        ResourceManager.imgs.put(player.getUsername(),ResourceManager.loadImage("save/players/"+player.getUsername()+"/profile.png"));
        System.out.print("Loaded");
        this.playerProfile.setImage(new Image(player.getUsername()));

        this.player_name = new Text(player.getUsername()+"_Played", player.getName(), new Font("黑体", Font.PLAIN, Font_Size));
        this.Recall1 = new OthelloObject(this.getId()+"_Recall1", new Image("recallCnt"));
        this.Recall2 = new OthelloObject(this.getId()+"_Recall2", new Image("recallCnt"));
        this.Recall3 = new OthelloObject(this.getId()+"_Recall3", new Image("recallCnt"));


        this.addObj(this.player_name);
        this.addObj(this.playerProfile);
        this.addObj(this.Recall);
        this.addObj(this.Surrender);

        this.Recalled = 0;

        this.playerProfile.setPosition(0,-80);
        this.playerProfile.resizeTo(120,120);
        this.player_name.setPosition(0,15);
//        this.player_name.resizeTo(100, 40);
        if (player.getColor() == -1) {
            this.Recall.setPosition(60, 80);
            this.Recall1.setPosition( this.Recall.getTrans().position.x - RecallCntFirstSpace, this.Recall.getTrans().position.y);
            this.Recall2.setPosition(this.Recall.getTrans().position.x - RecallCntFirstSpace - RecallCntSpace, this.Recall.getTrans().position.y);
            this.Recall3.setPosition(this.Recall.getTrans().position.x - RecallCntFirstSpace - 2*RecallCntSpace, this.Recall.getTrans().position.y);
        }
        if (player.getColor() == 1) {
            this.Recall.setPosition(-60, 80);
            this.Recall1.setPosition(this.Recall.getTrans().position.x + RecallCntFirstSpace , this.Recall.getTrans().position.y);
            this.Recall2.setPosition(this.Recall.getTrans().position.x + RecallCntFirstSpace + RecallCntSpace, this.Recall.getTrans().position.y);
            this.Recall3.setPosition(this.Recall.getTrans().position.x + RecallCntFirstSpace + 2*RecallCntSpace, this.Recall.getTrans().position.y);
        }
        this.Recall.resizeTo(90, 50);
        this.addObj(Recall1);
        this.addObj(Recall2);
        this.addObj(Recall3);
        this.Recall1.resizeTo(RecallCntSize,RecallCntSize);
        this.Recall2.resizeTo(RecallCntSize,RecallCntSize);
        this.Recall3.resizeTo(RecallCntSize,RecallCntSize);

        this.Surrender.setPosition(0,140);
        this.Surrender.resizeTo(120,50 );
        this.Surrender.setActive(true);
    }

    public boolean isWantRecall(){
        return this.wantRecall;
    }

    public boolean isWantSurrender(){
        return this.wantSurrender;
    }

    public void setRecall(boolean a){
        this.wantRecall = a;
    }

    public void setSurrender(boolean flag) {
        this.wantSurrender = flag;
    }

    @Override
    public void update(double dt){
        if(this.Recall.isClicked()){
            this.wantRecall = true;
        }
        if(Surrender.isClicked()){
            this.wantSurrender = true;
        }

        this.Recalled = this.player.getReCalledTime();
        RecallCheck();
        super.update(dt);
    }
    public void RecallCheck(){
        if (this.Recalled == 0) {
            this.Recall1.setAlpha(1);
            this.Recall2.setAlpha(1);
            this.Recall3.setAlpha(1);
        } if (this.Recalled == 1) {
            this.Recall1.setAlpha(1);
            this.Recall2.setAlpha(1);
            this.Recall3.setAlpha(0.5);
        } if (this.Recalled == 2) {
            this.Recall1.setAlpha(1);
            this.Recall2.setAlpha(0.5);
            this.Recall3.setAlpha(0.5);
        } if (this.Recalled == 3) {
            this.Recall1.setAlpha(0.5);
            this.Recall2.setAlpha(0.5);
            this.Recall3.setAlpha(0.5);
        }
    }

}
