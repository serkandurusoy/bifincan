package com.dna.bifincan.integration.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dna.bifincan.integration.SurveyServiceTest;
import com.dna.bifincan.model.survey.Survey;
import com.dna.bifincan.model.survey.SurveyAnswer;
import com.dna.bifincan.model.survey.SurveyBrand;
import com.dna.bifincan.model.survey.SurveyBrandProductCategory;
import com.dna.bifincan.model.survey.SurveyCriteria;
import com.dna.bifincan.model.survey.SurveyOption;
import com.dna.bifincan.model.survey.SurveyProduct;
import com.dna.bifincan.model.survey.SurveyProductCategory;
import com.dna.bifincan.model.survey.SurveyProfile;
import com.dna.bifincan.model.survey.SurveyQuestion;
import com.dna.bifincan.model.survey.SurveyQuestionCriteria;
import com.dna.bifincan.model.type.SurveyQuestionCriteriaType;
import com.dna.bifincan.model.type.SurveyType;
import com.dna.bifincan.model.user.SubscriptionOption;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.model.user.verification.EmailVerification;
import com.dna.bifincan.model.user.verification.GsmVerification;
import com.dna.bifincan.repository.survey.SurveyAnswerRepository;
import com.dna.bifincan.repository.survey.SurveyQuestionCriteriaRepository;
import com.dna.bifincan.repository.survey.SurveyQuestionRepository;
import com.dna.bifincan.repository.survey.SurveyRepository;
import com.dna.bifincan.service.SurveyService;

public class SurveyDataManager extends SurveyService {
	protected final static Logger log = LoggerFactory.getLogger(SurveyServiceTest.class);
	
	protected final String STANDARD_SURVEY = "Car preferences profile";
	protected final String SECOND_SURVEY = "Movie preferences profile";
	protected final String THIRD_SURVEY = "Food preferences profile";
	
	protected List<Survey> surveys = new ArrayList<Survey>();

    @PersistenceContext
    protected EntityManager em;
    
    @SuppressWarnings("unused")
	private SurveyRepository surveyRepository;
    @SuppressWarnings("unused")
	private SurveyQuestionRepository surveyQuestionRepository;
    @SuppressWarnings("unused")
	private SurveyAnswerRepository surveyAnswerRepository;

    @SuppressWarnings("unused")
	private SurveyQuestionCriteriaRepository surveyQuestionCriteriaRepository;   
    
    public SurveyDataManager() { }
    
  /*  @Inject
	public SurveyDataManager(SurveyRepository surveyRepository,
			SurveyQuestionRepository surveyQuestionRepository,
			SurveyAnswerRepository surveyAnswerRepository,
			SurveyAgeCriteriaRepository surveyAgeCriteriaRepository,
			SurveyAddressCriteriaRepository surveyAddressCriteriaRepository,
			SurveyOptionCriteriaRepository surveyOptionCriteriaRepository,
			SurveyQuestionCriteriaRepository surveyQuestionCriteriaRepository) {
		super(surveyRepository, surveyQuestionRepository, surveyAnswerRepository, 
				surveyAgeCriteriaRepository, surveyAddressCriteriaRepository, surveyOptionCriteriaRepository,
				surveyQuestionCriteriaRepository);
		this.surveyRepository = surveyRepository;
		this.surveyQuestionRepository = surveyQuestionRepository;
		this.surveyAnswerRepository = surveyAnswerRepository;
		this.surveyAgeCriteriaRepository = surveyAgeCriteriaRepository;
		this.surveyAddressCriteriaRepository = surveyAddressCriteriaRepository;
		this.surveyOptionCriteriaRepository = surveyOptionCriteriaRepository;
		this.surveyQuestionCriteriaRepository = surveyQuestionCriteriaRepository;
	}*/

