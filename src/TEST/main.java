package TEST;

import object.PlayerManager;

public class main {
    static PlayerManager pm = new PlayerManager();
    public static void main(String[] args) {
        pm.createPlayer("Mark455","Mark455");
        pm.save();
        pm.readAll();
        System.out.println(pm.getPlayer("Mark455").getUsername());
    }
}
