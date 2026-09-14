package br.com.george.commerce.exception;

public class PasswordResetTokenExpiredException extends RuntimeException {

    public PasswordResetTokenExpiredException() {
        super("Password reset token has expired");
    }

}
