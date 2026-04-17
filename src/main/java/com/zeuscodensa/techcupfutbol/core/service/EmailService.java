package com.zeuscodensa.techcupfutbol.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final String fromAddress;

    public EmailService(JavaMailSender mailSender,
                        @Value("${app.mail.from:noreply@techcup.com}") String fromAddress) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
    }

    @Async
    public void sendWelcomeEmail(String toEmail, String userName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromAddress);
            helper.setTo(toEmail);
            helper.setSubject("¡Bienvenido a TechCup Fútbol 7! ⚽");
            helper.setText(buildWelcomeHtml(userName), true);

            mailSender.send(message);
            log.info("Welcome email sent to {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send welcome email to {}: {}", toEmail, e.getMessage());
        }
    }

    @Async
    public void sendPasswordResetEmail(String toEmail, String userName, String resetToken) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromAddress);
            helper.setTo(toEmail);
            helper.setSubject("Restablece tu contraseña — TechCup Fútbol 7");
            helper.setText(buildPasswordResetHtml(userName, resetToken), true);

            mailSender.send(message);
            log.info("Password reset email sent to {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send password reset email to {}: {}", toEmail, e.getMessage());
        }
    }

    @Async
    public void sendLoginNotificationEmail(String toEmail, String userName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromAddress);
            helper.setTo(toEmail);
            helper.setSubject("Nuevo inicio de sesión en TechCup ⚽");
            helper.setText(buildLoginNotificationHtml(userName), true);

            mailSender.send(message);
            log.info("Login notification email sent to {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send login notification email to {}: {}", toEmail, e.getMessage());
        }
    }

    private String buildWelcomeHtml(String userName) {
        return """
            <!DOCTYPE html>
            <html lang="es">
            <head>
              <meta charset="UTF-8"/>
              <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
            </head>
            <body style="margin:0;padding:0;background:#0f0f0f;font-family:'Helvetica Neue',Arial,sans-serif;">
              <table width="100%%" cellpadding="0" cellspacing="0" style="background:#0f0f0f;padding:40px 0;">
                <tr><td align="center">
                  <table width="600" cellpadding="0" cellspacing="0" style="background:#1a1a1a;border-radius:16px;overflow:hidden;border:1px solid #2a2a2a;">
                    <!-- Header -->
                    <tr>
                      <td style="background:linear-gradient(135deg,#15803d,#65a30d);padding:40px 40px 32px;text-align:center;">
                        <h1 style="margin:0;color:#ffffff;font-size:32px;font-weight:900;letter-spacing:-1px;">
                          ⚽ TECH<span style="color:#d9f99d;">CUP</span>
                        </h1>
                        <p style="margin:8px 0 0;color:#bbf7d0;font-size:13px;letter-spacing:2px;text-transform:uppercase;">Fútbol 7 Universitario</p>
                      </td>
                    </tr>
                    <!-- Body -->
                    <tr>
                      <td style="padding:40px;">
                        <h2 style="color:#f4f4f5;font-size:22px;margin:0 0 12px;">¡Hola, %s! 👋</h2>
                        <p style="color:#a1a1aa;font-size:15px;line-height:1.7;margin:0 0 24px;">
                          Tu cuenta en <strong style="color:#84cc16;">TechCup Fútbol 7</strong> ha sido creada exitosamente.
                          Ya puedes acceder a la plataforma, buscar equipo, ver torneos y mucho más.
                        </p>
                        <table cellpadding="0" cellspacing="0" width="100%%">
                          <tr>
                            <td align="center" style="padding:8px 0 32px;">
                              <a href="https://zeus-codensa-front-end-yuap.vercel.app/auth/login"
                                 style="display:inline-block;background:#84cc16;color:#0f0f0f;font-weight:700;font-size:15px;
                                        padding:14px 36px;border-radius:10px;text-decoration:none;letter-spacing:0.5px;">
                                Ir a TechCup →
                              </a>
                            </td>
                          </tr>
                        </table>
                        <hr style="border:none;border-top:1px solid #2a2a2a;margin:0 0 24px;"/>
                        <p style="color:#52525b;font-size:12px;margin:0;text-align:center;">
                          Si no creaste esta cuenta, puedes ignorar este correo.<br/>
                          © 2026 TechCup · Escuela Colombiana de Ingeniería
                        </p>
                      </td>
                    </tr>
                  </table>
                </td></tr>
              </table>
            </body>
            </html>
            """.formatted(sanitizeForLog(userName));
    }

    private String buildPasswordResetHtml(String userName, String resetToken) {
        return """
            <!DOCTYPE html>
            <html lang="es">
            <head><meta charset="UTF-8"/></head>
            <body style="margin:0;padding:0;background:#0f0f0f;font-family:'Helvetica Neue',Arial,sans-serif;">
              <table width="100%%" cellpadding="0" cellspacing="0" style="background:#0f0f0f;padding:40px 0;">
                <tr><td align="center">
                  <table width="600" cellpadding="0" cellspacing="0" style="background:#1a1a1a;border-radius:16px;overflow:hidden;border:1px solid #2a2a2a;">
                    <tr>
                      <td style="background:linear-gradient(135deg,#15803d,#65a30d);padding:40px;text-align:center;">
                        <h1 style="margin:0;color:#fff;font-size:28px;font-weight:900;">⚽ TECH<span style="color:#d9f99d;">CUP</span></h1>
                      </td>
                    </tr>
                    <tr>
                      <td style="padding:40px;">
                        <h2 style="color:#f4f4f5;font-size:20px;margin:0 0 12px;">Restablecer contraseña</h2>
                        <p style="color:#a1a1aa;font-size:15px;line-height:1.7;margin:0 0 24px;">
                          Hola <strong style="color:#e4e4e7;">%s</strong>, recibimos una solicitud para restablecer tu contraseña.
                          Usa el código de abajo — es válido por <strong style="color:#84cc16;">30 minutos</strong>.
                        </p>
                        <div style="background:#0f0f0f;border:1px solid #2a2a2a;border-radius:10px;padding:20px;text-align:center;margin:0 0 24px;">
                          <p style="margin:0;color:#84cc16;font-size:32px;font-weight:900;letter-spacing:8px;font-family:monospace;">%s</p>
                        </div>
                        <p style="color:#52525b;font-size:12px;margin:0;text-align:center;">
                          Si no solicitaste esto, ignora este correo.<br/>© 2026 TechCup
                        </p>
                      </td>
                    </tr>
                  </table>
                </td></tr>
              </table>
            </body>
            </html>
            """.formatted(sanitizeForLog(userName), resetToken);
    }

    // NOSONAR — sanitize input before embedding in log messages (CWE-117)
    private String sanitizeForLog(String input) {
        if (input == null) return "Usuario";
        return input.replaceAll("[\r\n]", " ").trim();
    }

    private String buildLoginNotificationHtml(String userName) {
        String time = java.time.ZonedDateTime.now(java.time.ZoneId.of("America/Bogota"))
                .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy 'a las' HH:mm 'COT'"));
        return """
            <!DOCTYPE html>
            <html lang="es">
            <head><meta charset="UTF-8"/></head>
            <body style="margin:0;padding:0;background:#0f0f0f;font-family:'Helvetica Neue',Arial,sans-serif;">
              <table width="100%%" cellpadding="0" cellspacing="0" style="background:#0f0f0f;padding:40px 0;">
                <tr><td align="center">
                  <table width="600" cellpadding="0" cellspacing="0" style="background:#1a1a1a;border-radius:16px;overflow:hidden;border:1px solid #2a2a2a;">
                    <tr>
                      <td style="background:linear-gradient(135deg,#15803d,#65a30d);padding:36px 40px;text-align:center;">
                        <h1 style="margin:0;color:#fff;font-size:28px;font-weight:900;">⚽ TECH<span style="color:#d9f99d;">CUP</span></h1>
                        <p style="margin:8px 0 0;color:#bbf7d0;font-size:12px;letter-spacing:2px;text-transform:uppercase;">Alerta de seguridad</p>
                      </td>
                    </tr>
                    <tr>
                      <td style="padding:40px;">
                        <h2 style="color:#f4f4f5;font-size:20px;margin:0 0 8px;">Nuevo inicio de sesión detectado 🔐</h2>
                        <p style="color:#a1a1aa;font-size:15px;line-height:1.7;margin:0 0 24px;">
                          Hola <strong style="color:#e4e4e7;">%s</strong>, tu cuenta fue accedida el <strong style="color:#84cc16;">%s</strong>.
                        </p>
                        <div style="background:#0f0f0f;border:1px solid #2a2a2a;border-radius:12px;padding:20px;margin:0 0 24px;">
                          <p style="margin:0 0 8px;color:#71717a;font-size:12px;text-transform:uppercase;letter-spacing:1px;">Detalles del acceso</p>
                          <p style="margin:0;color:#e4e4e7;font-size:14px;">🕐 <strong>Fecha:</strong> %s</p>
                          <p style="margin:8px 0 0;color:#e4e4e7;font-size:14px;">📍 <strong>Plataforma:</strong> TechCup Fútbol 7</p>
                        </div>
                        <div style="background:#7c2d12;border:1px solid #9a3412;border-radius:10px;padding:16px;margin:0 0 24px;">
                          <p style="margin:0;color:#fed7aa;font-size:13px;line-height:1.6;">
                            ⚠️ Si <strong>no fuiste tú</strong>, cambia tu contraseña inmediatamente y contacta al administrador.
                          </p>
                        </div>
                        <hr style="border:none;border-top:1px solid #2a2a2a;margin:0 0 20px;"/>
                        <p style="color:#52525b;font-size:12px;margin:0;text-align:center;">
                          © 2026 TechCup · Escuela Colombiana de Ingeniería
                        </p>
                      </td>
                    </tr>
                  </table>
                </td></tr>
              </table>
            </body>
            </html>
            """.formatted(sanitizeForLog(userName), time, time);
    }
}
