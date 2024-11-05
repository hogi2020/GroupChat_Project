package Messenger;

import javax.swing.*;
import java.awt.*;

public class MessengerUI extends JFrame {
    JTextField msg_insert;
    JTextArea msg_display;
    JTextArea room_display;
    JButton send_button;
    //JButton rename_button;
    JButton create_room;
    DefaultListModel<String> listModel_room = new DefaultListModel<>();
    JList<String> list_room = new JList<>(listModel_room);
    JScrollPane msgScrollPane = new JScrollPane(msg_display);
    JScrollPane roomScrollPane = new JScrollPane(list_room);
    JLabel room_Label;
    Font font = new Font("netmarble Medium", Font.PLAIN, 15);
    Color color = new Color(225, 227, 255);
    String imagePath = "C:\\workspace_java\\Messenger\\src\\image\\";


    public MessengerUI() {
        this.setSize(715, 535);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        //크기고정
        this.setResizable(false);

        msg_insert = new JTextField();
        msg_display = new JTextArea();
        room_display = new JTextArea();
        send_button = new JButton(new ImageIcon(imagePath + "sendB.png"));
        //rename_button = new JButton(new ImageIcon(imagePath + "renicB2.png"));
        create_room = new JButton(new ImageIcon(imagePath + "create_roomB.png"));
        msgScrollPane = new JScrollPane(msg_display);
        roomScrollPane = new JScrollPane(room_display);
        room_Label = new JLabel("   채팅방 목록");
        //수정불가
        msg_display.setEditable(false);
        room_display.setEditable(false);
        //글자
        send_button.setFont(font);
        //rename_button.setFont(font);
        msg_insert.setFont(font);
        msg_display.setFont(font);
        msg_display.setForeground(new Color(0, 0, 0, 255));
        room_display.setFont(font);
        room_display.setForeground(new Color(43, 43, 43, 255));
        msgScrollPane.setFont(font);
        roomScrollPane.setFont(font);
        room_Label.setFont(font);
        //배경색
        msg_display.setBackground(color);
        room_display.setBackground(new Color(183, 255, 249, 255));
        send_button.setBackground(new Color(183, 183, 183, 255));
        //rename_button.setBackground(new Color(183, 183, 183, 255));;
        msgScrollPane.setBackground(color);
        roomScrollPane.setBackground(color);
        room_Label.setBackground(new Color(255, 183, 183, 255));
        //위치
        msgScrollPane.setBounds(1, 0, 530, 457);
        room_Label.setBounds(530, 0, 172, 40);
        roomScrollPane.setBounds(530, 40, 172, 417);
        msg_insert.setBounds(1, 457, 445, 40);
        send_button.setBounds(445, 457, 85, 40);
        //rename_button.setBounds(530, 457, 172, 40);
        create_room.setBounds(530, 457, 172, 40);
        //스크롤
        msgScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        roomScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //삽입
        this.add(msg_insert);
        this.add(send_button);
        //this.add(rename_button);
        this.add(create_room);
        this.add(msgScrollPane);
        this.add(roomScrollPane);
        this.add(room_Label);

        this.setVisible(false);
    }
    public JTextField getMsgInsertField() {return msg_insert;}
    public JTextArea getMsgDisplay() {return msg_display;}
    public JButton getSendButton() {return send_button;}
    //public JButton getRenameButton() {return rename_button;}
    public JTextArea getroomDisplay() {return room_display;}
    public JButton getCreateRoom() {return create_room;}
    // 메세지를 창에 업데이트
    public void displayMsg(String inMsg) {
        room_display.append(" " + inMsg + "\n");
    }
    // 그룹 목록 List 업데이트
    public void updateRoomList(String[] rooms) {
        listModel_room.clear();
        for (String room : rooms) {
            listModel_room.addElement(room);
        }
    }
}

/************** 미구현
 // Rename Class
 class Rename extends JFrame {
 JTextField renickname;
 JButton insert_button;
 Font font = new Font("netmarble Medium", Font.PLAIN, 15);
 String imagePath = "C:\\workspace_java\\Messenger\\src\\image\\";
 ImageIcon img = new ImageIcon(imagePath + "renic.png");

 //배경이미지
 class Mypanel extends JPanel {
 public void paintComponent(Graphics g) {
 g.drawImage(img.getImage(), 0, 0, null);
 super.paintComponents(g);
 }
 }

 public Rename() {
 renickname = new JTextField();
 insert_button = new JButton(new ImageIcon(imagePath + "renicB.png"));
 setContentPane(new Rename.Mypanel());

 //기본설정
 this.setLayout(null);
 this.setTitle("닉네임 변경");
 this.setSize(310, 310);
 this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 this.setLocationRelativeTo(null);

 //글씨설정
 insert_button.setFont(font);
 insert_button.setForeground(new Color(40,40,40));
 renickname.setFont(font);
 renickname.setForeground(new Color(40,40,40));
 renickname.setHorizontalAlignment(SwingConstants.CENTER);
 renickname.setCaretColor(new Color(40,40,40));

 //버튼테두리제거
 insert_button.setBorder(null);

 //위치설정
 renickname.setBounds(50, 145, 200, 30);
 insert_button.setBounds(100, 195, 100, 30);

 //요소삽입
 this.add(renickname);
 this.add(insert_button);

 //사이즈변경불가
 this.setResizable(false);
 }

 public JTextField getRenameField() {
 return renickname;
 }

 public JButton getRenameButton() {
 return insert_button;
 }
 }
 *********************************************/
