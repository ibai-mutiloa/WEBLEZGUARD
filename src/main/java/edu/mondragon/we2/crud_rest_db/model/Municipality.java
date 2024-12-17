package edu.mondragon.we2.crud_rest_db.model;

import jakarta.persistence.*;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "municipalities")
public class Municipality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Municipality_ID")
    private Integer id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Address")
    private String address;

    @Column(name = "Contact_Phone")
    private String contactPhone;

    @ManyToMany
    @JoinTable(
      name = "municipality_location", 
      joinColumns = @JoinColumn(name = "municipality_id"), 
      inverseJoinColumns = @JoinColumn(name = "location_id"))
    @JsonIgnore // Ignorar la propiedad para evitar la recursión infinita
    private Set<Location> locations;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

}
