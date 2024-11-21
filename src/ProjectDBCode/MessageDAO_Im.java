package ProjectDBCode;

import java.sql.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MessageDAO_Im implements MessageDAO {
    private DBConnectionMgr dbMgr;
    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;

    public MessageDAO_Im() {
        this.dbMgr = DBConnectionMgr.getInstance();
    }

    @Override
    public void insertMsg(String msg, String mem_ip, String roomName) {
        int tfNum = -1;
        String sql = "insert into message values (seq_msg_no.nextval, ?, to_char(SYSDATE, 'yyyy-mm-dd hh24:mi'), " +
                "?, (select talk_room_id from talk_room where talk_room_name = ?))";
        try {
            conn = dbMgr.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, msg); //msg
            pstmt.setString(2, mem_ip); //mem_ip
            pstmt.setString(3, roomName); //talk_room_id
            tfNum = pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("동작 중 오류 발생 | " + e.getMessage());
        }
        finally {
            dbMgr.freeConnection(conn, pstmt);
        }
    }

    @Override
    public List<String> getMsgList(String roomName) {
        List<String> msgList = new CopyOnWriteArrayList<>();

        String sql = "select /*+ INDEX(mg idx_message_date_time_msg_id) */ mg.msg " +
                "from message mg " +
                "join talk_room tr on mg.talk_room_id = tr.talk_room_id " +
                "where tr.talk_room_name = ? " +
                "ORDER BY mg.date_time, mg.msg_id";
        try {
            conn = dbMgr.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, roomName);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                msgList.add(rs.getString("msg"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            dbMgr.freeConnection(conn, pstmt, rs);
        }

        return msgList;
    }
}

