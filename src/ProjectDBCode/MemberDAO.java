package ProjectDBCode;

import javax.swing.*;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class MemberDAO implements MemDAO {
    private final DBConnectionMgr dbMgr;
    private Connection conn;
    private PreparedStatement pstm;
    private CallableStatement cstmt;
    private ResultSet rs;

    //생성자
    public MemberDAO() {
        this.dbMgr = DBConnectionMgr.getInstance();
    }

    @Override
    public void insertMem(String mem_ip, String mem_nick, String pw) {
        String sql = "insert into member values (?,?,?,?)";

        try{
            conn = dbMgr.getConnection();
            pstm = conn.prepareStatement(sql);

            pstm.setString(1, mem_ip);
            pstm.setString(2, mem_nick);
            pstm.setString(3, null);
            pstm.setString(4, pw);
            pstm.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("insertMem Exception : " + e.getMessage());
        }
        finally {
            dbMgr.freeConnection(conn, pstm);
        }
    }

    @Override
    public void updateMem(String mem_ip, String update_nick) {
        String sql = "update member set mem_nick_change = mem_nick, mem_nick = ? where mem_ip = ?";

        try{
            conn = dbMgr.getConnection();
            pstm = conn.prepareStatement(sql);

            pstm.setString(1, update_nick);
            pstm.setString(2, mem_ip);
            pstm.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("updateMem Exception : " + e.getMessage());
        }
        finally {
            dbMgr.freeConnection(conn, pstm);
        }
    }

    @Override
    public void deleteMem(String mem_nick, String mem_ip) {
        String sql = "delete from member where mem_nick = ? and mem_ip = ?";

        try {
            conn = dbMgr.getConnection();
            pstm = conn.prepareStatement(sql);

            pstm.setString(1, mem_nick);
            pstm.setString(2, mem_ip);
            pstm.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("deleteMem Exception : " + e.getMessage());
        }
        finally {
            dbMgr.freeConnection(conn, pstm);
        }
    }

    @Override
    public String getMemIP(String nickName) {
        String sql = "select mem_ip from member where mem_nick = ?";
        String mem_ip = null;

        try {
            conn = dbMgr.getConnection();
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, nickName);
            rs = pstm.executeQuery();
            while (rs.next()) {
                mem_ip = rs.getString("mem_ip");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            dbMgr.freeConnection(conn, pstm, rs);
        }
        return mem_ip;
    }

    @Override
    public Map<String, String> getAllMem() {
        Map<String, String> memMap = new ConcurrentHashMap<>();
        String sql = "select * from member";

        try{
            conn = dbMgr.getConnection();
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();

            while (rs.next()) {
                memMap.put(rs.getString("mem_ip"), rs.getString("mem_nick"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            dbMgr.freeConnection(conn, pstm, rs);
        }

        return memMap;
    }

    @Override
    public List<String> getJoinMemList(String roomName) {
        List<String> joinMemList = new CopyOnWriteArrayList<>();
        String sql = "select mem_nick from room_member rm " +
                "join talk_room tr on rm.talk_room_id = tr.talk_room_id " +
                "join member mem on rm.mem_ip = mem.mem_ip " +
                "where tr.talk_room_name = ?";
        try{
            conn = dbMgr.getConnection();
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, roomName);
            rs = pstm.executeQuery();

            while (rs.next()) {
                joinMemList.add(rs.getString("mem_nick"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            dbMgr.freeConnection(conn, pstm, rs);
        }
        return joinMemList;
    }
}
