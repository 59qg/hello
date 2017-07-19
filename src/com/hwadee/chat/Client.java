package com.hwadee.chat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Client extends JFrame implements ActionListener {
    
    private JPanel pane;
    
    public JTextField ipName;
    
    public JLabel ip;
    
    public JLabel textRecievePort;
    
    public JTextField textRecievePortName;
    
    public JLabel textSendPort;
    
    public JTextField textSendPortName;
    
    public JLabel talk;
    
    public JTextField talkName;
    
    public JButton button;
    
    public static boolean showTag = false;
    
    // public UdpServer talkServer;
    
    public Client() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pane = new JPanel();
        textRecievePort = new JLabel("对方端口号：");
        textRecievePort.setBounds(120, 110, 100, 100);
        textRecievePortName = new JTextField(11);
        textRecievePortName.setText("");
        textRecievePortName.setBounds(220, 110, 100, 100);
        ip = new JLabel("对方IP地址ַ:");
        ip.setBounds(120, 310, 100, 100);
        ipName = new JTextField(11);
        ipName.setBounds(220, 310, 100, 100);
        ipName.setText("127.0.0.1");
        
        textSendPort = new JLabel("我的端口号：");
        textSendPort.setBounds(120, 410, 100, 100);
        textSendPortName = new JTextField(11);
        textSendPortName.setText("");
        textSendPortName.setBounds(220, 410, 100, 100);
        
        talk = new JLabel("我的昵称");
        talk.setBounds(120, 510, 100, 100);
        talkName = new JTextField(11);
        talkName.setText(" ");
        talkName.setBounds(220, 510, 100, 100);
        
        button = new JButton("进入");
        button.setBounds(200, 350, 100, 100);
        pane.add(textRecievePort);
        pane.add(textRecievePortName);
        pane.add(ip);
        pane.add(ipName);
        pane.add(textSendPort);
        pane.add(textSendPortName);
        pane.add(talk);
        pane.add(talkName);
        pane.add(button);
        button.addActionListener(this);
        
        this.add(pane);
        this.setVisible(true);
        this.setBounds(350, 100, 500, 400);
        this.setTitle("客户端");
        
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                System.exit(0);
            }
        });
        
    }
    
    public void actionPerformed(ActionEvent g) {
        // TODO Auto-generated method stub
        if (g.getSource() == button) {
            try {
                try {
                    new UdpClient(ipName.getText(),
                                  textSendPortName.getText(),
                                  InetAddress.getLocalHost().getHostAddress(),
                                  textRecievePortName.getText(),
                                  talkName.getText().trim());
                }
                catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            this.setVisible(false);
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.dispose();
        }
        
    }
    
    public static void main(String[] args) {
        new Client();
    }
    
}
