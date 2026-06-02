package br.com.picarauto.model;

/**
 * Classe base para todas as entidades do sistema.
 * Contém o identificador único comum a todos os modelos.
 */
public abstract class BaseModel {

    private int id;

    public BaseModel() {}

    public BaseModel(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}