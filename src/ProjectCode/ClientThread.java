package ProjectCode;

import java.util.StringTokenizer;
import java.util.Vector;

public class ClientThread extends Thread{
    ClientProtocol cp = null;
    ClientUI ui = null;
    ServerThread svr = null;
    public ClientThread (ClientProtocol tc) {
        this.cp = cp;
    }
    public void run() {
        boolean isStop = false;
        while(!isStop) {
            try {
                String msg = "";//100#apple
                msg = (String)cp.in.readObject();
                StringTokenizer st = null;
                int protocol = 0;//100|200|201|202|500
                if(msg !=null) {
                    st = new StringTokenizer(msg,"#");
                    protocol = Integer.parseInt(st.nextToken());//100
                }
                switch(protocol) {
                    case 100:{//100#apple
                        String Nickname = st.nextToken();
                        ui.txt_area.append(Nickname+"님이 입장하였습니다.\n");
                        Vector<String> v = new Vector<>();
                        v.add(Nickname);
                        cp.dtm.addRow(v);
                    }break;
                    case 200:{

                    }break;
                    case 201:{
                        String Nickname = st.nextToken();
                        String message = st.nextToken();
                        ui.txt_area.append("["+Nickname+"]"+message+"\n");
                        ui.txt_area.setCaretPosition
                                (ui.txt_area.getDocument().getLength());
                    }break;
                    case 202:{
                        String Nickname = st.nextToken();
                        String afterName = st.nextToken();
                        String message = st.nextToken();
                        //테이블에 대화명 변경하기
                        for(int i=0;i<cp.dtm.getRowCount();i++) {
                            String imsi = (String)cp.dtm.getValueAt(i, 0);
                            if(Nickname.equals(imsi)) {
                                cp.dtm.setValueAt(afterName, i, 0);
                                break;
                            }
                        }
                        //채팅창에 타이틀바에도 대화명을 변경처리 한다.
                        if(Nickname.equals(svr.Nickname)) {
                            ui.setTitle(afterName+"님의 대화창");
                            svr.Nickname = afterName;
                        }
                        ui.txt_area.append(message+"\n");
                    }break;
                    case 500:{
                        String nickName = st.nextToken();
                        ui.txt_area.append(nickName+"님이 퇴장 하였습니다.\n");
                        ui.txt_area.setCaretPosition
                                (ui.txt_area.getDocument().getLength());
                        //입장하면 대화명 테이블에 등록됨 - 퇴장하므로 삭제하기
                        for(int i=0;i<cp.dtm.getRowCount();i++) { //defaultTableModel
                            String n =(String)cp.dtm.getValueAt(i, 0);
                            if(n.equals(nickName)) {
                                cp.dtm.removeRow(i);
                            }
                        }
                    }break;
                }////////////end of switch
            } catch (Exception e) {
                // TODO: handle exception
            }
        }////////////////////end of while
    }////////////////////////end of run
}