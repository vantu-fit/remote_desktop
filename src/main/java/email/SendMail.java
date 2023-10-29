package email;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMail {

  Message message;
  Session session;

  public SendMail(String to) {
    Properties properties = new Properties();
    properties.put("mail.smtp.host", "smtp.gmail.com");
    properties.put("mail.smtp.port", "587");
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");

    this.session =
      Session.getDefaultInstance(
        properties,
        new Authenticator() {
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(
              "dotu10257@gmail.com",
              "zhylipsbmyvwzrkg"
            );
          }
        }
      );

    try {
      this.message = new MimeMessage(this.session);

      this.message.setFrom(new InternetAddress("dotu10257@gmail.com"));

      this.message.setRecipient(
          Message.RecipientType.TO,
          new InternetAddress(to)
        );
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  public void sendSubject(String subject) {
    try {
      this.message.setSubject(subject);
      Transport.send(message);
      System.out.println("Send mail file success!");
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  public void sendFile(String path) {
    try {
      this.message.setSubject("Response remote file");
      MimeBodyPart filePart = new MimeBodyPart();
      filePart.attachFile(path);
      Multipart multipart = new MimeMultipart();
      multipart.addBodyPart(filePart);
      this.message.setContent(multipart);
      Transport.send(message);
      System.out.println("Send mail file success!");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    SendMail send = new SendMail("dotu30257@gmail.com");
    send.sendFile("name1.png");
  }
}
