package pe.edu.upc.latia_lati.dtos;

public class CountMedicalConditionDTO {
    private String conditionName;
    private int totalRecords;

    public String getConditionName() {
        return conditionName;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }
}
