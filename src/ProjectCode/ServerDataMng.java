package ProjectCode;

import ProjectDBCode.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerDataMng {
    // 선언부
    MemberDAO_Im memDaoIm;
    MessageDAO_Im msgDaoIm;
    ChatRoomDAO_Im chatRoomDaoIm;
    ConcurrentHashMap<String, String> clientRoomMap;    // nickName, roomName
    ConcurrentHashMap<String, List<ObjectOutputStream>> roomMsgMap;    // RoomName, ClientList
    ConcurrentHashMap<String, ObjectOutputStream> clientInfoMap;    // nickName, OutputStream

    // 생성자
    public ServerDataMng() {
        memDaoIm = new MemberDAO_Im();
        msgDaoIm = new MessageDAO_Im();
        chatRoomDaoIm = new ChatRoomDAO_Im();

        clientRoomMap = new ConcurrentHashMap<>();
        roomMsgMap = new ConcurrentHashMap<>();
        clientInfoMap = new ConcurrentHashMap<>();
    }/////////////// ServerDataMng

    /// /// Map 관련 메서드 집합 /// ///

    public int loginCheck(String nickName, String pw, ObjectOutputStream outStream) {
        // 로그인 성공 시 1 반환, 실패 시 0 반환
        int password = Integer.parseInt(pw);
        if(memDaoIm.loginCheck(nickName, password) == 1) {
            // 로그인에 성공하면 ClientList에 Outstream 추가
            clientInfoMap.put(nickName, outStream);
            return 1;
        } else {
            return 0;
        }
    }////////////////// loginCheck

    public String getIP(String nickName) {
        return memDaoIm.getMemIP(nickName);
    }////////////////// getIP

    // 그룹창 생성 메서드
    public int createRoom(String mem_ip, String nickName, String roomName, String createMsg) {
        // 그룹 생성 | 중복이 아니면 1, 중복이면 0 반환
        if (chatRoomDaoIm.insertRoom(roomName) == 1) {
            roomMsgMap.put(roomName, new CopyOnWriteArrayList<>());
            chatRoomDaoIm.enterRoom(nickName, roomName);
            msgDaoIm.insertMsg(createMsg, mem_ip, roomName);
            return 1;
        } else {
            return 0;
        }
    }/// //////////////// createRoom

    // RoomList -> 모든 클라이언트에게 전송
    public void broadcastRoomList(String protocolName) {
        Collection<String> roomList = chatRoomDaoIm.getRoomMap().values();
        String roomListStr = String.join(",", roomList);
//        String roomList = "RoomList#" + String.join(",", chatRoomMap.keySet());

        try {
            for (String key : clientInfoMap.keySet()) {
                clientInfoMap.get(key).writeObject(protocolName + roomListStr);
                clientInfoMap.get(key).flush();
            }
        } catch (IOException e) {
            System.out.println("broadcastRoomList 에러 발생 | " + e.getMessage());

        }
    }//////////////// broadcastRoomList

    public void enterRoom(String nickName, String roomName) {
        clientRoomMap.put(nickName, roomName);
        chatRoomDaoIm.enterRoom(nickName, roomName);
    }/////////////////// enterRoom

    public void saveMsg(String msg, String mem_ip, String roomName) {
        msgDaoIm.insertMsg(msg, mem_ip, roomName); // 메세지 저장
    }/////////////////// saveMsg

    public void broadcastMsg(String protocolName, String roomName) {
        List<String> joinMemList = memDaoIm.getJoinMemList(roomName);  // 그룹에 입장한 회원리스트
        List<String> msgList = msgDaoIm.getMsgList(roomName);          // 그룹에 저장된 메세지리스트

        for (String nick : joinMemList) {
            if (clientInfoMap.containsKey(nick) && clientRoomMap.get(nick).equals(roomName)) {
                System.out.println("msg Nick : " + nick);
                try {
                    clientInfoMap.get(nick).writeObject("Reset#");
                    for (String msg : msgList) {
                        clientInfoMap.get(nick).writeObject(protocolName + msg);
                    }
                } catch (IOException e) {
                    System.out.println("broadcastMsg 에러 발생 | " + e.getMessage());
                }
            }
        }
    }

    public int crudSQL(String command, String mem_ip, String nickName, String pw) {
        switch (command) {
            case "insert" -> {
                memDaoIm.insertMem(mem_ip, nickName, pw);
                return 1;
            }
            case "delete" -> {
                memDaoIm.deleteMem(nickName, pw);
                return 1;
            }
            case "update" -> {
                memDaoIm.updateMem(mem_ip, nickName, pw);
                return 1;
            }
            default -> {
                return 0;
            }
        }
    }//////////////////// crudSQL
}
