package ProjectCode;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.StringTokenizer;

public class ServerThread implements Runnable {
    // 선언부 | Class
    public ServerMain sm = null;
    ServerDataMng sdm;
    Socket clientSocket;
    ObjectOutputStream outStream;
    ObjectInputStream inStream;

    // 선언부
    String nickName;
    String mem_ip;
    String roomName;
    String createMsg;
    String[] strings;
    int loginTF;


    // 생성자 | 서버 소켓
    public ServerThread(Socket socket, ServerDataMng sdm, ServerMain sm) {
        this.sm = sm;
        this.clientSocket = socket;
        this.sdm = sdm;
    }

    @Override
    public void run() {
        try {
            // 입출력 스트림 객체 생성
            outStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inStream = new ObjectInputStream(clientSocket.getInputStream());
            System.out.println("입출력 Stream 객체 생성 | " + clientSocket);

            String msg = (String) inStream.readObject(); //클라이언트로부터 메시지 수신
            String[] msgList = msg.split("#", 2);
            nickName = msgList[1];

//            if (nick != null && nick.contains("#")) {
//                String[] parts = nick.split("#");
//                if (parts.length > 1) {
//                    nickName = parts[1];
//                }
//            }
            // 조건문을 통해서 nick이 없으면 join 후에 입장
            // nick이 있으면 그냥 입장
            // if (로그인 체크 넣어서 비교 있으면 JO어쩌구 해서띄우고)
            // else (체크 해서 crudSQL join넣어서 로그인)
//            if (loginTF == 0) {
//                JOptionPane.showMessageDialog(null,"닉네임이 있습니다. 다시 적어주세요.");
//                outStream.writeObject("Error#닉네임이 중복입니다.");
//
//            }
//            else {
//                sm.jta_log.append(nickName + " 입장\n" + sm.setDays() + "\n"); //입장시 나오는 문구
//                sdm.crudSQL("insert", mem_ip, nickName,null);
//                nick = (String) inStream.readObject(); //클라이언트로부터 메시지 수신
//                StringTokenizer stz = new StringTokenizer(nick, "#"); //메시지에서 닉네임 추출
//                stz.nextToken(); //미정, 사용자 대화 받아오기 프로토콜 부분 스킵
//                nickName = stz.nextToken(); //닉네임 가져오기
//            }
//            if (nickName ==null || nickName.isEmpty()){
//                outStream.writeObject("Error#닉네임이 필요합니다.");
//                return;
//            }

            String fixePassword = "1234";
//            int password = Integer.parseInt(fixePassword);
            loginTF = sdm.loginCheck(nickName, fixePassword, outStream);
            mem_ip = clientSocket.getInetAddress().getHostAddress();

            if (loginTF == 1) {
                sm.jta_log.append(nickName + " 입장\n" + sm.setDays() + "\n");
                //outStream.writeObject("LoginSuccess#환영합니다, " + nickName);
            } else {
                int joinTF = sdm.crudSQL("insert", mem_ip, nickName, fixePassword);
                loginTF = sdm.loginCheck(nickName, fixePassword, outStream);

                if (loginTF != 1) {
                    int updateTF = sdm.crudSQL("update", mem_ip, nickName, fixePassword);
                    loginTF = sdm.loginCheck(nickName, fixePassword, outStream);
                }
                //outStream.writeObject("LoginFail#닉네임 또는 비밀번호가 올바르지 않습니다.");
            }

            System.out.println(sdm.clientInfoMap.size());
            sdm.broadcastRoomList("RoomList#");

//            StringTokenizer stz = new StringTokenizer(nick, "#"); //메시지에서 닉네임 추출
//            stz.nextToken(); //미정, 사용자 대화 받아오기 프로토콜 부분 스킵
//            nickName = stz.nextToken(); //닉네임 가져오기
//
//            sm.jta_log.append(nickName + " 입장\n" + sm.setDays() + "\n"); //입장시 나오는 문구
//
            // 스레드 동작 처리
            while (true) {
                msg = (String) inStream.readObject();
                if (msg == null) break;
                sm.jta_log.append(msg + "\n");
                sm.jta_log.setCaretPosition(sm.jta_log.getDocument().getLength()); //대화 내용 전부 가져오는 코드
                StringTokenizer stz2 = new StringTokenizer(msg, "#");
                int pro = 0; //
                System.out.println("스레드 동작 | " + msg);
//                pdao.insertMem(clientIP, nickName, null); //DB에 닉네임과 IP저장

                // 프로토콜 & 컨텐츠 분리
                String[] strArray = msg.split("#", 2);
                String command = strArray[0];
                String content = strArray[1];

                // 프로토콜에 따른 서버 동작 실행
                switch (command) {
                    case "MsgSend":     /// 메세지 발송
                        sm.jta_log.append(sm.setDays() + "\n");
                        sdm.saveMsg(content, mem_ip, roomName);
                        sdm.broadcastMsg("MsgSend#", roomName);
                        break;
                    case "Create":      /// 그룹창 생성
//                        sm.jta_log.append(sm.setDays() + "\n");
                        createMsg = ">>[" + content + "]에 입장하였습니다.";

                        // 그룹 생성 | 중복이 아니면 1, 중복이면 0 반환
                        if (sdm.createRoom(mem_ip, nickName, content, createMsg) == 0) {
                            outStream.writeObject("MsgGroup#동일한 그룹이 이미 존재합니다.");
                        } else {
                            sdm.broadcastRoomList("RoomList#");
                            sm.jta_log.append(sm.setDays() + "\n");
                        }
                        break;
                    case "Join":       /// 그룹창 입장
                        //strings = content.split("/", 2);  // 그룹명과 회원명으로 분리
                        roomName = content;                       // 현재 입장중인 그룹명
                        sdm.enterRoom(nickName, roomName);        // 그룹에 입장
                        sdm.broadcastMsg("MsgSend#", roomName);
                        break;
//                    case "Join":
//                        sm.jta_log.append(sm.setDays() + "\n");
//                        strings = content.split("/", 2);
//                        mem_ip = clientSocket.getInetAddress().getHostAddress();
//                        if (sdm.crudSQL("insert", mem_ip, strings[0], strings[1]) == 0) {
//                            outStream.writeObject("MsgSQL#가입된 IP주소 입니다!");
//                        } else {
//                            outStream.writeObject("MsgSQL#가입이 완료되었습니다.");
//                        }
//                        break;
//                    case "Update":
//                        strings = content.split("/", 2);
//                        if (sdm.crudSQL("update", mem_ip, strings[0], strings[1]) == 1) {
//                            outStream.writeObject("MsgSQL#닉네임이 변경되었습니다.");
//                        }
//                        break;
//                    case "Delete":
//                        strings = content.split("/", 2);
//                        if (sdm.crudSQL("delete", mem_ip, strings[0], strings[1]) == 1) {
//                            outStream.writeObject("MsgSQL#닉네임이 삭제되었습니다.");
//                        }
//                        break;
//                    case "LoginCheck":
//                        strings = content.split("/", 2);
//                        nickName = strings[0];
//                        mem_ip = sdm.getIP(strings[0]);
//                        loginTF = sdm.loginCheck(nickName, strings[1], outStream);
//                        if(loginTF == 1) {sdm.broadcastRoomList("RoomList#");}
//                        outStream.writeObject("LoginCheck#" + loginTF);
//                        break;
                }
            }
        }

        catch (IOException | ClassNotFoundException e) {
            System.out.println("입출력 오류 발생 | " + e.getMessage());
        }
        catch (NumberFormatException  e) {
            System.out.println("비밀번호 형식 오류 | " + e.getMessage());
            try {
                outStream.writeObject("Error#비밀번호는 숫자여야 합니다.");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        finally {
            try {
                sdm.clientInfoMap.remove(outStream);
                clientSocket.close();
            }
            catch (IOException e) {
                System.out.println("커넥션 종료 중 오류 발생 | " + e.getMessage());
            }
        }
    }
}
