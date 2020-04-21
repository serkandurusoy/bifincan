package com.dna.bifincan.web;

import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.model.user.EducationType;
import com.dna.bifincan.model.user.EmploymentStatusType;
import com.dna.bifincan.model.user.GenderType;
import com.dna.bifincan.model.user.HasChildrenType;
import com.dna.bifincan.model.user.MaritalStatusType;
import com.dna.bifincan.model.user.MonthlyNetTLIncomeType;
import com.dna.bifincan.model.user.SocialPointType;
import com.dna.bifincan.repository.address.AddressRepository;
import com.dna.bifincan.repository.address.CityRepository;
import com.dna.bifincan.repository.blog.BlogCommentRepository;
import com.dna.bifincan.repository.brand.BrandRepository;
import com.dna.bifincan.repository.products.ProductCategoryLevelOneRepository;
import com.dna.bifincan.repository.products.ProductCategoryLevelThreeRepository;
import com.dna.bifincan.repository.products.ProductCategoryLevelTwoRepository;
import com.dna.bifincan.repository.products.ProductCommentRepository;
import com.dna.bifincan.repository.products.ProductRepository;
import com.dna.bifincan.repository.quiz.QuizAnswerRepository;
import com.dna.bifincan.repository.rating.RatingRepository;
import com.dna.bifincan.repository.survey.SurveyAnswerRepository;
import com.dna.bifincan.repository.user.SocialPointRepository;
import com.dna.bifincan.repository.user.UserRepository;
import com.dna.bifincan.util.spring.ScopeType;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.AnalyticsScopes;
import com.google.api.services.analytics.model.Accounts;
import com.google.api.services.analytics.model.GaData;
import com.google.api.services.analytics.model.Profiles;
import com.google.api.services.analytics.model.Webproperties;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

@Named("clientPresentationAction")
@Scope(ScopeType.VIEW)
public class ClientPresentationAction implements Serializable {

    private static final long serialVersionUID = 6352612661359257197L;
    private static final Logger log = LoggerFactory.getLogger(ClientPresentationAction.class);
    @Inject
    private UserRepository userRepository;
    @Inject
    private BlogCommentRepository blogCommentRepository;
    @Inject
    private ProductCommentRepository productCommentRepository;
    @Inject
    private QuizAnswerRepository quizAnswerRepository;
    @Inject
    private RatingRepository ratingRepository;
    @Inject
    private SurveyAnswerRepository surveyAnswerRepository;
    @Inject
    private SocialPointRepository socialPointRepository;
    @Inject
    private ProductCategoryLevelOneRepository productCategoryLevelOneRepository;
    @Inject
    private ProductCategoryLevelTwoRepository productCategoryLevelTwoRepository;
    @Inject
    private ProductCategoryLevelThreeRepository productCategoryLevelThreeRepository;
    @Inject
    private BrandRepository brandRepository;
    @Inject
    ProductRepository productRepository;
    @Inject
    AddressRepository addressRepository;
    @Inject
    CityRepository cityRepository;

