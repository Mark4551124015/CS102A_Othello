package network;

import graphics.Text;
import main.AttentionManager;
import net.sf.json.JSONObject;
import object.OthelloObject;
import object.Player;
import util.FontLib;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

import static main.PlayerManager.Competitor;
import static main.PlayerManager.User;

public class Client_Room extends OthelloObject {
    private Client client;

    private Text serverAddress;

    public final static int SleepWhenNoMsg = 20;
    private boolean clientReadyEvent;
    private boolean syncEnded;

    private boolean autoClean;
    private boolean shutdownHandled;
    private boolean inGame;

    Queue<JSONObject> clientMsgQueue;
    private Thread clientSyncThread;

    public Client_Room() {
        super("room");

        this.serverAddress = new Text("server_address", "", FontLib.GetMenuButtonFont(30));
        this.serverAddress.setPosition(0, 250);
        this.serverAddress.setColor(new Color(212, 212, 212));
        this.addObj(this.serverAddress);

//        this.client = new Client(address, port);
        initClientSyncThread();

        this.clientReadyEvent = false;

        Competitor = null;

        this.clientMsgQueue = new LinkedList<>();

        this.inGame = false;

        // exit handler
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                super.run();
                leave();
            }
        });
    }

    @Override
    public void update (double dt) {
        super.update(dt);
        if (this.client.getInitState() == 1) {
            this.serverAddress.setText("Server: " + this.client.getServerAddress());
        }
    }

    public String getJoinFailInfo() {
        if (this.client.getInitState() == 2)
            return this.client.getFailedJoinMessage();
        return null;
    }

    public void client_updateMapInfo() {
        JSONObject msg = new JSONObject();
        msg.put("event_type", "update_mapInfo");
        msg.put("session_id", this.client.getSession_id());
        this.client.send(msg);
    }

    public int getClientInitState() {
        return this.client.getInitState();
    }

    public boolean clientReadyEvent() {
        if (!this.clientReadyEvent && this.client != null && this.client.getInitState() != 0) {
            this.clientReadyEvent = true;
            return true;
        }
        return false;
    }

    private void initClientSyncThread() {
        this.clientSyncThread = new Thread() {
            @Override
            public void run() {
                super.run();
                JSONObject msg;
                while (!syncEnded) {
                    msg = client.getMsg();
                    if (msg != null) {
                        clientSync(msg);
                    } else {
                        try {
                            Thread.sleep(SleepWhenNoMsg);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        this.clientSyncThread.start();
    }

    private void clientSync(JSONObject msg) {
        switch(msg.getString("event_type")) {
            case "room_player_join_success":   //玩家加入
                this.serverCallback_playerJoined(
                        new Player(JSONObject.fromObject(msg.getInt("Player"))),
                        msg.getBoolean("need_announcement")
                );
                break;
            case "room_player_join_fail":
                AttentionManager.showWarnMessage("Sorry, the room is full!");
                break;
            case "room_player_quit":
                this.serverCallback_playerQuit(msg.getString("username"));
                break;
            case "room_server_shutdown":
                this.client_onServerShutdown();
                break;
            case "room_player_toggle_ready":
                this.serverCallback_toggleReady(msg.getString("Username"), msg.getBoolean("flag"));
                break;
            case "enter_game":
                this.serverCallback_enterGame();
                break;
            default:
                this.clientMsgQueue.offer(msg);
        }
    }

    public void leave() {
        JSONObject msg = new JSONObject();
        msg.put("event_type", "room_client_leave");
        msg.put("session_id", this.client.getSession_id());
        if (this.client != null) {
            this.client.send(msg);
            this.client.terminate();
        }
        this.syncEnded = true;
    }

    public void Join_Server() {
        JSONObject msg = new JSONObject();
        msg.put("event_type", "Player_join_application");
        msg.put("session_id", this.client.getSession_id());
        msg.put("Player", User.toJson().toString());
        this.client.send(msg);
    }

    private void serverCallback_playerJoined(Player player, boolean needAnnouncement) {
        if (player.getUsername() != User.getUsername()) {
            Competitor = player;
            if ( needAnnouncement) {
                AttentionManager.showGoodMessage(Competitor.getName() + " has joined the game");
            }
        }
    }

    public void serverCallback_playerQuit(String Username) {
        if (Competitor!=null && Competitor.getUsername() == Username) {
            AttentionManager.showWarnMessage(Competitor.getName() + " has leave the game");
            Competitor=null;
        }
    }

    public void serverCallback_toggleReady(String username, boolean bool) {
        if (Competitor!=null) {
            if (Competitor.getUsername() == username) {
                Competitor.setReady(bool);
            }
        }

        if (User.getUsername() == username) {
            User.setReady(bool);
        }

    }

    public void serverCallback_enterGame(){
        this.inGame = true;
        User.resetForGame();
    }

    public void client_toggleReady(boolean flag) {
        JSONObject msg = new JSONObject();
        msg.put("event_type", "room_player_toggle_ready");
        msg.put("username", User.getUsername());
        msg.put("flag", flag);
        this.client.send(msg);
    }

    public void client_getCompetitor() {
        JSONObject msg = new JSONObject();
        msg.put("event_type", "Client_get_Competitor");
        msg.put("session_id", this.client.getSession_id());
        this.client.send(msg);
    }

    private void client_onServerShutdown() {
        AttentionManager.showWarnMessage("The room has been dismissed by the host");
        this.leave();
    }

    public void setShutdownHandled(boolean flag) {
        this.shutdownHandled = flag;
    }

    public boolean isInGame() {
        return this.inGame;
    }

    public boolean getShutdownHandled() {
        return this.shutdownHandled;
    }

    public boolean isDead() {
        return this.getClientInitState() == 2 || this.syncEnded;
    }

    public Client getClient() {
        return this.client;
    }

    public void connect(String host, int port) {
        this.client = new Client(host, port);
        initClientSyncThread();
    }

}
