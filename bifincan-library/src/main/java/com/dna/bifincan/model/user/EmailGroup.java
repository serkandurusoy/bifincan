package com.dna.bifincan.model.user;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.dna.bifincan.model.BaseEntity;

/**
 * The persistent class for the email_group database table.
 * 
 */
@Entity
@Table(name="email_group")
public class EmailGroup extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 5635444178334372706L;

    @Column(name = "name", nullable = false, length = 250)
    @NotNull
    @NotEmpty
	private String name;
    
    public EmailGroup() { }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
        
}
