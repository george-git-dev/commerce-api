package br.com.george.commerce.exception;

public class LastSuperAdminDeletionException extends RuntimeException {

    public LastSuperAdminDeletionException() {
        super("Cannot remove the last SUPER_ADMIN");
    }
}
