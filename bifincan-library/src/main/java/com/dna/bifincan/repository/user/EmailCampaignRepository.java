package com.dna.bifincan.repository.user;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.dna.bifincan.model.user.EmailCampaign;

public interface EmailCampaignRepository extends CrudRepository<EmailCampaign, Long> {
	
	@Query("select o from EmailCampaign o where o.active = true and o.timeScheduled < ?1 and o.timeStart is null")
	public List<EmailCampaign> findPastScheduledCampaignsByDate(Date currentDate); 	
}
