package br.com.picarauto.model.exception;

public enum Severity {
	INFO, // Informativo
	WARNING, // Alerta (regra de negócio contornável)
	ERROR, // Erro impeditivo
	FATAL // Erro crítico de sistema
}