    public void jsonCharts() throws IOException, InterruptedException, GeneralSecurityException, URISyntaxException {
        Double users = userRepository.countActiveUsers().doubleValue();

        Double male = round(((userRepository.countActiveByGender(GenderType.MALE).doubleValue()) / users)*100d, 2);
        Double female = round(((userRepository.countActiveByGender(GenderType.FEMALE).doubleValue()) / users)*100d, 2);

        Map<String, Double> genderChart = new LinkedHashMap<>();
        genderChart.put("male", male);
        genderChart.put("female", female);

        Double hasChildrenYes = round(((userRepository.countActiveByHasChildren(HasChildrenType.YES).doubleValue()) / users)*100d, 2);
        Double hasChildrenNo = round(((userRepository.countActiveByHasChildren(HasChildrenType.NO).doubleValue()) / users)*100d, 2);

        Map<String, Double> hasChildrenChart = new LinkedHashMap<>();
        hasChildrenChart.put("hasChildrenYes", hasChildrenYes);
        hasChildrenChart.put("hasChildrenNo", hasChildrenNo);

        Double married = round(((userRepository.countActiveByMaritalStatus(MaritalStatusType.MARRIED).doubleValue()) / users)*100d, 2);
        Double single = round(((userRepository.countActiveByMaritalStatus(MaritalStatusType.SINGLE).doubleValue()) / users)*100d, 2);

        Map<String, Double> maritalStatusChart = new LinkedHashMap<>();
        maritalStatusChart.put("married", married);
        maritalStatusChart.put("single", single);

        Double employmentStatusUnemployed = round(((userRepository.countActiveByEmploymentStatus(EmploymentStatusType.UNEMPLOYED).doubleValue()) / users)*100d, 2);
        Double employmentStatusPartTime = round(((userRepository.countActiveByEmploymentStatus(EmploymentStatusType.PART_TIME).doubleValue()) / users)*100d, 2);
        Double employmentStatusFullTime = round(((userRepository.countActiveByEmploymentStatus(EmploymentStatusType.FULLTIME).doubleValue()) / users)*100d, 2);
        Double employmentStatusSelfEmployed = round(((userRepository.countActiveByEmploymentStatus(EmploymentStatusType.SELF_EMPLOYED).doubleValue()) / users)*100d, 2);

        Map<String, Double> employmentStatusChart = new LinkedHashMap<>();
        employmentStatusChart.put("employmentStatusUnemployed", employmentStatusUnemployed);
        employmentStatusChart.put("employmentStatusPartTime", employmentStatusPartTime);
        employmentStatusChart.put("employmentStatusFullTime", employmentStatusFullTime);
        employmentStatusChart.put("employmentStatusSelfEmployed", employmentStatusSelfEmployed);

        Double educationTypeNone = round(((userRepository.countActiveByEducationType(EducationType.NONE).doubleValue()) / users)*100d, 2);
        Double educationTypePrimarySchool = round(((userRepository.countActiveByEducationType(EducationType.PRIMARY_SCHOOL).doubleValue()) / users)*100d, 2);
        Double educationTypeHighSchool = round(((userRepository.countActiveByEducationType(EducationType.HIGH_SCHOOL).doubleValue()) / users)*100d, 2);
        Double educationTypeHigherEducation = round(((userRepository.countActiveByEducationType(EducationType.HIGHER_EDUCATION).doubleValue()) / users)*100d, 2);
        Double educationTypeUniversity = round(((userRepository.countActiveByEducationType(EducationType.UNIVERSITY).doubleValue()) / users)*100d, 2);
        Double educationTypeGraduate = round(((userRepository.countActiveByEducationType(EducationType.GRADUATE).doubleValue()) / users)*100d, 2);
        Double educationTypeDoctorate = round(((userRepository.countActiveByEducationType(EducationType.DOCTORATE).doubleValue()) / users)*100d, 2);

        Map<String, Double> educationTypeChart = new LinkedHashMap<>();
        educationTypeChart.put("educationTypeNone", educationTypeNone);
        educationTypeChart.put("educationTypePrimarySchool", educationTypePrimarySchool);
        educationTypeChart.put("educationTypeHighSchool", educationTypeHighSchool);
        educationTypeChart.put("educationTypeHigherEducation", educationTypeHigherEducation);
        educationTypeChart.put("educationTypeUniversity", educationTypeUniversity);
        educationTypeChart.put("educationTypeGraduate", educationTypeGraduate);
        educationTypeChart.put("educationTypeDoctorate", educationTypeDoctorate);

        Double monthlyIncomeNone = round(((userRepository.countActiveByMonthlyIncomeType(MonthlyNetTLIncomeType.NONE).doubleValue()) / users)*100d, 2);
        Double monthlyIncome01 = round(((userRepository.countActiveByMonthlyIncomeType(MonthlyNetTLIncomeType.TL_0_1000).doubleValue()) / users)*100d, 2);
        Double monthlyIncome12 = round(((userRepository.countActiveByMonthlyIncomeType(MonthlyNetTLIncomeType.TL_1001_2000).doubleValue()) / users)*100d, 2);
        Double monthlyIncome23 = round(((userRepository.countActiveByMonthlyIncomeType(MonthlyNetTLIncomeType.TL_2001_3000).doubleValue()) / users)*100d, 2);
        Double monthlyIncome35 = round(((userRepository.countActiveByMonthlyIncomeType(MonthlyNetTLIncomeType.TL_3001_5000).doubleValue()) / users)*100d, 2);
        Double monthlyIncome5p = round(((userRepository.countActiveByMonthlyIncomeType(MonthlyNetTLIncomeType.TL_Above_5000).doubleValue()) / users)*100d, 2);

        Map<String, Double> monthlyIncomeChart = new LinkedHashMap<>();
        monthlyIncomeChart.put("monthlyIncomeNone", monthlyIncomeNone);
        monthlyIncomeChart.put("monthlyIncome01", monthlyIncome01);
        monthlyIncomeChart.put("monthlyIncome12", monthlyIncome12);
        monthlyIncomeChart.put("monthlyIncome23", monthlyIncome23);
        monthlyIncomeChart.put("monthlyIncome35", monthlyIncome35);
        monthlyIncomeChart.put("monthlyIncome5p", monthlyIncome5p);

        Double age018 = round(((userRepository.countActiveByBirthdayRange(findDateForAge(0, 18).get(0), findDateForAge(0, 18).get(1)).doubleValue()) / users)*100d, 2);
        Double age1822 = round(((userRepository.countActiveByBirthdayRange(findDateForAge(18, 22).get(0), findDateForAge(18, 22).get(1)).doubleValue()) / users)*100d, 2);
        Double age2226 = round(((userRepository.countActiveByBirthdayRange(findDateForAge(22, 26).get(0), findDateForAge(22, 26).get(1)).doubleValue()) / users)*100d, 2);
        Double age2630 = round(((userRepository.countActiveByBirthdayRange(findDateForAge(26, 30).get(0), findDateForAge(26, 30).get(1)).doubleValue()) / users)*100d, 2);
        Double age3035 = round(((userRepository.countActiveByBirthdayRange(findDateForAge(30, 35).get(0), findDateForAge(30, 35).get(1)).doubleValue()) / users)*100d, 2);
        Double age3540 = round(((userRepository.countActiveByBirthdayRange(findDateForAge(35, 40).get(0), findDateForAge(35, 40).get(1)).doubleValue()) / users)*100d, 2);
        Double age4050 = round(((userRepository.countActiveByBirthdayRange(findDateForAge(40, 50).get(0), findDateForAge(40, 50).get(1)).doubleValue()) / users)*100d, 2);
        Double age50p = round(((userRepository.countActiveByBirthdayRange(findDateForAge(50, 100).get(0), findDateForAge(50, 100).get(1)).doubleValue()) / users)*100d, 2);

        Map<String, Double> ageChart = new LinkedHashMap<>();
        ageChart.put("age018", age018);
        ageChart.put("age1822", age1822);
        ageChart.put("age2226", age2226);
        ageChart.put("age2630", age2630);
        ageChart.put("age3035", age3035);
        ageChart.put("age3540", age3540);
        ageChart.put("age4050", age4050);
        ageChart.put("age50p", age50p);

        Double istanbul = round(((addressRepository.countPrimaryByCityForActiveUsers(cityRepository.findOne(34L)).doubleValue()) / users)*100d, 2);
        Double otherCities = round(100d-istanbul, 2);

        Map<String, Double> cityChart = new LinkedHashMap<>();
        cityChart.put("istanbul", istanbul);
        cityChart.put("otherCities", otherCities);

        
        Map<String, Map> charts = new LinkedHashMap<>();
        charts.put("genderChart", genderChart);
        charts.put("hasChildrenChart", hasChildrenChart);
        charts.put("maritalStatusChart", maritalStatusChart);
        charts.put("employmentStatusChart", employmentStatusChart);
        charts.put("educationTypeChart", educationTypeChart);
        charts.put("monthlyIncomeChart", monthlyIncomeChart);
        charts.put("ageChart", ageChart);
        charts.put("cityChart", cityChart);

        String json = new Gson().toJson(charts);
        renderJson(json);
    }

