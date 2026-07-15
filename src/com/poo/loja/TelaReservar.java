package com.poo.loja;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Tópico 2 - Reservas com Garantia de Disponibilidade.
 * Permite criar reservas futuras (bloqueando a peça no estoque) e depois
 * confirmá-las (transformando em aluguel ativo) ou cancelá-las.
 */
public class TelaReservar extends JFrame {

    private final Loja loja = new Loja();
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private JTextField txtIdCliente;
    private JTextField txtIdRoupa;
    private JTextField txtDataInicio;
    private JTextField txtDataFim;

    private JTextField txtIdReserva;

    private JTextArea areaResultado;

    public TelaReservar() {

        setTitle("Lumière Fashion - Reservar Roupa");
        setSize(950,720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel principal = new JPanel(new BorderLayout(15,15));
        principal.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        principal.setBackground(new Color(245,245,245));

        JLabel titulo = new JLabel("RESERVAS COM GARANTIA DE DISPONIBILIDADE");
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(70,70,70));

        principal.add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(20,20));
        centro.setOpaque(false);

        // =========================
        // FORMULÁRIO NOVA RESERVA
        // =========================

        JPanel nova = new JPanel(new GridBagLayout());
        nova.setBackground(Color.WHITE);
        nova.setBorder(BorderFactory.createTitledBorder("Nova Reserva"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.anchor = GridBagConstraints.WEST;

        txtIdCliente = new JTextField(12);
        txtIdRoupa = new JTextField(12);
        txtDataInicio = new JTextField(12);
        txtDataFim = new JTextField(12);

        JButton btnReservar = new JButton("Criar Reserva");
        btnReservar.setBackground(new Color(180,140,60));
        btnReservar.setForeground(Color.WHITE);
        btnReservar.setFocusPainted(false);

        gbc.gridx = 0; gbc.gridy = 0;
        nova.add(new JLabel("ID Cliente:"), gbc);
        gbc.gridx = 1;
        nova.add(txtIdCliente, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        nova.add(new JLabel("ID Roupa:"), gbc);
        gbc.gridx = 1;
        nova.add(txtIdRoupa, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        nova.add(new JLabel("Data Início (AAAA-MM-DD):"), gbc);
        gbc.gridx = 1;
        nova.add(txtDataInicio, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        nova.add(new JLabel("Data Fim (AAAA-MM-DD):"), gbc);
        gbc.gridx = 1;
        nova.add(txtDataFim, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        nova.add(btnReservar, gbc);

        // =========================
        // AÇÕES SOBRE RESERVA EXISTENTE
        // =========================

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        acoes.setBackground(Color.WHITE);
        acoes.setBorder(BorderFactory.createTitledBorder("Confirmar / Cancelar Reserva"));

        txtIdReserva = new JTextField(8);

        JButton btnConfirmar = new JButton("Confirmar Reserva");
        btnConfirmar.setBackground(new Color(90,110,140));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFocusPainted(false);

        JButton btnCancelar = new JButton("Cancelar Reserva");
        btnCancelar.setBackground(new Color(170,60,60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);

        acoes.add(new JLabel("ID Reserva:"));
        acoes.add(txtIdReserva);
        acoes.add(btnConfirmar);
        acoes.add(btnCancelar);

        JPanel superior = new JPanel(new BorderLayout(15,15));
        superior.setOpaque(false);
        superior.add(nova, BorderLayout.CENTER);
        superior.add(acoes, BorderLayout.SOUTH);

        // =========================
        // LISTA DE RESERVAS PENDENTES
        // =========================

        JPanel lista = new JPanel(new BorderLayout());
        lista.setBackground(Color.WHITE);
        lista.setBorder(BorderFactory.createTitledBorder("Reservas Pendentes"));

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

        centro.add(superior, BorderLayout.NORTH);
        centro.add(lista, BorderLayout.CENTER);

        principal.add(centro, BorderLayout.CENTER);

        add(principal);

        // =========================
        // EVENTOS
        // =========================

        btnReservar.addActionListener(e -> {

            try {

                int idCliente = Integer.parseInt(txtIdCliente.getText().trim());
                int idRoupa = Integer.parseInt(txtIdRoupa.getText().trim());
                LocalDate inicio = LocalDate.parse(txtDataInicio.getText().trim(), FORMATO);
                LocalDate fim = LocalDate.parse(txtDataFim.getText().trim(), FORMATO);

                String resposta = loja.realizarReserva(idCliente, idRoupa, inicio, fim);

                JOptionPane.showMessageDialog(this, resposta);

                atualizarLista();

            } catch (DateTimeParseException dtpe) {

                JOptionPane.showMessageDialog(this, "Informe as datas no formato AAAA-MM-DD.");

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this, "Informe IDs e datas válidos.");

            }

        });

        btnConfirmar.addActionListener(e -> {

            try {

                int idReserva = Integer.parseInt(txtIdReserva.getText().trim());
                String resposta = loja.confirmarReserva(idReserva);
                JOptionPane.showMessageDialog(this, resposta);
                atualizarLista();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this, "Informe um ID de reserva válido.");

            }

        });

        btnCancelar.addActionListener(e -> {

            try {

                int idReserva = Integer.parseInt(txtIdReserva.getText().trim());

                int confirmacao = JOptionPane.showConfirmDialog(
                        this,
                        "Tem certeza que deseja cancelar a reserva #" + idReserva + "?",
                        "Confirmar cancelamento",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirmacao != JOptionPane.YES_OPTION) return;

                String resposta = loja.cancelarReserva(idReserva);
                JOptionPane.showMessageDialog(this, resposta);
                atualizarLista();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this, "Informe um ID de reserva válido.");

            }

        });

        btnAtualizar.addActionListener(e -> atualizarLista());

        atualizarLista();

        setVisible(true);
    }

    private void atualizarLista() {
        List<Aluguel> reservas = loja.listarReservasPendentes();

        if (reservas.isEmpty()) {
            areaResultado.setText("Nenhuma reserva pendente no momento.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Aluguel a : reservas) {
            sb.append(a.toString()).append("\n");
        }

        areaResultado.setText(sb.toString());
    }
}
