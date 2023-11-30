package email;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import java.util.Date;

class Pair<K, V, T> {

    private K key;
    private V value;
    private Date time;

    public Pair(K key, V value, Date time) {
        this.key = key;
        this.value = value;
        this.time = time;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public Date getTime() {
        return time;
    }
}

public class CheckMail {

  String host = "pop.gmail.com";
  String mailStoreType = "pop3";
  String username = "dotu10257@gmail.com";
  String password = "zhylipsbmyvwzrkg";

  public CheckMail(
    String pop3Host,
    String storeType,
    String user,
    String password
  ) {
    this.host = pop3Host;
    this.mailStoreType = storeType;
    this.username = user;
    this.password = password;
  }

  public List<Pair<String, String, Date>> fetch() { // return null
    try {
      // create properties field
      Properties properties = new Properties();
      properties.put("mail.store.protocol", "pop3");
      properties.put("mail.pop3.host", this.host);
      properties.put("mail.pop3.port", "995");
      properties.put("mail.pop3.starttls.enable", "true");
      Session emailSession = Session.getInstance(properties);
      Store store = emailSession.getStore("pop3s");

      store.connect(this.host, this.username, this.password);

      Folder emailFolder = store.getFolder("INBOX");
      emailFolder.open(Folder.READ_ONLY);

      int totalMessages = emailFolder.getMessageCount();
      System.out.println(totalMessages);
      int start = Math.max(1, totalMessages - 4);
      System.out.println(start);

      List<Pair<String, String, Date>> pairList = new ArrayList<>();
      Message[] messages = emailFolder.getMessages(start, totalMessages);

      for (int i = 0; i < messages.length; i++) {
        Message message = messages[i];
        String from = ((InternetAddress) message.getFrom()[0]).getAddress();
        String subject = message.getSubject();
        Date time = message.getSentDate(); // Thời gian gửi email
        pairList.add(new Pair<String, String, Date>(from, subject, time));
      }
      BufferedWriter writer = new BufferedWriter(
        new FileWriter("new_mail.txt")
      );
      for (Pair<String, String, Date> pair : pairList) {
        //writer.write(pair.getKey() + "=" + pair.getValue());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        writer.write(pair.getKey() + "=" + pair.getValue() + "," + dateFormat.format(pair.getTime()));
        writer.newLine();
      }

      writer.close();

      emailFolder.close(false);
      store.close();
      return pairList;
    } catch (NoSuchProviderException e) {
      e.printStackTrace();
      return null;
    } catch (MessagingException e) {
      e.printStackTrace();
      return null;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static void main(String[] args) {
    CheckMail gmailmail = new CheckMail(
      "pop.gmail.com",
      "pop3",
      "dotu10257@gmail.com",
      "zhylipsbmyvwzrkg"
    );
    gmailmail.fetch();
  }
}
