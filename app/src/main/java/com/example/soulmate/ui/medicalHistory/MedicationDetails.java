package com.example.soulmate.ui.medicalHistory;

public class MedicationDetails {
    private String medicationName;
    private String dosage;
    private String frequency;
    private String duration;
    private String lastDate;
    private String instructions;
    private String allergicDetails;
    private String surgeryDetails;
    private boolean allergic;
    private boolean surgery;

    // Default constructor required for Firebase
    public MedicationDetails() {
    }

    public MedicationDetails(String medicationName, String dosage, String frequency, String duration, String lastDate, String instructions,
                             boolean allergic, String allergicDetails, boolean surgery, String surgeryDetails) {
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.duration = duration;
        this.lastDate = lastDate;
        this.instructions = instructions;
        this.allergicDetails = allergicDetails;
        this.surgeryDetails = surgeryDetails;
        this.allergic = allergic;
        this.surgery = surgery;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public String getDosage() {
        return dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getDuration() {
        return duration;
    }

    public String getLastDate() {
        return lastDate;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getAllergicDetails(){
        return allergicDetails;
    }

    public String getSurgeryDetails(){
        return surgeryDetails;
    }

    public boolean isAllergic() {
        return allergic;
    }

    public boolean isSurgery() {
        return surgery;
    }
}
