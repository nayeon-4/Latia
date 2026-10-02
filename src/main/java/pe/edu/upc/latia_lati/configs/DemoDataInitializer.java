package pe.edu.upc.latia_lati.configs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.latia_lati.entities.*;
import pe.edu.upc.latia_lati.repositories.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@Component
@ConditionalOnProperty(name = "latia.demo-data.enabled", havingValue = "true")
public class DemoDataInitializer implements CommandLineRunner {
    private static final String DEMO_USERNAME_PREFIX = "latia.ficticio.";
    private static final String LEGACY_DEMO_USERNAME_PREFIX = "demo.latia.";

    private final IUsersRepository usersRepository;
    private final IHealthProfileRepository healthProfileRepository;
    private final IEmergencyContactRepository emergencyContactRepository;
    private final IFamilyRelationshipRepository familyRelationshipRepository;
    private final IMedicalConditionRepository medicalConditionRepository;
    private final IClinicalRecordRepository clinicalRecordRepository;
    private final IExamResultRepository examResultRepository;
    private final IMedicationsRepository medicationsRepository;
    private final IMedicationTreatmentRepository medicationTreatmentRepository;
    private final IMedicationScheduleRepository medicationScheduleRepository;
    private final IMedicalDocumentsRepository medicalDocumentsRepository;
    private final IClinicalRecordDocumentRepository clinicalRecordDocumentRepository;
    private final PasswordEncoder passwordEncoder;

    public DemoDataInitializer(
            IUsersRepository usersRepository,
            IHealthProfileRepository healthProfileRepository,
            IEmergencyContactRepository emergencyContactRepository,
            IFamilyRelationshipRepository familyRelationshipRepository,
            IMedicalConditionRepository medicalConditionRepository,
            IClinicalRecordRepository clinicalRecordRepository,
            IExamResultRepository examResultRepository,
            IMedicationsRepository medicationsRepository,
            IMedicationTreatmentRepository medicationTreatmentRepository,
            IMedicationScheduleRepository medicationScheduleRepository,
            IMedicalDocumentsRepository medicalDocumentsRepository,
            IClinicalRecordDocumentRepository clinicalRecordDocumentRepository,
            PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.healthProfileRepository = healthProfileRepository;
        this.emergencyContactRepository = emergencyContactRepository;
        this.familyRelationshipRepository = familyRelationshipRepository;
        this.medicalConditionRepository = medicalConditionRepository;
        this.clinicalRecordRepository = clinicalRecordRepository;
        this.examResultRepository = examResultRepository;
        this.medicationsRepository = medicationsRepository;
        this.medicationTreatmentRepository = medicationTreatmentRepository;
        this.medicationScheduleRepository = medicationScheduleRepository;
        this.medicalDocumentsRepository = medicalDocumentsRepository;
        this.clinicalRecordDocumentRepository = clinicalRecordDocumentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        removePreviousDemoData();
        if (usersRepository.findByUsername(DEMO_USERNAME_PREFIX + "01").isPresent()) {
            return;
        }

        List<Users> users = createUsers();
        List<HealthProfile> profiles = createHealthProfiles(users);
        List<MedicalCondition> conditions = medicalConditionRepository.saveAll(List.of(
                new MedicalCondition(null, "Asma persistente leve",
                        "Sintomas ocasionales con ejercicio; sin crisis recientes y con respuesta al inhalador."),
                new MedicalCondition(null, "Migraña episodica",
                        "Cefalea pulsante ocasional acompañada de fotofobia, sin signos neurologicos de alarma."),
                new MedicalCondition(null, "Rinitis alergica estacional",
                        "Congestion y estornudos durante la temporada de polen, sin dificultad respiratoria."),
                new MedicalCondition(null, "Anemia ferropenica leve",
                        "Hemoglobina por debajo del rango esperado; en seguimiento con suplementacion de hierro."),
                new MedicalCondition(null, "Hipertension arterial esencial",
                        "Presion arterial controlada con tratamiento y monitoreo domiciliario.")
        ));
        List<Medications> medications = medicationsRepository.saveAll(List.of(
                new Medications(null, "Salbutamol", "Inhalador presurizado", "100 mcg por dosis"),
                new Medications(null, "Paracetamol", "Tableta", "500 mg"),
                new Medications(null, "Loratadina", "Tableta", "10 mg"),
                new Medications(null, "Sulfato ferroso", "Tableta", "300 mg"),
                new Medications(null, "Losartan", "Tableta", "50 mg")
        ));

        List<ClinicalRecord> clinicalRecords = createClinicalRecords(profiles, conditions);
        createEmergencyContacts(profiles);
        createFamilyRelationships(profiles);
        createExamResults(clinicalRecords);
        List<MedicationTreatment> treatments = createMedicationTreatments(clinicalRecords, medications);
        createMedicationSchedules(treatments);
        List<MedicalDocuments> documents = createMedicalDocuments(profiles);
        createClinicalRecordDocuments(clinicalRecords, documents);
    }