    @SuppressWarnings("unused")
	@Before
	public void createTestData() {
    	log.info(">>>>>>> Initialing Survey Test Data.... <<<<<<<<<");
    	
    	try {
    		deleteThemAll();
    	} catch(Exception ex) { 
    		ex.printStackTrace();
    	}
    	
    	addStandardUser();
    	
    	Survey survey1 = createProfileSurvey(SurveyType.PROFILE, STANDARD_SURVEY , true, 1L);
    	survey1.addSurveyQuestion(createSurveyQuestion("Do you like cars", 1, survey1, 
    			new Object[][] {  {"yes", true, 1L} , {"no", true, 1L} },
    			true, 1, false));
    	survey1.addSurveyQuestion(createSurveyQuestion("Do you like small or big cars", 2, survey1, 
    			new Object[][] {  {"small", true, 1L} , {"big", true, 1L} },
    			true, 1, false));
    	survey1.addSurveyQuestion(createSurveyQuestion("Do you like sport or classic cars", 3, survey1, 
    			new Object[][] {  {"sport", true, 1L} , {"classic", true, 1L}, {"futuristic", false, 1L} },
    			true, 1, false));
    	survey1.addSurveyQuestion(createSurveyQuestion("Do you like cabrio sport cars", 4, survey1, 
    			new Object[][] {  {"yes", true, 1L} , {"no", true, 1L} },
    			true, 1, false));
    	surveys.add(survey1);
    	
    	for(Survey survey : surveys) {
    		try {
    			for(Survey tSurvey : surveys) {
    				em.persist(tSurvey);
    			}
    		} catch(Exception ex) { 
    			ex.printStackTrace();
    		}
    	}  
    	
    	log.info(">>>>>>> INITIALIZING Survey Test Data : SUCCESS <<<<<<<<<");
    }

	@After
	public void destroyTestData() {
    	deleteThemAll();
    }    
    
    @SuppressWarnings("unchecked")
	private void deleteThemAll() {
    	log.info(">>>>>>> Deleting Survey Test Data.... <<<<<<<<<");

    	// delete survey criteria
		Query sCriQuery = em.createQuery("select sc from SurveyCriteria sc");			
		
		List<SurveyCriteria> sCriteria = (List<SurveyCriteria>)sCriQuery.getResultList(); 
		
		if(sCriteria != null) {
			for(SurveyCriteria criterio : sCriteria) {
				em.remove(criterio);
			}
		}
		
    	// delete question criteria
		Query criQuery = em.createQuery("select sc from SurveyQuestionCriteria sc");			
		
		List<SurveyQuestionCriteria> criteria = (List<SurveyQuestionCriteria>)criQuery.getResultList(); 
		
		if(criteria != null) {
			for(SurveyQuestionCriteria criterio : criteria) {
				em.remove(criterio);
			}
		}

		
    	// delete answers
		Query ansQuery = em.createQuery("select sa from SurveyAnswer sa");			

		List<SurveyAnswer> answers = (List<SurveyAnswer>)ansQuery.getResultList(); 

		if(answers != null) {
			for(SurveyAnswer answer : answers) {
				em.remove(answer);
			}
		}
		
		// delete surveys
    	for(Survey survey : surveys) {
    		try {
    			Query query = em.createQuery("select s from Survey s where s.title = :title");
    			query.setParameter("title", survey.getTitle()); 
    			
    			List<Survey> tSurveys = (List<Survey>)query.getResultList();
    			
    			if(tSurveys != null) {
    				for(Survey tSurvey : tSurveys) {
    					log.info(">>> Deleting survey " + tSurvey.getTitle() + " with id " + tSurvey.getId());
    					em.remove(tSurvey);
    				}
    			}    			
    		} catch(Exception ex) { 
    			ex.printStackTrace();
    		}
    	}  
    	
    	// delete additional users
		Query uQuery = em.createQuery("select u from User u where u.password = 'qwaszx12'");			
		
		List<User> users = (List<User>)uQuery.getResultList(); 
		
		if(users != null) {
			for(User user : users) {
				em.remove(user);
			}
		}
		
    	log.info(">>>>>>> DELETING Survey Test Data : SUCCESS <<<<<<<<<");    	
    }
	private Survey createProfileSurvey(SurveyType type, String title, boolean active, long version) {
    	Survey survey = null;
    	
    	switch(type) {
    		case PROFILE : survey = new SurveyProfile();
    			break;
    		case BRAND : survey = new SurveyBrand();
    			break;
    		case PROD_CAT : survey = new SurveyProductCategory();
    			break;
    		case BRAND_PROD_CAT : survey = new SurveyBrandProductCategory();
    			break;
    		case PROD : survey = new SurveyProduct();
    	}
    	
    	survey.setTitle(title);
    	survey.setActive(active);
    	survey.setVersion(version);
    	
    	return survey;
    }
    
