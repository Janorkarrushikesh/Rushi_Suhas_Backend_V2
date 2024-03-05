package com.syntiaro_pos_system.entity.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;


@Data
@Entity
@Table(name = "Store",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "contact")
        })
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Store {

    @Id//STORE ID
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Storeid;

    @Column(name = "registrationNumber")
    private String registrationNo;

    @NotBlank(message = "Please enter the name")
    @Size(max = 20)
    private String username;

    @Column(name = "Store_Address")
    private String storeAddress;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @Column(name = "Store_Name")
    private String storeName;

    @Column(name = "Contact")
    private String contact;

    @Column(name = "GST_NO")
    private String gstNo;

    @Column(name = "Date")
    private Date date = new Date();

    @Column(name = "Currency")
    private String currency;

    @Column(name = "Country")
    private String country;

    @Column(name = "State")
    private String state;

    @Column(name = "pinCode")
    private String pinCode;

    @Column(name = "Address")
    private String address;

    @Column(name = "UPI")
    private String upi;

    @Lob
    private byte[] logo;//LOGO FIELD

    @Column(name = "subscription_expiration")
    private LocalDateTime subscriptionExpiration;

    private String subscriptionType;

    @NotBlank
    @Size(max = 120)
    private String password;

    @Column(name = "Created_Date")
    private String createdDate;

    @Column(name = "Updated_Date")
    private String updatedDate;

    @Column(name = "Created_By")
    private String createdBy;

    @Column(name = "Updated_By")
    private String updatedBy;

    private String comfirmPassword;

    private String countryCode;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "store_roles",
            joinColumns = @JoinColumn(name = "store_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<StoreRole> storeRoles = new HashSet<>();

    @OneToMany(targetEntity = Tax.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "Storeid", referencedColumnName = "Storeid")
    private List<Tax> tax;

    @OneToMany(targetEntity = StorePayment.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "Storeid", referencedColumnName = "Storeid")

    private List<StorePayment> storePayments;

    public Store(String username, String storeAddress, String email, String storeName, String contact, String gstno, Date date, String currency, String country, String countryCode, String state, String createdby, String updatedby, String comfirmpassword, String subscriptionType, String upi, String address, String pinCode, String encode) {
    }

    public Store(String usernames, String saddress, String email, String store_name, String contact, String gstNo, Date date, String currency, String country, String country_code, String state, String createdby, String updatedby, String comfirmpassword, String subscriptionType, String password) {
        this.username = username;
        this.storeAddress = saddress;
        this.email = email;
        this.storeName = store_name;
        this.contact = contact;
        this.gstNo = gstNo;
        this.date = date;
        this.currency = currency;
        this.country = country;
        this.countryCode = country_code;
        this.state = state;
        this.createdBy = createdby;
        this.updatedBy = updatedby;
        this.comfirmPassword = comfirmpassword;
        this.subscriptionType = subscriptionType;
        this.password = password;
    }

    @PostPersist
    public void generateStoreCode() {

        if (Storeid <= 9999) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM");
            String formattedDate = dateFormat.format(date);
            String formattedId = String.format("%04d", Storeid);
            this.registrationNo = formattedDate + "/" + formattedId;


            SimpleDateFormat dateFormats = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss a");
            String formattedDates = dateFormats.format(date);
            this.updatedDate = formattedDates;
            this.createdDate = formattedDates;
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM");
            String formattedDate = dateFormat.format(date);
            this.registrationNo = formattedDate + "/" + Storeid;


            SimpleDateFormat dateFormats = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss a");
            String formattedDates = dateFormats.format(date);
            this.updatedDate = formattedDates;
            this.createdDate = formattedDates;

        }
    }

    public Long Storeid() {
        return Storeid;
    }

    public LocalDateTime getSubscriptionExpiration() {
        return subscriptionExpiration;
    }


    public void setSubscriptionExpiration(LocalDateTime subscriptionExpiration) {
        this.subscriptionExpiration = subscriptionExpiration;
    }


    public String getLogoUrl() {
        return Arrays.toString(logo);
    }

    public Long getStoreid() {
        return Storeid;
    }

    public void setStoreid(Long storeid) {
        Storeid = storeid;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Set<StoreRole> getStoreRoles() {
        return storeRoles;
    }

    public void setStoreRoles(Set<StoreRole> storeRoles) {
        this.storeRoles = storeRoles;
    }

    public List<Tax> getTax() {
        return tax;
    }

    public void setTax(List<Tax> tax) {
        this.tax = tax;
    }

    public List<StorePayment> getStorePayments() {
        return storePayments;
    }

    public void setStorePayments(List<StorePayment> storePayments) {
        this.storePayments = storePayments;
    }


    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public boolean isFreeTrial() {
        return "Free Trial".equals(subscriptionType);
    }

    public String getComfirmPassword() {
        return comfirmPassword;
    }

    public void setComfirmPassword(String comfirmPassword) {
        this.comfirmPassword = comfirmPassword;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }


    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUpi() {
        return upi;
    }

    public void setUpi(String upi) {
        this.upi = upi;
    }
}
