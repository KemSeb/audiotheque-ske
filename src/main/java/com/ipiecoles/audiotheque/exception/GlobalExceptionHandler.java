package com.ipiecoles.audiotheque.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

public class GlobalExceptionHandler {

    // Argument invalide - Erreur 400 BAD_REQUEST
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handlerMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return "La valeur " + e.getValue() + " est incorrecte pour le paramètre "
                + e.getName();
    }

    // Entité déjà existante - Erreur 409 CONFLICT
    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handlerEntityExistsException(EntityExistsException e) {
        return e.getMessage();
    }

    // Entité non trouvée -  Erreur 404 NOT_FOUND
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handlerEntityNotFoundException(EntityNotFoundException e) {
        return e.getMessage();
    }
}
