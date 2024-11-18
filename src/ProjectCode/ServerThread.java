package ProjectCode;

import ProjectDBCode.ProjectDAO;
import ProjectDBCode.ServerDataMng;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.StringTokenizer;

public class ServerThread implements Runnable {
    // 선언부
    public ServerMain sm = null;
    ServerDataMng sdm;
    String nickName = null; //서버에 입장한 클러이언트 스레드 닉네임 저장하는 코드

    ProjectDAO pdao;
    Socket clientSocket;
    ObjectOutputStream outStream;
    ObjectInputStream inStream;

    // 생성자 | 서버 소켓
    public ServerThread() {}
    public ServerThread(Socket socket, ServerDataMng sdm, ServerMain sm ,ProjectDAO pdao) {
        this.sm = sm;
        this.clientSocket = socket;
        this.sdm = sdm;
        this.pdao = pdao;
    }

    @Override
    public void run() {
        try {
            // 입출력 스트림 객체 생성
            outStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inStream = new ObjectInputStream(clientSocket.getInputStream());

            String msg = (String) inStream.readObject(); //클라이언트로부터 메시지 수신

            StringTokenizer stz = new StringTokenizer(msg, "#"); //메시지에서 닉네임 추출
            stz.nextToken(); //미정, 사용자 대화 받아오기 프로토콜 부분 스킵
            nickName = stz.nextToken(); //닉네임 가져오기
            String clientIP = clientSocket.getInetAddress().getHostAddress(); // IP주소 가져오기
            sm.jta_log.append(nickName + " 입장\n" + sm.setDays() + "\n"); //입장시 나오는 문구

            System.out.println("입출력 Stream 객체 생성 | " + clientSocket);

            // RoomMap에 클라이언트 추가
            sdm.ClientToRoom(outStream, "NULL");
            sdm.broadcastRoomList();

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

                String[] strArray = msg.split("#", 2);
                String command = strArray[0];
                String content = strArray[1];
                String roomName;
                ServerRoomMsg roomMsg;
                if (strArray.length == 2) {
                    // 프로토콜에 따른 서버 동작 실행
                    switch(command) {
                        case "MsgSend":     /// 메세지 발송
                            sm.jta_log.append(sm.setDays() + "\n");
                            roomName = sdm.getRoomName(outStream);
                            roomMsg = sdm.getRoomMsg(roomName);
                            roomMsg.msgSave(content);
                            roomMsg.broadcastMsg(roomName);
                            // outStream.writeObject("MsgSend#" + content);
                            break;
                        case "Create":      /// 그룹창 생성
                            sm.jta_log.append(sm.setDays() + "\n");
                            sdm.createRoom(content);
                            break;
                        case "Join":
                            sm.jta_log.append(sm.setDays() + "\n");
                            sdm.ClientToRoom(outStream, content);
                            roomMsg = sdm.getRoomMsg(content);
                            roomMsg.addClient(outStream, content);
                            roomName = sdm.getRoomName(outStream);
                            roomMsg.broadcastMsg(roomName);
                            break;
                    }
                }
            }
        }
        catch (IOException | ClassNotFoundException e) {
            System.out.println("입출력 오류 발생 | " + e.getMessage());
        }
    }
}
