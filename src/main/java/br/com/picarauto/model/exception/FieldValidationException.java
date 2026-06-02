package br.com.picarauto.model.exception;

public class FieldValidationException extends BaseException {

    private static final long serialVersionUID = -1215394565893203773L;
    private final String field;

    public FieldValidationException(String field, String message) {
        super("Erro de Validação de Campo", message, Severity.WARNING, "FIELD_VALIDATION_ERROR");
        this.field = field;
    }

    public String getField() { return field; }
}