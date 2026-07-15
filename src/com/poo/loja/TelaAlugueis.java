package com.poo.loja;

import javax.swing.*;
import java.awt.*;

public class TelaAlugueis extends JFrame {

    private Loja loja = new Loja();

    public TelaAlugueis() {

        setTitle("Lumière Fashion - Aluguéis");
        setSize(900,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel principal = new JPanel(new BorderLayout(15,15));
        principal.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        principal.setBackground(new Color(245,245,245));

        JLabel titulo = new JLabel("TODOS OS ALUGUÉIS");
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setFont(new Font("Arial",Font.BOLD,28));

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced",Font.PLAIN,14));

        JScrollPane scroll = new JScrollPane(area);

        JButton atualizar = new JButton("Atualizar");

        atualizar.setBackground(new Color(180,140,60));
        atualizar.setForeground(Color.WHITE);

        atualizar.addActionListener(e ->
                area.setText(loja.listarAlugueis())
        );

        principal.add(titulo,BorderLayout.NORTH);
        principal.add(scroll,BorderLayout.CENTER);
        principal.add(atualizar,BorderLayout.SOUTH);

        add(principal);

        setVisible(true);
    }
}