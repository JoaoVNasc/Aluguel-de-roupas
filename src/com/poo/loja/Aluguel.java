package com.poo.loja;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Aluguel {

    private Cliente cliente;
    private Roupa roupa;
    private LocalDate dataAluguel;
    private LocalDate dataDevolucao;
    private boolean ativo;

    public Aluguel(Cliente cliente, Roupa roupa) {
        this.cliente = cliente;
        this.roupa = roupa;
        this.dataAluguel = LocalDate.now();
        this.ativo = true;
    }

    // Construtor usado na persistência
    public Aluguel(Cliente cliente, Roupa roupa,
                   LocalDate dataAluguel,
                   LocalDate dataDevolucao,
                   boolean ativo) {
        this.cliente = cliente;
        this.roupa = roupa;
        this.dataAluguel = dataAluguel;
        this.dataDevolucao = dataDevolucao;
        this.ativo = ativo;
    }

    public Cliente getCliente() { return cliente; }
    public Roupa getRoupa() { return roupa; }
    public LocalDate getDataAluguel() { return dataAluguel; }
    public boolean isAtivo() { return ativo; }

    public boolean devolver() {
        if (!ativo) return false;
        ativo = false;
        dataDevolucao = LocalDate.now();
        return true;
    }

    @Override
    public String toString() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return "Aluguel [Cliente=" + cliente.getNome() +
                ", Roupa=" + roupa.getDescricao() +
                ", Data=" + dataAluguel.format(f) +
                ", Status=" + (ativo ? "Ativo" : "Devolvido") + "]";
    }

    // =========================
// PERSISTÊNCIA TXT
// =========================

    public String toFileString() {
        return cliente.getId() + ";" +
                roupa.getId() + ";" +
                dataAluguel + ";" +
                (dataDevolucao != null ? dataDevolucao : "null") + ";" +
                ativo;
    }

}