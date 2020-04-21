package com.dna.bifincan.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.dna.bifincan.model.products.Company;
import com.dna.bifincan.repository.brand.BrandRepository;
import com.dna.bifincan.repository.products.CompanyRepository;

@Service
@Named("companyService")
public class CompanyService {
	@Inject
	private CompanyRepository companyRepository;
	@Inject
	private BrandRepository brandRepository;
	
	public CompanyService() { }
	
    public List<Company> findCompanies() {
        List<Order> sortOrders = new ArrayList<Order>();

        Order order = new Order(Direction.ASC, "name");
        sortOrders.add(order);

        Sort sort = new Sort(sortOrders);
        return this.companyRepository.findAll(sort);
    }
    
    /**
     * Saves a company entity, but first checks for duplicate name.
     * @param company
     * @return  true for success or false for failure 
     */    
    public boolean saveCompany(Company company) {
    	Long total = companyRepository.countByName(company.getName(), 
    			company.getId() != null ? company.getId() : 0);
    	if(total == 0) {
    		this.companyRepository.save(company);
    		return true;
    	} else {
    		return false;
    	}
    }

    /**
     * Deletes a company entity, but first checks for existing dependencies.
     * @param company
     * @return  true for success or false for failure (ONLY due to dependencies)
     */
    public boolean deleteCompany(Company company) {
    	Long total = brandRepository.countByCompany(company.getId());
    	if(total == 0) {
    		// get a fresh copy of the entity
    		Company tempCompany = this.companyRepository.findOne(company.getId());
    		this.companyRepository.delete(tempCompany);
    		return true;
    	} else {
    		return false;
    	}
    }      
}
