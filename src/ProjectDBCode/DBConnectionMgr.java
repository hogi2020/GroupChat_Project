package ProjectDBCode;

import java.sql.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class DBConnectionMgr {
    // Connection Class
    private static DBConnectionMgr dbMgr;
    private BlockingQueue<Connection> connPoolQueue;

    // pool 설정 |
    // 상수이나 인스턴스가 한번만 이루어지므로 Static 굳이 사용하지 않아도 된다.
    // 다만 확장성과 유지보수를 고려하여 명시적으로 인스턴스가 되지 않도록 Static을 사용합니다.
    private final int INIT_POOL_SIZE = 5;
    private final int MAX_POOL_SIZE = 20;

    // Oracle DB Connection info
    public final String _DRIVER = "oracle.jdbc.driver.OracleDriver";
    public final String _URL = "jdbc:oracle:thin:@192.168.0.36:1521:orcl11";
    public final String _USER = "scott";
    public final String _PW = "tiger";


    // 생성자 설정
    private DBConnectionMgr() {
        try {
            Class.forName(_DRIVER);
            connPoolQueue = new LinkedBlockingQueue<>(MAX_POOL_SIZE);
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC 드라이버 로드 실패" + e.getMessage());
        }
    }

    // 커넥션 생성
    private Connection createNewConn() {
        try {
            return DriverManager.getConnection(_URL, _USER, _PW);
        } catch (SQLException e) {
            System.out.println("커넥션 생성 실패" + e.getMessage());
            return null;
        }
    }


    // Pool 초기화 선언
    private void initializeConnPool() throws SQLException {
        for (int i = 0; i < INIT_POOL_SIZE; i++) {
            connPoolQueue.offer(createNewConn());
        }
    }


    // DBConnection 타입의 변수를 선언
    // DBConnection 클래스에 대한 싱글톤 패턴 구현
    /// null 이라면 DBConnectionMgr 클래스의 인스턴스 생성
    /// 다만 초기 2개의 Thread가 동시에 호출될 경우 모두 null인 상태이므로 2번 호출 가능
    /// 해당 메서드가 동시에 실행되는 것을 방지하기 위해 메소드 "synchronized"를 사용
    /// 하지만 getInstance가 호출될때마다 쓰레드가 동기화 블록을 통과해야 하므로 성능이 좋지않음
    /// 그래서 인스턴스가 생기면 동기화 블록을 건너뛰도록 "더블체크 락킹" 구현
    public static DBConnectionMgr getInstance() {
        if(dbMgr == null) {
            synchronized (DBConnectionMgr.class){
                if(dbMgr == null) {
                    dbMgr = new DBConnectionMgr();
                }
            }
        }
        return dbMgr;
    }


    // Database 연동 메서드 생성
    /// 모든 자원이 사용되어 반환되지 않을 수 있으므로 타임아웃을 추가
    /// 대기시간을 발생시켜 유연한 자원 관리가 가능하도록 함.
    public Connection getConnection() {
        try {
            Connection conn = connPoolQueue.poll(1000, TimeUnit.MILLISECONDS);
            if (conn == null || conn.isClosed()) {
                if (connPoolQueue.size() < MAX_POOL_SIZE) {
                    conn = createNewConn();
                }  else {
                    conn = connPoolQueue.take(); // 대기
                }
            }
            return conn;
        } catch (InterruptedException | SQLException e) {
            System.out.println("커넥션 획득 중 인터럽트 발생 | " + e.getMessage());
            return null;
        }
    }


    private void releaseConn(Connection conn) {
        try {
            if (!conn.isClosed() && connPoolQueue.size() < MAX_POOL_SIZE) {
                connPoolQueue.offer(conn);
            }
        } catch (SQLException e) {
            System.out.println("커넥션 반환 중 오류 발생 | " + e.getMessage());
        }
    }


    // 사용한 자원 반납하기
    // 생략 시, JVM의 가비지컬렉터가 대신 하지만, 명시적으로 구현하는 것을 권장
    public void freeConnection(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if(pstmt != null) { pstmt.close();}
            if(rs != null) { rs.close();}
            if(conn != null) { releaseConn(conn);}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void freeConnection(Connection conn, PreparedStatement pstmt) {
        try {
            if(pstmt != null) { pstmt.close();}
            if(conn != null) { releaseConn(conn);}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
