package sampleProjectCode;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class MsgSave extends JFrame {
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JList<String> roomList;

    private sampleProjectCode.TalkDao talkDao = new sampleProjectCode.TalkDao();
    private String nick_ip = "user1"; // 예시 사용자 IP

    public MsgSave() {
        // GUI 초기화
        frame = new JFrame("메신저");
        chatArea = new JTextArea();
        messageField = new JTextField(30);
        sendButton = new JButton("전송");
        roomList = new JList<>(new String[]{"room1", "room2"}); // 예시 대화방 목록

        JPanel panel = new JPanel();
        panel.add(messageField);
        panel.add(sendButton);

        frame.setLayout(new java.awt.BorderLayout());
        frame.add(new JScrollPane(chatArea), java.awt.BorderLayout.CENTER);
        frame.add(panel, java.awt.BorderLayout.SOUTH);
        frame.add(new JScrollPane(roomList), java.awt.BorderLayout.EAST);

        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // 이전 메시지 불러오기
        loadMessages();

        // 전송 버튼 이벤트 처리
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // 엔터 키로 메시지 전송
        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
    }

    // 프로그램 시작 시 이전 메시지를 데이터베이스에서 불러오는 메서드
    private void loadMessages() {
        String talk_room_id = roomList.getSelectedValue();
        if (talk_room_id == null) return;

        // talkDao를 통해 메시지 불러오기
        List<String> messages = talkDao.getMessages(talk_room_id);
        chatArea.setText("");
        for (String message : messages) {
            chatArea.append(message + "\n");
        }
    }

    // 메시지 전송 및 데이터베이스 저장
    private void sendMessage() {
        String messageText = messageField.getText();
        String talk_room_id = roomList.getSelectedValue();

        if (messageText.isEmpty() || talk_room_id == null) return;

        // 메시지 전송
        int result = talkDao.sendMessage(nick_ip, talk_room_id, messageText);
        if (result > 0) {
            chatArea.append("You: " + messageText + "\n");
            messageField.setText("");
        }
    }

    public static void main(String[] args) {
        MsgSave msgSave = new MsgSave();
    }
}