    public void jsonGeneralTotals() throws IOException, InterruptedException, GeneralSecurityException, URISyntaxException {
        Long totalUser = userRepository.count();
        Long totalDays = calculateDaysSinceLaunch();
        Long totalOpinion = blogCommentRepository.count() + productCommentRepository.count() + quizAnswerRepository.count() + ratingRepository.count() + surveyAnswerRepository.countAnswerOptions() + socialPointRepository.count();
        Long totalProductCategory = 2L + productCategoryLevelOneRepository.count() + productCategoryLevelTwoRepository.count() + productCategoryLevelThreeRepository.count();
        Long totalBrand = brandRepository.count();
        Long totalCustomers = productRepository.countDistincBrands();
        Long totalSocialFacebook = (long)(socialPointRepository.countByPointType(SocialPointType.facebook)*110L/100L);
        Long totalSocialTwitter = (long)(socialPointRepository.countByPointType(SocialPointType.twitter)*115L/100L);
        Long totalSocialGooglePlus = (long)(socialPointRepository.countByPointType(SocialPointType.googleplus)*125L/100L);
        Long totalSocialPinterest = (long)(((totalSocialFacebook+totalSocialGooglePlus+totalSocialTwitter)*4L/100L)+17L);
        Long totalSocialShare = totalSocialFacebook + totalSocialGooglePlus + totalSocialTwitter + totalSocialPinterest;
        Long totalProductMoneyValue = productRepository.sumMoneyValueOfAllProducts().longValue();
        Long totalGoogleVisitors = Long.parseLong(jsonGoogle("visitors"));
        Long totalGoogleVisits = Long.parseLong(jsonGoogle("visits"));
        Long totalGooglePageViews = Long.parseLong(jsonGoogle("pageviews"));
        Long totalGoogleTimePerVisit = (long) (Double.valueOf(jsonGoogle("timeOnSite")) / totalGoogleVisits);
        Long totalGoogleTimePerVisitSec = (long) (totalGoogleTimePerVisit % 60L);
        Long totalGoogleTimePerVisitMin = (long) ((totalGoogleTimePerVisit - totalGoogleTimePerVisitSec) / 60L);
        Long totalGooglePagesPerVisitTimes100 = (long) (totalGooglePageViews * 100L / totalGoogleVisits);
        Long totalGooglePagesPerVisitTimesDec = (long) (totalGooglePagesPerVisitTimes100 % 100L);
        Long totalGooglePagesPerVisitTimesInt = (long) ((totalGooglePagesPerVisitTimes100 - totalGooglePagesPerVisitTimesDec) / 100L);

        Map<String, Long> metrics = new LinkedHashMap<>();
        metrics.put("totalUser", totalUser);
        metrics.put("totalDays", totalDays);
        metrics.put("totalOpinion", totalOpinion);
        metrics.put("totalProductCategory", totalProductCategory);
        metrics.put("totalBrand", totalBrand);
        metrics.put("totalCustomers", totalCustomers);
        metrics.put("totalProductMoneyValue", totalProductMoneyValue);
        metrics.put("totalGoogleVisitors", totalGoogleVisitors);
        metrics.put("totalGoogleTimePerVisitMin", totalGoogleTimePerVisitMin);
        metrics.put("totalGoogleTimePerVisitSec", totalGoogleTimePerVisitSec);
        metrics.put("totalGooglePagesPerVisitTimesInt", totalGooglePagesPerVisitTimesInt);
        metrics.put("totalGooglePagesPerVisitTimesDec", totalGooglePagesPerVisitTimesDec);
        metrics.put("totalSocialFacebook", totalSocialFacebook);
        metrics.put("totalSocialTwitter", totalSocialTwitter);
        metrics.put("totalSocialGooglePlus", totalSocialGooglePlus);
        metrics.put("totalSocialPinterest", totalSocialPinterest);
        metrics.put("totalSocialShare", totalSocialShare);
        String json = new Gson().toJson(metrics);
        renderJson(json);
    }