	private SurveyQuestion createSurveyQuestion(String title, int position, Survey survey, 
			Object[][] opts, boolean active, long version, boolean selectMultiple) {
    	SurveyQuestion surveyQuestion = new SurveyQuestion();
    	
    	surveyQuestion.setTitle(title);
    	surveyQuestion.setPosition(position);
    	surveyQuestion.setSurvey(survey);
    	surveyQuestion.setVersion(version);
    	surveyQuestion.setSelectMultiple(selectMultiple);
    	surveyQuestion.setActive(active);
    	
    	List<SurveyOption> options = null;
    	if(opts != null) {
    		options = new ArrayList<SurveyOption>();
    		for(int i = 0;i < opts.length; i++) {
    			options.add(createSurveyOption((String)opts[i][0], surveyQuestion,   // title & parent question
    					(Boolean)opts[i][1], (Long)opts[i][2]));  // active & version
    		}
    	}
    	surveyQuestion.setOptions(options);
    	
    	return surveyQuestion;
    }
    
	private SurveyOption createSurveyOption(String title, SurveyQuestion question, boolean active, long version) {
    	SurveyOption option = new SurveyOption();
    	
    	option.setTitle(title);
    	option.setQuestion(question);
    	option.setActive(active);
    	option.setVersion(version);
    	
    	return option;
    }   
	
	protected User getStandardUser() {
		User user = null;
		try {
			Query query = em.createQuery("select u from User u where u.username = 'halloworld12'");
		
			user = (User)query.getSingleResult();
		} catch(Exception ex) { }
    	return user;
	}
	
	protected Survey getSurvey(String title) {  // possible because the sample data are constructed for each test case
		Survey survey = null;
		for(Survey tSurvey : surveys) {
			if(title.equals(tSurvey.getTitle())) {
				survey = tSurvey;
			}
			
		}
    	return survey;
	}
	
	protected void addSimpleAnswer(SurveyQuestion question, String answerTitle,  User user) {
		SurveyAnswer answer = new SurveyAnswer();

		List<SurveyOption> options = (List<SurveyOption>)question.getOptions();
		
		List<SurveyOption> answerOptions = new ArrayList<SurveyOption>();
		
		SurveyOption answerOption = findOptionFromList(answerTitle, options);
		answerOptions.add(answerOption);
		
		answer.setSurveyQuestion(question);
		answer.setSurveyOptions(answerOptions);
		
		answer.setUser(user);
		
		answer.setAttendanceTime(new java.util.Date());
		answer.setRetries(1);
		answer.setVersion(1L);
		answer.setBrand(null);
		answer.setProduct(null);
		answer.setProductCategory(null);
		
		em.persist(answer);
	}
	
	protected void disableQuestionInPosition(int position) {
		//try {
			Query query = em.createQuery("select s from SurveyQuestion s where s.position = :position");
			query.setParameter("position", position); 
			
			SurveyQuestion question = (SurveyQuestion)query.getSingleResult();
			
			question.setActive(false);
			
			em.merge(question);
		//} catch(Exception ex) { 
		//	ex.printStackTrace();
		//}		
	}
	
	private SurveyOption findOptionFromList(String title, List<SurveyOption> options) {
		SurveyOption option = null;
		if(options != null) {
			for(SurveyOption tOption : options) {
				if(title.equals(tOption.getTitle())) { 
					option = tOption;  // optionally cloning might be safer (but rather not necessary) 
					break;
				}	
			}
		}
		return option;
	}
	
