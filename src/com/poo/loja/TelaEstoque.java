package com.poo.loja;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaEstoque extends JFrame {

    private Loja loja = new Loja();

    public TelaEstoque(){

        setTitle("Lumière Fashion - Estoque");
        setSize(900,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel principal = new JPanel(new BorderLayout(15,15));
        principal.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        principal.setBackground(new Color(245,245,245));

        JLabel titulo = new JLabel("ESTOQUE");
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setFont(new Font("Arial",Font.BOLD,28));

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced",Font.PLAIN,14));

        JScrollPane scroll = new JScrollPane(area);

        JPanel controles = new JPanel(new FlowLayout(FlowLayout.CENTER));
        controles.setOpaque(false);

        JButton atualizar = new JButton("Listar Estoque Completo");

        atualizar.setBackground(new Color(180,140,60));
        atualizar.setForeground(Color.WHITE);
        atualizar.setFocusPainted(false);

        JLabel lblLimite = new JLabel("Limite de alerta:");
        JTextField txtLimite = new JTextField("2", 4);

        JButton btnAlerta = new JButton("Ver Estoque Baixo");
        btnAlerta.setBackground(new Color(170,60,60));
        btnAlerta.setForeground(Color.WHITE);
        btnAlerta.setFocusPainted(false);

        controles.add(atualizar);
        controles.add(lblLimite);
        controles.add(txtLimite);
        controles.add(btnAlerta);

        atualizar.addActionListener(e ->
                area.setText(loja.listarRoupa())
        );

        btnAlerta.addActionListener(e -> {

            try {

                int limite = Integer.parseInt(txtLimite.getText());
                List<Roupa> baixas = loja.roupasComEstoqueBaixo(limite);

                if (baixas.isEmpty()) {
                    area.setText("Nenhuma roupa com estoque igual ou abaixo de " + limite + ".");
                    return;
                }

                StringBuilder sb = new StringBuilder();
                sb.append("⚠ Peças com estoque baixo (limite = ").append(limite).append("):\n\n");

                for (Roupa r : baixas) {
                    sb.append(r).append("\n");
                }

                area.setText(sb.toString());

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(this, "Informe um limite numérico válido.");

            }

        });

        principal.add(titulo,BorderLayout.NORTH);
        principal.add(scroll,BorderLayout.CENTER);
        principal.add(controles,BorderLayout.SOUTH);

        add(principal);

        setVisible(true);

    }

}