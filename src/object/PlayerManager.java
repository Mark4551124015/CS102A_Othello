package object;

import net.sf.json.JSONObject;

import java.util.ArrayList;

import static object.Player.playerType.*;
import static util.JsonHanderler.saveDataToFile;

public class PlayerManager extends OthelloObject{
    private ArrayList<Player> players = new ArrayList<>();
    public static Player User;

    public PlayerManager(){
        super("PlayerManger");
    }

    public void createPlayer(String name) {
        players.add(new Player("" + players.size(), "name", local));
    }

    public Player getPlayer(String username) {
        for (Player index : players) {
            if (index.getUsername() == username) {
                return index;
            }
        }
        System.out.print("未找到用户");
        return null;
    }

    public void login (String username) {
        if (User != null) {
            Player object = getPlayer(username);
            if (object != null) {
                    User = object;
            } else {
                System.out.print("User not found");
            }
        } else {
            System.out.print("Already Login");
        }
    }

    public void savePlayer(String username) {
        if (User == null) {
            System.out.println("Failed");
        }
        JSONObject jsonObject = JSONObject.fromObject(User);
        String jsonStr=jsonObject.toString();
        saveDataToFile("/player/" +username,jsonStr);
        System.out.println("Successfully Saved");
    }

    public void readPlayerList(String username) {
        if (User == null) {
            System.out.println("Failed");
        }
    }




}
