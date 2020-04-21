package com.dna.bifincan.model.user;

import com.dna.bifincan.model.address.Address;
import com.dna.bifincan.model.gsm.GsmPrefix;
import com.dna.bifincan.model.order.ShoppingCart;
import com.dna.bifincan.model.user.verification.EmailVerification;
import com.dna.bifincan.model.user.verification.GsmVerification;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * The persistent class for the user database table.
 *
 */
@Entity
//@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Table(name = "user", uniqueConstraints = {
    @UniqueConstraint(name = "UQ_gsmPrefix_gsmNumber", columnNames = {"gsm_prefix", "gsm_number"}),
    @UniqueConstraint(name = "UQ_email", columnNames = "email"),
    @UniqueConstraint(name = "UQ_username", columnNames = "username")})
@NamedQueries({
    @NamedQuery(name = "User.findByUsername", query = "select u from User u where u.username=:username"),
    @NamedQuery(name = "User.findByEmail", query = "select u from User u where u.email=:email"),
    @NamedQuery(name = "User.findAndLockAll", query = "select u from User u where u.active=true", lockMode=LockModeType.PESSIMISTIC_WRITE)    
})
@Access(AccessType.FIELD)
public class User extends com.dna.bifincan.model.BaseEntity implements UserDetails, Serializable {

    private static final long serialVersionUID = -6409644848220709964L;

    @Column(name = "firstname", nullable = false, length = 48)
    @NotNull
    @NotEmpty
    @NotBlank
    //@Audited
    private String firstName;

    @Column(name = "lastname", nullable = false, length = 48)
    @NotNull
    @NotEmpty
    @NotBlank
    //@Audited
    private String lastName;

    @Column(name = "password", nullable = false, length = 256)
    @NotNull
    @NotEmpty
    @Length(min = 6, max = 256)
    @Pattern(regexp = "^[-a-zA-Z0-9_#&@!*\\$\\.\\*\\+\\(\\)\\?%=\\\\/ıIiİşŞöÖçÇğĞüÜ]([-a-zA-Z0-9_#&@!\\$\\.\\*\\+\\(\\)\\?%=\\\\/ıIiİşŞöÖçÇğĞüÜ_]+)$", message = "şifren 6-20 karakter arasında olmalı")
    @Audited
    private String password;

    @Column(name = "username", nullable = false, length = 20)
    @NotNull
    @NotEmpty
    @Length(min = 6, max = 20)
    @Pattern(regexp = "^[-a-z0-9_\\.\\+]([-a-z0-9_\\.\\+_]+)$", message = "kullanıcı 6-20 karakter arasında olmalı, küçük harfler ve rakamlardan oluşmalı ve Türkçe karakter içermemeli")
    //@Audited
    private String username;

    @Column(name = "active", nullable = false, columnDefinition = "TINYINT(1) unsigned")
    //@Audited
    private boolean active = true;

    @Column(name = "founder", nullable = false, columnDefinition = "TINYINT(1) unsigned")
    //@Audited
    private boolean founder = false;

    @Column(name = "id_verified", nullable = false, columnDefinition = "TINYINT(1) unsigned")
    //@Audited
    private boolean idVerified = false;

    @Column(name = "marketing_professional", nullable = false, columnDefinition = "TINYINT(1) unsigned")
    //@Audited
    private boolean marketingProfessional = false;

    @Column(name = "extra_invitations", nullable = false)
    //@Audited
    private int extraInvitations = 0;

    @Column(name = "ambassador", nullable = false, columnDefinition = "TINYINT(1) unsigned")
    //@Audited
    private boolean ambassador = false;

