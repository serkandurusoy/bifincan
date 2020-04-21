package com.dna.bifincan.admin.product;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.dna.bifincan.admin.util.FacesUtils;
import com.dna.bifincan.admin.util.WebConstants;
import com.dna.bifincan.admin.util.spring.ScopeType;
import com.dna.bifincan.model.products.Company;
import com.dna.bifincan.service.CompanyService;

@Named("companyAction")
@Scope(ScopeType.VIEW)
public class CompanyAction implements Serializable {
	private static final long serialVersionUID = -3563146828489691683L;

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(CompanyAction.class);

    private List<Company> companies;
    private Company currentCompany;
    
    @Inject
    private CompanyService companyService;

    public CompanyAction() { }

    @PostConstruct
    public void initialize() {
    	loadCompanies();
    }
    
    // --- Action methods and listeners --- //
    public void loadCompanies() {
    	try {
    		this.companies = companyService.findCompanies();
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}
    }

    public void initAdd() {
        this.currentCompany = new Company();
    }

    public void initEdit(Company company) {
        this.currentCompany = company;
    }

    public void save() {
    	try {
    		Long id = this.currentCompany.getId();
	        if(this.companyService.saveCompany(this.currentCompany)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			id == null ? WebConstants.SUCCESS_INSERT_KEY : WebConstants.SUCCESS_UPDATE_KEY));    
	        } else { // failure (duplicate name)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
	        			"company.error.duplicateName"));
	        }
	        
	        this.currentCompany = null;
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
    				WebConstants.ERROR_GEN_ERROR_KEY));
    	}
    	
    	loadCompanies();    
    }
    
    public void delete() {
    	try {
	        if(this.companyService.deleteCompany(this.currentCompany)) { // success 
	        	FacesUtils.addSuccessMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			WebConstants.SUCCESS_DELETION_KEY));    
	        } else { // failure (due to dependencies)
	        	FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY, 
	        			"company.error.inUse"));
	        }
		} catch(Exception ex) {
			ex.printStackTrace();
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,
					WebConstants.ERROR_GEN_ERROR_KEY));
		}    
		
		loadCompanies();
    }

    // --- Getters and Setters --- //
	public List<Company> getCompanies() {
		return companies;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}

	public Company getCurrentCompany() {
		return currentCompany;
	}

	public void setCurrentCompany(Company currentCompany) {
		this.currentCompany = currentCompany;
	}
}
