package Object;

public class Player extends OthelloObject{
    private String playerID;
    private String playerName;

    private boolean Active;
    private boolean Ready;
//    private boolean isOnline;
//    private boolean isLocal;


    private int Score;
    private boolean isCheating;


    public Player(String playerID, String playerName) {
        super("Player_" + playerID);
        this.playerID = playerID;
        this.Ready = false;
        this.Active = true;
        this.isCheating = false;
    }

    public boolean getReady() {
        return this.Ready;
    }

    public void setReady(boolean bool) {
        this.Ready = bool;
    }

    public String Name() {
        return this.playerName;
    }

    public void reName(String name) {
        this.playerName = name;
    }







}
