package classes.day3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TimeServer2 extends Thread{
    //선언부
    //생성자
    public void init(){
        int port = 5000;
        try{
            //예외가 발생할 가능성이 있는 코드를 쓴다.
            ServerSocket ss = new ServerSocket(port);
            System.out.println("Server Ready....\n");
            //insert here
            while(true){
                //아래 코드는 진행이 안됨. - waiting
                //다음코드가 실행되지 않고 있다면 new Socket(ip,port);
                Socket s = ss.accept();//client측의 소켓을 받아온다.
                //소켓을 이용해서 inputstream, outputstream 생성함.
                System.out.println("Accepted connection from " + s.getInetAddress());
            }
        }catch(IOException e){

        }
    }
    //메인메소드
    public static void main(String[] args) {
        TimeServer2 ts = new TimeServer2();
        ts.init();//준비
        ts.start();//스레드가 동작함 - run()호출됨
    }
}
