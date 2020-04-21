package com.dna.bifincan.model.user;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * The persistent class for the user database table.
 *
 */
@Entity
@Table(name = "admin_user", uniqueConstraints = {
    @UniqueConstraint(name = "UQ_username", columnNames = "username")})
@NamedQueries({
    @NamedQuery(name = "AdminUserUser.findByUsername", query = "select u from AdminUser u where u.username=:username"),
    @NamedQuery(name = "AdminUserUser.findByEmail", query = "select u from AdminUser u where u.email=:email")})
@Access(AccessType.FIELD)
public class AdminUser extends com.dna.bifincan.model.BaseEntity implements UserDetails, Serializable {

    private static final long serialVersionUID = -6409644848220709964L;

    @Column(name = "firstname", nullable = false, length = 48)
    @NotNull
    @NotEmpty
    private String firstName;

    @Column(name = "lastname", nullable = false, length = 48)
    @NotNull
    @NotEmpty
    private String lastName;

    @Column(name = "password", nullable = false, length = 256)
    @NotNull
    @NotEmpty
    @Length(min = 6, max = 256)
    private String password;

    @Column(name = "username", nullable = false, length = 20)
    @NotNull
    @NotEmpty
    @Length(min = 6, max = 20)
    @Pattern(regexp = "^[a-z0-9]([a-z0-9_]+)$", message = "Username must be consist of characters and digits.")
    private String username;

    @Column(name = "active", nullable = false, columnDefinition = "TINYINT(1) unsigned")
    private boolean active = true;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false)
    @NotNull
    private Date createDate = new Date();

    @Column(name = "email", nullable = false, length = 250)
    @NotNull
    @NotEmpty
    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type")
    private AdminRoleType roleType = AdminRoleType.ROLE_ADMINISTRATOR;

    public AdminUser() {
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public AdminRoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(AdminRoleType roleType) {
        this.roleType = roleType;
    }

    @Transient
    public String getSalt() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        return sdf.format(this.getCreateDate()) + this.getUsername();
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
        roles.add(new GrantedAuthorityImpl(this.roleType.toString()));
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

    @Override
    public boolean isEnabled() {
        return this.active;
    }
}