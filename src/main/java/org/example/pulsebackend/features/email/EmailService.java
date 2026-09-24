package org.example.pulsebackend.features.email;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import org.example.pulsebackend.features.email.dto.InviteEmailDto;
import org.example.pulsebackend.features.email.dto.WelcomeEmailDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final Resend resend;

    @Value("${app.sender-email}")
    private String senderEmail;


    public EmailService(
            @Value("${resend.api-key}") String resendApiKey
    ) {
        this.resend = new Resend(resendApiKey);
    }


    public static final String WELCOME_TEMPLATE =
            """
            <div style="font-family: system-ui, -apple-system, sans-serif, Arial; font-size: 15px; color: #1e293b; background-color: #f8fafc; padding: 32px 16px; line-height: 1.6;">
              <div style="max-width: 560px; margin: 0 auto; background-color: #ffffff; padding: 32px; border-radius: 12px; border: 1px solid #e2e8f0; box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);">

                <div style="margin-bottom: 20px;">
                  <img src="https://pulse-epicordia.web.app/assets/pulse-logo-D303Lxmm.png"
                       width="45"
                       height="48"
                       alt="Pulse">
                </div>

                <h2 style="font-size: 20px; font-weight: 700; color: #0f172a; margin-top: 0; margin-bottom: 12px;">
                  Welcome to Pulse, {{to_name}}!
                </h2>

                <p style="margin-bottom: 16px;">
                  We're excited to have you on board. Your account has been successfully created,
                  and you're now ready to coordinate tasks, align on daily sprint goals,
                  and collaborate with your team.
                </p>

                <div style="margin: 28px 0;">
                  <a href="{{login_url}}"
                     target="_blank"
                     rel="noopener"
                     style="display: inline-block; text-decoration: none; outline: none; color: #ffffff; background-color: #0f172a; padding: 10px 22px; border-radius: 6px; font-size: 14px; font-weight: 600;">
                    Open Pulse
                  </a>
                </div>

                <p style="font-size: 13px; color: #64748b; margin-bottom: 24px;">
                  If the button above does not work, copy and paste this link into your browser:<br>
                  <a href="{{login_url}}" style="color: #2563eb; word-break: break-all;">
                    {{login_url}}
                  </a>
                </p>

                <p style="margin-bottom: 0;">
                  Best regards,<br>
                  <strong>The Pulse Team</strong>
                </p>

              </div>
            </div>
            """;


    public static final String INVITE_TEMPLATE =
            """
            <div style="font-family: system-ui, -apple-system, sans-serif, Arial; font-size: 15px; color: #1e293b; background-color: #f8fafc; padding: 32px 16px; line-height: 1.6;">
              <div style="max-width: 560px; margin: 0 auto; background-color: #ffffff; padding: 32px; border-radius: 12px; border: 1px solid #e2e8f0; box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);">

                <div style="margin-bottom: 20px;">
                  <img src="https://pulse-epicordia.web.app/assets/pulse-logo-D303Lxmm.png"
                       width="45"
                       height="48"
                       alt="Pulse">
                </div>

                <h2 style="font-size: 20px; font-weight: 700; color: #0f172a; margin-top: 0; margin-bottom: 12px;">
                  You're invited to join {{org_name}}
                </h2>

                <p style="margin-bottom: 16px;">
                  <strong>{{from_name}}</strong> has invited you to collaborate in the
                  <strong>{{org_name}}</strong> workspace on Pulse.
                </p>

                <div style="margin: 28px 0;">
                  <a href="{{invite_link}}"
                     target="_blank"
                     rel="noopener"
                     style="display: inline-block; text-decoration: none; outline: none; color: #ffffff; background-color: #0f172a; padding: 10px 22px; border-radius: 6px; font-size: 14px; font-weight: 600;">
                    Accept Invitation
                  </a>
                </div>

                <div style="background-color: #f8fafc; border: 1px dashed #cbd5e1; border-radius: 6px; padding: 10px 14px; margin-bottom: 20px;">
                  <span style="font-size: 12px; color: #64748b;">
                    Join Token:
                    <code style="font-family: monospace; font-weight: 600; color: #0f172a;">
                      {{token}}
                    </code>
                  </span>
                </div>

                <p style="font-size: 13px; color: #64748b; margin-bottom: 24px;">
                  If the button above does not work, copy and paste this link into your browser:<br>
                  <a href="{{invite_link}}" style="color: #2563eb; word-break: break-all;">
                    {{invite_link}}
                  </a>
                </p>

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

        sendHtmlEmail(
                welcomeEmailDto.recipientEmail(),
                "Welcome to Pulse!",
                htmlBody
        );
    }


    public void sendInviteEmail(InviteEmailDto inviteEmailDto) {

        String htmlBody = INVITE_TEMPLATE
                .replace("{{from_name}}", inviteEmailDto.fromName())
                .replace("{{org_name}}", inviteEmailDto.orgName())
                .replace("{{invite_link}}", inviteEmailDto.inviteLink())
                .replace("{{token}}", inviteEmailDto.token());

        sendHtmlEmail(
                inviteEmailDto.recipientEmail(),
                "You're invited to join "
                        + inviteEmailDto.orgName()
                        + " on Pulse",
                htmlBody
        );
    }


    public void sendHtmlEmail(
            String recipientEmail,
            String subject,
            String htmlBody
    ) {

        try {

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from(senderEmail)
                    .to(recipientEmail)
                    .subject(subject)
                    .html(htmlBody)
                    .build();

            var response = resend.emails().send(params);

            System.out.println(
                    "Email sent successfully to "
                            + recipientEmail
                            + " | ID: "
                            + response.getId()
            );

        } catch (ResendException e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Failed to send email: " + e.getMessage(),
                    e
            );
        }
    }
}