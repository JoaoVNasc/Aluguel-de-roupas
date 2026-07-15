package com.poo.loja;

import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDate;

public class Loja {
    private Map<Integer, Roupa> estoque = new HashMap<>();
    private Map<Integer, Cliente> clientes = new HashMap<>();
    private List<Aluguel> alugueis = new ArrayList<>();

    public Loja() {
        estoque = PersistenciaTXT.carregarRoupasMap();
        clientes = PersistenciaTXT.carregarClientesMap();

        alugueis = new ArrayList<>();

        List<String> linhas = PersistenciaTXT.carregarAlugueisBruto();
        int legadoSeq = 900000;

        for (String linha : linhas) {
            if (linha == null || linha.trim().isEmpty()) continue;

            try {
                String[] partes = linha.split(";", -1);

                if (partes.length >= 11) {
                    // Formato atual (Tópicos 2/4/5): id;idCliente;idRoupa;dataAluguel;dataDevolucao;
                    // ativo;status;dataInicioPrevista;dataFimPrevista;diasAluguel;valorTotal
                    int id = Integer.parseInt(partes[0]);
                    int idCliente = Integer.parseInt(partes[1]);
                    int idRoupa = Integer.parseInt(partes[2]);
                    LocalDate dataAluguel = LocalDate.parse(partes[3]);
                    LocalDate dataDevolucao = "null".equals(partes[4]) ? null : LocalDate.parse(partes[4]);
                    boolean ativo = Boolean.parseBoolean(partes[5]);
                    Aluguel.StatusAluguel status = Aluguel.StatusAluguel.valueOf(partes[6]);
                    LocalDate dataInicioPrevista = "null".equals(partes[7]) ? null : LocalDate.parse(partes[7]);
                    LocalDate dataFimPrevista = "null".equals(partes[8]) ? null : LocalDate.parse(partes[8]);
                    int diasAluguel = Integer.parseInt(partes[9]);
                    double valorTotal = Double.parseDouble(partes[10]);

                    Cliente cliente = clientes.get(idCliente);
                    Roupa roupa = estoque.get(idRoupa);

                    if (cliente != null && roupa != null) {
                        alugueis.add(new Aluguel(
                                id, cliente, roupa,
                                dataAluguel, dataDevolucao, ativo,
                                status, dataInicioPrevista, dataFimPrevista,
                                diasAluguel, valorTotal
                        ));
                    }
                } else if (partes.length >= 5) {
                    // Formato legado (Tópico 1): idCliente;idRoupa;dataAluguel;dataDevolucao;ativo
                    int idCliente = Integer.parseInt(partes[0]);
                    int idRoupa = Integer.parseInt(partes[1]);
                    LocalDate dataAluguel = LocalDate.parse(partes[2]);
                    LocalDate dataDevolucao = "null".equals(partes[3]) ? null : LocalDate.parse(partes[3]);
                    boolean ativo = Boolean.parseBoolean(partes[4]);

                    Cliente cliente = clientes.get(idCliente);
                    Roupa roupa = estoque.get(idRoupa);

                    if (cliente != null && roupa != null) {
                        Aluguel.StatusAluguel status = ativo
                                ? Aluguel.StatusAluguel.ALUGUEL_ATIVO
                                : Aluguel.StatusAluguel.DEVOLVIDO;
                        double valorTotal = roupa.getValorDiaria();

                        alugueis.add(new Aluguel(
                                legadoSeq++, cliente, roupa,
                                dataAluguel, dataDevolucao, ativo,
                                status, null, null,
                                1, valorTotal
                        ));
                    }
                }
            } catch (Exception ex) {
                // Linha corrompida ou em formato incompatível: ignora e continua o carregamento.
            }
        }
    }

    // =========================
    // ROUPAS
    // =========================

    public String cadastrarRoupa(Roupa roupa) {
        if (estoque.containsKey(roupa.getId()))
            return "Erro: Roupa já cadastrada.";

        estoque.put(roupa.getId(), roupa);
        PersistenciaTXT.salvarRoupas(new ArrayList<>(estoque.values()));
        return "✔ Roupa cadastrada com sucesso.";
    }

    public Roupa buscarRoupaPorId(int id) {
        return estoque.get(id);
    }

    /** Mantido por compatibilidade: preserva o valor de diária já existente. */
    public String atualizarRoupa(int id, String descricao, String categoria) {
        Roupa roupa = estoque.get(id);
        double valorAtual = roupa != null ? roupa.getValorDiaria() : 0.0;
        return atualizarRoupa(id, descricao, categoria, valorAtual);
    }

    /** Tópico 4: também persiste o valor da diária. */
    public String atualizarRoupa(int id, String descricao, String categoria, double valorDiaria) {
        Roupa roupa = estoque.get(id);
        if (roupa == null) return "Erro: Roupa não encontrada.";

        try {
            roupa.setDescricao(descricao);
            roupa.setCategoria(categoria);
            roupa.setValorDiaria(valorDiaria);
        } catch (IllegalArgumentException ex) {
            return "Erro: " + ex.getMessage();
        }

        PersistenciaTXT.salvarRoupas(new ArrayList<>(estoque.values()));
        return "✔ Roupa atualizada com sucesso.";
    }

