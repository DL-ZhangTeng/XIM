package com.githang.androidcrash.util;

/**
 * Created by swing on 2018/6/27.
 */
public class MailUtils {
//    /**
//     * 以文本格式发送邮件
//     *
//     * @param mailInfo 待发送的邮件的信息
//     */
//    public static boolean sendTextMail(MailSenderInfo mailInfo, String str_file_path) {
//        // 判断是否需要身份认证
//        MyAuthenticator authenticator = null;
//        Properties pro = mailInfo.getProperties();
//        if (mailInfo.isValidate()) {
//            // 如果需要身份认证，则创建一个密码验证器
//            authenticator = new MyAuthenticator(mailInfo.getUsername(), mailInfo.getPassword());
//        }
//        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
//        Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
//        try {
//            // 根据session创建一个邮件消息
//            Message mailMessage = new MimeMessage(sendMailSession);
//            // 创建邮件发送者地址
//            Address from = new InternetAddress(mailInfo.getFromAddress());
//            // 设置邮件消息的发送者
//            mailMessage.setFrom(from);
//            // 创建邮件的接收者地址，并设置到邮件消息中
//            Address to = new InternetAddress(mailInfo.getToAddress());
//            mailMessage.setRecipient(Message.RecipientType.TO, to);
//            // 设置邮件消息的主题
//            mailMessage.setSubject(mailInfo.getSubject());
//            // 设置邮件消息发送的时间
//            mailMessage.setSentDate(new Date());
//
//            Multipart allMultipart = new MimeMultipart();
//            //   设置邮件的文本内容
//            BodyPart contentPart = new MimeBodyPart();
//            String mailContent = mailInfo.getContent();
//            contentPart.setText(mailContent);
//            allMultipart.addBodyPart(contentPart);
//
//            if (str_file_path != null) {
//                //添加附件
//                BodyPart messageBodyPart = new MimeBodyPart();
//                DataSource source = new FileDataSource(str_file_path);
//                //添加附件的内容
//                messageBodyPart.setDataHandler(new DataHandler(source));
//                allMultipart.addBodyPart(messageBodyPart);
//            }
//            mailMessage.setContent(allMultipart);
////            mailMessage.saveChanges();
//            // 发送邮件
//            Transport.send(mailMessage);
//            return true;
//        } catch (MessagingException ex) {
//            ex.printStackTrace();
//        }
//        return false;
//    }
}
