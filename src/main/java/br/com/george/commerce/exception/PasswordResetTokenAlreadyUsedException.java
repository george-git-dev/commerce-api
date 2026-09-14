package br.com.george.commerce.exception;

public class PasswordResetTokenAlreadyUsedException extends RuntimeException {

    public PasswordResetTokenAlreadyUsedException() {
        super("Password reset token has already been used");
    }

}
