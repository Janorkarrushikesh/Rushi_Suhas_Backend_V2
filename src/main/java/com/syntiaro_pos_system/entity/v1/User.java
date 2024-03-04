package com.syntiaro_pos_system.entity.v1;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users",
    uniqueConstraints = {
      @UniqueConstraint(columnNames = "username"),
      @UniqueConstraint(columnNames = "email")
    })
public class User {



  // ----------------------RUSHIKESH ADDED THIS NEW CODE----------------------
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long Serial_no ;



  private Long id;

  @NotBlank
  @Size(max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 120)
  private String password;

  private Integer storeId;

  @Column(name="registrationDate")
  private Date registerDate = new Date();

  private String createdBy;

  private String updatedBy;

  private Date createdDate = new Date();

  private Date updatedDate = new Date();

  private String gstNo;

  private String address;

  private Long contact;

  private String comfirmPassword;

  private String currency;


  // Rushikesh added this new code ////
  @Column(name="registrationNumber")
  private String registno;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(  name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<UserRole> userRoles = new HashSet<>();

  // Rushikesh added this new code  registno , ID////
  public User(String username, String email, String password, String crtby, Integer storeid, Date registerDate, String registno, String gstno, String updby, Date crtDate,Date updateDate,String address,String comfirmpassword ,Long contact,Long id , String currency) {

    this.username=username;
    this.email = email;
    this.password = password;
    this.createdBy = crtby;
    this.storeId = storeid;
    this.registerDate = registerDate;
    this.registno=registno;
    this.gstNo = gstno;
    this.updatedBy = updby;
    this.createdDate =crtDate;
    this.updatedDate =updateDate;
    this.address = address;
    this.comfirmPassword =comfirmpassword;
    this.contact = contact;
    this.id = id;
    this.currency = currency;


  }


  public Long getSerial_no() {
    return Serial_no;
  }

  public void setSerial_no(Long serial_no) {
    Serial_no = serial_no;
  }



  public void setId(Long id) {
    this.id = id;
  }



  public void setUsername(String username) {
    this.username = username;
  }

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public Set<UserRole> getUserRoles() {
    return userRoles;
  }

  public void setUserRoles(Set<UserRole> userRoles) {
    this.userRoles = userRoles;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public String getAddress() {return address;}

  public void setAddress(String address) {this.address = address;}

  public Long getContact() {return contact;}

  public void setContact(Long contact) {
    this.contact = contact;
  }

  public String getGstNo() {
    return gstNo;
  }

  public void setGstNo(String gstNo) {
    this.gstNo = gstNo;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Integer getStoreId() {
    return storeId;
  }

  public void setStoreId(Integer storeId) {
    this.storeId = storeId;
  }

  public Date getRegisterDate() {
    return registerDate;
  }

  public void setRegisterDate(Date registerDate) {
    this.registerDate = registerDate;
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

  public void setUpdatedBy(String updatedBy) {this.updatedBy = updatedBy;}

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Date getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(Date updatedDate) {
    this.updatedDate = updatedDate;
  }

  public Set<UserRole> getRoles() {
    return userRoles;
  }

  public void setRoles(Set<UserRole> userRoles) {
    this.userRoles = userRoles;
  }

  public String getRegistno() {
    return registno;
  }

  public void setRegistno(String registno) {
    this.registno = registno;
  }

  public String getComfirmPassword() {
    return comfirmPassword;
  }

  public void setComfirmPassword(String comfirmPassword) {
    this.comfirmPassword = comfirmPassword;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public User() {
  }
}
