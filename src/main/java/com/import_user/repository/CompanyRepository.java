package com.import_user.repository;

import com.import_user.entity.Company;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CompanyRepository extends CrudRepository<Company, Long> {

    List<Company> findByName(String name);

    Company findById(long id);
}
