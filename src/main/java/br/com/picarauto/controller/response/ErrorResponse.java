package br.com.picarauto.controller.response;

import br.com.picarauto.model.exception.BaseException;
import br.com.picarauto.model.exception.BusinessException;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;

public class ErrorResponse {

    private String title;
    private String message;

    private ErrorResponse(String message) {
        this("Erro", message);
    }

    private ErrorResponse(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public static ErrorResponse error(BusinessException ex) {
        return new ErrorResponse(ex.getTitle(), ex.getMessage().concat(" -> ").concat(ex.getMotive()));
    }

    public static ErrorResponse error(FieldValidationException ex) {
        return new ErrorResponse(ex.getTitle(), ex.getMessage().concat(" -> ").concat(ex.getMotive()));
    }

    public static ErrorResponse error(RuleValidationException ex) {
        return new ErrorResponse(ex.getTitle(), ex.getMessage().concat(" -> ").concat(ex.getMotive()));
    }

    public static ErrorResponse error(BaseException ex) {
        return new ErrorResponse(ex.getTitle(), ex.getMessage().concat(" -> ").concat(ex.getMotive()));
    }

    public static ErrorResponse error(String message) {
        return new ErrorResponse(message);
    }

    public String getTitle() { return title; }
    public String getMessage() { return message; }
}