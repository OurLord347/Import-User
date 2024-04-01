package com.import_user.services;

import com.import_user.entity.Company;
import com.import_user.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    public synchronized Company getByName(String name){
        Company company = null;
        List<Company> companyList = companyRepository.findByName(name);
        if (companyList.size() == 0){
            Company companyEntity = new Company();
            companyEntity.setName(name);
            company = companyRepository.save(companyEntity);
        } else {
            company = companyList.get(0);
        }

        return company;
    }
}
