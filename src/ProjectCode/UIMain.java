package ProjectCode;

public class UIMain {
    ClientProtocol op = new ClientProtocol(this);
    InsertNickname insertNickname = new InsertNickname();
    UIMessenger ui = new UIMessenger();
    UICustomActionListener actionListener = new UICustomActionListener(insertNickname, ui, this, op);

    private void setupActionListeners() { //액션리스너 중계 - 꼬이지 않게 메인경유
        insertNickname.setActionListener(actionListener);
        ui.setActionListener(actionListener);
    }

    public static void main(String[] args) {
        UIMain uiMain = new UIMain();
        uiMain.setupActionListeners();
        //입장화면 띄움
        uiMain.insertNickname.setVisible(true);
    }
}