	protected void addSecondSurvey() {
    	Survey survey2 = createProfileSurvey(SurveyType.PROFILE, SECOND_SURVEY, true, 1L);
    	survey2.addSurveyQuestion(createSurveyQuestion("How often do you use a car daily?", 1, survey2, 
    			new Object[][] {  {"yes", true, 1L} , {"no", true, 1L} },
    			true, 1, false));
    	
    	em.persist(survey2);
    	surveys.add(survey2);
	}
	
	protected void addThirdSurvey() {
    	Survey survey3 = createProfileSurvey(SurveyType.PROFILE, THIRD_SURVEY, true, 1L);
    	em.persist(survey3);
    	surveys.add(survey3);
	}
	
	protected void disableSecondSurvey() {
		//try {
			Query query = em.createQuery("select s from Survey s where s.title = 'Movie preferences profile'");
			
			Survey survey2 = (Survey)query.getSingleResult();
			
			survey2.setActive(false);
			
			em.merge(survey2);
	//	} catch(Exception ex) { 
		//	ex.printStackTrace();
		//}
	}
	
	protected void installSampleQuestionCriteria1() {
		createQuestionCriteria(1, "yes", 2, SurveyQuestionCriteriaType.OPTION_REQUIRED_BY_QUESTION);
		createQuestionCriteria(1, "no", 2, SurveyQuestionCriteriaType.OPTION_RESTRICTS_QUESTION);
		createQuestionCriteria(1, "no", 3, SurveyQuestionCriteriaType.OPTION_RESTRICTS_QUESTION);
		createQuestionCriteria(1, "no", 4, SurveyQuestionCriteriaType.OPTION_RESTRICTS_QUESTION);
		
		createQuestionCriteria(2, "big", 3, SurveyQuestionCriteriaType.OPTION_REQUIRED_BY_QUESTION);
		createQuestionCriteria(2, "small", 3, SurveyQuestionCriteriaType.OPTION_RESTRICTS_QUESTION);
		createQuestionCriteria(2, "small", 4, SurveyQuestionCriteriaType.OPTION_RESTRICTS_QUESTION);

		createQuestionCriteria(3, "sport", 4, SurveyQuestionCriteriaType.OPTION_REQUIRED_BY_QUESTION);
		createQuestionCriteria(3, "classic", 4, SurveyQuestionCriteriaType.OPTION_RESTRICTS_QUESTION);
		createQuestionCriteria(3, "futuristic", 4, SurveyQuestionCriteriaType.OPTION_RESTRICTS_QUESTION);
		
	}
	
	protected void installSampleQuestionCriteria2() {
		createQuestionCriteria(1, "yes", 2, SurveyQuestionCriteriaType.OPTION_REQUIRED_BY_QUESTION);
		createQuestionCriteria(1, "no", 2, SurveyQuestionCriteriaType.OPTION_RESTRICTS_QUESTION);
		createQuestionCriteria(1, "no", 3, SurveyQuestionCriteriaType.OPTION_RESTRICTS_QUESTION);
		createQuestionCriteria(1, "no", 4, SurveyQuestionCriteriaType.OPTION_RESTRICTS_QUESTION);
		
		createQuestionCriteria(2, "big", 3, SurveyQuestionCriteriaType.OPTION_REQUIRED_BY_QUESTION);
		createQuestionCriteria(2, "small", 3, SurveyQuestionCriteriaType.OPTION_RESTRICTS_QUESTION);

		createQuestionCriteria(3, "sport", 4, SurveyQuestionCriteriaType.OPTION_REQUIRED_BY_QUESTION);
		createQuestionCriteria(3, "classic", 4, SurveyQuestionCriteriaType.OPTION_RESTRICTS_QUESTION);
		createQuestionCriteria(3, "futuristic", 4, SurveyQuestionCriteriaType.OPTION_RESTRICTS_QUESTION);
		
	}
	
