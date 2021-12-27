package main;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import object.OthelloObject;
import object.Player;
import util.Tools;

import java.io.*;
import java.util.ArrayList;

import static object.Player.playerType.*;
import static util.Tools.*;

public class PlayerManager extends OthelloObject {
    private ArrayList<Player> players;
    private ArrayList<String> playersName;

    public static Player User;
    public static Player Competitor;

    public static String playerPath = "players/";

    public PlayerManager(){
        super("PlayerManger");
        this.players = new ArrayList<>();
        this.playersName = new ArrayList<>();
    }

    public void init() {

    }

    public void createPlayer(String username, String name) {
        if (this.players.size() >= 4) {
            return;
        }
        for (String index : this.playersName) {
            if (index.equals(username)) {
                return;
            }
        }

        try{
            File file = new File("./save/"+playerPath + username);
            file.mkdir();
            File oldFile = new File("./Resources/profile.png");
            File newfile = new File("./save/"+playerPath + username+ "/profile.png");

            FileInputStream in = new FileInputStream(oldFile);
            FileOutputStream out = new FileOutputStream(newfile);;


            byte[] buffer=new byte[2097152];
            int readByte = 0;
            while((readByte = in.read(buffer)) != -1) {
                out.write(buffer, 0, readByte);
            }
            in.close();
            out.close();

            this.players.add(new Player(username, name, local));
            this.playersName.add(username);

        } catch (Exception e) {
            System.out.println("Failed to create");
        }

    }

    public Player getPlayer(String username) {
        for (Player index : this.players) {
            if (index.getUsername().equals(username)) {
                return index;
            }
        }
        System.out.print("未找到用户");
        return null;
    }

    public ArrayList getPlayers(){
        return this.players;
    }

    public ArrayList getPlayersName(){
        return this.playersName;
    }

    public void savePlayers() {
        for (Player player : this.players) {
            if (player == null) {
                System.out.println("Failed to save null Player");
                return;
            }

            if (player.getType() != local) {
                System.out.println("Failed to save not Player on cloud");
                return;
            }
            JSONObject jsonPlayer = player.toJson();
            String str = jsonPlayer.toString();
            saveDataToFile("players/" + player.getUsername() + "/" + player.getUsername(), str);
            System.out.println("Successfully Saved");
        }
    }

    public void  savePlayersName() {
//        System.out.println(this.playersName.toString());
        JSONArray names = new JSONArray();
        for (String name : this.playersName) {
//            JSONObject nameJson = new JSONObject();
//            nameJson.put("username",name);
            names.add(name);
        }
        Tools.saveDataToFile(playerPath+"playerNameList",names.toString());
    }

    private void readPlayersName() {
       String playersNameString = getStringFromFile(playerPath + "playerNameList");
       JSONArray players = JSONArray.fromObject(playersNameString);
       for (int i =0; i<players.size();i++) {
           this.playersName.add(players.getString(i));
       }
    }

    private void readPlayers() {
        try {
            for (String index : this.playersName) {
                JSONObject jsonPlayer = JSONObject.fromObject(Tools.getStringFromFile(playerPath+index+"/"+index));
                this.players.add(new Player(jsonPlayer));
            }
        } catch (Exception e) {}
    }

    public void readAll() {
        this.readPlayersName();
        this.readPlayers();
    }

    public void save() {
        this.savePlayersName();
        this.savePlayers();
    }

    public void deletePlayer(Player player){
       if(player != null) {
           Tools.delete(playerPath+player.getUsername());
           removePlayer(this.players, player);
           removeString(this.playersName, player.getUsername());
       }
       System.out.println("删除成功");
    }


}
