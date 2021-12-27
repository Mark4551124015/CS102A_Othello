package object.inGame;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import newData.Operation;
import object.Game;
import object.OthelloObject;
import object.Player;

import java.util.ArrayList;

import static newData.Operation.Operation_Type.*;
import static object.Game.gamePath;
import static util.Tools.getStringFromFile;
import static util.Tools.saveDataToFile;

public class OperationManager extends OthelloObject {
    private Game game;

    private ArrayList<Operation> operationList;
    private ArrayList<Operation> incomingOperations;


    public static final String OperationFileName="Operations";

    public OperationManager(Game game) {
        super("OperationManager");
        this.game = game;
        this.operationList = new ArrayList<>(0);
        this.incomingOperations = new ArrayList<>(0);

    }

    public void OperationHandler(Operation operation) {
        Player operator = null;
        if (this.game.getPlayer(1).getUsername().equals(operation.Operator)) {
            operator = this.game.getPlayer(1);
        }
        if (this.game.getPlayer(-1).getUsername().equals(operation.Operator)) {
            operator = this.game.getPlayer(-1);
        }

        if (operation.type == Recall) {
            this.game.playerRecall(operator);
            this.game.setHinted(false);
            this.operationList.add(operation);
        }


        if (operation.type == Surrender) {
            if (operator.getColor() == 1) {
                this.game.setWinner(this.game.getPlayer(-1));
                this.game.endTheGame();
            } else if (operator.getColor() == -1) {
                this.game.setWinner(this.game.getPlayer(1));
                this.game.endTheGame();
            }
        } else if (operation.type == MadeInHeaven) {
            this.game.getGrid().forceSetDisk(operation.position, operator.getColor());
            this.operationList.add(operation);
        } else {
            if (operator.getColor() == this.game.getCurrentSide()) {
                if (operation.type == SetDisk) {
                    this.game.getGrid().SetDisk(operation.position, operator);
                    this.operationList.add(operation);
                }
            } else if (operator.getColor() != this.game.getCurrentSide()) {
                System.out.println("Not you round");
            }
        }
    }

    public void OperationHandler(JSONObject json){
        Operation operation = new Operation(json);
        OperationHandler(operation);
    }

    public void OperationHandler(String operationStr) {
        OperationHandler(JSONObject.fromObject(operationStr));
    }

    public ArrayList getOperationList(){
        return this.operationList;
    }

    public JSONArray getOperationListJson(){
        JSONArray operationListJson = new JSONArray();
        for (Operation index : this.operationList) {
            operationListJson.add(index.toJson().toString());
        }
        return operationListJson;
    }

    public void save(){
        saveDataToFile(gamePath + this.game.getName()+"/" + OperationFileName,getOperationListJson().toString());
    }

    public void renew(){
        this.operationList = new ArrayList<>();
    }

    public void loadOperations() {
        String operationsStr = getStringFromFile(gamePath+this.game.getName()+"/"+OperationFileName);
        for (Object index : JSONArray.fromObject(operationsStr)) {
            this.incomingOperations.add(new Operation(JSONObject.fromObject(index)));
            System.out.println(index);
        }
    }

    public ArrayList getIncomingOperations(){
        return this.incomingOperations;
    }
    public void deleteFirstOperation() {
        this.incomingOperations.remove(0);
    }

    public void cleanInComingOperations() {
        if (!this.getIncomingOperations().isEmpty()) {
                this.OperationHandler((Operation) this.getIncomingOperations().get(0));
                this.deleteFirstOperation();
        } else {
        }
    }

}
