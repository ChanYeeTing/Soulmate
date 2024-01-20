package com.example.soulmate.ui.medicalHistory;

public class AllergicSurgeryDetails {
    private boolean allergic;
    private String allergicDetails;
    private boolean surgery;
    private String surgeryDetails;

    // Default constructor required for Firebase
    public AllergicSurgeryDetails() {
    }

    public AllergicSurgeryDetails(boolean allergic, String allergicDetails, boolean surgery, String surgeryDetails) {
        this.allergic = allergic;
        this.allergicDetails = allergicDetails;
        this.surgery = surgery;
        this.surgeryDetails = surgeryDetails;
    }

    public boolean isAllergic() {
        return allergic;
    }

    public String getAllergicDetails() {
        return allergicDetails;
    }

    public boolean isSurgery() {
        return surgery;
    }

    public String getSurgeryDetails() {
        return surgeryDetails;
    }
}
