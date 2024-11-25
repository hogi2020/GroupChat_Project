package ProjectCode;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;

public class ServerMain extends JFrame {
    // 선언부
    Socket clientSocket;
    ServerDataMng sdm;

    //서버로그 보여주는 창 선언
    JTextArea jta_log = new JTextArea(10,50); //서버 로그를 보여주는 창
    JScrollPane jsp_log = new JScrollPane(jta_log, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); //서버 로그가 많아 지면 스크롤한다.
    JPanel jp_north = new JPanel();

    // 서버 실행 및 클라이언트 접속
    public void ServerStart() {

        try(ServerSocket ss = new ServerSocket(9000)) {
            jta_log.append("Ready to Server " + this.setDays() + "\n");

            // 채팅데이터 관리를 위한 Map 객체 생성
            sdm = new ServerDataMng();

            while(true) {
                // 새로운 클라이언트가 들어올때까지 accept()는 Block!!
                clientSocket = ss.accept();
                jta_log.append("client info : " + clientSocket + " " + setDays() + "\n");
                System.out.println("클라이언트 접속 | " + clientSocket.getInetAddress());

                // ServerThread 클래스의 run()스레드 생성
                new Thread(new ServerThread(clientSocket, sdm, this)).start(); //pdao
            }
        } catch (IOException e) {
            System.out.println("서버 작동 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //서버로그창 실행하는 코드문
    public void initDisplay(){
        jta_log.setEditable(false); //텍스트 에리어를 읽기 전용으로 만드는 코드
        jp_north.setLayout(new FlowLayout(FlowLayout.LEFT));
        jta_log.setBackground(new Color(135, 206, 235));

        this.add("South",jp_north);
        this.add("Center",jsp_log);
        this.setSize(700, 500);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    // 시스템의 날짜 정보 가져오기.
    public String setDays(){
        Calendar cal = Calendar.getInstance(); //날짜 정보 가져오기
        int yyyy = cal.get(Calendar.YEAR); //년도
        int mm = cal.get(Calendar.MONTH)+1; //월 +1을 하는 이유는 0부터 시작해서 1을 더해 1월부터 시작하려고 한다.
        int day = cal.get(Calendar.DAY_OF_MONTH); //일
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        return yyyy+ "-" + (mm < 10 ? "0"+mm:""+mm) + "-" + (day < 10 ? "0"+day:""+day)
                + " " + (hour < 10 ? "0" + hour:""+hour) + ":" + (minute < 10 ? "0" + minute:""+minute);
    }

    // 메인 메소드 실행
    public static void main(String[] args) {
        ServerMain sm = new ServerMain();
        sm.initDisplay();
        sm.ServerStart();
    }
}