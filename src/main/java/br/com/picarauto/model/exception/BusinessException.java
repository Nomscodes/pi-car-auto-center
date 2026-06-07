package br.com.picarauto.model.exception;

public class BusinessException extends BaseException {

    private static final long serialVersionUID = -224350778964358350L;

    public BusinessException(String message) {
        super("Erro de Negócio", message, Severity.ERROR, "BUSINESS_ERROR");
    }

    public BusinessException(String message, Throwable cause) {
        super("Erro de Operação", message, Severity.ERROR, "OPERATION_FAILED");
        this.initCause(cause);
    }

    public BusinessException(Throwable cause) {
        super("Erro Interno", cause.getMessage(), Severity.FATAL, "INTERNAL_ERROR");
        this.initCause(cause);
    }

    public static void handleSQLException(Exception e, String entityName) {
        String message = e.getMessage();
        if (message != null && message.contains("UNIQUE constraint failed")) {
            throw new BusinessException("Valor duplicado em " + entityName + ". Este valor já existe no sistema.");
        }
        if (message != null && message.contains("FOREIGN KEY constraint failed")) {
            throw new BusinessException("Não é possível processar esta operação. Verifique as referências a outras entidades.");
        }
        throw new BusinessException("Erro ao processar " + entityName + ". Verifique os dados informados.", e);
    }
}