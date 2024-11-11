package ProjectCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CustomActionListener  extends Component implements ActionListener, MouseListener {
    private InsertNickname insertNickname;
    private MessengerUI ui;
    //private Rename rename;
    private String nickname;
    private ClientProtocol op;
    public UIMain uiMain;
    ServerMain sm = new ServerMain();

    public CustomActionListener(InsertNickname insertNickname, MessengerUI ui/*, Rename rename*/, UIMain uiMain, ClientProtocol op) {
        this.insertNickname = insertNickname;
        this.ui = ui;
        this.uiMain = uiMain;
        //this.rename = rename;
        this.op = op;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("insert_nickname")) {
            JTextField nicknameField = insertNickname.getNicknameField();
            nickname = nicknameField.getText();
            if (nickname.length() > 0 && nickname.length() <= 10) {
                insertNickname.dispose();
                op.sendMsg("MsgSend#" + nickname + " " + sm.setDays());
                ui.setTitle(nickname +"님의 대화창");
                ui.setVisible(true);
                ui.msg_insert.requestFocusInWindow();

            } else {
                JOptionPane.showMessageDialog(insertNickname, "닉네임은 1~10자로 입력해주세요.");
            }
        } else if (command.equals("send_message")) {
            JTextField msgInsertField = ui.getMsgInsertField();
            JTextArea msg_display = ui.getMsgDisplay();
            String message = msgInsertField.getText();
            if (!message.trim().isEmpty()) {
                JTextArea msgDisplay = ui.getMsgDisplay();
                op.sendMsg("MsgSend#" + nickname + " : " + message + " " + sm.setDays() + "\n");  // 메세지 보내기
                msgInsertField.setText("");  // 텍스트필드 초기화
                msg_display.setText("");

            }
        } else if (command.equals("create_room")) {
            String roomName = JOptionPane.showInputDialog("그룹명을 입력해주세요!");
            if (roomName != null && !roomName.isEmpty()) {
                System.out.println(roomName);
                op.sendMsg("Create#" + roomName); // 그룹창 생성
            }
        }
        /*} else if (command.equals("rename_nickname")) {
            rename.setVisible(true);
        } else if (command.equals("rename")) {
            JTextField renameField = rename.getRenameField();
            String newNickname = renameField.getText();
            if (newNickname.length() > 0 && newNickname.length() <= 10) {
                String oldNickname = nickname;
                nickname = newNickname;
                ui.setTitle(nickname + "님의 대화창");
                JTextArea roomDisplay = ui.getroomDisplay();
                roomDisplay.setText(roomDisplay.getText().replace(oldNickname, nickname));
                JTextArea msgDisplay = ui.getMsgDisplay();
                msgDisplay.append(oldNickname + "님이 닉네임을 변경했습니다. 새로운 닉네임: " + nickname + "\n");
                rename.setVisible(false);
                ui.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(rename, "닉네임은 1~10자로 입력해주세요.");
            }
        }*/
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getClickCount()==2){
            String roomSelect = ui.list_room.getSelectedValue();
            ui.msg_insert.setText("");
            if (roomSelect != null) {
                ui.msg_display.setText("");
                op.sendMsg("Join#" + roomSelect);
            } else {
                // 동작 시, 확인 불가 알림
                JOptionPane.showMessageDialog(this, "참여 가능한 그룹이 없습니다.");
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
