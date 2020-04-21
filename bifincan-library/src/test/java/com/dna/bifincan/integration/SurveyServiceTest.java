package com.dna.bifincan.integration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.dna.bifincan.integration.data.SurveyDataManager;
import com.dna.bifincan.model.survey.Survey;
import com.dna.bifincan.model.survey.SurveyQuestion;
import com.dna.bifincan.model.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext-test.xml" })
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@Transactional
public class SurveyServiceTest extends SurveyDataManager {
   
	
	/*  @Test
 //   @Ignore
    public void testFetchingRandomSurveys() {
     	log.info(">>>>>>> TEST: testFetchingRandomSurveys() <<<<<<<<<");
     	
     	// this actually tries to guess if we take random surveys just by comparing the fetched surveys
     	// upon subsequent fetches. It should be considered quite reliable but not 100% accurate..
     	addSecondSurvey(); // add a temporary sample second survey
     	
     	User user = getStandardUser();
      	Survey survey = getSurvey(STANDARD_SURVEY);

      	boolean uniqueFetch = true;
      	for(int i = 0;i <= 20 ;i++) {
      		Survey tSurvey = getRandomSurvey(user);
      		//log.info(">>> 1. id = " + tSurvey.getId());
      		if(tSurvey.getId() != survey.getId()) {
      			uniqueFetch = false;
      			break;
      		}
      	}
      	
      	assertFalse("It seems that fetching surveys is not a random process", uniqueFetch);
      	
      	log.info(">>>>>>> END TEST: testFetchingRandomSurveys() <<<<<<<<<");
    } 
    
    @Test
//    @Ignore
    public void testFetchingActiveSurveys() {
     	log.info(">>>>>>> TEST: testFetchingActiveSurveys() <<<<<<<<<");

     	addSecondSurvey(); // add a temporary sample second survey
     	disableSecondSurvey();
     	
     	User user = getStandardUser();
      	Survey survey = getSurvey(STANDARD_SURVEY);

      	boolean uniqueFetch = true;
      	for(int i = 0;i <= 20 ;i++) {
      		Survey tSurvey = getRandomSurvey(user);
      		//log.info(">>> 2. id = " + tSurvey.getId());
      		if(tSurvey.getId() != survey.getId()) {
      			uniqueFetch = false;
      			break;
      		}
      	}
      	
      	assertTrue("Fetching non-active surveys is possible", uniqueFetch);
      	
      	log.info(">>>>>>> END TEST: testFetchingActiveSurveys() <<<<<<<<<");
    } 
    
    @Test
 //   @Ignore
    public void testFetchingLastSurveyOfUser() {
    	log.info(">>>>>>> INSIDE testFetchingLastSurveyOfUser() <<<<<<<<<");
    	User user = getStandardUser();
    	addSecondSurvey(); // add a temporary sample second survey
    	Survey survey1_1 = getSurvey(STANDARD_SURVEY);
    	
     	SurveyQuestion question1 = getNextSurveyQuestion(survey1_1, user);
     	assertTrue("Question with position 1 was not fetched", question1.getPosition() == 1);
     	addSimpleAnswer(question1, "yes", user);
     	
     	Survey survey1_2 = getUserLastSurvey(user);
     	assertTrue("A new survey is fetched while an incomplete one exists in the repository", 
     			survey1_1.getId() == survey1_2.getId());     	
     	
    	log.info(">>>>>>> END TEST: testFetchingActiveSurveys() <<<<<<<<<");
    }
    
     @Test
 //    @Ignore
     public void testFetchingQuestionsInSequenceWithoutCriteria() {
    	log.info(">>>>>>> TEST: testFetchingQuestionsInSequenceWithoutCriteria() <<<<<<<<<");
    	User user = getStandardUser();
     	Survey survey = getSurvey(STANDARD_SURVEY);
     	
     	SurveyQuestion question1 = getNextSurveyQuestion(survey, user);
     	assertTrue("Question with position 1 was not fetched", question1.getPosition() == 1);
     	addSimpleAnswer(question1, "yes", user);
     	
     	SurveyQuestion question2 = getNextSurveyQuestion(survey, user);
     	assertTrue("Question with position 2 was not fetched", question2.getPosition() == 2);
     	addSimpleAnswer(question2, "big", user);
     	
     	SurveyQuestion question3 = getNextSurveyQuestion(survey, user);
     	assertTrue("Question with position 3 was not fetched", question3.getPosition() == 3);
     	addSimpleAnswer(question3, "sport", user);
     	
     	SurveyQuestion question4 = getNextSurveyQuestion(survey, user);
     	assertTrue("Question with position 4 was not fetched", question4.getPosition() == 4);
     	addSimpleAnswer(question4, "no", user);
     	
     	SurveyQuestion question5 = getNextSurveyQuestion(survey, user);   
     	assertNull("Question with position 5 was fetched !!", question5);
     	
     	log.info(">>>>>>> END TEST: testFetchingQuestionsInSequenceWithoutCriteria() <<<<<<<<<");
     }    
  
     
     @Test
 //    @Ignore
     public void testFetchingNonActiveQuestions() {
      	log.info(">>>>>>> TEST: testFetchingNonActiveQuestions() <<<<<<<<<");
      	User user = getStandardUser();
       	Survey survey = getSurvey(STANDARD_SURVEY);
       	
       	disableQuestionInPosition(2);
       	
       	SurveyQuestion question1 = getNextSurveyQuestion(survey, user);
       	assertTrue("Question with position 1 was not fetched", question1.getPosition() == 1);
       	addSimpleAnswer(question1, "yes", user);
       	
       	SurveyQuestion questionX = getNextSurveyQuestion(survey, user);
       	assertTrue("Question with position 2 is fetched !", questionX.getPosition() != 2);
       	assertTrue("Question with position 3 was not fetched", questionX.getPosition() == 3);
       	
       	log.info(">>>>>>> END TEST: testFetchingNonActiveQuestions() <<<<<<<<<");
    } 
    

    @Test
 //   @Ignore
    public void testFetchingSurveyUponAppliedCriteriaByAge() {
     	log.info(">>>>>>> TEST: testFetchingSurveyUponAppliedCriteriaByAge() <<<<<<<<<");
     	
     	User user25 = addUser(1986,null);
     	User user35 = addUser(1976,null);
     	User user45 = addUser(1966,null);
     	
     	addSecondSurvey(); // add a second survey
     	addThirdSurvey();  // add another one
     	
        // 1st survey matches 25 years old users
        // 2nd survey matches 35 years old users
        // 3rd survey matches 45 years old users
     	installSampleSurveyCriteriaAge();  
     	
     	Survey survey25 = getSurvey(STANDARD_SURVEY);
     	assertNotNull("Survey (1st: standard) was not fetched.", survey25);
     	assertTrue("User25 was not fetched.", user25 != null && "2w3e4r5t1986".equals(user25.getUsername()));
     	boolean res1 = validateSurveyByCriteria(survey25, user25);
     	assertTrue("Survey (1st: standard) for 25 years old has not been validated succesfully.", res1);
     	
     	Survey survey35 = getSurvey(SECOND_SURVEY);
     	assertNotNull("Survey (2nd) was not fetched.", survey35);
     	assertTrue("User35 was not fetched.", user35 != null && "2w3e4r5t1976".equals(user35.getUsername()));     	
     	boolean res2 = validateSurveyByCriteria(survey35, user35);
     	assertTrue("Survey (2nd) for 35 years old has not been validated succesfully.",res2);
     	
     	Survey survey45 = getSurvey(THIRD_SURVEY);
     	assertNotNull("Survey (3rd) was not fetched.", survey45);
     	assertTrue("User45 was not fetched.", user45 != null && "2w3e4r5t1966".equals(user45.getUsername()));       	
     	boolean res3 = validateSurveyByCriteria(survey45, user45);
     	assertTrue("Survey (3rd) for 45 years old has not been validated succesfully.", res3);
   
      	log.info(">>>>>>> END TEST: testFetchingSurveyUponAppliedCriteriaByAge() <<<<<<<<<");
    }

    @Test
 //   @Ignore
    public void testFetchingSurveyUponAppliedCriteriaByOption1() {
     	log.info(">>>>>>> TEST: testFetchingSurveyUponAppliedCriteriaByOption1() <<<<<<<<<");
     	
     	addSecondSurvey(); // add a second survey
     	
     	User user = getStandardUser(); 
     	
     	Survey survey1 = getSurvey(STANDARD_SURVEY);
     	
     	SurveyQuestion question1_1 = getNextSurveyQuestion(survey1, user);
     	assertTrue("Question with position 1 was not fetched", question1_1.getPosition() == 1);
     	addSimpleAnswer(question1_1, "yes", user);
     	
     	Survey survey2 = getSurvey(SECOND_SURVEY);
     	assertNotNull("Survey 2 was not fetched.", survey2);   
     	
     	installSampleSurveyOption(survey1, 1, "yes", survey2);
     	
     	boolean res = validateSurveyByCriteria(survey2, user);
     	assertTrue("Survey 2 for <Survey1.Q1.Option1=yes> has not been validated succesfully.", res);
   
      	log.info(">>>>>>> END TEST: testFetchingSurveyUponAppliedCriteriaByOption1() <<<<<<<<<");
    }
    
    @Test
 //   @Ignore
    public void testFetchingSurveyUponAppliedCriteriaByOption2() {
       	log.info(">>>>>>> TEST: testFetchingSurveyUponAppliedCriteriaByOption2() <<<<<<<<<");
       	
       	addSecondSurvey(); // add a second survey
       	
       	User user = getStandardUser(); 
       	
       	Survey survey1 = getSurvey(STANDARD_SURVEY);
       	
       	SurveyQuestion question1_1 = getNextSurveyQuestion(survey1, user);
       	assertTrue("Question with position 1 was not fetched", question1_1.getPosition() == 1);
       	addSimpleAnswer(question1_1, "yes", user);
       	
       	Survey survey2 = getSurvey(SECOND_SURVEY);
       	assertNotNull("Survey 2 was not fetched.", survey2);   
       	
       	installSampleSurveyOption(survey1, 1, "no", survey2);
       	
       	boolean res = validateSurveyByCriteria(survey2, user);
       	assertFalse("Survey 2 for <Survey1.Q1.Option1=no> has been validated succesfully !!!.", res);
     
        log.info(">>>>>>> END TEST: testFetchingSurveyUponAppliedCriteriaByOption2() <<<<<<<<<");
    }
    
    @Test
 //   @Ignore
    public void testFetchingSurveyUponAppliedCriteriaByAgeAndOption() {
       	log.info(">>>>>>> TEST: testFetchingSurveyUponAppliedCriteriaByAgeAndOption() <<<<<<<<<");
       	
     	User user25 = addUser(1986,null);
     	User user35 = addUser(1976,null);
     	User user45 = addUser(1966,null);
     	
     	addSecondSurvey(); // add a second survey
     	addThirdSurvey();  // add another one
       	
        // 1st survey matches 25 years old users
        // 2nd survey matches 35 years old users
        // 3rd survey matches 45 years old users
     	installSampleSurveyCriteriaAge();  
     	
     	
       	Survey survey1 = getSurvey(STANDARD_SURVEY);
       	
       	SurveyQuestion question1_1 = getNextSurveyQuestion(survey1, user45);
       	assertTrue("Question with position 1 was not fetched", question1_1.getPosition() == 1);
       	addSimpleAnswer(question1_1, "yes", user45);
       	
       	
     	Survey survey35 = getSurvey(SECOND_SURVEY);
     	assertNotNull("Survey (2nd) was not fetched.", survey35);
     	
     	Survey survey45 = getSurvey(THIRD_SURVEY);
     	assertNotNull("Survey (3rd) was not fetched.", survey45);
     	
     	installSampleSurveyOption(survey1, 1, "yes", survey35);
     	installSampleSurveyOption(survey1, 1, "yes", survey45);
     	     	
     	boolean res2 = validateSurveyByCriteria(survey35, user35);
     	assertFalse("Survey (2nd) for 35 years old has been validated succesfully !!!",res2);
     	      	
     	boolean res3 = validateSurveyByCriteria(survey45, user45);
     	assertTrue("Survey (3rd) for 45 years old has not been validated succesfully.", res3);    	
       	
        log.info(">>>>>>> END TEST: testFetchingSurveyUponAppliedCriteriaByAgeAndOption() <<<<<<<<<");
    }
    
    
    @Test
  //  @Ignore
    public void testFetchingRandomSurveysUponAppliedCriteriaByAgeAndOption() {
     	log.info(">>>>>>> TEST: testFetchingRandomSurveysUponAppliedCriteriaByOption() <<<<<<<<<");

     	addSecondSurvey(); // add a temporary sample second survey
     	addThirdSurvey(); // add a temporary sample second survey
     	
     	User user35 = addUser(1976,null);
     	
       	Survey survey1 = getSurvey(STANDARD_SURVEY);
       	
       	SurveyQuestion question1_1 = getNextSurveyQuestion(survey1, user35);
       	assertTrue("Question with position 1 was not fetched", question1_1.getPosition() == 1);
       	addSimpleAnswer(question1_1, "yes", user35);
       	
     	Survey survey2 = getSurvey(SECOND_SURVEY);
     	assertNotNull("Survey (2nd) was not fetched.", survey2);
     	
     	Survey survey3 = getSurvey(THIRD_SURVEY);
     	assertNotNull("Survey (3rd) was not fetched.", survey3);
     	
     	installSampleSurveyOption(survey1, 1, "no", survey2); // reject the 2nd
     	
      	boolean uniqueFetch = true;
      	int count = 0;
      	for(int i = 0;i <= 20 ;i++) {
      		Survey tSurvey = getRandomSurvey(user35);
      		if(tSurvey != null) {
      			count++;
      			assertTrue("Survey (2nd) was fetched.", tSurvey.getId() != survey2.getId());
      		}	
      	}
      	log.info("+++++++++ Count 3rd's fetches = " + count);
      	
      	assertTrue("Fetching non-active surveys is possible", uniqueFetch);
      	
      	log.info(">>>>>>> END TEST: testFetchingRandomSurveysUponAppliedCriteriaByAgeAndOption() <<<<<<<<<");
    } 
    
    @Test
 //   @Ignore
    public void testFetchingQuestionsInSequenceUponAppliedCriteria1() {
     	log.info(">>>>>>> TEST: testFetchingQuestionsInSequenceUponAppliedCriteria1() <<<<<<<<<");
     	User user = getStandardUser();
     	installSampleQuestionCriteria1();
     	
     	Survey survey = getSurvey(STANDARD_SURVEY);
     	
     	SurveyQuestion question1 = getNextSurveyQuestion(survey, user);
     	assertTrue("Question with position 1 was not fetched", question1.getPosition() == 1);
     	addSimpleAnswer(question1, "yes", user);
     	
     	SurveyQuestion question2 = getNextSurveyQuestion(survey, user);
     	assertTrue("Question with position 2 was not fetched", question2.getPosition() == 2);
     	addSimpleAnswer(question2, "big", user);
     	
     	SurveyQuestion question3 = getNextSurveyQuestion(survey, user);
     	assertTrue("Question with position 3 was not fetched", question3.getPosition() == 3);
     	addSimpleAnswer(question3, "sport", user);
     	
     	SurveyQuestion question4 = getNextSurveyQuestion(survey, user);
     	assertTrue("Question with position 4 was not fetched", question4.getPosition() == 4);
     	addSimpleAnswer(question4, "no", user);
     	
     	SurveyQuestion question5 = getNextSurveyQuestion(survey, user);   
     	assertNull("Question with position 5 was fetched !!", question5);     	

      	log.info(">>>>>>> END TEST: testFetchingQuestionsInSequenceUponAppliedCriteria1() <<<<<<<<<");
    }  
    
     @Test
  //   @Ignore
     public void testFetchingQuestionsInSequenceUponAppliedCriteria2() {
      	log.info(">>>>>>> TEST: testFetchingQuestionsInSequenceUponAppliedCriteria2() <<<<<<<<<");
      	User user = getStandardUser();
      	installSampleQuestionCriteria1();
      	
      	Survey survey = getSurvey(STANDARD_SURVEY);
      	
      	SurveyQuestion question1 = getNextSurveyQuestion(survey, user);
      	assertTrue("Question with position 1 was not fetched", question1.getPosition() == 1);
      	addSimpleAnswer(question1, "yes", user);
      	
      	SurveyQuestion question2 = getNextSurveyQuestion(survey, user);
      	assertTrue("Question with position 2 was not fetched", question2.getPosition() == 2);
      	addSimpleAnswer(question2, "small", user);
      	
      	SurveyQuestion questionX = getNextSurveyQuestion(survey, user);
      	assertNull("Question with position 5 was fetched !!", questionX);

       	log.info(">>>>>>> END TEST: testFetchingQuestionsInSequenceUponAppliedCriteria2() <<<<<<<<<");
     }  
    
     @Test
 //    @Ignore
     public void testFetchingQuestionsInSequenceUponAppliedCriteria3() {
      	log.info(">>>>>>> TEST: testFetchingQuestionsInSequenceUponAppliedCriteria3() <<<<<<<<<");
      	User user = getStandardUser();
      	installSampleQuestionCriteria2();
      	
      	Survey survey = getSurvey(STANDARD_SURVEY);
      	
      	SurveyQuestion question1 = getNextSurveyQuestion(survey, user);
      	assertTrue("Question with position 1 was not fetched", question1.getPosition() == 1);
      	addSimpleAnswer(question1, "yes", user);
      	
      	SurveyQuestion question2 = getNextSurveyQuestion(survey, user);
      	assertTrue("Question with position 2 was not fetched", question2.getPosition() == 2);
      	addSimpleAnswer(question2, "small", user);
      	
      	SurveyQuestion questionX = getNextSurveyQuestion(survey, user);
      	assertNotNull("Question with position 5 was fetched !!", questionX);
      	assertTrue("Question with position 3 was fetched", questionX.getPosition() != 3);
      	assertTrue("Question with position 4 was not fetched", questionX.getPosition() == 4);

       	log.info(">>>>>>> END TEST: testFetchingQuestionsInSequenceUponAppliedCriteria3() <<<<<<<<<");
     } 
     
     @Test
  //   @Ignore
     public void testFetchingQuestionsInSequenceUponAppliedCriteria4() {
       	log.info(">>>>>>> TEST: testFetchingQuestionsInSequenceUponAppliedCriteria4() <<<<<<<<<");
       	User user = getStandardUser();
       	
       	installSampleQuestionCriteria2();
       	disableQuestionInPosition(2);
       	
       	Survey survey = getSurvey(STANDARD_SURVEY);
       	
       	SurveyQuestion question1 = getNextSurveyQuestion(survey, user);
       	assertTrue("Question with position 1 was not fetched", question1.getPosition() == 1);
       	addSimpleAnswer(question1, "yes", user);
       	
       	SurveyQuestion questionX = getNextSurveyQuestion(survey, user);
       	assertNotNull("Question with any position was not fetched !!", questionX);
       	assertTrue("Question with position 2 was fetched", questionX.getPosition() != 2);  // non active
       	assertTrue("Question with position 3 was not fetched", questionX.getPosition() == 3); 
       	
    	addSimpleAnswer(questionX, "classic", user);  // an answer to 3 actually
    	SurveyQuestion question4 = getNextSurveyQuestion(survey, user); // invalid
    	assertNull("Question with position 4 was fetched !!", question4);

        log.info(">>>>>>> END TEST: testFetchingQuestionsInSequenceUponAppliedCriteria4() <<<<<<<<<");
     } 
     
     @Test
  //   @Ignore
     public void testFetchingQuestionsInSequenceUponAppliedCriteria5() {
       	log.info(">>>>>>> TEST: testFetchingQuestionsInSequenceUponAppliedCriteria5() <<<<<<<<<");
       	User user = getStandardUser();
       	
       	installSampleQuestionCriteria2();
       	disableQuestionInPosition(2);
       	disableQuestionInPosition(3);
       	
       	Survey survey = getSurvey(STANDARD_SURVEY);
       	
       	SurveyQuestion question1 = getNextSurveyQuestion(survey, user);
       	assertTrue("Question with position 1 was not fetched", question1.getPosition() == 1);
       	addSimpleAnswer(question1, "yes", user);
       	
       	SurveyQuestion questionX = getNextSurveyQuestion(survey, user);
       	assertNotNull("Question with any position was not fetched !!", questionX);
       	assertTrue("Question with position 2 was fetched", questionX.getPosition() != 2);  // non active
       	assertTrue("Question with position 3 was fetched", questionX.getPosition() != 3); // non active
       	assertTrue("Question with position 4 was not fetched", questionX.getPosition() == 4); 
       	
    	addSimpleAnswer(questionX, "yes", user);  // an answer to 4 actually
    	SurveyQuestion question5 = getNextSurveyQuestion(survey, user); // no more questions
    	assertNull("Question with position > 4 was fetched !!", question5);

        log.info(">>>>>>> END TEST: testFetchingQuestionsInSequenceUponAppliedCriteria5() <<<<<<<<<");
    }      */
     
 	@Test
	public void dummy() {
		
	}     
}
