package com.dna.bifincan.repository.user;

import com.dna.bifincan.model.address.City;
import com.dna.bifincan.model.user.EducationType;
import com.dna.bifincan.model.user.EmploymentStatusType;
import com.dna.bifincan.model.user.GenderType;
import com.dna.bifincan.model.user.HasChildrenType;
import com.dna.bifincan.model.user.MaritalStatusType;
import com.dna.bifincan.model.user.MonthlyNetTLIncomeType;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.user.User;
import com.dna.bifincan.model.user.UserLevelValue;
import java.util.Date;

public interface UserRepository extends PagingAndSortingRepository<User, Long>, UserRepositoryCustom {

    public User findByUsername(@Param("username")
    String username);

    public User findByEmail(@Param("email")
    String email);
    
    public List<User> findAndLockAll();
    
    @Query("select new com.dna.bifincan.model.user.UserLevelValue(u.username, u.activityLevel ) from User u where u.active=true and u.ambassador=false")
    public List<UserLevelValue> findAllActiveUserLevels();
    
    // TODO: probably this will take additional args for (sorting, paging etc.)
    @Query("select u from User u")
    public List<User> findUsers(); 
    
	@Query("select count(uadd.area.district.county.city) from User u left join u.addresses uadd" +
		" where u=:user and uadd.area.district.county.city.id =:cityId")		   
	public Long findAddressMatches(@Param("user") User user, @Param("cityId") Long cityId);

	@Query("select count(uadd.area.district.county.city) from User u left join u.addresses uadd" +
			" where u=:user and uadd.area.district.county.city.id !=:cityId")		   
	public Long findAddressNotMatches(@Param("user") User user, @Param("cityId") Long cityId);
	
	@Query("select count(opt) from SurveyAnswer sa inner join sa.surveyOptions opt " +
		"where sa.user.id = :userId and opt.id = :optionId")		   
		public Long findOptionMatches(@Param("userId") Long userId, @Param("optionId") Long optionId); 

	@Query("select count(o) from User o where o.gsmVerification.gsmOperator.id = :operatorId")
	public Long countByGsmOperator(@Param("operatorId")Long operatorId);
	
	@Query("select count(o) from User o where o.gsmVerification.gsmPrefix.id = :operatorId")
	public Long countByGsmPrefix(@Param("operatorId")Long operatorId);	

	@Query("select count(o) from User o where o.acceptedInvitation.id = :invitationId")
	public Long countByInvitation(@Param("invitationId")Long invitationId);	
	
	@Query("select count(u) from User u where u.active = false and u.acceptedInvitation.id in (select i.id from UserInvitation i where i.inviter=:user)")
	public Long countInactiveInviteesByUser(@Param("user") User user);	
	
	@Query("select count(o) from User o where o.email = :email")
	public Long countByUserEmail(@Param("email")String email);		
	
	@Query("select count(o) from User o where o.username = :username")
	public Long countByUsername(@Param("username")String username);	
	
	@Query("select count(o) from User o where o.gsmVerification.gsmPrefix.code = ?1 and o.gsmVerification.gsmNumber = ?2")
	public Long countByGsmPrefixAndNumber(Integer gsmPrefix, String gsmNumber);	

	@Query("select count(o) from User o where o.gsmVerification.gsmPrefix.code = ?1 and o.gsmVerification.gsmNumber = ?2 and o.id <> ?3")
	public Long countByGsmPrefixAndNumberAndUserId(Integer gsmPrefix, String gsmNumber, Long userId);	

    @Query("select o.user from UserLogin o where o.user.active = true and o.user.subscriptionOption.acceptSiteMail = true and o.user.createDate <=?1 and o.id = (select max(o1.id) from UserLogin o1 where o1.user.id = o.user.id) and o.loginTime <=?1")
    public List<User> findByLongAbsence(Date refDate);  
    
    @Query("select o.user.id from UserLogin o where o.user.active = true and o.user.subscriptionOption.acceptSiteMail = true and o.user.createDate <=?1 and o.id = (select max(o1.id) from UserLogin o1 where o1.user.id = o.user.id) and o.loginTime >?1")
    public List<Long> findIdsByRecentLogin(Date refDate);    
    
	@Query("select o from User o where o.id in (?1)")
	public List<User> findByTargetListOfIds(List<Long> userIds);	   
	
	@Query("select o from User o where o.active = true and o.emailVerification.emailVerified = true and o.subscriptionOption.acceptSiteMail = true")
	public List<User> findActiveSiteEmailAcceptedAndEmailVerified();

	@Query("select count(o) from User o where o.active = true and o.createDate <= :untilDate")
	public Long countActiveUsersUntilPreviousWeek(@Param("untilDate")Date untilDate);	

	@Query("select count(o) from User o where o.active = true")
	public Long countActiveUsers();	

        @Query("select count(o) from User o where o.active = true and o.genderType = :gender ")
        public Long countActiveByGender(@Param("gender") GenderType gender);

        @Query("select count(o) from User o where o.active = true and o.hasChildrenType = :hasChildren ")
        public Long countActiveByHasChildren(@Param("hasChildren") HasChildrenType hasChildren);

        @Query("select count(o) from User o where o.active = true and o.employmentStatusType = :employmentStatus ")
        public Long countActiveByEmploymentStatus(@Param("employmentStatus") EmploymentStatusType employmentStatus);

        @Query("select count(o) from User o where o.active = true and o.maritalStatusType = :maritalStatus ")
        public Long countActiveByMaritalStatus(@Param("maritalStatus") MaritalStatusType maritalStatus);

        @Query("select count(o) from User o where o.active = true and o.educationType = :educationType ")
        public Long countActiveByEducationType(@Param("educationType") EducationType educationType);

        @Query("select count(o) from User o where o.active = true and o.monthlyNetTLIncomeType = :monthlyIncome ")
        public Long countActiveByMonthlyIncomeType(@Param("monthlyIncome") MonthlyNetTLIncomeType monthlyIncome);

        @Query("select count(o) from User o where o.active = true and o.birthday <= :dateBegin and o.birthday > :dateEnd ")
        public Long countActiveByBirthdayRange(@Param("dateBegin") Date dateBegin, @Param("dateEnd") Date dateEnd);

}
