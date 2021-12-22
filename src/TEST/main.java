package TEST;

import net.sf.json.JSONObject;
import object.Game;
import main.PlayerManager;
import object.inGame.OperationManager;
import util.Tools;

public class main {
    static PlayerManager pm = new PlayerManager();
    static Game game;
    static OperationManager operationManager;
    public static void main(String[] args) {

        pm.readAll();
        PlayerManager.User = pm.getPlayer("Jerry");
        PlayerManager.Competitor = pm.getPlayer("Mark455");
        game = new Game(pm.User,pm.Competitor);

        game.getPlayer(-1).winCntPlus(1);

        System.out.println(pm.User.getWinCnt());
    }
}
