package newData;

import net.sf.json.JSONObject;
import object.Player;

public class Operation {
    public String Operator;
    public Operation_Type type;
    public boolean isValid;
    public intVct position;

    public Operation(String operator, intVct position, Operation_Type type) {
        this.Operator = operator;
        this.type = type;
        this.position = position;
    }

    public Operation (JSONObject jsonOperation) {
        this(jsonOperation.getString("Operator"), new intVct( Integer.parseInt(jsonOperation.getString("row")), Integer.parseInt(jsonOperation.getString("column"))), typeFromString(jsonOperation.getString("type")));
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Operator", this.Operator);
        json.put("row", this.position.r);
        json.put("column", this.position.c);
        json.put("type", this.type);
        return json;
    }

    private static Operation_Type typeFromString(String str) {
        switch (str) {
            case "SetDisk":
                return Operation_Type.SetDisk;
            case "Recall":
                return Operation_Type.Recall;
            case "Surrender":
                return Operation_Type.Surrender;
            case "MadeInHeaven":
                return Operation_Type.MadeInHeaven;
        }
        return null;
    }

    public String getOperator(){
        return this.Operator;
    }

    public enum Operation_Type {
        SetDisk,Recall,Surrender,MadeInHeaven
    }


}

