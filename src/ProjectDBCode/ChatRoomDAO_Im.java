package ProjectDBCode;

import java.sql.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatRoomDAO_Im implements ChatRoomDAO {
    private final DBConnectionMgr dbMgr;
    private Connection conn;
    private PreparedStatement pstm;
    private ResultSet rs;

    public ChatRoomDAO_Im() {
        this.dbMgr = DBConnectionMgr.getInstance();
    }

    @Override
    public int insertRoom(String roomName){
        int num = -1;
        String[] sqls = {
                "select count(*) from talk_room where talk_room_name = ?",
                "insert into talk_room values (talk_room_seq.nextval,?)" };
        try {
            conn = dbMgr.getConnection();
            pstm = conn.prepareStatement(sqls[0]);
            pstm.setString(1, roomName);
            rs = pstm.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                num = 0;
            }
            else {
                pstm.close();
                pstm = conn.prepareStatement(sqls[1]);
                pstm.setString(1, roomName);
                num = pstm.executeUpdate();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();    // 오류를 콘솔에 출력하여 추적
        }
        finally {
            dbMgr.freeConnection(conn, pstm, rs);
        }
        return num;
    }

    @Override
    public void enterRoom(String nickName, String roomName) {
        int num = -1;
        String sql = "insert into room_member values (" +
                "(select mem_ip from member where mem_nick = ?), " +
                "(select talk_room_id from talk_room where talk_room_name = ?))";
        try {
            conn = dbMgr.getConnection();
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, nickName);
            pstm.setString(2, roomName);
            num = pstm.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("EnterRoom Exception : " + e.getMessage());
        }
        finally {
            dbMgr.freeConnection(conn, pstm, rs);
        }
    }

    @Override
    public Map<String, String> getRoomMap() {
        Map<String, String> roomMap = new ConcurrentHashMap<>();
        String sql = "select * from talk_room";

        try {
            conn = dbMgr.getConnection();
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();

            while (rs.next()) {
                roomMap.put(rs.getString("talk_room_id"), rs.getString("talk_room_name"));
            }
        } catch (SQLException e) { e.printStackTrace();
        } finally { dbMgr.freeConnection(conn, pstm, rs);}
        return roomMap;
    }
}
