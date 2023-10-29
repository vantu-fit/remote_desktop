package email;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientSocket {

  Socket socket;
  BufferedReader reader;
  PrintWriter writer;
  List<String> task;

  public ClientSocket(List<String> task) {
    this.task = task;
    try {
      this.socket = new Socket("localhost", 5000);
      this.reader =
        new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
      this.writer = new PrintWriter(this.socket.getOutputStream(), true);
      this.writer.println("Hello server");
      for (String s : this.task) {
        this.writer.println(s);
      }
      socket.close();
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}
