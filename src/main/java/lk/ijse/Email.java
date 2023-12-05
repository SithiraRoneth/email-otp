
/*
author:
sithiraRoneth
 */

package lk.ijse;
import jakarta.mail.*;
import jakarta.mail.Session;
import jakarta.mail.Authenticator;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Random;

import java.util.Properties;
import java.util.Scanner;
// first you want to create app password in google account
// after that use smtp tool web

public class Email {
    public static void main(String[] args) {
        email();
    }
    public static void email() {
        System.out.println("Start");

        // Generate OTP
        String otp = generateOTP();

        // Construct email message
        Mail mail = new Mail();
        mail.setMsg("Your OTP is: " + otp);
        mail.setTo("sender's email");
        mail.setSubject("OTP");

        Thread thread = new Thread(mail);
        thread.start();
        checkOtp(otp);
        System.out.println("End");
    }

    private static void checkOtp(String otp) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the OTP received in your email: ");
        String userEnteredOtp = scanner.nextLine();

        if (userEnteredOtp.equals(otp)) {
            System.out.println("OTP verification successful!");
        } else {
            System.out.println("Invalid OTP. Please try again.");
         checkOtp(otp);
        }
    }

    private static String generateOTP() {

        return String.format("%06d", new Random().nextInt(1000000));
    }

    public static class Mail implements Runnable{
        private String msg;
        private String to;
        private String subject;
        public void setMsg(String msg) {
            this.msg = msg;
        }
        public void setTo(String to) {
            this.to = to;
        }
        public void setSubject(String subject) {
            this.subject = subject;
        }

        public boolean outMail() throws MessagingException {
            String from = "sender's@gmail.com"; //sender's email address
            String host = "localhost";

            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", 587);

            Session session = Session.getInstance(properties, new Authenticator() {


                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("sender's@gmail.com", "password");  // email and password
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(from));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setSubject(this.subject);
            mimeMessage.setText(this.msg);
            Transport.send(mimeMessage);
            return true;
        }
        public void run() {
            if (msg != null) {
                try {
                    outMail();
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("not sent. empty msg!");
            }
        }
    }

}
