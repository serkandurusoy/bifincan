package com.dna.bifincan.repository.user;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.dna.bifincan.model.user.EmailCampaign;
import com.dna.bifincan.model.user.EmailGroup;
import com.dna.bifincan.model.user.EmailListEntry;

public interface EmailListEntryRepository extends CrudRepository<EmailListEntry, String> {

	@Query("select o from EmailListEntry o where o.group = ?1 and o.optedOut = false")
	public List<EmailListEntry> findByGroup(EmailGroup group); 	
	
	public List<EmailListEntry> findByEmail(String email); 
}
