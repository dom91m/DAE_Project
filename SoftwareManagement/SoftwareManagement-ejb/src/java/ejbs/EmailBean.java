package ejbs;


import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


@Stateless
@LocalBean
public class EmailBean {
    
    Properties props;
    Session session;
    @Resource(lookup = "mail/dae")
    private Session mailSession;
    
    @PostConstruct
    public void init() {
        props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "false");
        props.put("mail.smtp.ssl.enable", "true");

        session = Session.getInstance(props, new EmailAuth());
    }
    
    public void sendEmail(String to, String content, String subject){
    try {
        
        Message msg = new MimeMessage(session);

        InternetAddress from = new InternetAddress("projetodae20182019@gmail.com", "projetodae20182019@gmail.com");
        msg.setFrom(from);

        InternetAddress toAddress = new InternetAddress(to);

        msg.setRecipient(Message.RecipientType.TO, toAddress);

        String messageToSend = "<html>\n<body>\n" + content + "</body>\n</html>";
        msg.setSubject(subject);
        msg.setContent(messageToSend, "text/html");
        
       
        Transport.send(msg);
    } catch (UnsupportedEncodingException ex) {
        ex.printStackTrace();

    } catch (MessagingException ex) {
        ex.printStackTrace();
    }
    }

    static class EmailAuth extends Authenticator {

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {

            return new PasswordAuthentication("projetodae20182019@gmail.com", "proj.danmarjoao");

        }
    }
}