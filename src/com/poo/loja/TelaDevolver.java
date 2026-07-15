package com.poo.loja;

import javax.swing.*;
import java.awt.*;

public class TelaDevolver extends JFrame {

    private final Loja loja = new Loja();

    private JTextField txtIdCliente;
    private JTextField txtIdRoupa;

    private JTextArea areaResultado;

    public TelaDevolver() {

        setTitle("Lumière Fashion - Devolver Roupa");
        setSize(900,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel principal = new JPanel(new BorderLayout(15,15));
        principal.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        principal.setBackground(new Color(245,245,245));

        JLabel titulo = new JLabel("REALIZAR DEVOLUÇÃO");
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(new Color(70,70,70));

        principal.add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(20,20));
        centro.setOpaque(false);

        JPanel devolucao = new JPanel(new GridBagLayout());
        devolucao.setBackground(Color.WHITE);
        devolucao.setBorder(BorderFactory.createTitledBorder("Devolver Roupa"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.anchor = GridBagConstraints.WEST;

        txtIdCliente = new JTextField(15);
        txtIdRoupa = new JTextField(15);

        JButton btnDevolver = new JButton("Realizar Devolução");
        JButton btnLimpar = new JButton("Limpar");

        btnDevolver.setBackground(new Color(180,140,60));
        btnDevolver.setForeground(Color.WHITE);
        btnDevolver.setFocusPainted(false);

        btnLimpar.setBackground(new Color(120,120,120));
        btnLimpar.setForeground(Color.WHITE);
        btnLimpar.setFocusPainted(false);

        gbc.gridx = 0;
        gbc.gridy = 0;
        devolucao.add(new JLabel("ID Cliente:"), gbc);

        gbc.gridx = 1;
        devolucao.add(txtIdCliente, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        devolucao.add(new JLabel("ID Roupa:"), gbc);

        gbc.gridx = 1;
        devolucao.add(txtIdRoupa, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        devolucao.add(btnDevolver, gbc);

        gbc.gridx = 2;
        devolucao.add(btnLimpar, gbc);

        JPanel lista = new JPanel(new BorderLayout());
        lista.setBackground(Color.WHITE);
        lista.setBorder(BorderFactory.createTitledBorder("Histórico de Aluguéis"));

        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(areaResultado);

        JButton btnAtualizar = new JButton("Atualizar Lista");

        btnAtualizar.setBackground(new Color(180,140,60));
        btnAtualizar.setForeground(Color.WHITE);
        btnAtualizar.setFocusPainted(false);

        lista.add(scroll, BorderLayout.CENTER);
        lista.add(btnAtualizar, BorderLayout.SOUTH);

        centro.add(devolucao, BorderLayout.NORTH);
        centro.add(lista, BorderLayout.CENTER);

        principal.add(centro, BorderLayout.CENTER);

        add(principal);

        // =========================
        // EVENTOS
        // =========================

        btnDevolver.addActionListener(e -> {

            try {

                String resposta = loja.realizarDevolucao(
                        Integer.parseInt(txtIdCliente.getText()),
                        Integer.parseInt(txtIdRoupa.getText())
                );

                JOptionPane.showMessageDialog(this, resposta);

                areaResultado.setText(loja.listarAlugueis());

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Informe IDs válidos."
                );

            }

        });

        btnAtualizar.addActionListener(e ->
                areaResultado.setText(loja.listarAlugueis())
        );

        btnLimpar.addActionListener(e -> {

            txtIdCliente.setText("");
            txtIdRoupa.setText("");
            areaResultado.setText("");

        });

        setVisible(true);
    }
}