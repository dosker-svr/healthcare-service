package ru.netology.patient.service.medical;


import org.junit.Test;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.service.alert.SendAlertServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicalServiceImpl_Test {

    @Test
    public void checkBloodPressureWithAlert_Test() {
        // Given:
        String id1 = "123456";
        String alertMessage = String.format("Warning, patient with id: %s, need help", id1);

        BloodPressure bloodPressureNORMAL = new BloodPressure(120, 80);
        BloodPressure bloodPressureWithWARNING = new BloodPressure(100, 20);

        HealthInfo healthInfo = new HealthInfo(new BigDecimal("36.65"), bloodPressureNORMAL);
        PatientInfo patientInfo = new PatientInfo(
                id1, "Роберто", "Васькин",
                LocalDate.of(1980, 11, 26),
                healthInfo
        );

        PatientInfoFileRepository fileRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(fileRepository.getById(id1)).thenReturn(patientInfo);

        SendAlertServiceImpl alertService =  Mockito.spy(SendAlertServiceImpl.class);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(fileRepository, alertService);

        // When:
        medicalService.checkBloodPressure(id1, bloodPressureWithWARNING);
        Mockito.verify(alertService, Mockito.times(1)).send(captor.capture());


        // Then:
        Assertions.assertEquals(alertMessage, captor.getValue());
    }

    @Test
    public void checkTemperatureWithAlert_Test() {
        // Given:
        String id1 = "123456";
        String alertMessage = String.format("Warning, patient with id: %s, need help", id1);

        BigDecimal normalTemperature = new BigDecimal("36.65");
        BigDecimal warningTemperature = new BigDecimal("0");

        HealthInfo healthInfo = new HealthInfo(normalTemperature, new BloodPressure(120, 80));
        PatientInfo patientInfo = new PatientInfo(
                id1, "Роберто", "Васькин",
                LocalDate.of(1980, 11, 26),
                healthInfo
        );

        PatientInfoFileRepository fileRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(fileRepository.getById(id1)).thenReturn(patientInfo);

        SendAlertServiceImpl alertService =  Mockito.spy(SendAlertServiceImpl.class);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(fileRepository, alertService);

        // When:
        medicalService.checkTemperature(id1, warningTemperature);
        Mockito.verify(alertService, Mockito.times(1)).send(captor.capture());


        // Then:
        Assertions.assertEquals(alertMessage, captor.getValue());
    }

    @Test
    public void checkBloodPressureWithOUTAlert_Test() {
        // Given:
        String id1 = "123456";

        BloodPressure normalBloodPressure = new BloodPressure(120, 80);
        BloodPressure otherBloodPressure = new BloodPressure(120, 80);

        HealthInfo healthInfo = new HealthInfo(new BigDecimal("36.65"), normalBloodPressure);
        PatientInfo patientInfo = new PatientInfo(
                id1, "Роберто", "Васькин",
                LocalDate.of(1980, 11, 26),
                healthInfo
        );

        PatientInfoFileRepository fileRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(fileRepository.getById(id1)).thenReturn(patientInfo);

        SendAlertServiceImpl alertService =  Mockito.spy(SendAlertServiceImpl.class);
//        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(fileRepository, alertService);

        // When:
        medicalService.checkBloodPressure(id1, otherBloodPressure);

        // Then:
        Mockito.verify(alertService, Mockito.never()).send(Mockito.any());
    }

    @Test
    public void checkTemperatureWithOUTAlert_Test() {
        // Given:
        String id1 = "123456";

        BigDecimal normalTemperature = new BigDecimal("36.65");
        BigDecimal warningTemperature = new BigDecimal("36.65");

        HealthInfo healthInfo = new HealthInfo(normalTemperature, new BloodPressure(120, 80));
        PatientInfo patientInfo = new PatientInfo(
                id1, "Роберто", "Васькин",
                LocalDate.of(1980, 11, 26),
                healthInfo
        );

        PatientInfoFileRepository fileRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(fileRepository.getById(id1)).thenReturn(patientInfo);

        SendAlertServiceImpl alertService =  Mockito.spy(SendAlertServiceImpl.class);
//        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(fileRepository, alertService);

        // When:
        medicalService.checkTemperature(id1, warningTemperature);

        // Then:
        Mockito.verify(alertService, Mockito.never()).send(Mockito.any());
    }
}
