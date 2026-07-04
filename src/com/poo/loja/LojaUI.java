package com.poo.loja;

import javax.swing.*;
import java.awt.*;

public class LojaUI extends JFrame {

    private Loja loja = new Loja();

    public LojaUI() {
        setTitle("Sistema Loja de Aluguel de Roupas");
        setSize(750, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.add("Cadastrar Roupa", painelCadastrarRoupa());
        tabs.add("Cadastrar Cliente", painelCadastrarCliente());
        tabs.add("Alugar", painelAlugar());
        tabs.add("Devolver", painelDevolver());
        tabs.add("Buscar por Categoria", painelBuscarPorCategoria());
        tabs.add("Listar Roupas", painelListarRoupas());
        tabs.add("Listar Clientes", painelListarClientes());
        tabs.add("Todos Aluguéis", painelListarAlugueis());

        add(tabs);
    }

    // =============================================
    // SCRUM-24: Cadastrar Roupa com sugestão de ID
    // =============================================

    private JPanel painelCadastrarRoupa() {
        JPanel p = new JPanel(new FlowLayout());

        JTextField id = new JTextField(5);
        JTextField desc = new JTextField(10);
        JTextField cat = new JTextField(10);
        JTextField qtd = new JTextField(5);

        // Botão sugerir ID automático
        JButton btnSugerir = new JButton("Sugerir ID");
        btnSugerir.addActionListener(e ->
                id.setText(String.valueOf(loja.sugerirProximoId()))
        );

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.addActionListener(e -> {
            try {
                Roupa r = new Roupa(
                        Integer.parseInt(id.getText()),
                        desc.getText(),
                        cat.getText(),
                        Integer.parseInt(qtd.getText())
                );
                JOptionPane.showMessageDialog(this, loja.cadastrarRoupa(r));
                id.setText("");
                desc.setText("");
                cat.setText("");
                qtd.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro: ID e Quantidade devem ser números inteiros.",
                        "Erro de validação",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro: " + ex.getMessage(),
                        "Erro de validação",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        p.add(new JLabel("ID:"));
        p.add(id);
        p.add(btnSugerir);
        p.add(new JLabel("Descrição:"));
        p.add(desc);
        p.add(new JLabel("Categoria:"));
        p.add(cat);
        p.add(new JLabel("Qtd:"));
        p.add(qtd);
        p.add(btnCadastrar);

        return p;
    }

    // =============================================
    // Cadastrar Cliente
    // =============================================

    private JPanel painelCadastrarCliente() {
        JPanel p = new JPanel(new FlowLayout());

        JTextField id = new JTextField(5);
        JTextField nome = new JTextField(10);
        JButton btn = new JButton("Cadastrar");

        btn.addActionListener(e -> {
            try {
                Cliente c = new Cliente(
                        Integer.parseInt(id.getText()),
                        nome.getText()
                );
                JOptionPane.showMessageDialog(this, loja.cadastrarCliente(c));
                id.setText("");
                nome.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro: ID deve ser um número inteiro.",
                        "Erro de validação",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro: " + ex.getMessage(),
                        "Erro de validação",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        p.add(new JLabel("ID:"));
        p.add(id);
        p.add(new JLabel("Nome:"));
        p.add(nome);
        p.add(btn);

        return p;
    }

    // =============================================
    // Alugar
    // =============================================

    private JPanel painelAlugar() {
        JPanel p = new JPanel(new FlowLayout());

        JTextField idCliente = new JTextField(5);
        JTextField idRoupa = new JTextField(5);
        JButton btn = new JButton("Alugar");

        btn.addActionListener(e -> {
            try {
                String msg = loja.realizarAluguel(
                        Integer.parseInt(idCliente.getText()),
                        Integer.parseInt(idRoupa.getText())
                );
                JOptionPane.showMessageDialog(this, msg);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro: IDs devem ser números inteiros.",
                        "Erro de validação",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        p.add(new JLabel("ID Cliente:"));
        p.add(idCliente);
        p.add(new JLabel("ID Roupa:"));
        p.add(idRoupa);
        p.add(btn);

        return p;
    }

    // =============================================
    // SCRUM-21: Devolver — buscar cliente → listar → confirmar
    // =============================================

    private JPanel painelDevolver() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel topo = new JPanel(new FlowLayout());
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JTextField idCliente = new JTextField(5);
        JTextField idRoupa = new JTextField(5);

        JButton btnBuscar = new JButton("Ver Aluguéis Ativos");
        JButton btnDevolver = new JButton("Confirmar Devolução");

        btnBuscar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idCliente.getText().trim());
                area.setText(loja.listarAlugueisAtivos(id));
            } catch (NumberFormatException ex) {
                area.setText("Digite um ID de cliente válido.");
            }
        });

        btnDevolver.addActionListener(e -> {
            try {
                int idC = Integer.parseInt(idCliente.getText().trim());
                int idR = Integer.parseInt(idRoupa.getText().trim());
                String msg = loja.realizarDevolucao(idC, idR);
                JOptionPane.showMessageDialog(this, msg);
                area.setText(loja.listarAlugueisAtivos(idC));
                idRoupa.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro: IDs devem ser números inteiros.",
                        "Erro de validação",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        topo.add(new JLabel("ID Cliente:"));
        topo.add(idCliente);
        topo.add(btnBuscar);
        topo.add(new JLabel("ID Roupa:"));
        topo.add(idRoupa);
        topo.add(btnDevolver);

        p.add(topo, BorderLayout.NORTH);
        p.add(new JScrollPane(area), BorderLayout.CENTER);

        return p;
    }

    // =============================================
    // SCRUM-17: Buscar roupas disponíveis por categoria
    // =============================================

    private JPanel painelBuscarPorCategoria() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel topo = new JPanel(new FlowLayout());
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JTextField cat = new JTextField(15);
        JButton btn = new JButton("Buscar");

        btn.addActionListener(e -> {
            String categoria = cat.getText().trim();
            if (categoria.isEmpty()) {
                area.setText("Digite uma categoria para buscar.");
                return;
            }
            area.setText(loja.listarPorCategoria(categoria));
        });

        topo.add(new JLabel("Categoria:"));
        topo.add(cat);
        topo.add(btn);

        p.add(topo, BorderLayout.NORTH);
        p.add(new JScrollPane(area), BorderLayout.CENTER);

        return p;
    }

    // =============================================
    // Listagens gerais
    // =============================================

    private JPanel painelListarRoupas() {
        JPanel p = new JPanel(new BorderLayout());
        JTextArea area = new JTextArea();
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JButton btn = new JButton("Listar todas as roupas");

        btn.addActionListener(e -> area.setText(loja.listarRoupa()));

        p.add(btn, BorderLayout.NORTH);
        p.add(new JScrollPane(area), BorderLayout.CENTER);

        return p;
    }

    private JPanel painelListarClientes() {
        JPanel p = new JPanel(new BorderLayout());
        JTextArea area = new JTextArea();
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JButton btn = new JButton("Listar todos os clientes");

        btn.addActionListener(e -> area.setText(loja.listarClientes()));

        p.add(btn, BorderLayout.NORTH);
        p.add(new JScrollPane(area), BorderLayout.CENTER);

        return p;
    }

    private JPanel painelListarAlugueis() {
        JPanel p = new JPanel(new BorderLayout());
        JTextArea area = new JTextArea();
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JButton btn = new JButton("Listar todos os aluguéis");

        btn.addActionListener(e -> area.setText(loja.listarAlugueis()));

        p.add(btn, BorderLayout.NORTH);
        p.add(new JScrollPane(area), BorderLayout.CENTER);

        return p;
    }

    // =============================================
    // Main
    // =============================================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LojaUI().setVisible(true));
    }
}
