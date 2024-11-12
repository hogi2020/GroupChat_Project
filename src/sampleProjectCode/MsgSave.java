package sampleProjectCode;

import messanger.TalkDao;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class MsgSave extends JFrame {
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JList<String> roomList;

    private TalkDao talkDao; // TalkDao 객체 선언

    public MsgSave() {
        // TalkDao 객체 생성
        talkDao = new TalkDao();

        // GUI 초기화
        frame = new JFrame("메신저");
        chatArea = new JTextArea();
        messageField = new JTextField(30);
        sendButton = new JButton("전송");
        roomList = new JList<>(new String[]{"NULL", "dsf"});

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
        String chatRoom = roomList.getSelectedValue();
        if (chatRoom == null) return;

        // TalkDao를 통해 메시지 불러오기
        List<String> messages = talkDao.getMessages(chatRoom);
        for (String message : messages) {
            chatArea.append(message + "\n");
        }
    }

    // 메시지 전송 및 데이터베이스 저장
    private void sendMessage() {
        String messageText = messageField.getText();
        String userName = "dfas"; // 예시로 사용자가 'dfas'라고 가정
        String chatRoom = roomList.getSelectedValue();
        Timestamp timestamp = new Timestamp(new Date().getTime());

        if (messageText.isEmpty() || chatRoom == null) return;

        // 채팅창에 메시지 표시
        chatArea.append(userName + " : " + messageText + " " + timestamp + "\n");
        messageField.setText("");

        // TalkDao를 통해 메시지 전송
        talkDao.sendMessage(userName, chatRoom, messageText);
    }

    public static void main(String[] args) {
        MsgSave msgSave = new MsgSave();

        // 윈도우 닫기 이벤트 리스너 추가하여 종료 시 데이터베이스 연결 닫기
        msgSave.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
