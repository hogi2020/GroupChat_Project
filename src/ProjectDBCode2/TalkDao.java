package ProjectDBCode2;

import ProjectDBCode.DBConnectionMgr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class

TalkDao {
    // DB 연결 관리 객체
    DBConnectionMgrJS dbMgr = DBConnectionMgrJS.getInstance();
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    // 1. 사용자 로그인 (닉네임과 IP로 로그인 처리)
    public String login(String mem_nick, String nick_ip) {
        String result = null;  // 로그인 결과 (닉네임 또는 오류 메시지)
        StringBuilder sql_checkNick = new StringBuilder();
        StringBuilder sql_checkIp = new StringBuilder();

        try {
            // 닉네임이 존재하는지 확인
            sql_checkNick.append("SELECT COUNT(1) AS isExist FROM mem WHERE mem_nick=?");
            conn = dbMgr.getConnection();
            pstmt = conn.prepareStatement(sql_checkNick.toString());
            pstmt.setString(1, mem_nick);
            rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt("isExist") > 0) {
                // IP 주소로도 로그인할 수 있는지 확인
                sql_checkIp.append("SELECT mem_nick FROM mem WHERE mem_nick=? AND mem_ip=?");
                pstmt = conn.prepareStatement(sql_checkIp.toString());
                pstmt.setString(1, mem_nick);
                pstmt.setString(2, nick_ip);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    result = rs.getString("mem_nick");  // 로그인 성공: 닉네임 반환
                } else {
                    result = "IP 주소가 일치하지 않습니다.";  // IP 불일치
                }
            } else {
                result = "존재하지 않는 닉네임입니다.";  // 닉네임이 없으면 오류
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = "로그인 처리 중 오류가 발생했습니다.";  // 예외 발생 시 오류 메시지
        }finally {
            // 자원 정리
            closeResources();
        }
        return result;
    }

    // 2. 메시지 전송 (채팅)

    public int sendMessage(String sender, String receiver, String msg) {
        int result = 0;
        StringBuilder sql_sendMsg = new StringBuilder();

        try {
            // 메시지 삽입 SQL
            sql_sendMsg.append("INSERT INTO chat_messages (sender, receiver, message) ");
            sql_sendMsg.append("VALUES (?, ?, ?)");
            conn = dbMgr.getConnection();
            pstmt = conn.prepareStatement(sql_sendMsg.toString());
            pstmt.setString(1, sender);
            pstmt.setString(2, receiver);
            pstmt.setString(3, msg);

            result = pstmt.executeUpdate();  // 메시지 전송
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }

        return result;  // 전송 성공 시 1, 실패 시 0
    }
    // 3. 메시지 수신 (채팅)

    public List<String> getMessages(String mem_nick) {
        List<String> messages = new ArrayList<>();
        StringBuilder sql_getMsgs = new StringBuilder();

        try {
            // 받은 메시지 조회 SQL
            sql_getMsgs.append("SELECT sender, message FROM chat_messages ");
            sql_getMsgs.append("WHERE receiver=? ORDER BY timestamp DESC");
            conn = dbMgr.getConnection();
            pstmt = conn.prepareStatement(sql_getMsgs.toString());
            pstmt.setString(1, mem_nick);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String msg = "From: " + rs.getString("sender") + " - " + rs.getString("message");
                messages.add(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }

        return messages;  // 메시지 리스트 반환
    }

    // 자원 정리
    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
