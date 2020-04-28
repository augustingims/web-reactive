package com.teamdevsolution.webreactive.web.rest;

import com.teamdevsolution.webreactive.domain.Country;
import com.teamdevsolution.webreactive.repository.CountryRepository;
import com.teamdevsolution.webreactive.web.rest.exceptions.CountryNotFoundException;
import com.teamdevsolution.webreactive.web.rest.playload.ErrorResponse;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
public class CountryResource {
    private final CountryRepository countryRepository;

    public CountryResource(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @GetMapping("/countries")
    public Flux<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @PostMapping("/countries")
    public Mono<Country> createCountry(@Valid @RequestBody Country country) {
        return countryRepository.save(country);
    }

    @GetMapping("/countries/{id}")
    public Mono<ResponseEntity<Country>> getCountryById(@PathVariable(value = "id") String countryId) {
        return countryRepository.findById(countryId)
                .map(savedCountry -> ResponseEntity.ok(savedCountry))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/countries/{id}")
    public Mono<ResponseEntity<Country>> updateCountry(@PathVariable(value = "id") String countryId,
                                                   @Valid @RequestBody Country country) {
        return countryRepository.findById(countryId)
                .flatMap(existingCountry -> {
                    existingCountry.setName(country.getName());
                    existingCountry.setCode(country.getCode());
                    existingCountry.setFlag(country.getFlag());
                    return countryRepository.save(existingCountry);
                })
                .map(updateCountry -> new ResponseEntity<>(updateCountry, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/countries/{id}")
    public Mono<ResponseEntity<Void>> deleteCountry(@PathVariable(value = "id") String countryId) {

        return countryRepository.findById(countryId)
                .flatMap(existingCountry ->
                        countryRepository.delete(existingCountry)
                                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Countries are Sent to the client as Server Sent Events
    @GetMapping(value = "/stream/countries", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Country> streamAllCountries() {
        return countryRepository.findAll();
    }

    /*
        Exception Handling Examples (These can be put into a @ControllerAdvice to handle exceptions globally)
    */

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity handleDuplicateKeyException(DuplicateKeyException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("A Country with the same text already exists"));
    }

    @ExceptionHandler(CountryNotFoundException.class)
    public ResponseEntity handleTweetNotFoundException(CountryNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }
}
