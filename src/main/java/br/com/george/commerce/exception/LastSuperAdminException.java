package br.com.george.commerce.exception;

public class LastSuperAdminException extends RuntimeException {

    public LastSuperAdminException() {
        super("Cannot remove the last SUPER_ADMIN");
    }
}
