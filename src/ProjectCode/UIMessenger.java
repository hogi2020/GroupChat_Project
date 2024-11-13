package ProjectCode;

import javax.swing.*;
import java.awt.*;

//닉네임 입력창 UI
class InsertNickname extends JFrame{
    JTextField nickname;
    JButton insert_button;
    Font font = new Font("netmarble Medium", Font.PLAIN, 15);
    String imagePath = "src\\image\\";

    public InsertNickname() {
        nickname = new JTextField();
        insert_button = new JButton(new ImageIcon(imagePath + "insertB.png"));
        setContentPane(new JPanel() {
            private final Image img = Toolkit.getDefaultToolkit().getImage(imagePath + "insertnic.png");

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img, 0, 0, this);
            }
        });

        //프레임 기본설정
        setLayout(null);
        setTitle("닉네임 입력");
        setSize(310, 310);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //글씨설정
        nickname.setFont(font);
        nickname.setForeground(new Color(40, 40, 40));
        nickname.setHorizontalAlignment(SwingConstants.CENTER);
        nickname.setCaretColor(new Color(40, 40, 40));

        //버튼테두리제거
        insert_button.setBorder(null);

        //위치설정
        nickname.setBounds(50, 130, 200, 30);
        insert_button.setBounds(105, 180, 100, 30);

        //요소삽입
        add(nickname);
        add(insert_button);

        //크기변경 불가
        setResizable(false);
    }
    public void setActionListener(UICustomActionListener actionListener) {
        insert_button.setActionCommand("insert_nickname");
        insert_button.addActionListener(actionListener);
        nickname.setActionCommand("insert_nickname");
        nickname.addActionListener(actionListener);

    }
    public JTextField getNicknameField() {return nickname;}
    public JButton getInsertButton() {return insert_button;}
}

//채팅메인화면 UI
public class UIMessenger extends JFrame {
    JTextField msg_insert;
    JTextArea msg_display;
    JButton send_button;
    JButton create_room;
    DefaultListModel<String> listModel_room = new DefaultListModel<>();
    JList<String> list_room = new JList<>(listModel_room);
    JScrollPane msgScrollPane = new JScrollPane(msg_display);
    JScrollPane roomScrollPane = new JScrollPane(list_room);
    JLabel room_Label;
    Font font = new Font("netmarble Medium", Font.PLAIN, 15);
    String imagePath = "src\\image\\";


    public UIMessenger() {
        setSize(715, 535);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        //크기고정
        setResizable(false);

        msg_insert = new JTextField();
        msg_display = new JTextArea();
        send_button = new JButton(new ImageIcon(imagePath + "sendB.png"));
        create_room = new JButton(new ImageIcon(imagePath + "create_roomB.png"));
        msgScrollPane = new JScrollPane(msg_display);
        roomScrollPane = new JScrollPane(list_room);
        room_Label = new JLabel("   채팅방 목록");
        //수정불가
        msg_display.setEditable(false);
        //폰트설정
        send_button.setFont(font);
        msg_insert.setFont(font);
        msg_display.setFont(font);
        list_room.setFont(font);
        room_Label.setFont(font);
        //배경색
        msg_display.setBackground(new Color(225, 227, 255));
        list_room.setBackground(new Color(183, 255, 249, 255));
        send_button.setBackground(new Color(183, 183, 183, 255));
        room_Label.setBackground(new Color(255, 183, 183, 255));
        //스크롤
        msgScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        roomScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //위치
        msgScrollPane.setBounds(1, 0, 530, 457);
        room_Label.setBounds(530, 0, 172, 40);
        roomScrollPane.setBounds(530, 40, 172, 417);
        msg_insert.setBounds(1, 457, 445, 40);
        send_button.setBounds(445, 457, 85, 40);
        create_room.setBounds(530, 457, 172, 40);
        //요소삽입
        add(msg_insert);
        add(send_button);
        add(create_room);
        add(msgScrollPane);
        add(roomScrollPane);
        add(room_Label);
    }

    public void setActionListener(UICustomActionListener actionListener) {
        send_button.setActionCommand("send_message");
        send_button.addActionListener(actionListener);
        create_room.setActionCommand("create_room");
        create_room.addActionListener(actionListener);
        msg_insert.setActionCommand("send_message");
        msg_insert.addActionListener(actionListener);
        list_room.addMouseListener(actionListener);
    }
    public JTextField getMsgInsertField() {return msg_insert;}
    public JTextArea getMsgDisplay() {return msg_display;}
    public JButton getSendButton() {return send_button;}
    public JList<String> getroomDisplay() {return list_room;}
    public JButton getCreateRoom() {return create_room;}

    // 메세지를 창에 업데이트
    public void displayMsg(String inMsg) {
        msg_display.append(" " + inMsg + "\n");
        msg_display.setCaretPosition(msg_display.getDocument().getLength());
    }

    // 그룹 목록 List 업데이트
    public void updateRoomList(String[] rooms) {
        listModel_room.clear();
        for (String room : rooms) {
            listModel_room.addElement(room);
        }
    }
    public JList<String> getEnterRoom() {return list_room;}
}