    public String removerRoupa(int id) {
        Roupa roupa = estoque.get(id);
        if (roupa == null) return "Erro: Roupa não encontrada.";

        boolean possuiVinculoAtivo = alugueis.stream()
                .anyMatch(a -> (a.isAtivo() || a.getStatus() == Aluguel.StatusAluguel.RESERVA_PENDENTE)
                        && a.getRoupa().equals(roupa));

        if (possuiVinculoAtivo)
            return "Erro: Não é possível remover, há aluguel ou reserva ativa para esta roupa.";

        estoque.remove(id);
        PersistenciaTXT.salvarRoupas(new ArrayList<>(estoque.values()));
        return "✔ Roupa removida com sucesso.";
    }

    /**
     * Ajuste manual de estoque (entrada/saída), para o administrador manter
     * o estoque como uma representação real da operação (ex: peça comprada,
     * danificada, extraviada, enviada para conserto).
     */
    public String ajustarEstoque(int id, int novaQuantidade, String motivo) {
        Roupa roupa = estoque.get(id);
        if (roupa == null) return "Erro: Roupa não encontrada.";

        try {
            roupa.ajustarQuantidade(novaQuantidade);
        } catch (IllegalArgumentException ex) {
            return "Erro: " + ex.getMessage();
        }

        PersistenciaTXT.salvarRoupas(new ArrayList<>(estoque.values()));

        String motivoTexto = (motivo == null || motivo.trim().isEmpty())
                ? "" : " Motivo: " + motivo.trim() + ".";

        return "✔ Estoque ajustado com sucesso." + motivoTexto;
    }

    public List<Roupa> roupasComEstoqueBaixo(int limite) {
        return estoque.values().stream()
                .filter(r -> r.isEstoqueBaixo(limite))
                .collect(Collectors.toList());
    }

    // =========================
    // CLIENTES
    // =========================

    public String cadastrarCliente(Cliente cliente) {
        if (clientes.containsKey(cliente.getId()))
            return "Erro: Cliente já cadastrado.";

        clientes.put(cliente.getId(), cliente);
        PersistenciaTXT.salvarClientes(new ArrayList<>(clientes.values()));
        return "✔ Cliente cadastrado com sucesso.";
    }

    /** Tópico 3: localiza cliente pelo ID para permitir edição na tela. */
    public Cliente buscarClientePorId(int id) {
        return clientes.get(id);
    }

    /** Tópico 3: atualiza os dados cadastrais de um cliente já existente. */
    public String atualizarCliente(int id, String nome, String telefone, String endereco) {
        Cliente cliente = clientes.get(id);
        if (cliente == null) return "Erro: Cliente não encontrado.";

        try {
            cliente.setNome(nome);
            cliente.setTelefone(telefone);
            cliente.setEndereco(endereco);
        } catch (IllegalArgumentException ex) {
            return "Erro: " + ex.getMessage();
        }

        PersistenciaTXT.salvarClientes(new ArrayList<>(clientes.values()));
        return "✔ Cliente atualizado com sucesso.";
    }

    // =========================
    // ORÇAMENTO E ALUGUEL (Tópico 4)
    // =========================

    /** Retorna o valor estimado (dias * valorDiaria), ou -1 se a roupa não existir. */
    public double calcularOrcamento(int idRoupa, int dias) {
        Roupa roupa = estoque.get(idRoupa);
        if (roupa == null) return -1;
        int diasValidos = dias <= 0 ? 1 : dias;
        return diasValidos * roupa.getValorDiaria();
    }

    /** Sobrecarga de compatibilidade com telas antigas (ex: LojaUI): 1 dia como padrão. */
    public String realizarAluguel(int idCliente, int idRoupa) {
        return realizarAluguel(idCliente, idRoupa, 1);
    }

    public String realizarAluguel(int idCliente, int idRoupa, int dias) {

        Cliente cliente = clientes.get(idCliente);
        Roupa roupa = estoque.get(idRoupa);

        if (cliente == null) return "Cliente não encontrado.";
        if (roupa == null) return "Roupa não encontrada.";

        boolean jaAlugado = alugueis.stream()
                .anyMatch(a -> a.isAtivo() &&
                        a.getCliente().equals(cliente) &&
                        a.getRoupa().equals(roupa));

        if (jaAlugado)
            return "Cliente já alugou essa roupa.";

        if (roupa.alugar()) {
            Aluguel novo = new Aluguel(cliente, roupa, dias <= 0 ? 1 : dias);
            alugueis.add(novo);
            PersistenciaTXT.salvarAlugueis(alugueis);
            PersistenciaTXT.salvarRoupas(new ArrayList<>(estoque.values()));
            return "✔ Aluguel realizado com sucesso. Dias: " + novo.getDiasAluguel()
                    + " | Valor Total: R$ " + String.format("%.2f", novo.getValorTotal());
        } else {
            return "Roupa indisponível.";
        }
    }

