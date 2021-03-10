package ru.netology.patient.service.alert;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class SendAlertServiceImpl_Test {
    @Test
    public void send_Test() {
        // Given:
        String message = "Warning, patient with ... need help";
        SendAlertServiceImpl sendAlert = Mockito.mock(SendAlertServiceImpl.class);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        // When:
        sendAlert.send(message);
        Mockito.verify(sendAlert).send(captor.capture());

        // Then:
        assertEquals(message, captor.getValue());
    }
}
