package ProjectCode;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerRoomMsg {
    // 선언부
    private String roomName;
    CopyOnWriteArrayList<ObjectOutputStream> joinClients;  // 방의 클라이언트 리스트
    CopyOnWriteArrayList<String> msgArray;                 // 방의 메세지 리스트


    // 생성자
    public ServerRoomMsg (String roomName) {
        this.roomName = roomName;
        this.joinClients = new CopyOnWriteArrayList<>();
        this.msgArray = new CopyOnWriteArrayList<>();
    }


    // Join 클라이언트를 리스트에 추가
    public void addClient(ObjectOutputStream outStream, String content) {
        joinClients.addIfAbsent(outStream);
    }

    // Join 클라이언트 삭제
    public void removeClient(ObjectOutputStream outStream) {
        joinClients.remove(outStream);
    }

    // msgArray에 저장
    public void msgSave(String msg) {
        msgArray.add(msg);
    }


    // join 클라이언트들에게 메세지 전송
    public void broadcastMsg(String roomName) {
        // 채팅방 입장 메세지 전송
        for (ObjectOutputStream outStream : joinClients) {
            try {
                outStream.writeObject("MsgSend#>>["+ roomName +"]에 입장하였습니다." + " ");
            } catch (IOException e) {
                System.out.println("roomNameMsg 오류 | " + e.getMessage());
            }

            // 저장된 채팅방 메세지 전송
            for (String msg : msgArray) {
                try {
                    outStream.writeObject("MsgSend#" + msg);
                } catch (IOException e) {
                    System.out.println("RoomMsg 오류 | " + e.getMessage());
                }
            }
        }
    }
}