    public String realizarDevolucao(int idCliente, int idRoupa) {
        for (Aluguel a : alugueis) {
            if (a.getCliente().getId() == idCliente &&
                    a.getRoupa().getId() == idRoupa &&
                    a.isAtivo()) {

                a.devolver();
                a.getRoupa().devolver();
                PersistenciaTXT.salvarAlugueis(alugueis);
                PersistenciaTXT.salvarRoupas(new ArrayList<>(estoque.values()));
                return "✔ Devolução realizada com sucesso.";
            }
        }
        return "Aluguel não encontrado.";
    }

    // =========================
    // RESERVAS (Tópico 2)
    // =========================

    /** Cria uma reserva com garantia de disponibilidade, bloqueando a peça no estoque. */
    public String realizarReserva(int idCliente, int idRoupa, LocalDate inicio, LocalDate fim) {
        Cliente cliente = clientes.get(idCliente);
        Roupa roupa = estoque.get(idRoupa);

        if (cliente == null) return "Cliente não encontrado.";
        if (roupa == null) return "Roupa não encontrada.";
        if (inicio == null || fim == null) return "Informe as datas de início e fim da reserva.";
        if (fim.isBefore(inicio)) return "A data de fim não pode ser anterior à data de início.";

        if (!roupa.alugar())
            return "Roupa indisponível para reserva.";

        Aluguel reserva = new Aluguel(cliente, roupa, inicio, fim);
        alugueis.add(reserva);

        PersistenciaTXT.salvarAlugueis(alugueis);
        PersistenciaTXT.salvarRoupas(new ArrayList<>(estoque.values()));

        return "✔ Reserva #" + reserva.getId() + " criada com sucesso (pendente de confirmação).";
    }

    /** Confirma a reserva pendente, transformando-a em aluguel ativo. */
    public String confirmarReserva(int idReserva) {
        for (Aluguel a : alugueis) {
            if (a.getId() == idReserva && a.getStatus() == Aluguel.StatusAluguel.RESERVA_PENDENTE) {
                a.confirmarComoAluguelAtivo();
                PersistenciaTXT.salvarAlugueis(alugueis);
                return "✔ Reserva #" + idReserva + " confirmada como aluguel ativo.";
            }
        }
        return "Reserva não encontrada ou já processada.";
    }

    /** Cancela a reserva pendente, devolvendo a peça ao estoque disponível. */
    public String cancelarReserva(int idReserva) {
        for (Aluguel a : alugueis) {
            if (a.getId() == idReserva && a.getStatus() == Aluguel.StatusAluguel.RESERVA_PENDENTE) {
                a.cancelarReserva();
                a.getRoupa().devolver();
                PersistenciaTXT.salvarAlugueis(alugueis);
                PersistenciaTXT.salvarRoupas(new ArrayList<>(estoque.values()));
                return "✔ Reserva #" + idReserva + " cancelada e item devolvido ao estoque.";
            }
        }
        return "Reserva não encontrada ou já processada.";
    }

    public List<Aluguel> listarReservasPendentes() {
        return alugueis.stream()
                .filter(a -> a.getStatus() == Aluguel.StatusAluguel.RESERVA_PENDENTE)
                .collect(Collectors.toList());
    }

    // =========================
    // RELATÓRIOS (Tópico 5)
    // =========================

    public double calcularFaturamentoPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return listarAlugueisPeriodo(dataInicio, dataFim).stream()
                .mapToDouble(Aluguel::getValorTotal)
                .sum();
    }

    public List<Aluguel> listarAlugueisPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return alugueis.stream()
                .filter(a -> a.getStatus() == Aluguel.StatusAluguel.ALUGUEL_ATIVO
                        || a.getStatus() == Aluguel.StatusAluguel.DEVOLVIDO)
                .filter(a -> a.getDataAluguel() != null
                        && !a.getDataAluguel().isBefore(dataInicio)
                        && !a.getDataAluguel().isAfter(dataFim))
                .collect(Collectors.toList());
    }

    // =========================
    // LISTAGENS
    // =========================

    public String listarRoupa() {
        return estoque.values().stream()
                .map(Roupa::toString)
                .collect(Collectors.joining("\n"));
    }

    public String listarClientes() {
        return clientes.values().stream()
                .map(Cliente::toString)
                .collect(Collectors.joining("\n"));
    }

    public String listarAlugueis() {
        return alugueis.stream()
                .map(Aluguel::toString)
                .collect(Collectors.joining("\n"));
    }

    public String historicoCliente(int id) {
        return alugueis.stream()
                .filter(a -> a.getCliente().getId() == id)
                .map(Aluguel::toString)
                .collect(Collectors.joining("\n"));
    }

    public String buscarPorCategoria(String categoria) {

        String resultado = estoque.values().stream()
                .filter(r -> r.getCategoria().equalsIgnoreCase(categoria))
                .map(Roupa::toString)
                .collect(Collectors.joining("\n"));

        if (resultado.isEmpty()) {
            return "Nenhuma roupa encontrada para essa categoria.";
        }

        return resultado;
    }
}
