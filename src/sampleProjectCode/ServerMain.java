package sampleProjectCode;

// ghgh
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

public class ServerMain extends JFrame implements ActionListener {
    // 선언부
    ServerThread st = null;
    List<ServerThread> stl = null;
    Socket clientSocket;
    ServerDataMng sdm;

    //서버로그 보여주는 창 선언
    JTextArea jta_log = new JTextArea(10,50); //서버 로그를 보여주는 창
    JScrollPane jsp_log = new JScrollPane(jta_log, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); //서버 로그가 많아 지면 스크롤한다.
    JPanel jp_north = new JPanel();
    JButton jbtn_log = new JButton("저장");
    String log_save = "src\\Log\\";

    // 서버 실행 및 클라이언트 접속
    public void ServerStart() {
        stl = new Vector<>();
        try(ServerSocket ss = new ServerSocket(3000)) {
            System.out.println("Ready to Server.....");

            // 채팅데이터 관리를 위한 Map 객체 생성
            sdm = new ServerDataMng();

            while(true) {
                // 새로운 클라이언트가 들어올때까지 accept()는 Block!!
                clientSocket = ss.accept();
                System.out.println("클라이언트 접속 | " + clientSocket.getInetAddress());

                // ServerThread 클래스의 run()스레드 생성
                new Thread(new ServerThread(clientSocket, sdm, this)).start();
            }
        } catch (IOException e) {
            System.out.println("서버 작동 중 오류 발생: " + e.getMessage());
        }
    }

    //서버로그창 실행하는 코드문
    public void initDisplay(){
        jta_log.setEditable(false); //텍스트 에리어를 읽기 전용으로 만드는 코드
        jbtn_log.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent je) {
                Object obj = je.getSource();
                if (obj == jbtn_log){
                    String fileName = "log_" + setTimer() + ",txt"; //로그 저장
                    System.out.println(fileName); //저장된 파일이름 보여주기.
                    try{
                        File f = new File(fileName);
                        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f.getAbsolutePath())));
                        /*
                        버퍼라이터 - 문자 출력 스트림을 버퍼에 저장하고, 버퍼가 가득 차거나 명시적으로 flush() 메서드가 호출될 때 실제로 파일에 데이터를 씁니다.
                        프린트라이터 - 파일에 데이터를 쓰는 데 편리한 방법을 제공하는 클래스입니다.
                        파일라이터 - 절대 경로 반환 */
                        pw.write(jta_log.getText()); // 파일 데이터에 로그 반환
                        pw.close(); // 사용한 입출력 클래스는 반드시 닫아야 해서 닫는 명령어 설정
                    } catch (Exception e) {
                        e.printStackTrace(); //예외 발행 했을때 위치를 알려준다.(예외 발생때만 출력한다.)
                    }
                }
            }
        });

        jp_north.setLayout(new FlowLayout(FlowLayout.LEFT));
        jta_log.setBackground(new Color(135, 206, 235));
        jp_north.add(jbtn_log);

        this.add("South",jp_north);
        this.add("Center",jsp_log);
        this.setSize(500, 400);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    // 시스템의 날짜 정보 가져오기.
    public String setTimer(){
        Calendar cal = Calendar.getInstance(); //날짜 정보 가져오기
        int yyyy = cal.get(Calendar.YEAR); //년도
        int mm = cal.get(Calendar.MONTH)+1; //월 +1을 하는 이유는 0부터 시작해서 1을 더해 1월부터 시작하려고 한다.
        int day = cal.get(Calendar.DAY_OF_MONTH); //일
        return yyyy+"-" + (mm < 10 ? "0"+mm:""+mm) + "-" + (day < 10 ? "0"+day:""+day);
    }

    // 메인 메소드 실행
    public static void main(String[] args) {
        ServerMain sm = new ServerMain();
        sm.initDisplay();
        new ServerMain().ServerStart();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}