    private void removePreviousDemoData() {
        List<Users> previousDemoUsers = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            usersRepository.findByUsername(LEGACY_DEMO_USERNAME_PREFIX + String.format("%02d", i))
                    .ifPresent(previousDemoUsers::add);
        }
        if (previousDemoUsers.isEmpty()) {
            return;
        }

        List<HealthProfile> profiles = healthProfileRepository.findByOwnerUserIn(previousDemoUsers);
        List<ClinicalRecord> records = profiles.isEmpty()
                ? List.of()
                : clinicalRecordRepository.findByHealthProfileIn(profiles);
        List<MedicalDocuments> documents = profiles.isEmpty()
                ? List.of()
                : medicalDocumentsRepository.findByHealthProfileIn(profiles);
        List<MedicationTreatment> treatments = records.isEmpty()
                ? List.of()
                : medicationTreatmentRepository.findByClinicalRecordIn(records);

        if (!profiles.isEmpty()) {
            emergencyContactRepository.deleteAll(
                    emergencyContactRepository.findByHealthProfileIn(profiles));
            familyRelationshipRepository.deleteAll(
                    familyRelationshipRepository.findByOriginProfileInOrRelativeProfileIn(profiles, profiles));
        }
        if (!records.isEmpty()) {
            examResultRepository.deleteAll(examResultRepository.findByClinicalRecordIn(records));
        }
        if (!treatments.isEmpty()) {
            medicationScheduleRepository.deleteAll(
                    medicationScheduleRepository.findByTreatmentIn(treatments));
        }

        LinkedHashMap<Long, ClinicalRecordDocument> attachedDocuments = new LinkedHashMap<>();
        if (!records.isEmpty()) {
            clinicalRecordDocumentRepository.findByClinicalRecordIn(records)
                    .forEach(link -> attachedDocuments.put(link.getId(), link));
        }
        if (!documents.isEmpty()) {
            clinicalRecordDocumentRepository.findByMedicalDocumentIn(documents)
                    .forEach(link -> attachedDocuments.put(link.getId(), link));
        }
        clinicalRecordDocumentRepository.deleteAll(attachedDocuments.values());

        medicationTreatmentRepository.deleteAll(treatments);
        medicalDocumentsRepository.deleteAll(documents);
        clinicalRecordRepository.deleteAll(records);
        healthProfileRepository.deleteAll(profiles);
        usersRepository.deleteAll(previousDemoUsers);

