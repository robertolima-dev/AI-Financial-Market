package com.mali.crypfy.clustering.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckRest {

    @RequestMapping(value = "/health-check" ,method = RequestMethod.GET)
    public ResponseEntity<?> healthCheck() {
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }

    @RequestMapping(value = "/" ,method = RequestMethod.GET)
    public ResponseEntity<?> healthCheck2() {
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
}
