package ProjectCode;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientProtocol implements Runnable {
    // 클라이언트 소켓, 입출력 스트림 선언
    private Socket clientSocket = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    String msg = null;

    private ClientUI cui = null;


    // 생성자 생성
    public ClientProtocol(ClientUI cui) {
        this.cui = cui;
        connectToServer();
    }


    // 서버 연결
    public void connectToServer() {
        try {
            clientSocket = new Socket("localhost", 3000);
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            System.out.println("Connect to Server.....");

            new Thread(this).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // 프로토콜에 따른 입력스트림 처리
    @Override
    public void run() {
        try {
            while((msg = (String) in.readObject()) != null) {
                System.out.println("수신정보 | " + msg);
                String[] strArray = msg.split("#", 2);
                String protocol = strArray[0];
                String content = strArray[1];

                // 입력 스트림을 통한 RoomList 업데이트 진행
                if (protocol.equals("MsgSend")) {
                    cui.displayMsg(content);

                } else if (protocol.equals("RoomList")) {
                    cui.updateRoomList(content.split(","));

                } else if (protocol.equals("Join")) {
                    cui.displayMsg(content);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (clientSocket != null) clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    // 클라이언트-서버 출력스트림 메서드
    public void sendMsg(String msg) {
        try {
            out.writeObject(msg);
            // 메모리 임시공간(버퍼)은 일정 크기가 차면 그 때 출력되는데, flush()를 통해 지연 없이 즉시 출력되도록 할 수 있습니다.
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}