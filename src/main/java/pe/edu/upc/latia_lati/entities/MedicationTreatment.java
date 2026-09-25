package pe.edu.upc.latia_lati.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "medication_treatments")
public class MedicationTreatment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "clinical_record_id", nullable = false)
    private ClinicalRecord clinicalRecord;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medication_id", nullable = false)
    private Medication medication;

    @Column(name = "dose", nullable = false, length = 100)
    private String dose;

    @Column(name = "administration_route", nullable = false, length = 80)
    private String administrationRoute;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @JsonIgnore
    @OneToMany(mappedBy = "treatment")
    private List<MedicationSchedule> schedules;

    public MedicationTreatment() {
    }

    public MedicationTreatment(Long id, ClinicalRecord clinicalRecord, Medication medication, String dose,
                               String administrationRoute, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.clinicalRecord = clinicalRecord;
        this.medication = medication;
        this.dose = dose;
        this.administrationRoute = administrationRoute;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ClinicalRecord getClinicalRecord() { return clinicalRecord; }
    public void setClinicalRecord(ClinicalRecord clinicalRecord) { this.clinicalRecord = clinicalRecord; }
    public Medication getMedication() { return medication; }
    public void setMedication(Medication medication) { this.medication = medication; }
    public String getDose() { return dose; }
    public void setDose(String dose) { this.dose = dose; }
    public String getAdministrationRoute() { return administrationRoute; }
    public void setAdministrationRoute(String administrationRoute) { this.administrationRoute = administrationRoute; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public List<MedicationSchedule> getSchedules() { return schedules; }
    public void setSchedules(List<MedicationSchedule> schedules) { this.schedules = schedules; }
}
