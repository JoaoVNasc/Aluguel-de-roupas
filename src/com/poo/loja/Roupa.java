package com.poo.loja;

public class Roupa extends Entidade {

    private String descricao;
    private String categoria; // Terno, Vestido Noiva, Social etc.
    private int quantidade;

    public Roupa(int id, String descricao, String categoria, int quantidade) {
        super(id);
        if (id <= 0 || descricao == null || descricao.trim().isEmpty()
                || categoria == null || categoria.trim().isEmpty() || quantidade < 0) {
            throw new IllegalArgumentException("Dados inválidos para criação da roupa.");
        }
        this.id = id;
        this.descricao = descricao;
        this.categoria = categoria;
        this.quantidade = quantidade;
    }

    public int getId() { return id; }
    public String getDescricao() { return descricao; }
    public String getCategoria() { return categoria; }
    public int getQuantidade() { return quantidade; }

    public boolean isDisponivel() {
        return quantidade > 0;
    }

    public boolean alugar() {
        if (isDisponivel()) {
            quantidade--;
            return true;
        }
        return false;
    }

    public void devolver() {
        quantidade++;
    }

    @Override
    public String toString() {
        return "Roupa [ID=" + id +
                ", Descrição=\"" + descricao +
                "\", Categoria=\"" + categoria +
                "\", Qtd Disponível=" + quantidade + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Roupa)) return false;
        Roupa roupa = (Roupa) o;
        return id == roupa.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    // =========================
    // PERSISTÊNCIA TXT
    // =========================

    public String toFileString() {
        return id + ";" + descricao + ";" + categoria + ";" + quantidade;
    }

    public static Roupa fromFileString(String linha) {
        String[] partes = linha.split(";");
        return new Roupa(
                Integer.parseInt(partes[0]),
                partes[1],
                partes[2],
                Integer.parseInt(partes[3])
        );
    }
}