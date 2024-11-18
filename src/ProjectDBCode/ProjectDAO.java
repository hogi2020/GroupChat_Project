package ProjectDBCode;

import java.sql.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ProjectDAO {
    DBConnectionMgr dbMgr = DBConnectionMgr.getInstance();
    Connection conn;
    PreparedStatement pstmt;
    CallableStatement cstmt;
    ResultSet rs;
    String sql;
    String mem_ip;
    int tfNum = -1;     // 성공은 1, 실패는 0


    // Database 데이터 입력 메서드
    public void insertMem(String mem_ip, String mem_nick, String mem_pw) {
        mem_pw = "123";
        sql = "insert into member values (?,?,?,?)";

        try {
            conn = dbMgr.getConnection();
            pstmt = conn.prepareStatement(sql);

            // DB 내 데이터 저장
            pstmt.setString(1, mem_ip);
            pstmt.setString(2, mem_nick);
            pstmt.setString(3, null);
            pstmt.setString(4, mem_pw);
            tfNum = pstmt.executeUpdate();

        } catch (SQLException e) {
            this.tfNum = -1;
        } finally {
            dbMgr.freeConnection(conn, pstmt);
        } // 자원 명시적 종료
    }


    // Database 데이터 삭제 메서드
    public void deleteMem(String mem_nick, String mem_pw) {
        mem_pw = "123";
        sql = "delete from member where mem_nick = ? and mem_pw = ?";

        try {
            conn = dbMgr.getConnection();
            pstmt = conn.prepareStatement(sql);

            // DB 내 데이터 삭제
            pstmt.setString(1, mem_nick);
            pstmt.setString(2, mem_pw);
            tfNum = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbMgr.freeConnection(conn, pstmt);
        } // 자원 명시적 종료
    }


    // UPDATE [테이블] SET [열] = '변경할값' WHERE [조건]
    // Database 데이터 수정 메서드
    public void updateMem(String mem_ip, String update_nick, String update_pw) {
        update_pw = "123";
        sql = "update member set mem_nick_change = mem_nick, mem_nick = ? where mem_ip = ? and mem_pw = ?";

        try {
            conn = dbMgr.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, update_nick);
            pstmt.setString(2, mem_ip);
            pstmt.setString(3, update_pw);
            tfNum = pstmt.executeUpdate();

        } catch (SQLException e) {
            this.tfNum = -1;
        } finally {
            dbMgr.freeConnection(conn, pstmt);
        } // 자원 명시적 종료
    }


    // 결과 출력
    public int result() {
        return tfNum;
    }


    // 로그인 정보 확인 메서드
    public int loginCheck(String nickName, int pw) {
        pw = 123;
        try {
            // 1. 데이터베이스 연결
            conn = dbMgr.getConnection();

            // 2. CallableStatement 객체 생성 (프로시저 호출)
            cstmt = conn.prepareCall("call CHAT_JOIN(?,?,?)");

            // 3. 프로시저의 파라미터 설정
            cstmt.setString(1, nickName);
            cstmt.setInt(2, pw);
            cstmt.registerOutParameter(3, Types.INTEGER);

            // 4. 프로시저 실행 & 프로시저 결과 받기
            cstmt.execute();
            tfNum = cstmt.getInt(3);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbMgr.freeConnection(conn, cstmt);
        }
        return tfNum;
    }


    // 그룹창 이름 저장 메서드
    public int insertGroup(String group_name) {
        String[] sqls = {
                "select count(*) from talk_room where talk_room_name = ?",
                "insert into talk_room values (talk_room_seq.nextval,?)"};
        try {
            conn = dbMgr.getConnection();

            // 1. 그룹창 중복 체크
            pstmt = conn.prepareStatement(sqls[0]);
            pstmt.setString(1, group_name);
            rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                this.tfNum = 0;
            } else {
                // 2. 그룹창 추가
                pstmt.close();  // 기존 pstmt 제거
                pstmt = conn.prepareStatement(sqls[1]);
                pstmt.setString(1, group_name);
                tfNum = pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            this.tfNum = -1;
            e.printStackTrace();    // 오류를 콘솔에 출력하여 추적
        } finally {
            dbMgr.freeConnection(conn, pstmt, rs);
        }
        return tfNum;
    }


    public void insertMsg(String msg, String mem_ip, String roomName) {
        sql = "insert into message values (seq_msg_no.nextval, ?, to_char(SYSDATE, 'yyyy-mm-dd hh24:mi'), " +
                "?, (select talk_room_id from talk_room where talk_room_name = ?))";

        try {
            conn = dbMgr.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, msg);   // msg
            pstmt.setString(2, mem_ip);   // mem_ip
            pstmt.setString(3, roomName);   // talk_room_id
            tfNum = pstmt.executeUpdate();

        } catch (SQLException e) {
            this.tfNum = -1;
            System.out.println("동작 중 오류 발생 | " + e.getMessage());
        } finally {
            dbMgr.freeConnection(conn, pstmt);
        }
    }


    // 클라이언트 그룹창 입장
    public void joinGroup(String nickName, String roomName) {
        sql = "insert into room_member values (" +
                "(select mem_ip from member where mem_nick = ?), " +
                "(select talk_room_id from talk_room where talk_room_name = ?))";

        try {
            conn = dbMgr.getConnection();
            pstmt = conn.prepareStatement(sql);

            // DB 내 데이터 저장
            pstmt.setString(1, nickName);
            pstmt.setString(2, roomName);
            tfNum = pstmt.executeUpdate();

        } catch (SQLException e) {
            this.tfNum = -1;
        } finally {
            dbMgr.freeConnection(conn, pstmt);
        } // 자원 명시적 종료
    }


    // RoomMap 데이터 input
    public ConcurrentHashMap<String, String> getRoomMap() {
        ConcurrentHashMap<String, String> roomMap = new ConcurrentHashMap<>();
        sql = "select * from talk_room";

        try {
            conn = dbMgr.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                roomMap.put(rs.getString("talk_room_id"), rs.getString("talk_room_name"));
            }
        } catch (SQLException e) { e.printStackTrace();
        } finally { dbMgr.freeConnection(conn, pstmt, rs);}
        return roomMap;
    }


    // MemMap 데이터 Input
    public ConcurrentHashMap<String, String> getMemMap() {
        ConcurrentHashMap<String, String> memMap = new ConcurrentHashMap<>();
        sql = "select * from member";

        try {
            conn = dbMgr.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                memMap.put(rs.getString("mem_ip"), rs.getString("mem_nick"));
            }
        } catch (SQLException e) { e.printStackTrace();
        } finally { dbMgr.freeConnection(conn, pstmt, rs);}
        return memMap;
    }


    // MemMap 데이터 Input
    public CopyOnWriteArrayList<String> getJoinMemList(String roomName) {

        CopyOnWriteArrayList<String> joinMemList = new CopyOnWriteArrayList<>();
        sql = "select mem_nick from room_member rm " +
                "join talk_room tr on rm.talk_room_id = tr.talk_room_id " +
                "join member mem on rm.mem_ip = mem.mem_ip " +
                "where tr.talk_room_name = ?";
        try {
            conn = dbMgr.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, roomName);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                joinMemList.add(rs.getString("mem_nick"));
            }
        } catch (SQLException e) { e.printStackTrace();
        } finally { dbMgr.freeConnection(conn, pstmt, rs);}

        return joinMemList;
    }


    // DB 메세지 호출하기
    public CopyOnWriteArrayList<String> getMsgList(String roomName) {
        CopyOnWriteArrayList<String> msgList = new CopyOnWriteArrayList<>();

        sql = "select /*+ INDEX(mg idx_message_date_time_msg_id) */ mg.msg " +
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
        } catch (SQLException e) { e.printStackTrace();
        } finally { dbMgr.freeConnection(conn, pstmt, rs);}
        return msgList;
    }


    // NickName을 통해 IP주소 구하기
    public String getIP(String nickName) {
        sql = "select mem_ip from member where mem_nick = ?";

        try {
            conn = dbMgr.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nickName);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                mem_ip = rs.getString("mem_ip");
            }
        } catch (SQLException e) { e.printStackTrace();
        } finally { dbMgr.freeConnection(conn, pstmt, rs);}

        return mem_ip;
    }

//    public static void main(String[] args) {
//        ProjectDAO pr = new ProjectDAO();
//        System.out.println(pr.getIP("admin"));
//    }

}