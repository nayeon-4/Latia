package pe.edu.upc.latia_lati.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.latia_lati.entities.*;
import pe.edu.upc.latia_lati.repositories.*;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@ConditionalOnProperty(name = "latia.demo-data.enabled", havingValue = "true")
public class DemoDataInitializer implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoDataInitializer.class);

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
        syncUserSequenceIfNeeded();
        if (hasExistingDemoUsers()) {
            LOGGER.info("Demo data already exists; skipping demo data initialization.");
            return;
        }

        LOGGER.info("Initializing 50 demo records for all entities...");

        List<Users> users = createUsers();
        List<HealthProfile> profiles = createHealthProfiles(users);
        List<MedicalCondition> conditions = createMedicalConditions();
        List<Medications> medications = createMedications();
        List<ClinicalRecord> clinicalRecords = createClinicalRecords(profiles, conditions);

        createEmergencyContacts(profiles);
        createFamilyRelationships(profiles);
        createExamResults(clinicalRecords);

        List<MedicationTreatment> treatments = createMedicationTreatments(clinicalRecords, medications);
        createMedicationSchedules(treatments);

        List<MedicalDocuments> documents = createMedicalDocuments(profiles);
        createClinicalRecordDocuments(clinicalRecords, documents);

        LOGGER.info("50 demo records initialized successfully!");
    }

    private void syncUserSequenceIfNeeded() {
        if (usersRepository.count() > 0) {
            usersRepository.syncIdSequence();
        }
    }

    private boolean hasExistingDemoUsers() {
        return usersRepository.findByUsername("valeria.quiroz").isPresent();
    }

    private List<Users> createUsers() {
        String[] firstNames = {
                "Valeria", "Mateo", "Camila", "Diego", "Rosa", "Carlos", "Sofia", "Juan", "Lucia", "Gabriel",
                "Andrea", "Fernando", "Paula", "Javier", "Elena", "Santiago", "Mariana", "Alejandro", "Daniela", "Gonzalo",
                "Claudia", "Sebastian", "Beatriz", "Manuel", "Patricia", "Ricardo", "Teresa", "Hugo", "Isabel", "Adrian",
                "Monica", "Felipe", "Alonso", "Esteban", "Natalia", "Rodrigo", "Lorena", "Emilio", "Vanessa", "Ignacio",
                "Silvia", "Joaquin", "Carmen", "Julio", "Irene", "Tomas", "Gisela", "Oscar", "Veronica", "Guillermo"
        };
        String[] lastNames = {
                "Quiroz", "Rojas", "Herrera", "Salazar", "Fernandez", "Campos", "Vargas", "Lopez", "Gomez", "Torres",
                "Diaz", "Morales", "Romero", "Alvarez", "Mendoza", "Rios", "Castro", "Ortiz", "Silva", "Nunez",
                "Guerrero", "Medina", "Cortes", "Reyes", "Guzman", "Pena", "Delgado", "Vega", "Ruiz", "Suarez",
                "Aguilar", "Ramos", "Soto", "Navarro", "Paredes", "Espinoza", "Lara", "Miranda", "Arias", "Benitez",
                "Ibarra", "Ponce", "Cabrera", "Flores", "Acosta", "Villanueva", "Mejia", "Castillo", "Perez", "Sanchez"
        };

        List<Users> users = new ArrayList<>();
        String encodedPassword = passwordEncoder.encode("LatiaDemo2026!");

        for (int i = 0; i < 50; i++) {
            String firstName = firstNames[i];
            String lastName = lastNames[i];

            // Username basado en el nombre y apellido real (ej. valeria.quiroz, mateo.rojas)
            String cleanFirstName = firstName.toLowerCase().replaceAll("[^a-z0-9]", "");
            String cleanLastName = lastName.toLowerCase().replaceAll("[^a-z0-9]", "");
            String realUsername = cleanFirstName + "." + cleanLastName;

            Users user = new Users();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUsername(realUsername);
            user.setEmail(realUsername + "@example.com");
            user.setPasswordHash(encodedPassword);
            user.setActive(true);

            Role role = new Role();
            role.setRol(i == 0 ? "ROLE_ADMIN" : "ROLE_USER");
            role.setUser(user);
            user.getRoles().add(role);

            users.add(usersRepository.save(user));
        }
        return users;
    }

    private List<HealthProfile> createHealthProfiles(List<Users> users) {
        String[] bloodTypes = {"O+", "A-", "B+", "O-", "AB+", "A+", "B-", "AB-"};
        String[] sexes = {"Femenino", "Masculino"};
        List<HealthProfile> profiles = new ArrayList<>();

        for (int i = 0; i < users.size(); i++) {
            Users currentUser = users.get(i);

            HealthProfile profile = new HealthProfile();
            profile.setOwnerUser(currentUser);
            profile.setFirstName(currentUser.getFirstName());
            profile.setLastName(currentUser.getLastName());
            // Asignación explícita de id_holder_user (Holder User)
            profile.setHolderUser(currentUser);

            profile.setBirthDate(LocalDate.of(1965 + (i % 35), (i % 12) + 1, (i % 28) + 1));
            profile.setSex(sexes[i % 2]);
            profile.setBloodType(bloodTypes[i % 8]);
            profile.setPhone("+51 900 000 " + String.format("%03d", i + 1));
            profile.setActive(true);
            profiles.add(healthProfileRepository.save(profile));
        }
        return profiles;
    }

    private List<MedicalCondition> createMedicalConditions() {
        String[] names = {
                "Asma persistente leve", "Migraña episodica", "Rinitis alergica estacional", "Anemia ferropenica leve",
                "Hipertension arterial esencial", "Diabetes Mellitus Tipo 2", "Hipotiroidismo subclinico", "Gastritis cronica",
                "Dislipidemia", "Artrosis de rodilla", "Dermatitis atopica", "Sindrome de intestino irritable",
                "Insomnio primario", "Ansiedad generalizada", "Lumbalgia mecanica", "Esteatosis hepatica",
                "Reflujo gastroesofagico", "Gota", "Osteopenia", "Hiperuricemia",
                "Bronquitis cronica", "Alergia alimentaria", "Vertigo posicional", "Otitis media recurrente",
                "Fibromialgia", "Apnea del sueno", "Insuficiencia venosa", "Sindrome del tunel carpiano",
                "Conjuntivitis alergica", "Eczema numular", "Nodulo tiroideo benigno", "Psoriasis leve",
                "Colelitiasis asintomatica", "Nefritis leve", "Taquicardia sinusal", "Arritmia leve",
                "Sindrome metabolico", "Esteatohepatitis", "Trastorno depresivo leve", "Cefalea tensional",
                "Glaucoma de angulo abierto", "Catarata senil inicial", "Incontinencia de esfuerzo", "Prostatitis cronica",
                "Poliposis nasal", "Rosacea", "Sindrome de Sjogren", "Vitiligo", "Endometriosis leve", "Fascitis plantar"
        };

        List<MedicalCondition> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add(new MedicalCondition(
                    null,
                    names[i],
                    "Descripcion clinica parametrizada y evaluacion continua para la condicion " + names[i] + "."
            ));
        }
        return medicalConditionRepository.saveAll(list);
    }

    private List<Medications> createMedications() {
        String[] names = {
                "Salbutamol", "Paracetamol", "Loratadina", "Sulfato ferroso", "Losartan",
                "Metformina", "Levotiroxina", "Omeprazol", "Atorvastatina", "Ibuprofeno",
                "Cetirizina", "Esomeprazol", "Zolpidem", "Sertralina", "Naproxeno",
                "Simvastatina", "Ranitidina", "Allopurinol", "Calcio + Vitamina D", "Colchicina",
                "Budesonida", "Desloratadina", "Betahistina", "Amoxicilina", "Pregabalina",
                "Melatonina", "Diosmina", "Fisioterapia gel", "Olopatadina", "Hidrocortisona",
                "Levotiroxina 25mcg", "Metotrexato", "Acido ursodesoxicolico", "Enalapril", "Propranolol",
                "Amlodipino", "Metformina 500mg", "Vitamina E", "Fluoxetina", "Clonazepam",
                "Latanoprost", "Lagrimas artificiales", "Tamsulosina", "Ciprofloxacino", "Fluticasona",
                "Ivermectina", "Carboximetilcelulosa", "Tacrolimus", "Dienogest", "Diclofenaco gel"
        };
        String[] forms = {"Tableta", "Inhalador", "Capsula", "Jarabe", "Gotas", "Crema", "Unguentos", "Solucion"};

        List<Medications> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add(new Medications(null, names[i], forms[i % 8], ((i + 1) * 5) + " mg/dosis"));
        }
        return medicationsRepository.saveAll(list);
    }

    private List<ClinicalRecord> createClinicalRecords(List<HealthProfile> profiles, List<MedicalCondition> conditions) {
        String[] types = {"Consulta", "Laboratorio", "Seguimiento", "Evaluacion", "Control"};
        String[] professionals = {"Dra. Andrea Salazar", "Dr. Pablo Herrera", "Dra. Natalia Campos", "Dr. Luis Fernandez", "Dra. Elena Vargas"};

        List<ClinicalRecord> records = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            records.add(new ClinicalRecord(
                    null,
                    profiles.get(i),
                    types[i % 5],
                    "Registro clinico " + (i + 1) + " - " + conditions.get(i).getNameMedicalCondition(),
                    "Evaluacion medica detallada y evolucion de sintomas de " + conditions.get(i).getNameMedicalCondition() + ".",
                    Date.from(LocalDate.now().minusDays(i + 1).atStartOfDay().toInstant(ZoneOffset.UTC)),
                    professionals[i % 5],
                    conditions.get(i)
            ));
        }
        return clinicalRecordRepository.saveAll(records);
    }

    private void createEmergencyContacts(List<HealthProfile> profiles) {
        String[] relationships = {"Hermano", "Hermana", "Esposo", "Esposa", "Hija", "Madre", "Padre", "Tio"};
        List<EmergencyContact> contacts = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            contacts.add(new EmergencyContact(
                    null,
                    profiles.get(i),
                    "Contacto de Emergencia " + (i + 1),
                    "+51 900 000 " + String.format("%03d", i + 100),
                    relationships[i % 8],
                    true, null, null
            ));
        }
        emergencyContactRepository.saveAll(contacts);
    }

    private void createFamilyRelationships(List<HealthProfile> profiles) {
        String[] types = {"Hermanos", "Esposos", "Madre e hija", "Padre e hijo", "Primos"};
        List<FamilyRelationship> list = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            int relativeIndex = (i + 1) % 50;
            list.add(new FamilyRelationship(
                    null,
                    profiles.get(i),
                    profiles.get(relativeIndex),
                    types[i % 5]
            ));
        }
        familyRelationshipRepository.saveAll(list);
    }

    private void createExamResults(List<ClinicalRecord> records) {
        String[] params = {"Saturacion de oxigeno", "Presion arterial", "IgE total", "Hemoglobina", "Glucosa", "Colesterol", "Trigliceridos", "TSH"};
        String[] units = {"%", "mmHg", "UI/mL", "g/dL", "mg/dL", "mg/dL", "mg/dL", "uIU/mL"};

        List<ExamResult> results = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            results.add(new ExamResult(
                    null,
                    records.get(i),
                    params[i % 8],
                    String.valueOf(80 + (i % 40)),
                    units[i % 8]
            ));
        }
        examResultRepository.saveAll(results);
    }

    private List<MedicationTreatment> createMedicationTreatments(List<ClinicalRecord> records, List<Medications> medications) {
        String[] routes = {"Inhalatoria", "Oral", "Topica", "Oftalmica", "Sublingual"};
        List<MedicationTreatment> treatments = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            treatments.add(new MedicationTreatment(
                    null,
                    records.get(i),
                    medications.get(i),
                    "1 dosis cada " + (((i % 3) + 1) * 8) + " horas",
                    routes[i % 5],
                    LocalDate.now().minusDays(i),
                    (i % 5 == 0) ? null : LocalDate.now().plusDays(20 + i)
            ));
        }
        return medicationTreatmentRepository.saveAll(treatments);
    }

    private void createMedicationSchedules(List<MedicationTreatment> treatments) {
        int[] intervals = {6, 8, 12, 24};
        List<MedicationSchedule> schedules = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            schedules.add(new MedicationSchedule(
                    null,
                    treatments.get(i),
                    LocalDate.now().plusDays(1).atTime(6 + (i % 12), 0),
                    intervals[i % 4]
            ));
        }
        medicationScheduleRepository.saveAll(schedules);
    }

    private List<MedicalDocuments> createMedicalDocuments(List<HealthProfile> profiles) {
        String[] types = {"Resultado de laboratorio", "Receta", "Informe medico", "Control clinico", "Estudio de imagen"};
        List<MedicalDocuments> documents = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            documents.add(new MedicalDocuments(
                    null,
                    types[i % 5],
                    "Documento Medico Expediente #" + (i + 1),
                    "https://example.com/latia/documentos-ficticios/expediente-" + (i + 1) + ".pdf",
                    LocalDate.now().minusDays(i + 1),
                    profiles.get(i)
            ));
        }
        return medicalDocumentsRepository.saveAll(documents);
    }

    private void createClinicalRecordDocuments(List<ClinicalRecord> records, List<MedicalDocuments> documents) {
        List<ClinicalRecordDocument> links = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            links.add(new ClinicalRecordDocument(null, records.get(i), documents.get(i)));
        }
        clinicalRecordDocumentRepository.saveAll(links);
    }
}