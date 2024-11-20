package ProjectCode;


import ProjectDBCode.ServerDataMng;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.StringTokenizer;

public class ServerThread implements Runnable {
    // 선언부
    String nickName;
    String mem_ip;
    String roomName;
    String createMsg;
    String[] strings;

    // 선언부 | Class
    public ServerMain sm = null;
    ServerDataMng sdm;
    Socket clientSocket;
    ObjectOutputStream outStream;
    ObjectInputStream inStream;

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
            sm.jta_log.append(nickName + " 입장\n" + sm.setDays() + "\n"); //입장시 나오는 문구

            StringTokenizer stz = new StringTokenizer(msg, "#"); //메시지에서 닉네임 추출
            stz.nextToken(); //미정, 사용자 대화 받아오기 프로토콜 부분 스킵
            nickName = stz.nextToken(); //닉네임 가져오기


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
                    case "Enter":       /// 그룹창 입장
                        strings = content.split("/", 2);  // 그룹명과 회원명으로 분리
                        roomName = strings[0];                       // 현재 입장중인 그룹명
                        sdm.enterRoom(roomName, strings[1]);         // 그룹에 입장
                        sdm.broadcastMsg("MsgSend#", roomName);
                        break;
                    case "Join":
                        sm.jta_log.append(sm.setDays() + "\n");
                        strings = content.split("/", 2);
                        mem_ip = clientSocket.getInetAddress().getHostAddress();
                        if (sdm.crudSQL("insert", mem_ip, strings[0]) == 0) {
                            outStream.writeObject("MsgSQL#가입된 IP주소 입니다!");
                        } else {
                            outStream.writeObject("MsgSQL#가입이 완료되었습니다.");
                        }
                        break;
                    case "Update":
                        strings = content.split("/", 2);
                        if (sdm.crudSQL("update", mem_ip, strings[0]) == 1) {
                            outStream.writeObject("MsgSQL#닉네임이 변경되었습니다.");
                        }
                        break;
                    case "Delete":
                        strings = content.split("/", 2);
                        if (sdm.crudSQL("delete", mem_ip, strings[0]) == 1) {
                            outStream.writeObject("MsgSQL#닉네임이 삭제되었습니다.");
                        }
                        break;
                }
            }
        }
        catch (IOException | ClassNotFoundException e) {
            System.out.println("입출력 오류 발생 | " + e.getMessage());
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
