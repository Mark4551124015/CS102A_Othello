package TEST;

import object.PlayerManager;

public class main {
    static PlayerManager pm = new PlayerManager();
    public static void main(String[] args) {
        pm.createPlayer("Jerry","Jerry");
        pm.save();
        pm.readAll();
        System.out.println(pm.getPlayer("Jerry").getUsername());
    }
}
