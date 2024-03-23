package com.daw.webapp07.DTO;

public class GraphicsDTO {
    private String donationsOverTime_Quantity;
    private String donationsOverTime_Time;
    private String donationsByUser_Quantity;
    private String donationsByUser_User;
    public GraphicsDTO(String tq, String tt, String uq, String uu) {
        this.donationsOverTime_Quantity = tq;
        this.donationsOverTime_Time = tt;
        this.donationsByUser_Quantity = uq;
        this.donationsByUser_User = uu;
    }

    public String getDonationsOverTime_Quantity() {
        return donationsOverTime_Quantity;
    }

    public void setDonationsOverTime_Quantity(String donationsOverTime_Quantity) {
        this.donationsOverTime_Quantity = donationsOverTime_Quantity;
    }

    public String getDonationsOverTime_Time() {
        return donationsOverTime_Time;
    }

    public void setDonationsOverTime_Time(String donationsOverTime_Time) {
        this.donationsOverTime_Time = donationsOverTime_Time;
    }

    public String getDonationsByUser_Quantity() {
        return donationsByUser_Quantity;
    }

    public void setDonationsByUser_Quantity(String donationsByUser_Quantity) {
        this.donationsByUser_Quantity = donationsByUser_Quantity;
    }

    public String getDonationsByUser_User() {
        return donationsByUser_User;
    }

    public void setDonationsByUser_User(String donationsByUser_User) {
        this.donationsByUser_User = donationsByUser_User;
    }
}
