package com.teamdevsolution.webreactive.repository;

import com.teamdevsolution.webreactive.domain.Country;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends ReactiveMongoRepository<Country, String> {

}
