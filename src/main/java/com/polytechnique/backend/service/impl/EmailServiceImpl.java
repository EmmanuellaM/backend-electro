package com.polytechnique.backend.service.impl;

import com.polytechnique.backend.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from:noreply@maternicare.cm}")
    private String fromEmail;

    @Value("${app.mail.from-name:MaterniCare}")
    private String fromName;

    @Override
    public void sendPasswordResetCode(String to, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setTo(to);
            helper.setSubject("🔐 Code de réinitialisation - MaterniCare");

            String htmlContent = buildPasswordResetEmail(code);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Email de réinitialisation envoyé à {}", to);

        } catch (MessagingException | java.io.UnsupportedEncodingException e) {
            log.error("Erreur lors de l'envoi de l'email à {}: {}", to, e.getMessage());
            throw new RuntimeException("Erreur lors de l'envoi de l'email", e);
        }
    }

    private String buildPasswordResetEmail(String code) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                </head>
                <body style="margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f5f5f5;">
                    <table role="presentation" style="width: 100%%; border-collapse: collapse;">
                        <tr>
                            <td align="center" style="padding: 40px 0;">
                                <table role="presentation" style="width: 100%%; max-width: 600px; border-collapse: collapse; background-color: #ffffff; border-radius: 16px; box-shadow: 0 4px 20px rgba(0,0,0,0.1);">
                                    <!-- Header -->
                                    <tr>
                                        <td style="padding: 40px 40px 20px; text-align: center; background: linear-gradient(135deg, #ec4899 0%%, #8b5cf6 100%%); border-radius: 16px 16px 0 0;">
                                            <h1 style="margin: 0; color: #ffffff; font-size: 28px; font-weight: 700;">
                                                MaterniCare
                                            </h1>
                                            <p style="margin: 10px 0 0; color: rgba(255,255,255,0.9); font-size: 14px;">
                                                Surveillance prénatale intelligente
                                            </p>
                                        </td>
                                    </tr>

                                    <!-- Content -->
                                    <tr>
                                        <td style="padding: 40px;">
                                            <h2 style="margin: 0 0 20px; color: #1f2937; font-size: 24px; font-weight: 600;">
                                                Réinitialisation de mot de passe
                                            </h2>
                                            <p style="margin: 0 0 30px; color: #6b7280; font-size: 16px; line-height: 1.6;">
                                                Vous avez demandé à réinitialiser votre mot de passe. Utilisez le code ci-dessous pour continuer :
                                            </p>

                                            <!-- Code Box -->
                                            <div style="text-align: center; margin: 30px 0;">
                                                <div style="display: inline-block; background: linear-gradient(135deg, #fdf2f8 0%%, #f5f3ff 100%%); border: 2px solid #ec4899; border-radius: 12px; padding: 20px 40px;">
                                                    <span style="font-size: 36px; font-weight: 700; letter-spacing: 8px; color: #7c3aed; font-family: 'Courier New', monospace;">
                                                        %s
                                                    </span>
                                                </div>
                                            </div>

                                            <p style="margin: 30px 0 0; color: #9ca3af; font-size: 14px; text-align: center;">
                                                Ce code expire dans <strong>15 minutes</strong>.
                                            </p>

                                            <hr style="border: none; border-top: 1px solid #e5e7eb; margin: 30px 0;">

                                            <p style="margin: 0; color: #9ca3af; font-size: 13px; line-height: 1.5;">
                                                Si vous n'avez pas demandé cette réinitialisation, vous pouvez ignorer cet email en toute sécurité.
                                            </p>
                                        </td>
                                    </tr>

                                    <!-- Footer -->
                                    <tr>
                                        <td style="padding: 20px 40px; background-color: #f9fafb; border-radius: 0 0 16px 16px; text-align: center;">
                                            <p style="margin: 0; color: #9ca3af; font-size: 12px;">
                                                2026 MaterniCare - Tous droits réservés
                                            </p>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """
                .formatted(code);
    }
}
