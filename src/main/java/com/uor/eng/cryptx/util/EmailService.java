package com.uor.eng.cryptx.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

  @Autowired
  private JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String fromEmail;

//  public void sendBookingConfirmation(BookingResponseDTO bookingDetails) {
//    sendEmail("templates/booking-confirmation.html", "Appointment Confirmation - Reference ID: " + bookingDetails.getReferenceId(), bookingDetails);
//  }
//
//  public void sendBookingCancellation(BookingResponseDTO bookingDetails) {
//    sendEmail("templates/booking-cancellation.html", "Appointment Cancellation - Reference ID: " + bookingDetails.getReferenceId(), bookingDetails);
//  }
//
//  public void sendBookingActivation(BookingResponseDTO bookingDetails) {
//    sendEmail("templates/booking-activation.html", "Appointment Activation - Reference ID: " + bookingDetails.getReferenceId(), bookingDetails);
//  }
//
//  private void sendEmail(String templatePath, String subject, BookingResponseDTO bookingDetails) {
//    try {
//      String template = loadTemplate(templatePath);
//      String htmlContent = populateTemplate(template, bookingDetails);
//
//      MimeMessage message = mailSender.createMimeMessage();
//      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//
//      helper.setFrom(fromEmail);
//      helper.setTo(bookingDetails.getEmail());
//      helper.setSubject(subject);
//      helper.setText(htmlContent, true);
//
//      mailSender.send(message);
//      System.out.println("Email sent successfully to " + bookingDetails.getEmail() + " with subject: " + subject);
//    } catch (MessagingException | IOException e) {
//      System.err.println("Failed to send email (" + subject + ") to " + bookingDetails.getEmail() + ": " + e.getMessage());
//      throw new EmailSendingException("Failed to send email to " + bookingDetails.getEmail());
//    }
//  }

  private String loadTemplate(String templatePath) throws IOException {
    ClassPathResource classPathResource = new ClassPathResource(templatePath);
    try (InputStream inputStream = classPathResource.getInputStream();
         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
      StringBuilder content = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        content.append(line).append("\n");
      }
      return content.toString();
    }
  }

//  private String populateTemplate(String template, BookingResponseDTO bookingDetails) {
//    Map<String, String> placeholders = new HashMap<>();
//    placeholders.put("{{name}}", bookingDetails.getName() != null ? bookingDetails.getName() : "Guest");
//    placeholders.put("{{referenceId}}", bookingDetails.getReferenceId() != null ? bookingDetails.getReferenceId() : "N/A");
//    placeholders.put("{{appointmentNumber}}", bookingDetails.getAppointmentNumber() != null ? String.valueOf(bookingDetails.getAppointmentNumber()) : "N/A");
//    placeholders.put("{{scheduleDate}}", bookingDetails.getScheduleDate() != null ? bookingDetails.getScheduleDate().toString() : "N/A");
//    placeholders.put("{{scheduleDayOfWeek}}", bookingDetails.getScheduleDayOfWeek() != null ? bookingDetails.getScheduleDayOfWeek() : "N/A");
//    placeholders.put("{{scheduleStartTime}}", bookingDetails.getScheduleStartTime() != null ? bookingDetails.getScheduleStartTime().toString() : "N/A");
//    placeholders.put("{{doctorName}}", bookingDetails.getDoctorName() != null ? bookingDetails.getDoctorName() : "N/A");
//    placeholders.put("{{status}}", bookingDetails.getStatus() != null ? bookingDetails.getStatus().toString() : "N/A");
//    placeholders.put("{{bookingDate}}", bookingDetails.getDate() != null ? bookingDetails.getDate().toString() : "N/A");
//    placeholders.put("{{currentYear}}", String.valueOf(LocalDate.now().getYear()));
//
//    for (Map.Entry<String, String> entry : placeholders.entrySet()) {
//      template = template.replace(entry.getKey(), entry.getValue());
//    }
//
//    return template;
//  }

}
