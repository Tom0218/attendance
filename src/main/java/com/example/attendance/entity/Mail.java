package com.example.attendance.entity;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;

import javax.mail.PasswordAuthentication;

public class Mail {

	String senderName;
	String senderEmail;
	// ��J�H��̪����ε{���K�X�A�p�G�n�W�ǰO�o�������γ]�m�����ܼ�
	String senderPassword;
	// ����H���
	String recipientEmail;

	// ���U���\�o�e�q���H��
	public static void sentSignUpMail(String email, String authCode, String temporaryPassword) {
		System.out.println("email:" + email);
		// �H��H
		String senderName = "mark";
		String senderEmail = "mark29737148mark@gmail.com"; // email��J�ۤv��email
		String senderPassword = "fbqevgkcqjzdeduo"; // pwd��J�Vgoogle�ӽЪ����ε{���K�X
		// ����H
		String recipientEmail = email; // ��J�n�H�e�l���H��email

		// �]�wSMTP
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com"); // �H Gmail ����
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // ���w��ĳ

		// �Ы� Session
		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(senderEmail, senderPassword);
			}
		});

		try {
			// �]�w MimeMessage
			Message message = new MimeMessage(session);

			// �]�w�H��H
			message.setFrom(new InternetAddress(senderEmail, senderName));

			// �]�w����H
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

			// �]�w�H��D�D
			message.setSubject("���U���\");

			// �]�w�H�󤺮e
			message.setText("���ҽX:" + authCode + "\n�ȮɱK�X:" + temporaryPassword + "\n�Щ�n�J��ק�K�X");

			// �o�e�H��
			Transport.send(message);

			System.out.println("�o�e���\!!!");

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("�o�e����!!!");
		}
	}
	
	public static void sentLeaveApplyMail(String reviewerEmail, int serialNo) {
		String senderName = "Y.T";
		String senderEmail = "mark29737148mark@gmail.com"; // email��J�ۤv��email
		String senderPassword = "fbqevgkcqjzdeduo"; // pwd��J�Vgoogle�ӽЪ����ε{���K�X
		String recipientEmail = reviewerEmail; // ��J�n�H�e�l���H��email
		
		// �]�wSMTP
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com"); // �H Gmail ����
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // ���w��ĳ
		
		// �Ы� Session
		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(senderEmail, senderPassword);
			}
		});
		
		try {
			// �]�w MimeMessage
			Message message = new MimeMessage(session);

			// �]�w�H��H
			message.setFrom(new InternetAddress(senderEmail, senderName));

			// �]�w����H
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

			// �]�w�H��D�D
			message.setSubject("����ݼf��");

			// �]�w�H�󤺮e
			message.setText("����("+serialNo+")�ݼf��");

			// �o�e�H��
			Transport.send(message);

			System.out.println("�o�e���\!!!");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("�o�e����!!!");
		}
		
	}

}
