package org.zero_consult.people_backend.entities;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String city;
    private String companyName;
    private String contactPersonFirstName;
    private String contactPersonLastName;
    private String email;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Sector sector;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date startDate;
    @Enumerated(EnumType.STRING)
    private CustomerStatus status;
    private String website;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactPersonFirstName() {
        return contactPersonFirstName;
    }

    public void setContactPersonFirstName(String contactPersonFirstName) {
        this.contactPersonFirstName = contactPersonFirstName;
    }

    public String getContactPersonLastName() {
        return contactPersonLastName;
    }

    public void setContactPersonLastName(String contactPersonLastName) {
        this.contactPersonLastName = contactPersonLastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
