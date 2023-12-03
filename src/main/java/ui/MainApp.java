package ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import email.KeyLog;
import email.SendMail;
import email.ClientThreading;
import email.CheckMail;
import email.ClientSocket;
import email.Pair;

class ServerThreading extends Thread {

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

//==================================================================================================
//==================================================================================================
//==================================================================================================

class CheckingThreading extends Thread {

  String from;
  String host = "pop.gmail.com";
  String mailStoreType = "pop3";
  String username = "dotu10257@gmail.com";
  String password = "zhylipsbmyvwzrkg";
  CheckMail gmailmail;

  public CheckingThreading(String from) {
    this.from = from;
    this.gmailmail = new CheckMail(host, mailStoreType, username, password);
  }

  //==================================================================================================
  private Date parseDate(String dateStr) {
    try {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.parse(dateStr);
    } catch (ParseException e) {
        e.printStackTrace();
        return null;
    }
  } 
  
  public boolean checking(
    Pair<String, String, Date> newMail,
    List<Pair<String, String, Date>> oldMail
  ) {
    if (!newMail.getKey().equals(this.from)) {
      return false;
    }
    if (oldMail == null) {
      return true;
    }

    for (Pair<String, String, Date> old : oldMail) {
      if (old.getValue().equals(newMail.getValue()) && old.getTime().equals(newMail.getTime())) {
        return false;
      }
    }

    return true;
  }

  //==================================================================================================
  public void run() {
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        System.out.println("Task executed at regular interval");

        List<Pair<String, String, Date>> new_mail = new ArrayList<Pair<String, String, Date>>();
        List<Pair<String, String, Date>> old_mail = new ArrayList<Pair<String, String, Date>>();

        List<Pair<String, String, Date>> newMail = gmailmail.fetch();
        try {
          BufferedReader reader_new = new BufferedReader(
            new FileReader("new_mail.txt")
          );
          String line;
          while ((line = reader_new.readLine()) != null) {
            String[] parts = line.split("=");
            String email = parts[0];
            String commandAndDate = parts[1];
            String[] commandAndDateParts = commandAndDate.split(",");
            if (commandAndDateParts.length == 2) {
              String command = commandAndDateParts[0];
              String dateStr = commandAndDateParts[1];
              // Chuyển đổi chuỗi ngày thành đối tượng Date
              Date date = parseDate(dateStr);

              new_mail.add(new Pair<String, String, Date>(email, command, date));
            }
          }

          BufferedReader reader_old = new BufferedReader(
            new FileReader("old_mail.txt")
          );
          line = "";
          while ((line = reader_old.readLine()) != null) {
            String[] parts = line.split("=");
            String email = parts[0];
            String commandAndDate = parts[1];
            String[] commandAndDateParts = commandAndDate.split(",");
            if (commandAndDateParts.length == 2) {
              String command = commandAndDateParts[0];
              String dateStr = commandAndDateParts[1];
              // Chuyển đổi chuỗi ngày thành đối tượng Date
              Date date = parseDate(dateStr);

              old_mail.add(new Pair<String, String, Date>(email, command, date));
            }
          }
          reader_new.close();
          reader_old.close();
        } catch (Exception e) {
          System.out.println(e);
        }
        List<String> task = new ArrayList<String>();
        task.add("end");
        if (new_mail != null) {
          for (Pair<String, String, Date> mailItem : new_mail) {
            if (checking(mailItem, old_mail)) {
              task.add(0, mailItem.getValue());
            }
          }
        }
        for (String item : task) {
          System.out.println(item);
        }
        ClientSocket client = new ClientSocket(task);
        try {
          BufferedWriter writer = new BufferedWriter(
            new FileWriter("old_mail.txt")
          );
          for (Pair<String, String, Date> pair : new_mail) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            writer.write(pair.getKey() + "=" + pair.getValue() + "," + dateFormat.format(pair.getTime()));
            writer.newLine();
          }
          writer.close();
        } catch (Exception e) {
          System.out.println(e);
        }
      }
    };

    long interval = 50000;

    timer.scheduleAtFixedRate(task, 0, interval);
  }
}
public class MainApp {

    public static void main(String[] args) {

        Login LoginFrame = new Login();
        LoginFrame.setVisible(true);
        LoginFrame.pack();
        LoginFrame.setLocationRelativeTo(null); 
    }
    
}
