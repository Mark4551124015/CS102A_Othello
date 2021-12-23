package main;

import net.sf.json.JSONObject;
import network.Server;
import object.OthelloObject;
import object.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class OthelloServer {
    public final static int RoomCapacity = 2;
    private Server server;
    private int port;
    private boolean serverReadyEvent;
    Queue<JSONObject> serverMsgQueue;
    private Thread serverSyncThread;
    public final static int SleepWhenNoMsg = 20;

    private boolean inGame;
    private boolean syncEnded;

    private ArrayList<Player> players;
    private JSONObject clients;

    public OthelloServer(int port){
        this.port = port;
    }

    private void initServerSyncThread() {
        this.serverSyncThread = new Thread() {
            @Override
            public void run() {
                super.run();
                JSONObject msg;
                while (!syncEnded) {
                    msg = server.getMsg();
                    if (msg != null) {
                        serverSync(msg);
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
        this.serverSyncThread.start();
    }

    private void serverSync(JSONObject msg) {
        switch(msg.getString("event_type")) {
            case "Player_join_application":
                this.server_PlayerJoin(msg.getString("session_id"), msg.getString("Player"));
                break;

            case "room_player_toggle_ready":
                this.player_toggleReady(msg.getString("username"), msg.getBoolean("flag"));
                break;
            case "room_getExistPlayers":
                this.server_getExistPlayers(msg.getString("session_id"));
                break;

//            case "room_client_join":
//                if (this.inGame) {
//                    this.server_announceEnterGame(msg.getString("session_id"));
//                    JSONObject json = new JSONObject();
//                    json.put("event_type", "init_spectator");
//                    json.put("session_id", msg.getString("session_id"));
//                    this.serverMsgQueue.offer(json);
//                }
//                break;

            case "room_client_leave":
                if (!this.inGame)
                    this.server_playerLeaved(msg.getString("session_id"));
                else
                    this.shutdown();
                break;
            default:
                this.serverMsgQueue.offer(msg);
        }
    }


    public void server_setAcceptJoin(boolean flag) {
        this.server.setActive(flag);
    }

    public int getServerInitState() {
        return this.server.getInitState();
    }

    public JSONObject getMsgForServer() {
        return this.serverMsgQueue.poll();
    }

    public void shutdown() {
        JSONObject msg = new JSONObject();
        this.server_announce_shutdown();
        this.server.terminate();
        this.syncEnded = true;
    }

    private void server_PlayerJoin(String session_id, String PlayerStr) {
        int playerNum = this.players.size();
        if (playerNum == RoomCapacity) {
            JSONObject msg = new JSONObject();
            msg.put("event_type", "room_player_join_fail");
            msg.put("message", "房间已满!");
            this.server.send(session_id, msg);
        } else {
            JSONObject msg = new JSONObject();
            msg.put("event_type", "room_player_join_success");
            msg.put("Player", PlayerStr);
            msg.put("ip", "Client");
            msg.put("need_announcement", true);
            this.server.sendAll(msg);
            // [end]
        }
    }

    private void server_playerLeaved(String session_id) {
        while(true) {
            boolean has = false;
            for (Player player : this.players) {
                if (this.clients.get(player.getUsername()).equals(session_id)) {
                    JSONObject msg = new JSONObject();
                    msg.put("event_type", "room_player_quit");
                    msg.put("username", player.getUsername());
                    this.server.sendAll(msg);
                    // [end]
                    has = true;
                    break;
                }
            }
            if (!has)
                break;
        }
    }

    private void server_getExistPlayers(String session_id) {
        for (Player player : this.players) {
            JSONObject msg = new JSONObject();
            msg.put("event_type", "room_player_join_success");
            msg.put("Player", player.toJson().toString());
            msg.put("need_announcement", false);
            this.server.send(session_id, msg);
        }
    }

    public void server_announceEnterGame() {
        // [Client]
        JSONObject msg = new JSONObject();
        msg.put("event_type", "enter_game");
        this.server.sendAll(msg);
        // [end]
    }

    public boolean isDead() {
        return (this.getServerInitState() == 2) || this.syncEnded;
    }

    public Server getServer() {
        return this.server;
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    private void player_toggleReady(String username, boolean flag) {
        for (Player player : this.players) {
            if (player.getUsername().equals(username)) {
                JSONObject msg = new JSONObject();
                msg.put("event_type", "room_player_toggle_ready");
                msg.put("username", player.getUsername());
                msg.put("flag", flag);
                this.server.sendAll(msg);
            }
        }
    }

    private void server_announce_shutdown() {
        JSONObject msg = new JSONObject();
        msg.put("event_type", "room_server_shutdown");
        this.server.sendAll(msg);
    }

    public void init() {
        this.server = new Server(this.port);
        this.serverMsgQueue = new LinkedList<>();
        this.serverReadyEvent = false;
        this.inGame = false;
        this.players = new ArrayList<>();
        this.clients = new JSONObject();
        // exit handler
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                super.run();
                shutdown();
            }
        });
    }


    public static void main(String[] args) {
        OthelloServer server = new OthelloServer(14514);
        server.init();

    }

}
