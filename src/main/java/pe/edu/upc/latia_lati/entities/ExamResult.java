package pe.edu.upc.latia_lati.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "exam_results")
public class ExamResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "clinical_record_id", nullable = false)
    private ClinicalRecord clinicalRecord;

    @Column(name = "parameter_name", nullable = false, length = 150)
    private String parameterName;

    @Column(name = "result_value", nullable = false, length = 150)
    private String resultValue;

    @Column(name = "unit", length = 50)
    private String unit;

    public ExamResult() {
    }

    public ExamResult(Long id, ClinicalRecord clinicalRecord, String parameterName, String resultValue, String unit) {
        this.id = id;
        this.clinicalRecord = clinicalRecord;
        this.parameterName = parameterName;
        this.resultValue = resultValue;
        this.unit = unit;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ClinicalRecord getClinicalRecord() { return clinicalRecord; }
    public void setClinicalRecord(ClinicalRecord clinicalRecord) { this.clinicalRecord = clinicalRecord; }
    public String getParameterName() { return parameterName; }
    public void setParameterName(String parameterName) { this.parameterName = parameterName; }
    public String getResultValue() { return resultValue; }
    public void setResultValue(String resultValue) { this.resultValue = resultValue; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}
