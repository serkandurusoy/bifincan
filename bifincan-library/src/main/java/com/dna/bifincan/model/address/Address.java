package com.dna.bifincan.model.address;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.dna.bifincan.model.user.User;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.validator.constraints.NotBlank;

/**
 * The persistent class for the address database table.
 *
 */
@Entity
@Table(name = "address")
@Audited(targetAuditMode= RelationTargetAuditMode.NOT_AUDITED) 
public class Address extends com.dna.bifincan.model.BaseEntity implements Serializable {

    private static final long serialVersionUID = -4467657043247084267L;

    @Column(nullable = false, columnDefinition = "TINYINT(1) unsigned")
    private boolean active = true;

    @Column(name = "apartment_number", nullable = false, length = 250)
    @NotNull
    @NotEmpty
    @NotBlank
    private String apartmentNumber;

    @Column(name = "building_number", nullable = false, length = 250)
    @NotNull
    @NotEmpty
    @NotBlank
    private String buildingNumber;

    @Column(name = "phone_extension", columnDefinition = "INT(5) unsigned")
    private Long phoneExtension;

    @Column(name = "phone_number", columnDefinition = "INT(7) unsigned")
    private Long phoneNumber;

    @Column(name = "primary_address", nullable = false, columnDefinition = "TINYINT(1) unsigned")
    private boolean primary = false;

    @Column(name = "short_name", nullable = false, length = 250)
    @NotNull
    @NotEmpty
    @NotBlank
    private String shortName;

    @Column(name = "street_name", nullable = false, length = 250)
    @NotNull
    @NotEmpty
    @NotBlank
    private String streetName;

    // bi-directional many-to-one association to User
    @ManyToOne
    @JoinColumn(name = "user", nullable = false)
    private User user;

    // bi-directional many-to-one association to AddressType
    @ManyToOne
    @JoinColumn(name = "address_type", nullable = false)
    private AddressType addressType;

    // bi-directional many-to-one association to Area
    @ManyToOne
    @JoinColumn(name = "area", nullable = false)
    private Area area;

    public Address() {
    }

    public boolean getActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getApartmentNumber() {
        return this.apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getBuildingNumber() {
        return this.buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public Long getPhoneExtension() {
        return this.phoneExtension;
    }

    public void setPhoneExtension(Long phoneExtension) {
        this.phoneExtension = phoneExtension;
    }

    public Long getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean getPrimary() {
        return this.primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public String getShortName() {
        return this.shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getStreetName() {
        return this.streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AddressType getAddressType() {
        return this.addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    public Area getArea() {
        return this.area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (active ? 1231 : 1237);
        result = prime * result + ((addressType == null) ? 0 : addressType.hashCode());
        result = prime * result + ((apartmentNumber == null) ? 0 : apartmentNumber.hashCode());
        result = prime * result + ((area == null) ? 0 : area.hashCode());
        result = prime * result + ((buildingNumber == null) ? 0 : buildingNumber.hashCode());
        result = prime * result + ((phoneExtension == null) ? 0 : phoneExtension.hashCode());
        result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
        result = prime * result + (primary ? 1231 : 1237);
        result = prime * result + ((shortName == null) ? 0 : shortName.hashCode());
        result = prime * result + ((streetName == null) ? 0 : streetName.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Address other = (Address) obj;
        if (active != other.active) {
            return false;
        }
        if (addressType == null) {
            if (other.addressType != null) {
                return false;
            }
        } else if (!addressType.equals(other.addressType)) {
            return false;
        }
        if (apartmentNumber == null) {
            if (other.apartmentNumber != null) {
                return false;
            }
        } else if (!apartmentNumber.equals(other.apartmentNumber)) {
            return false;
        }
        if (area == null) {
            if (other.area != null) {
                return false;
            }
        } else if (!area.equals(other.area)) {
            return false;
        }
        if (buildingNumber == null) {
            if (other.buildingNumber != null) {
                return false;
            }
        } else if (!buildingNumber.equals(other.buildingNumber)) {
            return false;
        }
        if (phoneExtension == null) {
            if (other.phoneExtension != null) {
                return false;
            }
        } else if (!phoneExtension.equals(other.phoneExtension)) {
            return false;
        }
        if (phoneNumber == null) {
            if (other.phoneNumber != null) {
                return false;
            }
        } else if (!phoneNumber.equals(other.phoneNumber)) {
            return false;
        }
        if (primary != other.primary) {
            return false;
        }
        if (shortName == null) {
            if (other.shortName != null) {
                return false;
            }
        } else if (!shortName.equals(other.shortName)) {
            return false;
        }
        if (streetName == null) {
            if (other.streetName != null) {
                return false;
            }
        } else if (!streetName.equals(other.streetName)) {
            return false;
        }
        if (user == null) {
            if (other.user != null) {
                return false;
            }
        } else if (!user.equals(other.user)) {
            return false;
        }
        return true;
    }
}