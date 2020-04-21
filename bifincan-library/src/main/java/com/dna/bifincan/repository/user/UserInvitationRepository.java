package com.dna.bifincan.repository.user;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.user.User;
import com.dna.bifincan.model.user.UserInvitation;

public interface UserInvitationRepository extends CrudRepository<UserInvitation, Long> {

    public UserInvitation findByInviterAndEmail(@Param("inviter") User inviter, @Param("email") String email);

    @Query("select o from UserInvitation o where o.email = :email and o.optedOut = 0")
    public List<UserInvitation> findByEmailAndOptedOut(@Param("email") String email);

    @Query("select count(userInvitation) from UserInvitation userInvitation where userInvitation.inviter = :user and userInvitation.createTime >= :startCalendar and userInvitation.createTime <= :endCalendar")
    public Long countUserInvitationsByDate(@Param("startCalendar") Date startCalendar, @Param("endCalendar") Date endCalendar,
            @Param("user") User user);

    public List<UserInvitation> findByEmail(String email);

    public List<UserInvitation> findByInviter(User inviter);

    @Query("select count(o) from UserInvitation o where o.inviter =:_user")
    public long countByInviter(@Param("_user") User _user);

    @Query("select o from UserInvitation o where o.optedOut=false and o.lastInvitationTime< :last_time")
    public  List<UserInvitation> findUnsigned(@Param("last_time") Date _timestamp);
    
    @Query("select o.inviter.id from UserInvitation o where o.inviter.id in (?2) and o.createTime = (select max(o1.createTime) from UserInvitation o1 where o1.inviter.id = o.inviter.id) and o.createTime >?1")
    public List<Long> findIdsByRecentInvitationActivityAndTargetList(Date refDate, List<Long> userIds);    

	@Query("select distinct(o.email) from UserInvitation o where o.optedOut = false")
	public List<String> findActiveNotOptedOut();
}
