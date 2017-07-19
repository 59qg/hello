package com.hwadee.chat;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class UdpClient extends JFrame implements ActionListener {
    public byte[] buffer = new byte[1024];
    
    SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm:ss");
    
    public Menu menu;
    
    public MenuItem quit;
    
    public MenuBar wenjian;
    
    public JTextArea area;
    
    public JScrollPane pane;
    
    public JLabel nameLabel;
    
    public JTextField name;
    
    public JTextArea areaSend;
    
    public JButton send;
    
    int startThread = 0;
    
    ThreadA serverTextThread;
    
    String sendIp = "";
    
    String myIp = "";
    
    String myPort = "";
    
    String sendPort = "";
    
    String talkName = "";
    
    JPanel panle1 = new JPanel();
    
    JPanel panle2 = new JPanel();
    
    JPanel panle3 = new JPanel();
    
    FileDialog filedialog_load;
    
    FileDialog filedialog_saveAs;
    
    public static boolean serverTextThreadTag = true;
    
    Client connectClient = null;
    
    String talkClientName;
    
    public UdpClient(String sendIp,
                     String sendPort,
                     String myIp,
                     String myPort,
                     String talkName) throws IOException {
        
        this.sendIp = sendIp;
        this.myIp = myIp;
        this.sendPort = sendPort;
        this.myPort = myPort;
        this.talkName = talkName;
        
        // System.out.println(sendIp + sendPort + myIp + myPort + talkName);
        setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        wenjian = new MenuBar();
        menu = new Menu("menu");
        quit = new MenuItem("exit");
        menu.add(quit);
        wenjian.add(menu);
        setMenuBar(wenjian);
        quit.addActionListener(this);
        
        area = new JTextArea("");
        area.setBounds(0, 10, 380, 200);
        area.setSelectedTextColor(Color.RED);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.disable();
        area.setDisabledTextColor(Color.blue);
        pane = new JScrollPane();
        pane.setBounds(0, 10, 380, 200);
        pane.getViewport().add(area, null);
        this.add(pane);
        
        areaSend = new JTextArea("");
        areaSend.setBounds(0, 234, 380, 150);
        
        areaSend.setSelectedTextColor(Color.RED);
        areaSend.setLineWrap(true);
        areaSend.setWrapStyleWord(true);
        this.add(areaSend);
        
        send = new JButton("send");
        send.setBounds(230, 388, 100, 30);
        this.add(send);
        send.addActionListener(this);
        
        this.setVisible(true);
        this.setBounds(650, 100, 400, 500);
        this.setTitle(talkName + "的客户端");
        
        if (serverTextThread == null) {
            serverTextThread = new ThreadA();
            serverTextThread.start();
            System.out.println("客户端已启动，接收端口号：" + myPort + "\n");
            System.out.println("本机IP地址："
                               + InetAddress.getLocalHost()
                                            .getHostAddress()
                                            .toString());
        }
        
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                
                try {
                    if (serverTextThread != null) {
                        serverTextThread.interrupt();
                    }
                    
                }
                catch (RuntimeException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (Exception ee) {
                    
                }
                dispose();
                System.exit(0);
            }
        });
    }
    
    public void actionPerformed(ActionEvent e) {
        DatagramSocket ds = null;
        DatagramPacket dp = null;
        String mess = "";
        byte[] bytes = null;
        if (e.getSource() == send) {
            System.out.println("wode ceshi ");
            try {
                
                Date date = new Date();
                mess = "IP:" + myIp
                       + " Port:"
                       + myPort
                       + " "
                       + talkName
                       + " "
                       + dateformat.format(date)
                       + "\n";
                mess = mess + areaSend.getText();
                System.out.println(mess);
                
                bytes = messageFormat(sendIp, sendPort, mess).getBytes();
                ds = new DatagramSocket();
                dp =
                   new DatagramPacket(bytes,
                                      bytes.length,
                                      new InetSocketAddress("127.0.0.1", 5432));
                ds.send(dp);
                ds.close();
                ds = null;
                dp = null;
                System.out.println("发送消息到端口：" + sendPort + " 消息：" + mess);
                area.append(mess + "\n");
                areaSend.setText("");
                setMouse();
            }
            catch (Exception ee) {
                
            }
            
        }
        else if (e.getSource() == quit) {
            serverTextThread.interrupt();
            serverTextThreadTag = false;
            
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            dispose();
            System.exit(0);
        }
    }
    
    public void setMouse() {
        
        int height = 20;
        Point p = new Point();
        p.setLocation(0, area.getLineCount() * height);
        pane.getViewport().setViewPosition(p);
    }
    
    //
    public String messageFormat(String sendIp,
                                String sendPort,
                                String message) {
        
        return sendIp + "#" + sendPort + "#" + message;
    }
    
    protected class ThreadA extends Thread {
        DatagramSocket ds = null;
        
        DatagramPacket dp = null;
        
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            try {
                while (true) {
                    try {
                        ds = new DatagramSocket(Integer.parseInt(myPort));
                        dp = new DatagramPacket(buffer, buffer.length);
                        ds.receive(dp);
                        String info =
                                    new String(dp.getData(), 0, dp.getLength());
                        System.out.println("接收到消息： " + info);
                        area.append(info + "\n");
                        ds.close();
                        ds = null;
                        dp = null;
                    }
                    catch (IOException e) {
                        try {
                            if (ds != null) {
                                ds.close();
                                ds = null;
                            }
                        }
                        catch (Exception eee) {
                        }
                        try {
                            if (dp != null) {
                                dp = null;
                            }
                        }
                        catch (Exception ee3e) {
                        }
                    }
                    
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        public void interrupt() {
            // TODO Auto-generated method stub
            try {
                super.stop();
                System.out.println("==============客户端已停止 !");
            }
            catch (Exception e) {
            }
        }
    }
    
    public void stopThread() {
        if (serverTextThread != null) {
            serverTextThread.interrupt();
            serverTextThread = null;
        }
        
    }
    
    public static void main(String[] args) throws IOException {
        new UdpClient("127.0.0.1", "22", "127.0.0.1", "33", "小李");
    }
}
