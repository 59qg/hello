package com.hwadee.chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class Server {
    byte[] buffer = new byte[1024];
    
    public Server() throws IOException {
        
        // DatagramSocket ds =
        // new DatagramSocket(5432, InetAddress.getLocalHost());
        DatagramSocket ds = new DatagramSocket(5432);
        DatagramSocket sds = new DatagramSocket();
        DatagramPacket dp;
        dp = new DatagramPacket(buffer, buffer.length);
        
        System.out.println("服务器端启动！");
        
        do {
            ds.receive(dp);
            Thread td = new ServerThread(dp, sds);
            td.start();
            
        }
        while (true);
        
    }
    
    public static void main(String[] args) throws IOException {
        new Server();
    }
}

class ServerThread extends Thread {
    
    private DatagramSocket sds = null;
    
    private DatagramPacket dp = null;
    
    private DatagramPacket sdp = null;
    
    String sendPort;
    
    String sendIp;
    
    String message;
    
    byte[] buffer = new byte[1024];
    
    public ServerThread(DatagramPacket dp, DatagramSocket sds) {
        this.dp = dp;
        this.sds = sds;
        
    }
    
    public void run() {
        // TODO Auto-generated method stub
        String info = new String(dp.getData(), 0, dp.getLength());
        String[] arr = info.split("#");
        sendIp = arr[0];
        sendPort = arr[1];
        message = arr[2];
        System.out.println("服务器接收消息： " + message);
        new ConnectDB().insert(sendIp, sendPort, message);
        
        buffer = message.getBytes();
        try {
            try {
                sdp =
                    new DatagramPacket(buffer,
                                       buffer.length,
                                       new InetSocketAddress(sendIp,
                                                             Integer.parseInt(sendPort)));
            }
            catch (SocketException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        catch (NumberFormatException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            sds.send(sdp);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
