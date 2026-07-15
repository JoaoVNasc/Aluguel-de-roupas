package com.poo.loja;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Tópico 5 - Relatórios Administrativos de Faturamento.
 */
public class TelaRelatorios extends JFrame {

    private final Loja loja = new Loja();
    private static final DateTimeFormatter FORMATO_ENTRADA = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter FORMATO_SAIDA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private JTextField txtDataInicio;
    private JTextField txtDataFim;

    private JLabel lblTotal;
    private JTextArea areaResultado;

    public TelaRelatorios() {

        setTitle("Lumière Fashion - Relatórios de Faturamento");
        setSize(950,720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel principal = new JPanel(new BorderLayout(15,15));
        principal.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        principal.setBackground(new Color(245,245,245));

        JLabel titulo = new JLabel("RELATÓRIO DE FATURAMENTO");
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(new Color(70,70,70));

        principal.add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(20,20));
        centro.setOpaque(false);

        // =========================
        // FILTRO DE PERÍODO
        // =========================

        JPanel filtro = new JPanel(new GridBagLayout());
        filtro.setBackground(Color.WHITE);
        filtro.setBorder(BorderFactory.createTitledBorder("Período de Consulta"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.anchor = GridBagConstraints.WEST;

        txtDataInicio = new JTextField(12);
        txtDataFim = new JTextField(12);

        JButton btnGerar = new JButton("Gerar Relatório de Faturamento");
        btnGerar.setBackground(new Color(20,20,20));
        btnGerar.setForeground(Color.WHITE);
        btnGerar.setFocusPainted(false);

        gbc.gridx = 0; gbc.gridy = 0;
        filtro.add(new JLabel("Data Início (AAAA-MM-DD):"), gbc);
        gbc.gridx = 1;
        filtro.add(txtDataInicio, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        filtro.add(new JLabel("Data Fim (AAAA-MM-DD):"), gbc);
        gbc.gridx = 1;
        filtro.add(txtDataFim, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        filtro.add(btnGerar, gbc);

        lblTotal = new JLabel("Faturamento total: R$ 0,00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 20));
        lblTotal.setForeground(new Color(90,110,140));
        lblTotal.setHorizontalAlignment(SwingConstants.CENTER);
        lblTotal.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

        JPanel superior = new JPanel(new BorderLayout());
        superior.setOpaque(false);
        superior.add(filtro, BorderLayout.CENTER);
        superior.add(lblTotal, BorderLayout.SOUTH);

        // =========================
        // TABELA DETALHADA (TEXTUAL)
        // =========================

        JPanel lista = new JPanel(new BorderLayout());
        lista.setBackground(Color.WHITE);
        lista.setBorder(BorderFactory.createTitledBorder("Aluguéis do Período"));

        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(areaResultado);
        lista.add(scroll, BorderLayout.CENTER);

        centro.add(superior, BorderLayout.NORTH);
        centro.add(lista, BorderLayout.CENTER);

        principal.add(centro, BorderLayout.CENTER);

        add(principal);

        // =========================
        // EVENTOS
        // =========================

        btnGerar.addActionListener(e -> {

            try {

                LocalDate inicio = LocalDate.parse(txtDataInicio.getText().trim(), FORMATO_ENTRADA);
                LocalDate fim = LocalDate.parse(txtDataFim.getText().trim(), FORMATO_ENTRADA);

                if (fim.isBefore(inicio)) {
                    JOptionPane.showMessageDialog(this, "A data de fim não pode ser anterior à data de início.");
                    return;
                }

                double total = loja.calcularFaturamentoPeriodo(inicio, fim);
                List<Aluguel> alugueisPeriodo = loja.listarAlugueisPeriodo(inicio, fim);

                lblTotal.setText("Faturamento total ("
                        + inicio.format(FORMATO_SAIDA) + " a " + fim.format(FORMATO_SAIDA)
                        + "): R$ " + String.format("%.2f", total));

                if (alugueisPeriodo.isEmpty()) {
                    areaResultado.setText("Nenhum aluguel encontrado no período informado.");
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (Aluguel a : alugueisPeriodo) {
                        sb.append(a.toString()).append("\n");
                    }
                    areaResultado.setText(sb.toString());
                }

            } catch (DateTimeParseException dtpe) {

                JOptionPane.showMessageDialog(this, "Informe as datas no formato AAAA-MM-DD.");

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this, "Não foi possível gerar o relatório. Verifique as datas informadas.");

            }

        });

        setVisible(true);
    }
}
