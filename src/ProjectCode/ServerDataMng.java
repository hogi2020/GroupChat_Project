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
    ChatRoomDAO chatRoomDao;
    MemberDAO memDao;
    MessageDAO msgDao;
    ConcurrentHashMap<String, List<ObjectOutputStream>> roomMsgMap;    // RoomName, ClientList
    ConcurrentHashMap<String, ObjectOutputStream> clientInfoMap;    // nickName, OutputStream

    // 생성자
    public ServerDataMng() {
        chatRoomDao = new ChatRoomDAO();
        memDao = new MemberDAO();
        msgDao = new MessageDAO();

        roomMsgMap = new ConcurrentHashMap<>();
        clientInfoMap = new ConcurrentHashMap<>();
    }

    /// /// Map 관련 메서드 집합 /// ///
    public String getIP(String nickName) {
        return memDao.getMemIP(nickName);
    }////////////////// getIP

    // 그룹창 생성 메서드
    public int createRoom(String mem_ip, String nickName, String roomName, String createMsg) {
        // 그룹 생성 | 중복이 아니면 1, 중복이면 0 반환
        if (chatRoomDao.insertRoom(roomName) == 1) {
            roomMsgMap.put(roomName, new CopyOnWriteArrayList<>());
            chatRoomDao.enterRoom(nickName, roomName);
            msgDao.insertMsg(createMsg, mem_ip, roomName);
            return 1;
        } else {
            return 0;
        }
    }/// //////////////// createRoom

    // RoomList -> 모든 클라이언트에게 전송
    public void broadcastRoomList(String protocolName) {
        Collection<String> roomList = chatRoomDao.getRoomMap().values();
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
    }

    public void broadcastMsg(String protocolName, String roomName) {
        List<String> joinMemList = memDao.getJoinMemList(roomName);  // 그룹에 입장한 회원리스트
        List<String> msgList = msgDao.getMsgList(roomName);          // 그룹에 저장된 메세지리스트

        for (String nick : joinMemList) {
            if (clientInfoMap.containsKey(nick)) {
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

    public void enterRoom(String nickName, String roomName) {
        chatRoomDao.enterRoom(nickName, roomName);
    }/////////////////// enterRoom


    public void saveMsg(String msg, String mem_ip, String roomName) {
        memDao.insertMem(msg, mem_ip, roomName); // 메세지 저장
    }/////////////////// saveMsg

    public int crudSQL(String command, String mem_ip, String nickName) {
        switch (command) {
            case "insert" -> {
                memDao.insertMem(mem_ip, nickName,null);
                return 1;
            }
            case "delete" -> {
                memDao.deleteMem(nickName, mem_ip);
                return 1;
            }
            case "update" -> {
                memDao.updateMem(mem_ip, nickName);
                return 1;
            }
            default -> {
                return 0;
            }
        }
    }//////////////////// crudSQL
}
