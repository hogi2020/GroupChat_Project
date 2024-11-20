package ProjectDBCode;

import java.util.List;
import java.util.Map;

public interface MemDAO {
    // Database CRUD 메서드 //
    void insertMem(String mem_ip, String mem_nick, String msg);
    void updateMem(String mem_ip, String update_nick);
    void deleteMem(String mem_nick, String mem_ip);

    // Database Select 메서드 //
    String getMemIP(String nickName);
    Map<String, String> getAllMem();
    List<String> getJoinMemList(String roomName);
}
