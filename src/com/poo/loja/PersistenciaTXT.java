package com.poo.loja;

import java.io.*;
import java.util.*;

public class PersistenciaTXT {

    // =========================
    // ROUPAS
    // =========================

    public static void salvarRoupas(List<Roupa> roupas) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("roupas.txt"))) {
            for (Roupa r : roupas) {
                writer.write(r.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<Integer, Roupa> carregarRoupasMap() {
        Map<Integer, Roupa> roupas = new HashMap<>();
        File arquivo = new File("roupas.txt");

        if (!arquivo.exists()) return roupas;

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                Roupa r = Roupa.fromFileString(linha);
                roupas.put(r.getId(), r);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return roupas;
    }

    // =========================
    // CLIENTES
    // =========================

    public static void salvarClientes(List<Cliente> clientes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("clientes.txt"))) {
            for (Cliente c : clientes) {
                writer.write(c.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<Integer, Cliente> carregarClientesMap() {
        Map<Integer, Cliente> clientes = new HashMap<>();
        File arquivo = new File("clientes.txt");

        if (!arquivo.exists()) return clientes;

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                Cliente c = Cliente.fromFileString(linha);
                clientes.put(c.getId(), c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return clientes;
    }

    // =========================
    // ALUGUEIS
    // =========================

    public static void salvarAlugueis(List<Aluguel> alugueis) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("alugueis.txt"))) {
            for (Aluguel a : alugueis) {
                writer.write(a.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> carregarAlugueisBruto() {
        List<String> linhas = new ArrayList<>();
        File arquivo = new File("alugueis.txt");

        if (!arquivo.exists()) return linhas;

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                linhas.add(linha);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return linhas;
    }
}