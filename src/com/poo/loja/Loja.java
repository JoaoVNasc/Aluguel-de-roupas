package com.poo.loja;
import java.time.LocalDate;   // 👈 ADICIONE ISSO
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

    public String cadastrarRoupa(Roupa roupa) {
        if (estoque.containsKey(roupa.getId()))
            throw new IllegalArgumentException("Roupa já cadastrada.");
       // if (estoque.containsKey(roupa.getId()))
            //return "Erro: Roupa já cadastrada.";

        estoque.put(roupa.getId(), roupa);
        PersistenciaTXT.salvarRoupas(new ArrayList<>(estoque.values()));
        return "✔ Roupa cadastrada com sucesso.";
    }

    public String cadastrarCliente(Cliente cliente) {
        if (clientes.containsKey(cliente.getId()))
            throw new IllegalArgumentException("Cliente já cadastrado.");
       // if (clientes.containsKey(cliente.getId()))
           // return "Erro: Cliente já cadastrado.";

        clientes.put(cliente.getId(), cliente);
        PersistenciaTXT.salvarClientes(new ArrayList<>(clientes.values()));
        return "✔ Cliente cadastrado com sucesso.";
    }

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
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
        //return alugueis.stream()
               // .map(Aluguel::toString)
                //.collect(Collectors.joining("\n"));
    }

    public String historicoCliente(int id) {
        return alugueis.stream()
                .filter(a -> a.getCliente().getId() == id)
                .map(Aluguel::toString)
                .collect(Collectors.joining("\n"));
    }
}
