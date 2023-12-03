// package email;

// import java.awt.Dimension;
// import java.awt.GridBagConstraints;
// import java.awt.GridBagLayout;
// import java.awt.Insets;
// import java.awt.Toolkit;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
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

// import javax.swing.ImageIcon;
// import javax.swing.JButton;
// import javax.swing.JFrame;
// import javax.swing.JLabel;
// import javax.swing.JOptionPane;
// import javax.swing.JPanel;
// import javax.swing.JTextField;
// import java.text.ParseException;
// import java.text.SimpleDateFormat;
// import java.util.Date;

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

// public class App {

//     public static void main(String[] args) {
//         int width = 400;
//         int height = 300;

//         final JFrame frame = new JFrame("Ứng dụng Remote PC");
//         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         frame.setSize(width, height);

//         ImageIcon backgroundImage = new ImageIcon("./image/background.png");
//         JLabel backgroundLabel = new JLabel(backgroundImage);
//         backgroundLabel.setBounds(0, 0, width, height);

//         String status = "Chưa kết nối";
//         JLabel statusLabel = new JLabel("Trạng thái: " + status);

//         Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//         int x = (screenSize.width - width) / 2;
//         int y = (screenSize.height - height) / 2;
//         frame.setLocation(x, y);

//         Insets gap = new Insets(10, 30, 30, 30);
//         JPanel panel = new JPanel(new GridBagLayout());
//         GridBagConstraints constraints = new GridBagConstraints();
//         JLabel label = new JLabel("Nhập gmail điều khiển:");
//         final JTextField textField = new JTextField(20);
//         JButton connectButton = new JButton("Kết Nối");
//         JButton cancelButton = new JButton("Thoát");

        
//         //constraints.fill = GridBagConstraints.HORIZONTAL;
//         constraints.gridx = 0;
//         constraints.gridy = 0;
//         constraints.gridwidth = 2;
//         constraints.insets = gap;
//         panel.add(label, constraints);
//         constraints.gridy = 1;
//         panel.add(textField, constraints);
//         //constraints.fill = GridBagConstraints.HORIZONTAL;
//         constraints.gridy = 2;
//         constraints.gridwidth = 1;
//         panel.add(connectButton, constraints);
//         constraints.gridx = 1;
//         panel.add(cancelButton, constraints);
//         textField.setText("leminhquan19092004@gmail.com");

//         constraints.gridy = 3; 
//         constraints.gridx = 0;
//         constraints.gridwidth = 2;
//         panel.add(statusLabel, constraints);
//         panel.add(backgroundLabel);

//         frame.add(panel);
//         frame.pack();
//         frame.setVisible(true);

//         connectButton.addActionListener(new ActionListener() {
//             public void actionPerformed(ActionEvent e) {
//                 System.out.println("Nội dung nhập: " + textField.getText());
//                 String from = textField.getText();
//                 if (from.equals("")) {
//                     JOptionPane.showMessageDialog(
//                         frame,
//                         "Bạn chưa nhập gmail điều khiển",
//                         "Lỗi",
//                         JOptionPane.ERROR_MESSAGE
//                     );
//                 } else {
//                     Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@gmail.com$");
//                     if (!pattern.matcher(from).matches()) {
//                         JOptionPane.showMessageDialog(
//                             frame,
//                             "Gmail không hợp lệ",
//                             "Lỗi",
//                             JOptionPane.ERROR_MESSAGE
//                         );
//                     } else {
//                         textField.setText("ĐÃ KẾT NỐI");
//                         KeyLog keylog = new KeyLog();
//                         SendMail send = new SendMail(from);
//                         ServerThreading server = new ServerThreading(keylog, send);
//                         server.start();
//                         CheckingThreading checking = new CheckingThreading(from);
//                         checking.start();
//                     }
//                 }
//             }
//         });

//         cancelButton.addActionListener(new ActionListener() {
//             public void actionPerformed(ActionEvent e) {
//                 int choice = JOptionPane.showConfirmDialog(
//                     frame,
//                     "Bạn có chắc muốn thoát ứng dụng?",
//                     "Xác nhận",
//                     JOptionPane.YES_NO_OPTION
//                 );

//                 if (choice == JOptionPane.YES_OPTION) {
//                     System.exit(0);
//                 }
//             }
//         });
//     }
// }

