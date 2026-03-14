package com.jean.carrental.dto;

import java.time.LocalDate;

public class RentalDTO {

    private int rentalID;
    private String customerName;
    private String customerEmail;
    private String carMake;
    private String carModel;
    private LocalDate rentalDate;
    private LocalDate returnDate;
    private boolean returned;

    public RentalDTO(int rentalID,
                     String customerEmail,
                     String customerName,
                     String carMake,
                     String carModel,
                     LocalDate rentalDate,
                     LocalDate returnDate,
                     double price, boolean returned) {
        this.rentalID = rentalID;
        this.customerEmail = customerEmail;
        this.customerName = customerName;
        this.carMake = carMake;
        this.carModel = carModel;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.returned = returned;
    }

    public int getRentalID() {
        return rentalID;
    }

    public void setRentalID(int rentalID) {
        this.rentalID = rentalID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public LocalDate getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(LocalDate rentalDate) {
        this.rentalDate = rentalDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }
}
