package network;
import net.sf.json.JSONObject;
import util.ByteUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;

public class Client extends Thread {
    public static final int PACKAGE_HEAD_LENGTH = 2;

    private boolean ready;
    private boolean connected;
    private boolean exitFlag;


    private Socket socket = null;   //Socket套接字对象
    private String session_id;
    private InputStream is;
    private OutputStream os;

    private String failedJoinMessage;

    private Queue<JSONObject> queue;





    public Client(String host, int port) { // 新建客户端程序要传入2个数据，host地址，端口号
        this.ready = false;
        this.connected = false;
        this.exitFlag = false;

        this.queue = new LinkedList<>();

        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread connect = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    socket = new Socket();
                    socket.connect(new InetSocketAddress(host, port), 5000);
                    initStreams();
                } catch (SocketTimeoutException e) {
                    failedJoinMessage = "Connection timeout: " + host;
                    ready = true;
                    connected = false;
                } catch (SocketException e) {
                    failedJoinMessage = "Connection refused: " + host;
                    ready = true;
                    connected = false;
                } catch (IOException e) {
                }
            }
        };
        connect.start();
    }

    //初始化数据流
    private void initStreams() {
        try {
            this.is = this.socket.getInputStream();
            this.os = this.socket.getOutputStream();
        } catch (IOException e) {
        }
        this.start();
    }

    //关闭客户端
    public void terminate() {
        if (this.exitFlag) {
            return;
        }
        this.send("");
        this.exitFlag = true;
        try {
            if (this.socket != null)
                this.socket.close();
        } catch (IOException e) {
        }
        System.out.println("[Client] terminated");
    }

    public boolean send(String message) {
        if (this.getInitState() != 1)
            return false;
        if (this.os == null)
            return false;
        if (this.exitFlag)
            return false;
        byte[] sendBytes = message.getBytes(StandardCharsets.UTF_8);
        try {
            this.os.write(sendBytes.length >> 8);
            this.os.write(sendBytes.length & 0xff);
            this.os.write(sendBytes);
            this.os.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean send(JSONObject msg) {
        return this.send(msg.toString());
    }

    public int getInitState() {
        if (!this.ready) {
            return 0;
        }
        else if (this.connected) {
            return 1;
        }
        return 2;
    }

    public String getServerAddress() {
        if (this.getInitState() != 1) {
            return null;
        }
        return this.socket.getInetAddress().getHostAddress();
    }

    @Override
    public void run() {
        try {
            byte[] bytes = new byte[0];
            System.out.println("[Client] client start");

            while (!this.exitFlag) {
                if (this.is == null)
                    break;
                // process the head
                if (bytes.length < PACKAGE_HEAD_LENGTH) {
                    byte[] data = new byte[PACKAGE_HEAD_LENGTH - bytes.length];
                    int len = this.is.read(data);
                    if (len == -1)
                        continue;
                    bytes = ByteUtil.merge(bytes, data, 0, len);
                    if (len < data.length)
                        continue;
                }


                // process the body
                int bodyLength = ((bytes[0] & 0xff) << 8) + (bytes[1] & 0xff);
                if (bytes.length < PACKAGE_HEAD_LENGTH + bodyLength) {
                    byte[] data = new byte[PACKAGE_HEAD_LENGTH + bodyLength - bytes.length];
                    int len = this.is.read(data);
                    if (len == -1)
                        continue;
                    bytes = ByteUtil.merge(bytes, data, 0, len);
                    if (len < data.length)
                        continue;
                }
                byte[] body = new byte[bytes.length - PACKAGE_HEAD_LENGTH];
                System.arraycopy(bytes, PACKAGE_HEAD_LENGTH, body, 0, bytes.length - PACKAGE_HEAD_LENGTH);
                bytes = new byte[0];

                String data = new String(body, StandardCharsets.UTF_8);
                if (data.equals("")) {
                    this.exitFlag = true;
                    break;
                }
                JSONObject json = JSONObject.fromObject(data);
                if (json.getString("event_type").equals("join_result")) {
                    this.ready = true;
                    if (json.getBoolean("result")) {
                        this.connected = true;
                        this.session_id = json.getString("session_id");
                    } else {
                        this.connected = false;
                        this.failedJoinMessage = json.getString("msg");
                        break;
                    }
                } else {
                    this.queue.offer(json);
                }
            }
        } catch (IOException ignored) {
        } finally {
            this.terminate();
            System.out.println("[Client] the server has disconnected");
        }

    }

    public String getFailedJoinMessage() {
        return this.failedJoinMessage;
    }

    public JSONObject getMsg() {
        if (this.getInitState() != 1)
            return null;
        return this.queue.poll();
    }
}