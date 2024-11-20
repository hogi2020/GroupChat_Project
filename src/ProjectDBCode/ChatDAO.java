package ProjectDBCode;

import java.util.Map;

public interface ChatDAO {
    int insertRoom(String roomName);

    void enterRoom(String nickName, String roomName);

    Map<String, String> getRoomMap();

}