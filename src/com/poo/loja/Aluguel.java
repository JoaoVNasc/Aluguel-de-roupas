package com.poo.loja;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Aluguel {

    public enum StatusAluguel {
        ALUGUEL_ATIVO,
        RESERVA_PENDENTE,
        RESERVA_CONFIRMADA,
        RESERVA_CANCELADA,
        DEVOLVIDO
    }

    private static int proximoId = 1;

    private int id;
    private Cliente cliente;
    private Roupa roupa;
    private LocalDate dataAluguel;
    private LocalDate dataDevolucao;
    private boolean ativo;

    private StatusAluguel status;
    private LocalDate dataInicioPrevista;
    private LocalDate dataFimPrevista;
    private int diasAluguel;
    private double valorTotal;

    /** Construtor simples (aluguel imediato de 1 dia). */
    public Aluguel(Cliente cliente, Roupa roupa) {
        this(cliente, roupa, 1);
    }

    /** Construtor de aluguel imediato com quantidade de dias (Tópico 4). */
    public Aluguel(Cliente cliente, Roupa roupa, int diasAluguel) {
        this.id = proximoId++;
        this.cliente = cliente;
        this.roupa = roupa;
        this.dataAluguel = LocalDate.now();
        this.ativo = true;
        this.status = StatusAluguel.ALUGUEL_ATIVO;
        this.diasAluguel = diasAluguel <= 0 ? 1 : diasAluguel;
        this.valorTotal = this.diasAluguel * roupa.getValorDiaria();
    }

    /** Construtor de reserva com garantia de disponibilidade (Tópico 2). */
    public Aluguel(Cliente cliente, Roupa roupa, LocalDate dataInicioPrevista, LocalDate dataFimPrevista) {
        this.id = proximoId++;
        this.cliente = cliente;
        this.roupa = roupa;
        this.dataAluguel = LocalDate.now();
        this.ativo = false;
        this.status = StatusAluguel.RESERVA_PENDENTE;
        this.dataInicioPrevista = dataInicioPrevista;
        this.dataFimPrevista = dataFimPrevista;

        int dias = 1;
        if (dataInicioPrevista != null && dataFimPrevista != null) {
            long diff = ChronoUnit.DAYS.between(dataInicioPrevista, dataFimPrevista);
            dias = (int) Math.max(1, diff);
        }
        this.diasAluguel = dias;
        this.valorTotal = dias * roupa.getValorDiaria();
    }

    /** Construtor completo, usado pela persistência para reconstruir o objeto salvo em disco. */
    public Aluguel(int id, Cliente cliente, Roupa roupa,
                   LocalDate dataAluguel,
                   LocalDate dataDevolucao,
                   boolean ativo,
                   StatusAluguel status,
                   LocalDate dataInicioPrevista,
                   LocalDate dataFimPrevista,
                   int diasAluguel,
                   double valorTotal) {
        this.id = id;
        this.cliente = cliente;
        this.roupa = roupa;
        this.dataAluguel = dataAluguel;
        this.dataDevolucao = dataDevolucao;
        this.ativo = ativo;
        this.status = status;
        this.dataInicioPrevista = dataInicioPrevista;
        this.dataFimPrevista = dataFimPrevista;
        this.diasAluguel = diasAluguel;
        this.valorTotal = valorTotal;

        if (proximoId <= id) {
            proximoId = id + 1;
        }
    }

    public int getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public Roupa getRoupa() { return roupa; }
    public LocalDate getDataAluguel() { return dataAluguel; }
    public LocalDate getDataDevolucao() { return dataDevolucao; }
    public boolean isAtivo() { return ativo; }
    public StatusAluguel getStatus() { return status; }
    public LocalDate getDataInicioPrevista() { return dataInicioPrevista; }
    public LocalDate getDataFimPrevista() { return dataFimPrevista; }
    public int getDiasAluguel() { return diasAluguel; }
    public double getValorTotal() { return valorTotal; }

    public boolean devolver() {
        if (!ativo) return false;
        ativo = false;
        dataDevolucao = LocalDate.now();
        status = StatusAluguel.DEVOLVIDO;
        return true;
    }

    /** Transforma uma reserva pendente em um aluguel ativo (Tópico 2). */
    public void confirmarComoAluguelAtivo() {
        this.status = StatusAluguel.ALUGUEL_ATIVO;
        this.ativo = true;
        this.dataAluguel = LocalDate.now();
    }

    /** Cancela uma reserva pendente (Tópico 2). */
    public void cancelarReserva() {
        this.status = StatusAluguel.RESERVA_CANCELADA;
        this.ativo = false;
    }

    @Override
    public String toString() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        StringBuilder sb = new StringBuilder();
        sb.append("Aluguel [ID=").append(id)
          .append(", Cliente=").append(cliente.getNome())
          .append(", Roupa=").append(roupa.getDescricao())
          .append(", Data=").append(dataAluguel != null ? dataAluguel.format(f) : "-")
          .append(", Dias=").append(diasAluguel)
          .append(", Valor Total=R$ ").append(String.format("%.2f", valorTotal))
          .append(", Status=").append(status);

        if (dataInicioPrevista != null && dataFimPrevista != null) {
            sb.append(", Período Previsto=").append(dataInicioPrevista.format(f))
              .append(" a ").append(dataFimPrevista.format(f));
        }

        sb.append("]");
        return sb.toString();
    }

    // =========================
    // PERSISTÊNCIA TXT
    // =========================

    public String toFileString() {
        return id + ";" +
                cliente.getId() + ";" +
                roupa.getId() + ";" +
                dataAluguel + ";" +
                (dataDevolucao != null ? dataDevolucao : "null") + ";" +
                ativo + ";" +
                status + ";" +
                (dataInicioPrevista != null ? dataInicioPrevista : "null") + ";" +
                (dataFimPrevista != null ? dataFimPrevista : "null") + ";" +
                diasAluguel + ";" +
                valorTotal;
    }

}
