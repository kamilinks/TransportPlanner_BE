package it.palatransport.planner.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Eccezione personalizzata per risorse non trovate.
 * L'annotazione @ResponseStatus permette a Spring di restituire 
 * automaticamente il codice 404 se l'eccezione non viene gestita altrove.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
