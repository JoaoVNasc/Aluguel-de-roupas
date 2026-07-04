package com.poo.loja;

import javax.swing.*;
import java.awt.*;

public class LojaUI extends JFrame {

    private Loja loja = new Loja();

    public LojaUI() {

        setTitle("Sistema Loja de Aluguel de Roupas");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.add("Cadastrar Roupa", painelCadastrarRoupa());
        tabs.add("Cadastrar Cliente", painelCadastrarCliente());
        tabs.add("Alugar", painelAlugar());
        tabs.add("Devolver", painelDevolver());
        tabs.add("Listar Roupas", painelListarRoupas());
        tabs.add("Listar Clientes", painelListarClientes());
        tabs.add("Todos Aluguéis", painelListarAlugueis());

        add(tabs);
    }

    private JPanel painelCadastrarRoupa() {
        JPanel p = new JPanel(new FlowLayout());

        JTextField id = new JTextField(5);
        JTextField desc = new JTextField(10);
        JTextField cat = new JTextField(10);
        JTextField qtd = new JTextField(5);

        JButton btn = new JButton("Cadastrar");

        btn.addActionListener(e -> {
            try {
                Roupa r = new Roupa(
                        Integer.parseInt(id.getText()),
                        desc.getText(),
                        cat.getText(),
                        Integer.parseInt(qtd.getText())
                );

                JOptionPane.showMessageDialog(this,
                        loja.cadastrarRoupa(r));

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        p.add(new JLabel("ID:"));
        p.add(id);
        p.add(new JLabel("Descrição:"));
        p.add(desc);
        p.add(new JLabel("Categoria:"));
        p.add(cat);
        p.add(new JLabel("Qtd:"));
        p.add(qtd);
        p.add(btn);

        return p;
    }

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

                JOptionPane.showMessageDialog(this,
                        loja.cadastrarCliente(c));

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro: " + ex.getMessage(),
                        "Erro",
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

    private JPanel painelAlugar() {
        JPanel p = new JPanel(new FlowLayout());

        JTextField idCliente = new JTextField(5);
        JTextField idRoupa = new JTextField(5);
        JButton btn = new JButton("Alugar");

        btn.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        loja.realizarAluguel(
                                Integer.parseInt(idCliente.getText()),
                                Integer.parseInt(idRoupa.getText())
                        )
                )
        );

        p.add(new JLabel("Cliente:"));
        p.add(idCliente);
        p.add(new JLabel("Roupa:"));
        p.add(idRoupa);
        p.add(btn);

        return p;
    }

    private JPanel painelDevolver() {
        JPanel p = new JPanel(new FlowLayout());

        JTextField idCliente = new JTextField(5);
        JTextField idRoupa = new JTextField(5);
        JButton btn = new JButton("Devolver");

        btn.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        loja.realizarDevolucao(
                                Integer.parseInt(idCliente.getText()),
                                Integer.parseInt(idRoupa.getText())
                        )
                )
        );

        p.add(new JLabel("Cliente:"));
        p.add(idCliente);
        p.add(new JLabel("Roupa:"));
        p.add(idRoupa);
        p.add(btn);

        return p;
    }

    private JPanel painelListarRoupas() {
        JPanel p = new JPanel(new BorderLayout());
        JTextArea area = new JTextArea();
        JButton btn = new JButton("Listar");

        btn.addActionListener(e -> area.setText(loja.listarRoupa()));

        p.add(btn, BorderLayout.NORTH);
        p.add(new JScrollPane(area), BorderLayout.CENTER);

        return p;
    }

    private JPanel painelListarClientes() {
        JPanel p = new JPanel(new BorderLayout());
        JTextArea area = new JTextArea();
        JButton btn = new JButton("Listar");

        btn.addActionListener(e -> area.setText(loja.listarClientes()));

        p.add(btn, BorderLayout.NORTH);
        p.add(new JScrollPane(area), BorderLayout.CENTER);

        return p;
    }

    private JPanel painelListarAlugueis() {
        JPanel p = new JPanel(new BorderLayout());
        JTextArea area = new JTextArea();
        JButton btn = new JButton("Listar");

        btn.addActionListener(e -> area.setText(loja.listarAlugueis()));

        p.add(btn, BorderLayout.NORTH);
        p.add(new JScrollPane(area), BorderLayout.CENTER);

        return p;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LojaUI().setVisible(true));
    }
}