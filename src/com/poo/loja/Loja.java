package com.poo.loja;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Loja {
    private Map<Integer, Roupa> estoque = new HashMap<>();
    private Map<Integer, Cliente> clientes = new HashMap<>();
    private List<Aluguel> alugueis = new ArrayList<>();

    public Loja() {
        estoque = PersistenciaTXT.carregarRoupasMap();
        clientes = PersistenciaTXT.carregarClientesMap();

        alugueis = new ArrayList<>();

        List<String> linhas = PersistenciaTXT.carregarAlugueisBruto();

        for (String linha : linhas) {
            String[] partes = linha.split(";");

            int idCliente = Integer.parseInt(partes[0]);
            int idRoupa = Integer.parseInt(partes[1]);

            LocalDate dataAluguel = LocalDate.parse(partes[2]);

            LocalDate dataDevolucao = partes[3].equals("null")
                    ? null
                    : LocalDate.parse(partes[3]);

            boolean ativo = Boolean.parseBoolean(partes[4]);

            Cliente cliente = clientes.get(idCliente);
            Roupa roupa = estoque.get(idRoupa);

            if (cliente != null && roupa != null) {
                alugueis.add(new Aluguel(
                        cliente,
                        roupa,
                        dataAluguel,
                        dataDevolucao,
                        ativo
                ));
            }
        }
    }

    // =============================================
    // CADASTRO
    // =============================================

    public String cadastrarRoupa(Roupa roupa) {
        if (estoque.containsKey(roupa.getId()))
            return "Erro: já existe uma peça cadastrada com o ID " + roupa.getId() + ".";

        estoque.put(roupa.getId(), roupa);
        PersistenciaTXT.salvarRoupas(new ArrayList<>(estoque.values()));
        return "✔ Roupa cadastrada com sucesso.";
    }

    public String cadastrarCliente(Cliente cliente) {
        if (clientes.containsKey(cliente.getId()))
            return "Erro: Cliente já cadastrado.";

        clientes.put(cliente.getId(), cliente);
        PersistenciaTXT.salvarClientes(new ArrayList<>(clientes.values()));
        return "✔ Cliente cadastrado com sucesso.";
    }

    // =============================================
    // SCRUM-24: sugestão automática de próximo ID
    // =============================================

    public int sugerirProximoId() {
        if (estoque.isEmpty()) return 1;
        return estoque.keySet().stream().mapToInt(i -> i).max().getAsInt() + 1;
    }

    // =============================================
    // ALUGUEL E DEVOLUÇÃO
    // =============================================

    public String realizarAluguel(int idCliente, int idRoupa) {
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
            alugueis.add(new Aluguel(cliente, roupa));
            PersistenciaTXT.salvarAlugueis(alugueis);
            PersistenciaTXT.salvarRoupas(new ArrayList<>(estoque.values()));
            return "✔ Aluguel realizado com sucesso.";
        } else {
            return "Roupa indisponível no estoque.";
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
        return "Aluguel ativo não encontrado para este cliente e roupa.";
    }

    // =============================================
    // SCRUM-21: listar aluguéis ativos de um cliente
    // =============================================

    public String listarAlugueisAtivos(int idCliente) {
        String resultado = alugueis.stream()
                .filter(a -> a.getCliente().getId() == idCliente && a.isAtivo())
                .map(a -> "Roupa ID: " + a.getRoupa().getId()
                        + " - " + a.getRoupa().getDescricao()
                        + " | Categoria: " + a.getRoupa().getCategoria()
                        + " | Alugado em: " + a.getDataAluguel())
                .collect(Collectors.joining("\n"));

        return resultado.isEmpty()
                ? "Nenhum aluguel ativo encontrado para este cliente."
                : resultado;
    }

    // =============================================
    // SCRUM-17: filtrar roupas disponíveis por categoria
    // =============================================

    public String listarPorCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            return "Digite uma categoria para buscar.";
        }

        String resultado = estoque.values().stream()
                .filter(r -> r.getCategoria().equalsIgnoreCase(categoria.trim())
                        && r.getQuantidade() > 0)
                .map(Roupa::toString)
                .collect(Collectors.joining("\n"));

        return resultado.isEmpty()
                ? "Nenhuma roupa disponível para a categoria: " + categoria
                : resultado;
    }

    // =============================================
    // LISTAGENS GERAIS
    // =============================================

    public String listarRoupa() {
        if (estoque.isEmpty()) return "Nenhuma roupa cadastrada.";
        return estoque.values().stream()
                .map(Roupa::toString)
                .collect(Collectors.joining("\n"));
    }

    public String listarClientes() {
        if (clientes.isEmpty()) return "Nenhum cliente cadastrado.";
        return clientes.values().stream()
                .map(Cliente::toString)
                .collect(Collectors.joining("\n"));
    }

    public String listarAlugueis() {
        if (alugueis.isEmpty()) return "Nenhum aluguel registrado.";
        return alugueis.stream()
                .map(Aluguel::toString)
                .collect(Collectors.joining("\n"));
    }

    public String historicoCliente(int id) {
        String resultado = alugueis.stream()
                .filter(a -> a.getCliente().getId() == id)
                .map(Aluguel::toString)
                .collect(Collectors.joining("\n"));

        return resultado.isEmpty()
                ? "Nenhum histórico encontrado para este cliente."
                : resultado;
    }
}
