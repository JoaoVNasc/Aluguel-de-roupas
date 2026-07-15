package com.poo.loja;

import javax.swing.*;
import java.awt.*;

public class TelaAlugar extends JFrame {

    private final Loja loja = new Loja();

    private JTextField txtIdCliente;
    private JTextField txtIdRoupa;
    private JTextField txtDias;

    private JTextArea areaResultado;

    public TelaAlugar() {

        setTitle("Lumière Fashion - Alugar Roupa");
        setSize(900,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel principal = new JPanel(new BorderLayout(15,15));
        principal.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        principal.setBackground(new Color(245,245,245));

        JLabel titulo = new JLabel("REALIZAR ALUGUEL");
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(new Color(70,70,70));

        principal.add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(20,20));
        centro.setOpaque(false);

        JPanel aluguel = new JPanel(new GridBagLayout());
        aluguel.setBackground(Color.WHITE);
        aluguel.setBorder(BorderFactory.createTitledBorder("Novo Aluguel"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.anchor = GridBagConstraints.WEST;

        txtIdCliente = new JTextField(15);
        txtIdRoupa = new JTextField(15);
        txtDias = new JTextField(15);

        JButton btnAlugar = new JButton("Realizar Aluguel");
        JButton btnLimpar = new JButton("Limpar");

        btnAlugar.setBackground(new Color(180,140,60));
        btnAlugar.setForeground(Color.WHITE);
        btnAlugar.setFocusPainted(false);

        btnLimpar.setBackground(new Color(120,120,120));
        btnLimpar.setForeground(Color.WHITE);
        btnLimpar.setFocusPainted(false);

        gbc.gridx = 0;
        gbc.gridy = 0;
        aluguel.add(new JLabel("ID Cliente:"), gbc);

        gbc.gridx = 1;
        aluguel.add(txtIdCliente, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        aluguel.add(new JLabel("ID Roupa:"), gbc);

        gbc.gridx = 1;
        aluguel.add(txtIdRoupa, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        aluguel.add(new JLabel("Dias de Aluguel:"), gbc);

        gbc.gridx = 1;
        aluguel.add(txtDias, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        aluguel.add(btnAlugar, gbc);

        gbc.gridx = 2;
        aluguel.add(btnLimpar, gbc);

        JPanel lista = new JPanel(new BorderLayout());
        lista.setBackground(Color.WHITE);
        lista.setBorder(BorderFactory.createTitledBorder("Aluguéis"));

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

        centro.add(aluguel, BorderLayout.NORTH);
        centro.add(lista, BorderLayout.CENTER);

        principal.add(centro, BorderLayout.CENTER);

        add(principal);

        // =========================
        // EVENTOS
        // =========================

        btnAlugar.addActionListener(e -> {

            try {

                int idCliente = Integer.parseInt(txtIdCliente.getText());
                int idRoupa = Integer.parseInt(txtIdRoupa.getText());
                int dias = txtDias.getText().trim().isEmpty() ? 1 : Integer.parseInt(txtDias.getText().trim());
                if (dias <= 0) dias = 1;

                Cliente cliente = loja.buscarClientePorId(idCliente);
                Roupa roupa = loja.buscarRoupaPorId(idRoupa);

                if (cliente == null) {
                    JOptionPane.showMessageDialog(this, "Cliente não encontrado.");
                    return;
                }

                if (roupa == null) {
                    JOptionPane.showMessageDialog(this, "Roupa não encontrada.");
                    return;
                }

                double orcamento = loja.calcularOrcamento(idRoupa, dias);

                String resumo = "Resumo do Pedido\n\n"
                        + "Cliente: " + cliente.getNome() + "\n"
                        + "Roupa: " + roupa.getDescricao() + "\n"
                        + "Dias: " + dias + "\n"
                        + "Valor Unitário (diária): R$ " + String.format("%.2f", roupa.getValorDiaria()) + "\n"
                        + "Valor Total do Orçamento: R$ " + String.format("%.2f", orcamento) + "\n\n"
                        + "Confirmar o aluguel?";

                int confirmacao = JOptionPane.showConfirmDialog(
                        this,
                        resumo,
                        "Confirmar Aluguel",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirmacao != JOptionPane.YES_OPTION) return;

                String resposta = loja.realizarAluguel(idCliente, idRoupa, dias);

                JOptionPane.showMessageDialog(this, resposta);

                areaResultado.setText(loja.listarAlugueis());

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Informe IDs e quantidade de dias válidos."
                );

            }

        });

        btnAtualizar.addActionListener(e ->
                areaResultado.setText(loja.listarAlugueis())
        );

        btnLimpar.addActionListener(e -> {

            txtIdCliente.setText("");
            txtIdRoupa.setText("");
            txtDias.setText("");
            areaResultado.setText("");

        });

        setVisible(true);
    }
}
