package pe.edu.upc.latia_lati.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "medication_schedules")
public class MedicationSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idMedicamentTreatment", nullable = false)
    private MedicationTreatment treatment;

    @Column(name = "reminderTime", nullable = false)
    private LocalDateTime reminderTime;

    @Column(name = "intervalHours", nullable = false)
    private Integer intervalHours;

    public MedicationSchedule() {
    }

    public MedicationSchedule(Long id, MedicationTreatment treatment, LocalDateTime reminderTime, Integer intervalHours) {
        this.id = id;
        this.treatment = treatment;
        this.reminderTime = reminderTime;
        this.intervalHours = intervalHours;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public MedicationTreatment getTreatment() { return treatment; }
    public void setTreatment(MedicationTreatment treatment) { this.treatment = treatment; }
    public LocalDateTime getReminderTime() { return reminderTime; }
    public void setReminderTime(LocalDateTime reminderTime) { this.reminderTime = reminderTime; }
    public Integer getIntervalHours() { return intervalHours; }
    public void setIntervalHours(Integer intervalHours) { this.intervalHours = intervalHours; }
}
