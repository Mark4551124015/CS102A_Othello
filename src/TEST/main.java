package TEST;

import net.sf.json.JSONObject;
import newData.Operation;
import newData.intVct;
import object.Game;
import object.PlayerManager;
import object.inGame.OperationManager;
import util.Tools;

import static newData.Operation.Operation_Type.MadeInHeaven;

public class main {
    static PlayerManager pm = new PlayerManager();
    static Game game;
    static OperationManager operationManager;
    public static void main(String[] args) {
        pm.readAll();
        PlayerManager.User = pm.getPlayer("Jerry");
        PlayerManager.Competitor = pm.getPlayer("Mark455");
        game = new Game(pm.User,pm.Competitor);
        operationManager = new OperationManager(game);
//        operationManager.OperationHandler(new Operation("Jerry",new intVct(0,0), MadeInHeaven));
//        game.switchRound();
//        operationManager.OperationHandler(new Operation("Mark455",new intVct(0,1), MadeInHeaven));
        game.getGrid().printGrid();
//        game.save();
//        operationManager.save();
        JSONObject json = JSONObject.fromObject(Tools.getStringFromFile("games/Game_2021-12-21_22-22-09/gameInfo"));
        game.loadGameInfo(json);
        operationManager.loadOperations();

        for (int i = 0; i < 10; i++) {
            operationManager.cleanInComingOperations();
        }


        game.getGrid().printGrid();


    }
}
