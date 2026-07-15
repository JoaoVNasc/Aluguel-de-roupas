package com.poo.loja;

public class Roupa {

    private int id;
    private String descricao;
    private String categoria; // Terno, Vestido Noiva, Social etc.
    private int quantidade;
    private double valorDiaria;

    public Roupa(int id, String descricao, String categoria, int quantidade) {
        this(id, descricao, categoria, quantidade, 0.0);
    }

    public Roupa(int id, String descricao, String categoria, int quantidade, double valorDiaria) {
        if (id <= 0 || descricao == null || descricao.trim().isEmpty()
                || categoria == null || categoria.trim().isEmpty() || quantidade < 0
                || valorDiaria < 0) {
            throw new IllegalArgumentException("Dados inválidos para criação da roupa.");
        }
        this.id = id;
        this.descricao = descricao;
        this.categoria = categoria;
        this.quantidade = quantidade;
        this.valorDiaria = valorDiaria;
    }

    public int getId() { return id; }
    public String getDescricao() { return descricao; }
    public String getCategoria() { return categoria; }
    public int getQuantidade() { return quantidade; }
    public double getValorDiaria() { return valorDiaria; }

    public void setDescricao(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição inválida.");
        }
        this.descricao = descricao;
    }

    public void setCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria inválida.");
        }
        this.categoria = categoria;
    }

    /**
     * Valor da diária de aluguel desta roupa (Tópico 4 - Orçamento Estimado).
     * Não pode ser negativo.
     */
    public void setValorDiaria(double valorDiaria) {
        if (valorDiaria < 0) {
            throw new IllegalArgumentException("Valor da diária não pode ser negativo.");
        }
        this.valorDiaria = valorDiaria;
    }

    /**
     * Ajuste manual de estoque (entrada ou saída), usado pelo administrador
     * para refletir a operação real (ex: peça comprada, danificada, extraviada).
     * Não deve ser usado para aluguel/devolução, que têm fluxo próprio.
     */
    public void ajustarQuantidade(int novaQuantidade) {
        if (novaQuantidade < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa.");
        }
        this.quantidade = novaQuantidade;
    }

    public boolean isDisponivel() {
        return quantidade > 0;
    }

    public boolean isEstoqueBaixo(int limite) {
        return quantidade <= limite;
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
                "\", Qtd Disponível=" + quantidade +
                ", Valor Diária=R$ " + String.format("%.2f", valorDiaria) + "]";
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
        return id + ";" + descricao + ";" + categoria + ";" + quantidade + ";" + valorDiaria;
    }

    public static Roupa fromFileString(String linha) {
        String[] partes = linha.split(";", -1);

        int id = Integer.parseInt(partes[0].trim());
        String descricao = partes[1];
        String categoria = partes[2];
        int quantidade = Integer.parseInt(partes[3].trim());

        double valorDiaria = 0.0;
        if (partes.length > 4 && !partes[4].trim().isEmpty()) {
            try {
                valorDiaria = Double.parseDouble(partes[4].trim());
            } catch (NumberFormatException ignored) {
                valorDiaria = 0.0;
            }
        }

        return new Roupa(id, descricao, categoria, quantidade, valorDiaria);
    }
}
