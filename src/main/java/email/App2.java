// package email;

// import java.io.BufferedReader;
// import java.io.BufferedWriter;
// import java.io.FileReader;
// import java.io.FileWriter;
// import java.net.ServerSocket;
// import java.net.Socket;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Timer;
// import java.util.TimerTask;
// import java.util.regex.Pattern;
// import java.text.ParseException;
// import java.text.SimpleDateFormat;
// import java.util.Date;

// import javafx.scene.layout.VBox;
// import javafx.application.Application;
// import javafx.geometry.Insets;
// import javafx.scene.Scene;
// import javafx.scene.control.Button;
// import javafx.scene.control.Label;
// import javafx.scene.control.TextField;
// import javafx.scene.layout.HBox;
// import javafx.stage.Stage;

// class ServerThreading extends Thread {

//   public KeyLog keylog;
//   SendMail send;

//   public ServerThreading(KeyLog keyLog, SendMail send) {
//     this.keylog = keyLog;
//     this.send = send;
//   }

//   public void run() {
//     try {
//       ServerSocket serverSocket = new ServerSocket(5000);
//       System.out.println("Server is running...");
//       boolean stop = false;
//       while (!stop) {
//         Socket socket = serverSocket.accept();
//         ClientThreading clientSocket = new ClientThreading(
//           socket,
//           keylog,
//           send
//         );
//         clientSocket.start();
//       }
//       serverSocket.close();
//     } catch (Exception e) {
//       System.out.println(e);
//     }
//   }
// }

// //==================================================================================================
// //==================================================================================================
// //==================================================================================================

// class CheckingThreading extends Thread {

//   String from;
//   String host = "pop.gmail.com";
//   String mailStoreType = "pop3";
//   String username = "dotu10257@gmail.com";
//   String password = "zhylipsbmyvwzrkg";
//   CheckMail gmailmail;

//   public CheckingThreading(String from) {
//     this.from = from;
//     this.gmailmail = new CheckMail(host, mailStoreType, username, password);
//   }

//   //==================================================================================================
//   private Date parseDate(String dateStr) {
//     try {
//         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//         return dateFormat.parse(dateStr);
//     } catch (ParseException e) {
//         e.printStackTrace();
//         return null;
//     }
//   } 
  
//   public boolean checking(
//     Pair<String, String, Date> newMail,
//     List<Pair<String, String, Date>> oldMail
//   ) {
//     if (!newMail.getKey().equals(this.from)) {
//       return false;
//     }
//     if (oldMail == null) {
//       return true;
//     }

//     for (Pair<String, String, Date> old : oldMail) {
//       if (old.getValue().equals(newMail.getValue()) && old.getTime().equals(newMail.getTime())) {
//         return false;
//       }
//     }

//     return true;
//   }

//   //==================================================================================================
//   public void run() {
//     Timer timer = new Timer();
//     TimerTask task = new TimerTask() {
//       @Override
//       public void run() {
//         System.out.println("Task executed at regular interval");

//         List<Pair<String, String, Date>> new_mail = new ArrayList<Pair<String, String, Date>>();
//         List<Pair<String, String, Date>> old_mail = new ArrayList<Pair<String, String, Date>>();

//         List<Pair<String, String, Date>> newMail = gmailmail.fetch();
//         try {
//           BufferedReader reader_new = new BufferedReader(
//             new FileReader("new_mail.txt")
//           );
//           String line;
//           while ((line = reader_new.readLine()) != null) {
//             String[] parts = line.split("=");
//             String email = parts[0];
//             String commandAndDate = parts[1];
//             String[] commandAndDateParts = commandAndDate.split(",");
//             if (commandAndDateParts.length == 2) {
//               String command = commandAndDateParts[0];
//               String dateStr = commandAndDateParts[1];
//               // Chuyển đổi chuỗi ngày thành đối tượng Date
//               Date date = parseDate(dateStr);

//               new_mail.add(new Pair<String, String, Date>(email, command, date));
//             }
//           }

//           BufferedReader reader_old = new BufferedReader(
//             new FileReader("old_mail.txt")
//           );
//           line = "";
//           while ((line = reader_old.readLine()) != null) {
//             String[] parts = line.split("=");
//             String email = parts[0];
//             String commandAndDate = parts[1];
//             String[] commandAndDateParts = commandAndDate.split(",");
//             if (commandAndDateParts.length == 2) {
//               String command = commandAndDateParts[0];
//               String dateStr = commandAndDateParts[1];
//               // Chuyển đổi chuỗi ngày thành đối tượng Date
//               Date date = parseDate(dateStr);

//               old_mail.add(new Pair<String, String, Date>(email, command, date));
//             }
//           }
//           reader_new.close();
//           reader_old.close();
//         } catch (Exception e) {
//           System.out.println(e);
//         }
//         List<String> task = new ArrayList<String>();
//         task.add("end");
//         if (new_mail != null) {
//           for (Pair<String, String, Date> mailItem : new_mail) {
//             if (checking(mailItem, old_mail)) {
//               task.add(0, mailItem.getValue());
//             }
//           }
//         }
//         for (String item : task) {
//           System.out.println(item);
//         }
//         ClientSocket client = new ClientSocket(task);
//         try {
//           BufferedWriter writer = new BufferedWriter(
//             new FileWriter("old_mail.txt")
//           );
//           for (Pair<String, String, Date> pair : new_mail) {
//             SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//             writer.write(pair.getKey() + "=" + pair.getValue() + "," + dateFormat.format(pair.getTime()));
//             writer.newLine();
//           }
//           writer.close();
//         } catch (Exception e) {
//           System.out.println(e);
//         }
//       }
//     };

//     long interval = 50000;

//     timer.scheduleAtFixedRate(task, 0, interval);
//   }
// }




// public class App2 extends Application {

//     public static void main(String[] args) {
//         launch(args);
//     }

//     @Override
//     public void start(Stage primaryStage) {
//         primaryStage.setTitle("JavaFX Simple GUI");

//         // Tạo các nút
//         Button connectButton = new Button("Kết Nối");
//         Button exitButton = new Button("Thoát");
//         Button minimizeButton = new Button("Thu Nhỏ");
//         Button exitAppButton = new Button("Exit");

//         // Thiết lập sự kiện khi nút được nhấn
//         // connectButton.setOnAction(e -> System.out.println("Đang kết nối..."));
//         // exitButton.setOnAction(e -> System.exit(0));
//         // minimizeButton.setOnAction(e -> primaryStage.setIconified(true));
//         // exitAppButton.setOnAction(e -> {
//         //     System.out.println("Thoát ứng dụng...");
//         //     System.exit(0);
//         // });

//         // Tạo layout
//         VBox layout = new VBox(10);
//         layout.getChildren().addAll(connectButton, exitButton, minimizeButton, exitAppButton);

//         // Tạo Scene
//         Scene scene = new Scene(layout, 300, 200);

//         // Thiết lập Scene cho Stage
//         primaryStage.setScene(scene);

//         // Hiển thị Stage
//         primaryStage.show();
//     }
// }