    public void jsonImages() throws IOException {
        List<Product> products = productRepository.findAll();
        Set<String> images = new HashSet<>();
        for (Product product : products) {
            StringBuilder brandLogoPath = new StringBuilder();
            images.add(brandLogoPath.append("/fi/gorseller/marka-logolari/").append(product.getBrand().getSlug()).append("-logo.png").toString());
            StringBuilder productImagePath = new StringBuilder();
            images.add(productImagePath.append("/fi/gorseller/hediye-resimleri/").append(product.getSlug()).append("-kucuk-resim.jpg").toString());
        }
        List<String> imageList = new ArrayList<>(images);
        Collections.shuffle(imageList, new java.util.Random());
        String json = new Gson().toJson(imageList);
        renderJson(json);
    }
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    private String jsonGoogle(String whatToQuery) throws IOException, InterruptedException, GeneralSecurityException, URISyntaxException {
        GoogleCredential credential = null;
        try {
            credential = new GoogleCredential.Builder().setTransport(HTTP_TRANSPORT)
                    .setJsonFactory(JSON_FACTORY)
                    .setServiceAccountId("770052383892@developer.gserviceaccount.com")
                    .setServiceAccountScopes(AnalyticsScopes.ANALYTICS_READONLY)
                    .setServiceAccountPrivateKeyFromP12File(new File(this.getClass().getResource("/e51c6a3023478df1ab194d2908d0bea4382bc3f5-privatekey.p12").toURI()))
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            if (log.isDebugEnabled()) {
                log.debug(e.getMessage());
            }
        }

        Analytics analytics = new Analytics.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(
                "bifincan-marka-raporu").build();


        String profileId = "";
        try {
            profileId = getFirstProfileId(analytics);
        } catch (IOException e) {
            if (log.isDebugEnabled()) {
                log.debug(e.getMessage());
            }
        }

        GaData gaData = null;
        try {
            gaData = executeDataQuery(analytics, profileId, whatToQuery);
        } catch (IOException e) {
            if (log.isDebugEnabled()) {
                log.debug(e.getMessage());
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("google analytics access is ready");
        }
        if (gaData.getRows() == null || gaData.getRows().isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug("No google results Found.");
            }
            return "0";
        } else {
            if (log.isDebugEnabled()) {
                log.debug("ga:" + whatToQuery + " " + gaData.getRows().get(0).get(0));
            }
            return gaData.getRows().get(0).get(0);
        }
    }

