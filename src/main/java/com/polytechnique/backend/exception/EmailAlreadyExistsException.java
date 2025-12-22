package com.polytechnique.backend.exception;

/**
 * Exception levée quand un email existe déjà dans la base
 */
public class EmailAlreadyExistsException extends RuntimeException {
    
    public EmailAlreadyExistsException(String email) {
        super(String.format("Un médecin avec l'email '%s' existe déjà", email));
    }
}