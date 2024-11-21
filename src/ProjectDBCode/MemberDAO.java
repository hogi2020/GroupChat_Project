package ProjectDBCode;

import java.util.List;
import java.util.Map;

public interface MemberDAO {
    // Database CRUD 메서드 //
    void insertMem(String mem_ip, String mem_nick, String mem_pw);
    void updateMem(String mem_ip, String update_nick, String update_pw);
    void deleteMem(String mem_nick, String mem_pw);

    // Database Select 메서드 //
    int loginCheck(String nickName, int pw);
    String getMemIP(String nickName);
    Map<String, String> getAllMem();
    List<String> getJoinMemList(String roomName);
}
