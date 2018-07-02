package com.githang.androidcrash.reporter.mailreporter;

import android.util.Log;

import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
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

/**
 * 邮件类，用于构造要发送的邮件。
 */
public class LogMail extends Authenticator {
    private String host;
    private String port;
    private String user;
    private String pass;
    private String from;
    private String to;
    private String subject;
    private String body;

    private Multipart multipart;
    private Properties props;

    public LogMail() {
    }

    /**
     * @param user    邮箱登录名
     * @param pass    邮箱登录密码
     * @param from    发件人
     * @param to      收件人
     * @param host    主机
     * @param port    SMTP端口号
     * @param subject 邮件主题
     * @param body    邮件正文
     */
    public LogMail(String user, String pass, String from, String to, String host, String port,
                   String subject, String body) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    public LogMail setHost(String host) {
        this.host = host;
        return this;
    }

    public LogMail setPort(String port) {
        this.port = port;
        return this;
    }

    public LogMail setUser(String user) {
        this.user = user;
        return this;
    }

    public LogMail setPass(String pass) {
        this.pass = pass;
        return this;
    }

    public LogMail setFrom(String from) {
        this.from = from;
        return this;
    }

    public LogMail setTo(String to) {
        this.to = to;
        return this;
    }

    public LogMail setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public LogMail setBody(String body) {
        this.body = body;
        return this;
    }

    /**
     * 初始化。它在设置好用户名、密码、发件人、收件人、主题、正文、主机及端口号之后显示调用。
     */
    public void init() {
        multipart = new MimeMultipart();
        // There is something wrong with MailCap, javamail can not find a
        // handler for the multipart/mixed part, so this bit needs to be added.
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);

        props = new Properties();

        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
    }

    /**
     * 发送邮件
     *
     * @return 是否发送成功
     * @throws MessagingException
     */
    public boolean send() throws MessagingException {
        if (!user.equals("") && !pass.equals("") && !to.equals("") && !from.equals("")) {
            Session session = Session.getDefaultInstance(props, this);
            Log.d("SendUtil", host + "..." + port + ".." + user + "..." + pass);

            MimeMessage msg = new MimeMessage(session);
            //抄送一份给自己（避免邮箱检查垃圾邮件发送失败，报544）
            msg.addRecipients(Message.RecipientType.CC, InternetAddress.parse(from));
            msg.setFrom(new InternetAddress(from));

            InternetAddress addressTo = new InternetAddress(to);
            msg.setRecipient(MimeMessage.RecipientType.TO, addressTo);

            msg.setSubject(subject);
            msg.setSentDate(new Date());

            // setup message body
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);

            multipart.addBodyPart(messageBodyPart, 0);

            // Put parts in message
            msg.setContent(multipart);

            // send email
            Transport.send(msg);

            return true;
        } else {
            return false;
        }
    }

    /**
     * 添加附件
     *
     * @param filePath 附件路径
     * @param fileName 附件名称
     * @throws Exception
     */
    public void addAttachment(String filePath, String fileName) throws Exception {
        BodyPart messageBodyPart = new MimeBodyPart();
        ((MimeBodyPart) messageBodyPart).attachFile(filePath);
        multipart.addBodyPart(messageBodyPart);
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, pass);
    }
}