	private void createQuestionCriteria(int sourcePosition, String answer, int targetPosition, 
				SurveyQuestionCriteriaType rule) {
		//try {
			Query query = em.createQuery("select s from SurveyQuestion s where s.position = :position");
			
			query.setParameter("position", sourcePosition); 
			SurveyQuestion sourceQuestion = (SurveyQuestion)query.getSingleResult();
			
			query.setParameter("position", targetPosition); 
			SurveyQuestion targetQuestion = (SurveyQuestion)query.getSingleResult();

			SurveyOption reqOption = null;
			List<SurveyOption> options = sourceQuestion.getOptions();
			if(options != null) {
				for(SurveyOption option : options) {
					if(answer.equals(option.getTitle())) {
						reqOption = option;
						break;
					}
				}
			}
			
			SurveyQuestionCriteria criterion = new SurveyQuestionCriteria();
			
			criterion.setQuestion(targetQuestion);
			criterion.setRule(rule);
			criterion.setOption(reqOption);
			criterion.setVersion(1L);
			
			em.persist(criterion);
		///} catch(Exception ex) { 
		//	ex.printStackTrace();
		//}			
	}
	
	
	private User addStandardUser() {
		User user = addUser(100, "halloworld12");
		return user;
	}
	
	protected User addUser(int year, String username) {
		User user = new User();
		
		user.setActive(true);user.setVersion(1L);
		
		java.util.Date birthdate = new java.util.Date();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		birthdate = calendar.getTime();
		
		user.setBirthday(birthdate);user.setCreateDate(new java.util.Date());user.setEmail(year + "xyz@qwerty.com");
		
		EmailVerification ev = new EmailVerification();
		ev.setEmailVerificationSentTime(new java.util.Date());ev.setEmailVerified(true);
		
		user.setEmailVerification(ev);user.setFirstName("Firstname" + year);
		
		GsmVerification gv = new GsmVerification();
		gv.setGsmVerificationSentTime(new java.util.Date());gv.setGsmVerified(true);
		
		user.setGsmVerification(gv);user.setLastName("Lastname" + year);user.setPassword("qwaszx12");
	
		SubscriptionOption so = new SubscriptionOption();
		so.setAcceptSiteMail(true);so.setAcceptThirdpartyMail(true);so.setAcceptThirdpartySms(true);
		
		user.setSubscriptionOption(so);user.setUsername(username == null?"2w3e4r5t" + year:username);
		
		em.persist(user);
		
		return user;
	}
	
	protected void installSampleSurveyCriteriaAge() {
		createSurveyAgeCriteria(STANDARD_SURVEY, 21, 30);
		createSurveyAgeCriteria(SECOND_SURVEY, 31, 40);
		createSurveyAgeCriteria(THIRD_SURVEY, 41, 50);
	}
	
	private void createSurveyAgeCriteria(String surveyTitle, int minAge, int maxAge) {	
/*
		SurveyAgeCriteria criterion = new SurveyAgeCriteria();
		criterion.setAgeMinimum(minAge);
		criterion.setAgeMaximum(maxAge);
		criterion.setVersion(1L);

		Query query = em.createQuery("select s from Survey s where s.title = :title");

		query.setParameter("title", surveyTitle); 
		Survey survey = (Survey)query.getSingleResult();
	
		criterion.setSurvey(survey);
		
		em.persist(criterion);*/
	}
	
	protected void installSampleSurveyOption(Survey survey1, int position, 
				String answer, Survey survey2) {
		
		SurveyOption reqOption = null;
		if(survey1.getSurveyQuestions() != null) {
			for(SurveyQuestion question : survey1.getSurveyQuestions()) {
				List<SurveyOption> options = question.getOptions();
				if(question.getPosition() == position) {
					if(options != null) {
						reqOption = findOptionFromList(answer, options);
					}	
					break;
				}
			}	
		}
		/*
		SurveyOptionCriteria criterion = new SurveyOptionCriteria();
		
		criterion.setSurveyOption(reqOption);
		criterion.setSurvey(survey2);
		criterion.setVersion(1L);
		
		em.persist(criterion);*/		
	}	
}
