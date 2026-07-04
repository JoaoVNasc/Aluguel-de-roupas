package com.poo.loja;

public abstract class Entidade {

    protected int id;

    public Entidade(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
        this.id = id;
    }

    public int getId() {
        return id;
    }

    // Toda entidade precisa saber salvar no arquivo
    public abstract String toFileString();

    @Override
    public abstract String toString();
}