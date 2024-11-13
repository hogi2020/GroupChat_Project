package ProjectDBCode;

import java.sql.*;

public class ProjectDAO {
    DBConnectionMgr dbMgr = DBConnectionMgr.getInstance();
    Connection conn;
    PreparedStatement pstmt;
    CallableStatement cstmt;
    String sql = new String();
    int tfNum = -1;     // 성공은 1, 실패는 0


    public void insertUserInfo(String mem_ip, String mem_nick){
        sql = "insert into UserInfo (mem_ip, mem_nick) VALUES (?, ?)";

        try {
            conn = dbMgr.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, mem_ip);
            pstmt.setString(2, mem_nick);
            tfNum = pstmt.executeUpdate();
        }
        catch (SQLException se){

        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            dbMgr.freeConnection(conn, pstmt);
        }

    }

    // Database 데이터 입력 메서드
    public void insertMem(String mem_ip, String mem_nick, String mem_pw) {
        sql = "insert into member values (?,?,?)";

        try {
            conn = dbMgr.getConnection();
            pstmt = conn.prepareStatement(sql);

            // DB 내 데이터 저장
            pstmt.setString(1, mem_ip);
            pstmt.setString(2, mem_nick);
            pstmt.setString(3, mem_pw);
            tfNum = pstmt.executeUpdate();

        } catch (SQLException e) { this.tfNum = -1;
        } finally { dbMgr.freeConnection(conn, pstmt);} // 자원 명시적 종료
    }


    // Database 데이터 삭제 메서드
    public void deleteMem(String mem_nick, String mem_pw) {
        sql = "delete from member where mem_nick = ? and mem_pw = ?";

        try {
            conn = dbMgr.getConnection();
            pstmt = conn.prepareStatement(sql);

            // DB 내 데이터 삭제
            pstmt.setString(1, mem_nick);
            pstmt.setString(2, mem_pw);
            tfNum = pstmt.executeUpdate();

        } catch (SQLException e) { e.printStackTrace();
        } finally { dbMgr.freeConnection(conn, pstmt);} // 자원 명시적 종료
    }


    // UPDATE [테이블] SET [열] = '변경할값' WHERE [조건]
    // Database 데이터 수정 메서드
    public void updateMem(String mem_ip, String update_nick, String update_pw) {
        sql = "update member set mem_nick = ? where mem_ip = ? and mem_pw = ?";

        try {
            conn = dbMgr.getConnection();
            pstmt = conn.prepareStatement(sql);

            // DB 내 데이터 삭제
            pstmt.setString(1, update_nick);
            pstmt.setString(2, mem_ip);
            pstmt.setString(3, update_pw);
            tfNum = pstmt.executeUpdate();

        } catch (SQLException e) { this.tfNum = -1;
        } finally { dbMgr.freeConnection(conn, pstmt);} // 자원 명시적 종료
    }


    // 결과 출력
    public int result() {
        return tfNum;
    }


    // 로그인 정보 확인 메서드
    public int loginCheck(String nickName, int pw) {
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
        }

        return tfNum;
    }

//    public static void main(String[] args) {
//        ProjectDAO pr = new ProjectDAO();
//        pr.insertMem("192.168.0.31","KKK","123123");
//    }

}