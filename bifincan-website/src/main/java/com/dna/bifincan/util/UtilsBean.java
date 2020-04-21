package com.dna.bifincan.util;

import com.dna.bifincan.model.config.Configuration;
import com.dna.bifincan.model.type.ConfigurationType;
import com.dna.bifincan.service.ConfigurationService;
import com.dna.bifincan.util.spring.ScopeType;
import java.io.Serializable;
import java.util.Calendar;
import javax.inject.Inject;
import javax.inject.Named;
import org.springframework.context.annotation.Scope;

@Named("utilsBean")
@Scope(ScopeType.SINGLETON)
public class UtilsBean implements Serializable {
	private static final long serialVersionUID = -6942602350682744016L;
	@Inject
	private ConfigurationService configurationService;

	public String getThisYear() {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            String yearAsString = Integer.toString(year);
            if (year > 2012) return "2012 - " + yearAsString;
            return yearAsString;
        }
        
        public String getApplicationLocale() {
            String applicationLocale = "tr";
            Configuration localeConfig = configurationService.find((ConfigurationType.
                            APPLICATION_LOCALE.getKey()));
            if(localeConfig != null) applicationLocale = localeConfig.getValue();
            return applicationLocale;
        }

        public String getApplicationCanonicalURL() {
            String applicationCanonicalURL = "https://www.bifincan.com";
            Configuration canonicalUrlConfig = configurationService.find((ConfigurationType.
                            APPLICATION_CANONICAL_URL.getKey()));
            if(canonicalUrlConfig != null) applicationCanonicalURL = canonicalUrlConfig.getValue();
            return applicationCanonicalURL;
        }
        
        public String getPasswordRegexp() {
            String passwordRegexp = "^[-a-zA-Z0-9_#&@!*\\$\\.\\*\\+\\(\\)\\?%=\\\\/ıIiİşŞöÖçÇğĞüÜ]([-a-zA-Z0-9_#&@!\\$\\.\\*\\+\\(\\)\\?%=\\\\/ıIiİşŞöÖçÇğĞüÜ_]+)$";
            return passwordRegexp;
        }

        public String getUsernameRegexp() {
            String usernameRegexp = "^[-a-z0-9_\\.\\+]([-a-z0-9_\\.\\+_]+)$";
            return usernameRegexp;
        }

}
