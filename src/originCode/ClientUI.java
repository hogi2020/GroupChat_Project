package originCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientUI extends JFrame implements ActionListener {
    // 프로토콜 객체 선언
    ClientProtocol op = null;

    // Display 레이아웃 구성
    JPanel pnl_main = new JPanel(new BorderLayout());
    JPanel pnl_chat = new JPanel(new BorderLayout());
    JPanel pnl_room = new JPanel(new BorderLayout());
    DefaultListModel<String> listModel_room = new DefaultListModel<>();
    JList<String> list_room = new JList<>(listModel_room);
    JScrollPane scroll_list = new JScrollPane(list_room);

    JTextField txt_field = new JTextField();
    JTextArea txt_area = new JTextArea();
    JScrollPane scroll_area = new JScrollPane(txt_area);

    JPanel pnl_btn = new JPanel();
    JButton btn_create = new JButton("Add_Group+");
    JButton btn_join = new JButton("Join");


    // 생성자 생성
    public ClientUI() {
        inDisplay();
        op = new ClientProtocol(this);
    }


    // 디스플레이 메소드
    public void inDisplay() {
        this.setTitle("OhChat!");
        this.setSize(550, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 채팅창 패널
        txt_area.setEditable(false);    // 사용자가 txt를 수정할 수 없도록 제한 (읽기전용)
        pnl_chat.add("Center", scroll_area);
        pnl_chat.add("South", txt_field);
        txt_field.addActionListener(this);

        // 채팅창 목록 패널
        pnl_btn.add("Center", btn_create);
        pnl_btn.add("Center", btn_join);
        pnl_room.add("South", pnl_btn);
        pnl_room.add("Center", scroll_list);
        btn_create.addActionListener(this);
        btn_join.addActionListener(this);

        // 메인창 패널
        pnl_main.add("East", pnl_room);
        pnl_main.add("Center", pnl_chat);
        this.add(pnl_main);

        txt_field.requestFocusInWindow();
        this.setVisible(true);
    }


    // 동작 구현
    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        String outMsg = txt_field.getText();

        // 동작 시, 메세지 전송 (공란이 아닐 경우)
        if (!outMsg.isEmpty()) {
            op.sendMsg("MsgSend#" + outMsg);  // 메세지 보내기
            txt_field.setText("");            // 텍스트필드 초기화
            txt_area.setText("");             // 텍스트Area 초기화

            // 동작 시, 그룹창을 생성합니다.
        } else if (obj == btn_create) {
            String roomName = JOptionPane.showInputDialog(this, "그룹명을 입력해주세요!");
            if (roomName != null && !roomName.isEmpty()) {
                op.sendMsg("Create#" + roomName);    // 그룹창 생성
            }

            // 동작 시, 선택한 그룹창에 입장합니다.
        } else if (obj == btn_join) {
            String roomSelect = list_room.getSelectedValue();
            txt_area.setText("");
            if (roomSelect != null) {
                op.sendMsg("Join#" + roomSelect);
            } else {
                // 동작 시, 확인 불가 알림
                JOptionPane.showMessageDialog(this, "참여 가능한 그룹이 없습니다.");
            }
        }
        txt_field.setFocusable(true);   // 텍스트필드에 포커스
    }


    // 메세지를 창에 업데이트
    public void displayMsg(String inMsg) {
        txt_area.append(" " + inMsg + "\n");
    }

    // 그룹 목록 List 업데이트
    public void updateRoomList(String[] rooms) {
        listModel_room.clear();
        for (String room : rooms) {
            listModel_room.addElement(room);
        }
    }

    // 메인 메소드
    public static void main(String[] args) {
        new ClientUI();
    }
}