package ProjectCode;

public class UIMain {
    ClientProtocol op = new ClientProtocol(this);
    InsertNickname insertNickname = new InsertNickname();
    MessengerUI ui = new MessengerUI();
    CustomActionListener actionListener = new CustomActionListener(insertNickname, ui, this, op);
    //Rename rename = new Rename();
    public static void main(String[] args) {
        UIMain uiMain = new UIMain();

        // Add action listeners
        uiMain.insertNickname.getInsertButton().setActionCommand("insert_nickname");
        uiMain.insertNickname.getInsertButton().addActionListener(uiMain.actionListener);
        uiMain.insertNickname.getNicknameField().setActionCommand("insert_nickname");
        uiMain.insertNickname.getNicknameField().addActionListener(uiMain.actionListener);

        uiMain.ui.getSendButton().setActionCommand("send_message");
        uiMain.ui.getSendButton().addActionListener(uiMain.actionListener);
        uiMain.ui.getMsgInsertField().setActionCommand("send_message");
        uiMain.ui.getMsgInsertField().addActionListener(uiMain.actionListener);
        uiMain.ui.getEnterRoom().addMouseListener(uiMain.actionListener);
        uiMain.ui.getCreateRoom().setActionCommand("create_room");
        uiMain.ui.getCreateRoom().addActionListener(uiMain.actionListener);
        //ui.getRenameButton().setActionCommand("rename_nickname");
        //ui.getRenameButton().addActionListener(actionListener);
        /*
        rename.getRenameButton().setActionCommand("rename");
        rename.getRenameButton().addActionListener(actionListener);
        rename.getRenameField().setActionCommand("rename");
        rename.getRenameField().addActionListener(actionListener);
        */

        uiMain.insertNickname.setVisible(true);
        uiMain.ui.setVisible(false);
        //rename.setVisible(false);
    }
}