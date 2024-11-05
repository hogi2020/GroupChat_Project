package sampleProjectCode;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.StringTokenizer;

public class ServerThread implements Runnable {
    // 선언부
    public ServerMain sm = null;
    Socket clientSocket;
    ObjectOutputStream outStream;
    ObjectInputStream inStream;
    ServerDataMng sdm;
    String Nickname = null; //서버에 입장한 클러이언트 스레드 닉네임 저장하는 코드

    // 생성자 | 서버 소켓
    public ServerThread() {}
    public ServerThread(Socket socket, ServerDataMng sdm, ServerMain sm) {
        this.sm = sm;
        this.clientSocket = socket;
        this.sdm = sdm;
    }

    //현재 입장해 있는 친구들 모두에게 메시지 전송하기 구현
    public void broadCasting(String msg) {
        for(ServerThread st:sm.stl) {
            st.send(msg);
        }
    }

    //클라이언트에게 말하기 구현
    public void send(String msg) {
        try {
            outStream.writeObject(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            // 입출력 스트림 객체 생성
            outStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inStream = new ObjectInputStream(clientSocket.getInputStream());
            String msg = (String) inStream.readObject();
            sm.jta_log.append(msg + "\n"); //메시지를 로그에 보이기
            StringTokenizer stz = new StringTokenizer(msg, "#");
            stz.nextToken(); //미정, 사용자 대화 받아오기
            Nickname = stz.nextToken(); //닉네임 받아오기
            sm.jta_log.append(Nickname + " 님이 이리오너라~~ 를 시전했습니다. \n"); //입장시 나오는 문구

            for (ServerThread st:sm.stl){ //
                this.send("수신정보" + "#" + st.Nickname);
            }
            sm.stl.add(this);
            this.broadCasting(msg);
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

                String[] strArray = msg.split("#", 2);
                String command = strArray[0];
                String content = strArray[1];
                String roomName;
                ServerRoomMsg roomMsg;
                if (strArray.length == 2) {
                    // 프로토콜에 따른 서버 동작 실행
                    switch(command) {
                        case "MsgSend":     /// 메세지 발송
                            roomName = sdm.getRoomName(outStream);
                            roomMsg = sdm.getRoomMsg(roomName);
                            roomMsg.msgSave(content);
                            roomMsg.broadcastMsg(roomName);
                            // outStream.writeObject("MsgSend#" + content);
                            break;
                        case "Create":      /// 그룹창 생성
                            sdm.createRoom(content);
                            break;
                        case "Join":
                            sdm.ClientToRoom(outStream, content);
                            roomMsg = sdm.getRoomMsg(content);
                            roomMsg.addClient(outStream, content);

                            roomName = sdm.getRoomName(outStream);
                            roomMsg.broadcastMsg(roomName);
                            break;
                    }
                }
                for (pro = 200; pro <= 400; pro++){
                    switch (pro){
                        case 200:{

                        }break;
                        case 201:
                            if (stz2.hasMoreTokens()) {
                                String nickName = stz2.nextToken();
                                String message = stz2.hasMoreTokens() ? stz2.nextToken() : "";
                                broadCasting(pro + " # " + nickName + " # " + message);
                            }
                            break;
                        case 202:
                            if (stz2.hasMoreTokens()) {
                                String nickName = stz2.nextToken();
                                String afterName = stz2.hasMoreTokens() ? stz2.nextToken() : "";
                                String message = stz2.hasMoreTokens() ? stz2.nextToken() : "";
                                this.Nickname = afterName;
                                broadCasting(pro + " # " + nickName + " # " + afterName + " # " + message);
                            }
                            break;
                        default:
                            System.out.println("방 인원수가 최대치 입니다.");
                            break;
//                        case 201:{
//                            String nickName = stz2.nextToken();
//                            String message = stz2.nextToken();
//                            broadCasting(201
//                                    +"#"+nickName
//                                    +"#"+message);
//                        }break;
//                        case 202:{
//                            String nickName = stz2.nextToken();
//                            String afterName = stz2.nextToken();
//                            String message = stz2.nextToken();
//                            this.Nickname = afterName;
//                            broadCasting(pro +"#" + nickName + "#"+afterName + "#"+message);
//                        } break;
//                        case 500:{
//                            String nickName = stz2.nextToken();
//                            sm.stl.remove(this);
//                            broadCasting(500 + "#" + nickName);
//                            break;
//                        }
//                        default:
//                            System.out.println("방 인원수가 최대치 입니다.");
//                            break;
                    }
                }
                if (stz2.hasMoreTokens()){
                    String nickName = stz2.nextToken();
                    sm.stl.remove(this);
                    broadCasting(500 + "#" + nickName);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("입출력 오류 발생 | " + e.getMessage());
        }
    }
}
