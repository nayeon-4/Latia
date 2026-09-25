package pe.edu.upc.latia_lati.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Table(name = "medications")
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "dosage_form", nullable = false, length = 80)
    private String dosageForm;

    @Column(name = "strength", nullable = false, length = 80)
    private String strength;

    @JsonIgnore
    @OneToMany(mappedBy = "medication")
    private List<MedicationTreatment> treatments;

    public Medication() {
    }

    public Medication(Long id, String name, String dosageForm, String strength) {
        this.id = id;
        this.name = name;
        this.dosageForm = dosageForm;
        this.strength = strength;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDosageForm() { return dosageForm; }
    public void setDosageForm(String dosageForm) { this.dosageForm = dosageForm; }
    public String getStrength() { return strength; }
    public void setStrength(String strength) { this.strength = strength; }
    public List<MedicationTreatment> getTreatments() { return treatments; }
    public void setTreatments(List<MedicationTreatment> treatments) { this.treatments = treatments; }
}
