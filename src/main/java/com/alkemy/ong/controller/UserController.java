package com.alkemy.ong.controller;

import com.alkemy.ong.service.abstraction.IDeleteUserService;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Empty;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @Autowired
  public IDeleteUserService deleteUserService;

  @DeleteMapping(value = "/users/{id}")
  public ResponseEntity<Empty> delete(@PathVariable Long id) throws EntityNotFoundException {
    deleteUserService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
