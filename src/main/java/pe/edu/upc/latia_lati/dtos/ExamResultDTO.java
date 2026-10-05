package pe.edu.upc.latia_lati.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ExamResultDTO {
    private Long idExamResult;

    @NotNull(message = "El ID de la historia clínica es obligatorio")
    @Positive(message = "El ID de la historia clínica debe ser positivo")
    private Long idClinicalRecord;

    @NotBlank(message = "El nombre del parámetro es obligatorio")
    @Size(max = 150, message = "El nombre del parámetro no puede superar los 150 caracteres")
    private String parameterName;

    @NotBlank(message = "El valor del resultado es obligatorio")
    @Size(max = 150, message = "El valor del resultado no puede superar los 150 caracteres")
    private String resultValue;

    @Size(max = 50, message = "La unidad no puede superar los 50 caracteres")
    private String unit;

    public Long getIdExamResult() {
        return idExamResult;
    }

    public void setIdExamResult(Long idExamResult) {
        this.idExamResult = idExamResult;
    }

    public Long getIdClinicalRecord() {
        return idClinicalRecord;
    }

    public void setIdClinicalRecord(Long idClinicalRecord) {
        this.idClinicalRecord = idClinicalRecord;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