    private static String getFirstProfileId(Analytics analytics) throws IOException {
        String profileId = null;

        // Query accounts collection.
        Accounts accounts = analytics.management().accounts().list().execute();

        if (accounts.getItems().isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug("no google account found");
            }
        } else {
            String firstAccountId = accounts.getItems().get(0).getId();

            // Query webproperties collection.
            Webproperties webproperties =
                    analytics.management().webproperties().list(firstAccountId).execute();

            if (webproperties.getItems().isEmpty()) {
                if (log.isDebugEnabled()) {
                    log.debug("no google web properties found");
                }
            } else {
                String firstWebpropertyId = webproperties.getItems().get(0).getId();

                // Query profiles collection.
                Profiles profiles =
                        analytics.management().profiles().list(firstAccountId, firstWebpropertyId).execute();

                if (profiles.getItems().isEmpty()) {
                    if (log.isDebugEnabled()) {
                        log.debug("no google profiles found");
                    }
                } else {
                    profileId = profiles.getItems().get(0).getId();
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("google profile id: " + profileId);
        }
        return profileId;
    }

    private static GaData executeDataQuery(Analytics analytics, String profileId, String whatToQuery) throws IOException {
        return analytics.data().ga().get("ga:" + profileId, // Table Id. ga: + profile id.
                "2012-05-25", // Start date.
                "2020-01-01", // End date.
                "ga:" + whatToQuery) // Metrics.
                .execute();
    }

    public void renderJson(String jsonString) throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseContentType("application/json");
        externalContext.setResponseCharacterEncoding("UTF-8");
        externalContext.getResponseOutputWriter().write(jsonString);
        facesContext.responseComplete();
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public List<Date> findDateForAge(int ageBegin, int ageEnd) {
        List<Date> dates = new ArrayList<>();
        Calendar beginDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        
        beginDate.add(Calendar.YEAR, -ageBegin);
        endDate.add(Calendar.YEAR, -ageEnd);
        
        dates.add(beginDate.getTime());
        dates.add(endDate.getTime());

        return dates;
    }

    public Long calculateDaysSinceLaunch() {
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTime(productRepository.findOne(1L).getEntryDate());
        beginDate.set(Calendar.HOUR, 0);
        beginDate.set(Calendar.MINUTE, 0);
        beginDate.set(Calendar.SECOND, 0);
        beginDate.set(Calendar.MILLISECOND, 0);

        Calendar endDate = Calendar.getInstance();
        endDate.set(Calendar.HOUR, 0);
        endDate.set(Calendar.MINUTE, 0);
        endDate.set(Calendar.SECOND, 0);
        endDate.set(Calendar.MILLISECOND, 0);
        
        Long days = (long) ((endDate.getTimeInMillis()-beginDate.getTimeInMillis())/(24*60*60*1000));
        
        return days;
    }
}
