package org.example.cafemanager.services;

import org.example.cafemanager.Util;
import org.example.cafemanager.dto.user.CreateUserRequest;
import org.example.cafemanager.services.communication.impls.EmailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.mail.MailSender;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JmsTemplate jmsTemplate;

    @Mock
    private MailSender mailSender;

    @Test(expected = IllegalArgumentException.class)
    public void testNotifyWithInvalidArguments() {
        emailService.notify(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotifyWithInvalidEmail() {
        CreateUserRequest u = new CreateUserRequest();
        emailService.notify(u);

    }

    @Test
    public void testNotify() {
        String testText = Util.randomString(5);
        CreateUserRequest u = new CreateUserRequest(testText, testText, testText + "@mail.mail");
        emailService.notify(u);
        Mockito.verify(jmsTemplate, Mockito.only()).convertAndSend("mailbox", u);
    }
}
