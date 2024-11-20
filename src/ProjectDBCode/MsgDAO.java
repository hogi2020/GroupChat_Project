package ProjectDBCode;

import java.util.List;

public interface MsgDAO {
    void insertMsg(String msg, String mem_ip, String roomName);
    List<String> getMsgList(String roomName);
}
