package com.example.attendance.entity;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;

import javax.mail.PasswordAuthentication;

public class Mail {

	String senderName;
	String senderEmail;
	// 輸入寄件者的應用程式密碼，如果要上傳記得先拿掉或設置環境變數
	String senderPassword;
	// 收件人資料
	String recipientEmail;

	// 註冊成功發送通知信件
	public static void sentSignUpMail(String email, String authCode, String temporaryPassword) {
		System.out.println("email:" + email);
		// 寄件人
		String senderName = "mark";
		String senderEmail = "mark29737148mark@gmail.com"; // email填入自己的email
		String senderPassword = "fbqevgkcqjzdeduo"; // pwd填入向google申請的應用程式密碼
		// 收件人
		String recipientEmail = email; // 輸入要寄送郵件對象的email

		// 設定SMTP
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com"); // 以 Gmail 為例
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // 指定協議

		// 創建 Session
		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(senderEmail, senderPassword);
			}
		});

		try {
			// 設定 MimeMessage
			Message message = new MimeMessage(session);

			// 設定寄件人
			message.setFrom(new InternetAddress(senderEmail, senderName));

			// 設定收件人
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

			// 設定信件主題
			message.setSubject("註冊成功");

			// 設定信件內容
			message.setText("驗證碼:" + authCode + "\n暫時密碼:" + temporaryPassword + "\n請於登入後修改密碼");

			// 發送信件
			Transport.send(message);

			System.out.println("發送成功!!!");

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("發送失敗!!!");
		}
	}
	
	public static void sentLeaveApplyMail(String reviewerEmail, int serialNo) {
		String senderName = "Y.T";
		String senderEmail = "mark29737148mark@gmail.com"; // email填入自己的email
		String senderPassword = "fbqevgkcqjzdeduo"; // pwd填入向google申請的應用程式密碼
		String recipientEmail = reviewerEmail; // 輸入要寄送郵件對象的email
		
		// 設定SMTP
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com"); // 以 Gmail 為例
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // 指定協議
		
		// 創建 Session
		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(senderEmail, senderPassword);
			}
		});
		
		try {
			// 設定 MimeMessage
			Message message = new MimeMessage(session);

			// 設定寄件人
			message.setFrom(new InternetAddress(senderEmail, senderName));

			// 設定收件人
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

			// 設定信件主題
			message.setSubject("假單待審核");

			// 設定信件內容
			message.setText("假單("+serialNo+")待審核");

			// 發送信件
			Transport.send(message);

			System.out.println("發送成功!!!");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("發送失敗!!!");
		}
		
	}

}
