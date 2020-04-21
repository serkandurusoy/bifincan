package com.dna.bifincan.model.config;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "configuration")
public class Configuration implements Serializable {
	private static final long serialVersionUID = 3813508681465254544L;

	@Id
	@Column(name = "config_key", nullable = false, unique = true, length = 64)
	private String key;

	@Column(name = "config_value", nullable = false, length = 64)
	@NotNull
	@NotEmpty
	private String value;

	@Column(name = "config_description", nullable = false, length = 255)
	@NotNull
	@NotEmpty
	private String description;

	public Configuration() { }

	public Configuration(String key, String value, String description) { 
		this.key = key;
		this.value = value;
		this.description = description;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Configuration other = (Configuration) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Configuration [key=" + key + ", value=" + value + "]";
	}

}
