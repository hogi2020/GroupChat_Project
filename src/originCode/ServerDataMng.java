package originCode;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.ConcurrentHashMap;

public class ServerDataMng {
    // 선언부
    ConcurrentHashMap<String, ServerRoomMsg> chatRoomMap;
    ConcurrentHashMap<ObjectOutputStream, String> clientRoomMap;

    // 생성자
    public ServerDataMng() {
        chatRoomMap = new ConcurrentHashMap<>();
        clientRoomMap = new ConcurrentHashMap<>();
    }


    /// /// Map 관련 메서드 집합 /// ///
    // ClientRoomMap에 ClientOutStream 추가
    public void ClientToRoom(ObjectOutputStream clientOutStream, String roomName) {
        clientRoomMap.put(clientOutStream, roomName);
    }


    // RoomList -> 모든 클라이언트에게 전송
    public void broadcastRoomList() {
        String roomList = "RoomList#" + String.join(",", chatRoomMap.keySet());

        for (ObjectOutputStream outStream : clientRoomMap.keySet()) {
            try {
                outStream.writeObject(roomList);
                outStream.flush();
            } catch (IOException e) {
                System.out.println("DataMng-broadcast 에러 발생 | " + e.getMessage());
            }
        }
    }


    // 그룹창 생성 메서드
    public void createRoom(String roomName) {
        if (!chatRoomMap.containsKey(roomName)) {
            chatRoomMap.put(roomName, new ServerRoomMsg(roomName));
            broadcastRoomList();
        }
    }


    // Client RoomName 호출
    public String getRoomName(ObjectOutputStream outStream) {
        return clientRoomMap.get(outStream);
    }

    // RoomMsg 호출
    public ServerRoomMsg getRoomMsg(String roomName) {
        return chatRoomMap.computeIfAbsent(roomName, k -> new ServerRoomMsg(roomName));
    }
}
