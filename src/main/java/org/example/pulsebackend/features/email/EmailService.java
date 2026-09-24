package org.example.pulsebackend.features.email;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.example.pulsebackend.features.email.dto.InviteEmailDto;
import org.example.pulsebackend.features.email.dto.WelcomeEmailDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {

    @Value("${app.sender-email}")
     String senderEmail;
    @Value("${app.password}")
     String appPassword;

    public static final String WELCOME_TEMPLATE =
            """
            <div style="font-family: system-ui, -apple-system, sans-serif, Arial; font-size: 15px; color: #1e293b; background-color: #f8fafc; padding: 32px 16px; line-height: 1.6;">
              <div style="max-width: 560px; margin: 0 auto; background-color: #ffffff; padding: 32px; border-radius: 12px; border: 1px solid #e2e8f0; box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);">
            
                <!-- Keep your existing logo tag here -->
                <div style="margin-bottom: 20px;">
                  <img src="https://pulse-epicordia.web.app/assets/pulse-logo-D303Lxmm.png" width="45" height="48" alt="Pulse">
                </div>
            
                <!-- Personalized Heading -->
                <h2 style="font-size: 20px; font-weight: 700; color: #0f172a; margin-top: 0; margin-bottom: 12px;">
                  Welcome to Pulse, {{to_name}}!
                </h2>
            
                <p style="margin-bottom: 16px;">
                  We're excited to have you on board. Your account has been successfully created, and you're now ready to coordinate tasks, align on daily sprint goals, and collaborate with your team.
                </p>
            
                <!-- Dynamic Button linking to {{login_url}} -->
                <div style="margin: 28px 0;">
                  <a href="{{login_url}}" target="_blank" rel="noopener" style="display: inline-block; text-decoration: none; outline: none; color: #ffffff; background-color: #0f172a; padding: 10px 22px; border-radius: 6px; font-size: 14px; font-weight: 600;">
                    Open Pulse;
                  </a>
                </div>
            
                <p style="font-size: 13px; color: #64748b; margin-bottom: 24px;">
                  If the button above does not work, copy and paste this link into your browser:<br>
                  <a href="{{login_url}}" style="color: #2563eb; word-break: break-all;">{{login_url}}</a>
                </p>
            
                <!-- Sign-off -->
                <p style="margin-bottom: 0;">
                  Best regards,<br>
                  <strong>The Pulse Team</strong>
                </p>
              </div>
            </div>
""";


    public static final String INVITE_TEMPLATE = """
            <div style="font-family: system-ui, -apple-system, sans-serif, Arial; font-size: 15px; color: #1e293b; background-color: #f8fafc; padding: 32px 16px; line-height: 1.6;">
              <div style="max-width: 560px; margin: 0 auto; background-color: #ffffff; padding: 32px; border-radius: 12px; border: 1px solid #e2e8f0; box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);">
            
                <!-- Paste your Pulse base64 logo tag here -->
                <div style="margin-bottom: 20px;">
                  <img src="https://pulse-epicordia.web.app/assets/pulse-logo-D303Lxmm.png" width="45" height="48" alt="Pulse">
                </div>
            
                <!-- Heading -->
                <h2 style="font-size: 20px; font-weight: 700; color: #0f172a; margin-top: 0; margin-bottom: 12px;">
                  You're invited to join {{org_name}}
                </h2>
            
                <p style="margin-bottom: 16px;">
                  <strong>{{from_name}}</strong> has invited you to collaborate in the <strong>{{org_name}}</strong> workspace on Pulse.
                </p>
            
                <!-- Accept Invite Button -->
                <div style="margin: 28px 0;">
                  <a href="{{invite_link}}" target="_blank" rel="noopener" style="display: inline-block; text-decoration: none; outline: none; color: #ffffff; background-color: #0f172a; padding: 10px 22px; border-radius: 6px; font-size: 14px; font-weight: 600;">
                    Accept Invitation;
                  </a>
                </div>
            
                <!-- Token reference box -->
                <div style="background-color: #f8fafc; border: 1px dashed #cbd5e1; border-radius: 6px; padding: 10px 14px; margin-bottom: 20px;">
                  <span style="font-size: 12px; color: #64748b;">
                    Join Token: <code style="font-family: monospace; font-weight: 600; color: #0f172a;">{{token}}</code>
                  </span>
                </div>
            
                <p style="font-size: 13px; color: #64748b; margin-bottom: 24px;">
                  If the button above does not work, copy and paste this link into your browser:<br>
                  <a href="{{invite_link}}" style="color: #2563eb; word-break: break-all;">{{invite_link}}</a>
                </p>
            
                <!-- Sign-off -->
                <p style="margin-bottom: 0;">
                  Best regards,<br>
                  <strong>The Pulse Team</strong>
                </p>
              </div>
            </div>
            """;

    public void sendWelcomeEmail(WelcomeEmailDto welcomeEmailDto) {
        String htmlBody = WELCOME_TEMPLATE
                .replace("{{to_name}}", welcomeEmailDto.recipientName())
                .replace("{{login_url}}", welcomeEmailDto.loginUrl());

        sendHtmlEmail(welcomeEmailDto.recipientEmail(), "Welcome to Pulse!", htmlBody);
    }

    public void sendInviteEmail(InviteEmailDto inviteEmailDto) {
        String htmlBody = INVITE_TEMPLATE
                .replace("{{from_name}}", inviteEmailDto.fromName())
                .replace("{{org_name}}", inviteEmailDto.orgName())
                .replace("{{invite_link}}", inviteEmailDto.inviteLink())
                .replace("{{token}}", inviteEmailDto.token());

        sendHtmlEmail(inviteEmailDto.recipientEmail(), "You're invited to join " + inviteEmailDto.orgName()  + " on Pulse", htmlBody);
    }

    public  void sendHtmlEmail(String recipientEmail, String subject, String htmlBody) {


        // 1. Mail server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // 2. Create session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, appPassword);
            }
        });

        try {
            // 3. Compose the message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);

            // Set the content as HTML
            message.setContent(htmlBody, "text/html; charset=utf-8");

            // 4. Send the email
            Transport.send(message);
            System.out.println("HTML Email sent successfully to " + recipientEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send HTML email: " + e.getMessage());
        }
    }
}