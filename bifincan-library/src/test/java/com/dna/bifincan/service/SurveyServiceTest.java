package com.dna.bifincan.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.dna.bifincan.model.products.Brand;
import com.dna.bifincan.repository.survey.SurveyRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext-test.xml" })
public class SurveyServiceTest extends AbstractTransactionalJUnit4SpringContextTests {
    private final static Logger log = LoggerFactory.getLogger(SurveyServiceTest.class);

    @PersistenceContext
    EntityManager em;

    @BeforeClass
    public static void createEntityManagerFactory() {
	log.debug("beforeClass...");
    }

    @AfterClass
    public static void closeEntityManagerFactory() {
	log.debug("afterClass...");
    }

    @Before
    public void beginTransaction() {
    }

    @After
    public void rollbackTransaction() {

    }

    @Autowired
    SurveyRepository service;
    @SuppressWarnings("unused")
	private Brand brand;

    @Transactional
    void initializeData() {
	// em.getTransaction().begin();
//	brand = new Brand("Simsung");
//	em.persist(brand);
//
//	BrandQuestion brandQuestion = new BrandQuestion("Do u like Simsung mobile?");
//	em.persist(brandQuestion);
//
//	BrandQuestion brandQuestion2 = new BrandQuestion("Do u like Simsung TV?");
//	em.persist(brandQuestion2);
//
//	brand.getQuestions().add(brandQuestion);
//	brandQuestion.getBrands().add(brand);
//	// em.flush();
//
//	brand.getQuestions().add(brandQuestion);
//	brandQuestion2.getBrands().add(brand);
//	// em.flush();
//	// em.getTransaction().commit();
//    }
//
//    private void destroyData() {
//	this.deleteFromTables(" survey_question_brand", " survey_question", "brand");
//    }
//
//    @Inject
//    UserService userService;
//
//    @Test
//    public void testRandomQuestionByBrand() {
//	User user = userService.findUserByUsername("hantsy");
//	initializeData();
//	this.brand = em.merge(this.brand);
//
//	BrandQuestionDTO brandQuestion = service.randomBrandQuestionByUser(user);
//	log.debug("@brand question @" + brandQuestion);
//
//	log.debug("brand id@" + this.brand);
//	BrandQuestion bq = service.randomQuestionByBrand(this.brand);
//	log.debug("question 1 @" + bq.getTitle());
//	BrandQuestion bq2 = service.randomQuestionByBrand(this.brand);
//	log.debug("question 2 @" + bq2.getTitle());
	//destroyData();

    }

    @Test
    public void testNextRandomQuestion() {
//	prepareNextRandomQuestion();
//	User user = userService.findUserByUsername("hantsy");
//
//	SurveyQuestion q = service.nextRandomQuestion(null, user);
//	log.debug("qestion 1....@" + q.getTitle());
//
//	SurveyAnswer answer = new SurveyAnswer();
//	answer.setUser(user);
//	answer.setSurveyQuestion(q);
//	List<SurveyOption> options = q.getOptions();
//
//	SurveyOption _yes = null;
//	for (SurveyOption option : options) {
//	    if (option.getTitle().equals("yes")) {
//		_yes = option;
//		break;
//	    }
//	}
//
//	log.debug("qestion 1 answer option....@" + _yes);
//
//	List<SurveyOption> _answerOptions = new ArrayList<SurveyOption>();
//	if (_yes != null) {
//	    _answerOptions.add(_yes);
//	    answer.setSurveyOptions(_answerOptions);
//	}
//	em.persist(answer);
//	log.debug("qestion 1 answer....@" + answer);
//
//	SurveyQuestion q2 = service.nextRandomQuestion(_answerOptions, user);
//	log.debug("qestion 2....@" + q2.getTitle());
//
//	destroyNextRandomQuestion();

    }

    @Transactional
    public void prepareNextRandomQuestion() {
	// FOR EXAMPLE:
	//
	// Q1: Are you hungry
	// A1.1: yes
	// A1.2: no
	// A1.3: undecided
	//
	// Q2: What do you want to eat
	// A2.1: hamburger
	// A2.2: hotdog
	//
	// Q3: Why are you not hungry:
	// A3.1: blabla
	// A3.2: blablabla
	//
	// in this example
	//
	// A1.1 restricts Q3
	// A1.2 restricts Q2
	// A1.1 required by Q2
	// etc

	//ProfileQuestion q1 = new ProfileQuestion("Are you hungry");
	// q1.addOption(new SurveyOption("yes"));
	// q1.addOption(new SurveyOption("no"));
	/* ///q1.addOption(new SurveyOption("undecided"));
	em.persist(q1);

	SurveyOption _yes = new SurveyOption("yes");
	em.persist(_yes);
	q1.addOption(_yes);

	SurveyOption _no = new SurveyOption("no");
	em.persist(_no);
	q1.addOption(_no);

	ProfileQuestion q2 = new ProfileQuestion("What do you want to eat");
	q2.addOption(new SurveyOption("hamburger"));
	q2.addOption(new SurveyOption("hotdog"));
	em.persist(q2);

	ProfileQuestion q3 = new ProfileQuestion("Why are you not hungry");
	q3.addOption(new SurveyOption("blabla"));
	q3.addOption(new SurveyOption("blablabla"));
	em.persist(q3);

	ProfileQuestion q4 = new ProfileQuestion("Another question which has no restriction...");
	q4.addOption(new SurveyOption("abc"));
	q4.addOption(new SurveyOption("1234...."));
	em.persist(q4);

	SurveyQuestionCriteria c1 = new SurveyQuestionCriteria();
	c1.setRule(SurveyQuestionCriteriaType.OPTION_RESTRICTS_QUESTION);
	c1.setQuestion(q3);
	c1.setOption(_yes);
	em.persist(c1);

	SurveyQuestionCriteria c2 = new SurveyQuestionCriteria();
	c2.setRule(SurveyQuestionCriteriaType.OPTION_RESTRICTS_QUESTION);
	c2.setQuestion(q2);
	c2.setOption(_no);
	em.persist(c2);

	SurveyQuestionCriteria c3 = new SurveyQuestionCriteria();
	c3.setRule(SurveyQuestionCriteriaType.OPTION_REQUIRED_BY_QUESTION);
	c3.setQuestion(q2);
	c3.setOption(_yes);
	em.persist(c3);////*/

    }

    public void destroyNextRandomQuestion() {
    	//this.deleteFromTables("survey_criteria", "survey_option", "survey_answer", "survey_question");
    }

}
