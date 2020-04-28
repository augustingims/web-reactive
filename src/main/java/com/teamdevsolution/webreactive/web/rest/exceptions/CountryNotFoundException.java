package com.teamdevsolution.webreactive.web.rest.exceptions;

public class CountryNotFoundException extends RuntimeException{

    public CountryNotFoundException(String countryId){
        super("Country not found with id " + countryId);
    }
}