    @Temporal(TemporalType.DATE)
    @Column(name = "birthday", nullable = false)
    @NotNull
    //@Audited
    private Date birthday;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false)
    @NotNull
    private Date createDate;

    @Column(name = "email", nullable = false, length = 250)
    @NotNull
    @NotEmpty
    @Email
    @Audited
    private String email;

    @Embedded
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private EmailVerification emailVerification;

    @Embedded
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private GsmVerification gsmVerification;

    @Embedded
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private SubscriptionOption subscriptionOption;

    @Column(name = "activity_level", nullable = false)
    //@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private int activityLevel = 1;

    // bi-directional many-to-one association to Address
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Address> addresses = new HashSet<Address>();

    // bi-directional many-to-one association to UserInvitation
    @ManyToOne
    @JoinColumn(name = "user_invitation")
    private UserInvitation acceptedInvitation;

    // bi-directional many-to-one association to UserInvitation
    @OneToMany(mappedBy = "inviter", cascade = CascadeType.ALL)
    private List<UserInvitation> sentInvitations = new ArrayList<UserInvitation>();

    // bi-directional many-to-one association to UserLogin
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserLogin> userLogins = new HashSet<UserLogin>();

    @Column(name = "points_balance", nullable = false)
    //@Audited
    private Integer pointsBalance = 0;

    @Column(name = "education_type", nullable = false, length = 32)
    @Enumerated(EnumType.ORDINAL)
    @Audited
    private EducationType educationType;

    @Column(name = "employment_status", nullable = false, length = 32)
    @Enumerated(EnumType.ORDINAL)
    @Audited
    private EmploymentStatusType employmentStatusType;

    @Column(name = "gender", nullable = false, length = 32)
    @Enumerated(EnumType.ORDINAL)
    @Audited
    private GenderType genderType;

    @Column(name = "has_children", nullable = false, length = 32)
    @Enumerated(EnumType.ORDINAL)
    @Audited
    private HasChildrenType hasChildrenType;

    @Column(name = "marital_status", nullable = false, length = 32)
    @Enumerated(EnumType.ORDINAL)
    @Audited
    private MaritalStatusType maritalStatusType;

    @Column(name = "monthly_income", nullable = false, length = 32)
    @Enumerated(EnumType.ORDINAL)
    @Audited
    private MonthlyNetTLIncomeType monthlyNetTLIncomeType;

    @Transient
    private int noOfOrderableProducts;

    @Transient
    private ShoppingCart shoppingCart;

    @Column(name = "fast_responses", nullable = false)
    private Integer fastResponseCounter = 0;
    
    public User() {
    }

    public void increaseActivityLevel() {
        if (this.activityLevel < 3) {
            this.activityLevel++;
        }
    }

    public void decreaseActivityLevel() {
        if (this.activityLevel > 1) {
            this.activityLevel--;
        }
    }

    public void newLoginLog(UserLogin _log) {
        if (!this.userLogins.contains(_log)) {
            _log.setUser(this);
            this.userLogins.add(_log);
        }
    }

    public void newInvitation(UserInvitation _invitation) {
        if (!this.sentInvitations.contains(_invitation)) {
            _invitation.setInviter(this);
            this.sentInvitations.add(_invitation);
        }
    }

    public int getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(int activityLevel) {
        this.activityLevel = activityLevel;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isFounder() {
        return founder;
    }

    public void setFounder(boolean founder) {
        this.founder = founder;
    }

    public boolean isIdVerified() {
        return idVerified;
    }

    public void setIdVerified(boolean idVerified) {
        this.idVerified = idVerified;
    }

    public boolean isAmbassador() {
        return ambassador;
    }

    public void setAmbassador(boolean ambassador) {
        this.ambassador = ambassador;
    }

    public boolean isMarketingProfessional() {
        return marketingProfessional;
    }

    public void setMarketingProfessional(boolean marketingProfessional) {
        this.marketingProfessional = marketingProfessional;
    }

    public int getExtraInvitations() {
        return extraInvitations;
    }

    public void setExtraInvitations(int extraInvitations) {
        this.extraInvitations = extraInvitations;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Address> getAddresses() {
        return this.addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public UserInvitation getAcceptedInvitation() {
        return acceptedInvitation;
    }

    public void setAcceptedInvitation(UserInvitation acceptedInvitation) {
        this.acceptedInvitation = acceptedInvitation;
    }

    public List<UserInvitation> getSentInvitations() {
        return sentInvitations;
    }

    public void setSentInvitations(List<UserInvitation> sentInvitations) {
        this.sentInvitations = sentInvitations;
    }

    public Set<UserLogin> getUserLogins() {
        return this.userLogins;
    }

    public void setUserLogins(Set<UserLogin> userLogins) {
        this.userLogins = userLogins;
    }

    public EmailVerification getEmailVerification() {
        return emailVerification;
    }

    public void setEmailVerification(EmailVerification emailVerification) {
        this.emailVerification = emailVerification;
    }

    public GsmVerification getGsmVerification() {
        return gsmVerification;
    }

    public void setGsmVerification(GsmVerification gsmVerification) {
        this.gsmVerification = gsmVerification;
    }

    public SubscriptionOption getSubscriptionOption() {
        return subscriptionOption;
    }

    public void setSubscriptionOption(SubscriptionOption subscriptionOption) {
        this.subscriptionOption = subscriptionOption;
    }

    public Integer getPointsBalance() {
        return pointsBalance;
    }

    public void setPointsBalance(Integer pointsBalance) {
        this.pointsBalance = pointsBalance;
    }

    @Transient
    public String getSalt() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        return sdf.format(this.getCreateDate()) + this.getUsername();
    }

    public void addAddress(Address address) {
        if (!this.addresses.contains(address)) {
            this.addresses.add(address);
            address.setUser(this);
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Transient
    public int getAge() {
        Calendar calendar = Calendar.getInstance();

        long timeInMillis = calendar.getTimeInMillis();
        int year = calendar.get(Calendar.YEAR);

        calendar.setTime(getBirthday());
        int birthYear = calendar.get(Calendar.YEAR);

        int age = year - birthYear;

        calendar.add(Calendar.YEAR, age);
        long endInMillis = calendar.getTimeInMillis();

        if (endInMillis > timeInMillis) {
            age = age - 1;
        }

        return age;
    }

    @Transient
    public String getName() {
        return (this.getFirstName() != null ? this.getFirstName() : "") + (this.getLastName() != null ? " " + this.getLastName().substring(0, 1).toUpperCase() + "." : "");
    }

    @Transient
    public String getFullName() {
        return (this.getFirstName() != null ? this.getFirstName() : "") + (this.getLastName() != null ? " " + this.getLastName() : "");
    }

    @Transient
    public String getPhone() {
        if (this.getGsmVerification() != null) {
            final GsmPrefix gsmPrefix = this.getGsmVerification().getGsmPrefix();
            String phone = "";
            if (gsmPrefix != null) {
                phone += gsmPrefix.getCode() + " ";
            }

            if (this.getGsmVerification().getGsmNumber() != null) {
                phone += this.getGsmVerification().getGsmNumber();
            }

            return phone;
        }

        return "";
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
        roles.add(new GrantedAuthorityImpl("ROLE_USER"));
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public EducationType getEducationType() {
        return educationType;
    }

    public void setEducationType(EducationType educationType) {
        this.educationType = educationType;
    }

    public EmploymentStatusType getEmploymentStatusType() {
        return employmentStatusType;
    }

    public void setEmploymentStatusType(EmploymentStatusType employmentStatusType) {
        this.employmentStatusType = employmentStatusType;
    }

    public GenderType getGenderType() {
        return genderType;
    }

    public void setGenderType(GenderType genderType) {
        this.genderType = genderType;
    }

    public HasChildrenType getHasChildrenType() {
        return hasChildrenType;
    }

    public void setHasChildrenType(HasChildrenType hasChildrenType) {
        this.hasChildrenType = hasChildrenType;
    }

    public MaritalStatusType getMaritalStatusType() {
        return maritalStatusType;
    }

    public void setMaritalStatusType(MaritalStatusType maritalStatusType) {
        this.maritalStatusType = maritalStatusType;
    }

    public MonthlyNetTLIncomeType getMonthlyNetTLIncomeType() {
        return monthlyNetTLIncomeType;
    }

    public void setMonthlyNetTLIncomeType(
            MonthlyNetTLIncomeType monthlyNetTLIncomeType) {
        this.monthlyNetTLIncomeType = monthlyNetTLIncomeType;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }

    public int getNoOfOrderableProducts() {
        return noOfOrderableProducts;
    }

    public void setNoOfOrderableProducts(int noOfOrderableProducts) {
        this.noOfOrderableProducts = noOfOrderableProducts;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }
    
    public Integer getFastResponseCounter() {
		return fastResponseCounter;
	}

	public void setFastResponseCounter(Integer fastResponseCounter) {
		this.fastResponseCounter = fastResponseCounter;
	}

	@Transient
    public Address getMainAddress() {
        if (this.addresses != null && !this.addresses.isEmpty()) {
            for (Address address : this.addresses) {
                if (address.getPrimary() == true) {
                    return address;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "User [id=" + getId() + ",password=" + password + ", username=" + username + ", active=" + active
                + ", birthday=" + birthday + ", createDate=" + createDate + ", email=" + email + ", emailVerification="
                + emailVerification + ", gsmVerification=" + gsmVerification + ", subscriptionOption="
                + subscriptionOption + ",  acceptedInvitation=" + acceptedInvitation;
    }
}