        removeUnusedPreviousDemoCatalogEntries();
    }

    private void removeUnusedPreviousDemoCatalogEntries() {
        for (String name : List.of("Asma", "Migraña", "Alergia estacional", "Anemia", "Hipertensión")) {
            medicalConditionRepository.findByNameMedicalCondition(name)
                    .filter(condition -> !clinicalRecordRepository.existsByMedicalCondition(condition))
                    .ifPresent(medicalConditionRepository::delete);
        }
        for (String name : List.of("Salbutamol", "Paracetamol", "Loratadina", "Sulfato ferroso", "Losartán")) {
            medicationsRepository.findByNameMedications(name).stream()
                    .filter(medication -> !medicationTreatmentRepository.existsByMedication(medication))
                    .forEach(medicationsRepository::delete);
        }
    }

    private List<Users> createUsers() {
        List<Users> users = new ArrayList<>();
        String[] firstNames = {"Valeria", "Mateo", "Camila", "Diego", "Rosa"};
        String[] lastNames = {"Quiroz", "Quiroz", "Rojas", "Rojas", "Quiroz"};
        for (int i = 1; i <= 5; i++) {
            Users user = new Users();
            user.setFirstName(firstNames[i - 1]);
            user.setLastName(lastNames[i - 1]);
            user.setUsername(DEMO_USERNAME_PREFIX + String.format("%02d", i));
            user.setEmail(firstNames[i - 1].toLowerCase() + "." + lastNames[i - 1].toLowerCase()
                    + "@example.com");
            user.setPasswordHash(passwordEncoder.encode("LatiaDemo2026!"));
            user.setActive(true);

            Role role = new Role();
            role.setRol("ROLE_USER");
            role.setUser(user);
            user.getRoles().add(role);
            users.add(usersRepository.save(user));
        }
        return users;
    }

    private List<HealthProfile> createHealthProfiles(List<Users> users) {
        List<HealthProfile> profiles = new ArrayList<>();
        LocalDate[] birthDates = {
                LocalDate.of(1991, 4, 18),
                LocalDate.of(1986, 11, 2),
                LocalDate.of(1998, 7, 25),
                LocalDate.of(1979, 2, 14),
                LocalDate.of(1964, 9, 9)
        };
        String[] bloodTypes = {"O+", "A-", "B+", "O-", "AB+"};
        String[] phones = {"+51 900 000 001", "+51 900 000 002", "+51 900 000 003",
                "+51 900 000 004", "+51 900 000 005"};
        String[] sexes = {"Femenino", "Masculino", "Femenino", "Masculino", "Femenino"};
        for (int i = 0; i < users.size(); i++) {
            HealthProfile profile = new HealthProfile();
            profile.setOwnerUser(users.get(i));
            profile.setBirthDate(birthDates[i]);
            profile.setSex(sexes[i]);
            profile.setBloodType(bloodTypes[i]);
            profile.setPhone(phones[i]);
            profile.setActive(true);
            profiles.add(healthProfileRepository.save(profile));
        }
        return profiles;
    }

    private List<ClinicalRecord> createClinicalRecords(
            List<HealthProfile> profiles,
            List<MedicalCondition> conditions) {
        String[] types = {"Consulta", "Laboratorio", "Seguimiento", "Evaluación", "Control"};
        String[] titles = {
                "Control respiratorio", "Revisión neurológica", "Consulta por alergia",
                "Control de hemograma", "Control de presión"
        };
        List<ClinicalRecord> records = new ArrayList<>();
        String[] descriptions = {
                "Paciente refiere episodios de tos y sibilancias al realizar ejercicio. "
                        + "Se revisa tecnica de inhalacion y se indica control en cuatro semanas.",
                "Consulta por cefalea pulsante unilateral con fotofobia, sin signos de alarma. "
                        + "Se recomienda hidratacion, descanso y seguimiento si aumenta la frecuencia.",
                "Estornudos y congestion nasal recurrentes durante la temporada de polen. "
                        + "Se revisan medidas para reducir exposicion a alergenos.",
                "Refiere cansancio durante las ultimas semanas. Hemograma compatible con anemia leve; "
                        + "se indica suplementacion y nuevo control en dos meses.",
                "Control de hipertension sin sintomas actuales. Se revisan mediciones domiciliarias "
                        + "y se refuerzan recomendaciones de alimentacion y actividad fisica."
        };
        String[] professionals = {
                "Dra. Andrea Salazar", "Dr. Pablo Herrera", "Dra. Natalia Campos",
                "Dr. Luis Fernandez", "Dra. Elena Vargas"
        };
        for (int i = 0; i < profiles.size(); i++) {
            ClinicalRecord record = new ClinicalRecord(
                    null,
                    profiles.get(i),
                    types[i],
                    titles[i],
                    descriptions[i],
                    Date.from(LocalDate.now().minusDays(10L + i)
                            .atStartOfDay().toInstant(ZoneOffset.UTC)),
                    professionals[i],
                    conditions.get(i));
            records.add(clinicalRecordRepository.save(record));
        }
        return records;
    }

    private void createEmergencyContacts(List<HealthProfile> profiles) {
        String[] names = {"Mateo Quiroz", "Valeria Quiroz", "Diego Rojas", "Camila Rojas", "Lucia Quiroz"};
        String[] phones = {"+51 900 000 101", "+51 900 000 102", "+51 900 000 103",
                "+51 900 000 104", "+51 900 000 105"};
        String[] relationships = {"Hermano", "Hermana", "Esposo", "Esposa", "Hija"};
        List<EmergencyContact> contacts = new ArrayList<>();
        for (int i = 0; i < profiles.size(); i++) {
            contacts.add(new EmergencyContact(
                    null, profiles.get(i), names[i], phones[i], relationships[i], true, null, null));
        }
        emergencyContactRepository.saveAll(contacts);
    }

    private void createFamilyRelationships(List<HealthProfile> profiles) {
        String[] relationships = {"Hermanos", "Hermanos", "Esposos", "Esposos", "Madre e hija"};
        List<FamilyRelationship> familyRelationships = new ArrayList<>();
        for (int i = 0; i < profiles.size(); i++) {
            int relativeIndex = switch (i) {
                case 0 -> 1;
                case 1 -> 0;
                case 2 -> 3;
                case 3 -> 2;
                default -> 0;
            };
            familyRelationships.add(new FamilyRelationship(
                    null, profiles.get(i), profiles.get(relativeIndex), relationships[i]));
        }
        familyRelationshipRepository.saveAll(familyRelationships);
    }

    private void createExamResults(List<ClinicalRecord> records) {
        String[] parameters = {"Saturacion de oxigeno", "Presion arterial", "IgE total",
                "Hemoglobina", "Presion arterial"};
        String[] values = {"97", "118/76", "186", "10.8", "132/82"};
        String[] units = {"%", "mmHg", "UI/mL", "g/dL", "mmHg"};
        List<ExamResult> results = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            results.add(new ExamResult(null, records.get(i), parameters[i], values[i], units[i]));
        }
        examResultRepository.saveAll(results);
    }

    private List<MedicationTreatment> createMedicationTreatments(
            List<ClinicalRecord> records,
            List<Medications> medications) {
        String[] doses = {"2 inhalaciones cuando sea necesario", "1 tableta si inicia la cefalea",
                "1 tableta al dia", "1 tableta al dia con alimentos", "1 tableta al dia"};
        String[] routes = {"Inhalatoria", "Oral", "Oral", "Oral", "Oral"};
        List<MedicationTreatment> treatments = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            treatments.add(new MedicationTreatment(
                    null,
                    records.get(i),
                    medications.get(i),
                    doses[i],
                    routes[i],
                    LocalDate.now().minusDays(i),
                    i == 4 ? null : LocalDate.now().plusDays(30L + i)));
        }
        return medicationTreatmentRepository.saveAll(treatments);
    }

    private void createMedicationSchedules(List<MedicationTreatment> treatments) {
        List<MedicationSchedule> schedules = new ArrayList<>();
        int[] reminderHours = {8, 9, 7, 8, 20};
        int[] intervals = {6, 8, 24, 24, 24};
        for (int i = 0; i < treatments.size(); i++) {
            schedules.add(new MedicationSchedule(
                    null,
                    treatments.get(i),
                    LocalDate.now().plusDays(1).atTime(reminderHours[i], 0),
                    intervals[i]));
        }
        medicationScheduleRepository.saveAll(schedules);
    }

    private List<MedicalDocuments> createMedicalDocuments(List<HealthProfile> profiles) {
        String[] types = {"Resultado de laboratorio", "Receta", "Informe médico", "Radiografía", "Control clínico"};
        String[] titles = {
                "Espirometria de control", "Indicaciones para cefalea", "Evaluacion de rinitis alergica",
                "Hemograma completo", "Registro de presion arterial"
        };
        List<MedicalDocuments> documents = new ArrayList<>();
        for (int i = 0; i < profiles.size(); i++) {
            documents.add(new MedicalDocuments(
                    null,
                    types[i],
                    titles[i],
                    "https://example.com/latia/documentos-ficticios/expediente-" + (i + 1) + ".pdf",
                    LocalDate.now().minusDays(10L + i),
                    profiles.get(i)));
        }
        return medicalDocumentsRepository.saveAll(documents);
    }

    private void createClinicalRecordDocuments(
            List<ClinicalRecord> records,
            List<MedicalDocuments> documents) {
        List<ClinicalRecordDocument> links = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            links.add(new ClinicalRecordDocument(null, records.get(i), documents.get(i)));
        }
        clinicalRecordDocumentRepository.saveAll(links);
    }
}
