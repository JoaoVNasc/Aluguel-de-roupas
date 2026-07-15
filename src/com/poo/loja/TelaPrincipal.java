package com.poo.loja;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaPrincipal extends JFrame {

    private TelaRoupas telaRoupas;
    private TelaClientes telaClientes;
    private TelaAlugar telaAlugar;
    private TelaReservar telaReservar;
    private TelaDevolver telaDevolver;
    private TelaAlugueis telaAlugueis;
    private TelaEstoque telaEstoque;
    private TelaRelatorios telaRelatorios;

    public TelaPrincipal() {

        setTitle("Lumière Fashion");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel principal = new JPanel(new BorderLayout());

        // =========================
        // MENU LATERAL
        // =========================

        JPanel menu = new JPanel();
        menu.setPreferredSize(new Dimension(230, 0));
        menu.setBackground(new Color(20,20,20));
        menu.setLayout(new GridLayout(9,1,5,10));

        JButton roupas = criarBotao("Roupas", "/imagens/roupas.png");
        roupas.addActionListener(e -> {
            if (telaRoupas == null || !telaRoupas.isDisplayable()) {
                telaRoupas = new TelaRoupas();
            } else {
                telaRoupas.toFront();
                telaRoupas.requestFocus();
            }
        });
        menu.add(roupas);

        JButton clientes = criarBotao("Clientes", "/imagens/clientes.png");
        clientes.addActionListener(e -> {
            if (telaClientes == null || !telaClientes.isDisplayable()) {
                telaClientes = new TelaClientes();
            } else {
                telaClientes.toFront();
                telaClientes.requestFocus();
            }
        });
        menu.add(clientes);

        JButton alugar = criarBotao("Alugar", "/imagens/alugar.png");
        alugar.addActionListener(e -> {
            if (telaAlugar == null || !telaAlugar.isDisplayable()) {
                telaAlugar = new TelaAlugar();
            } else {
                telaAlugar.toFront();
                telaAlugar.requestFocus();
            }
        });
        menu.add(alugar);

        JButton reservar = criarBotao("Reservar", null);
        reservar.addActionListener(e -> {
            if (telaReservar == null || !telaReservar.isDisplayable()) {
                telaReservar = new TelaReservar();
            } else {
                telaReservar.toFront();
                telaReservar.requestFocus();
            }
        });
        menu.add(reservar);

        JButton devolver = criarBotao("Devolver", "/imagens/devolver.png");
        devolver.addActionListener(e -> {
            if (telaDevolver == null || !telaDevolver.isDisplayable()) {
                telaDevolver = new TelaDevolver();
            } else {
                telaDevolver.toFront();
                telaDevolver.requestFocus();
            }
        });
        menu.add(devolver);

        JButton alugueis = criarBotao("Aluguéis", "/imagens/alugueis.png");
        alugueis.addActionListener(e -> {
            if (telaAlugueis == null || !telaAlugueis.isDisplayable()) {
                telaAlugueis = new TelaAlugueis();
            } else {
                telaAlugueis.toFront();
                telaAlugueis.requestFocus();
            }
        });
        menu.add(alugueis);

        JButton estoque = criarBotao("Estoque", "/imagens/estoque.png");
        estoque.addActionListener(e -> {
            if (telaEstoque == null || !telaEstoque.isDisplayable()) {
                telaEstoque = new TelaEstoque();
            } else {
                telaEstoque.toFront();
                telaEstoque.requestFocus();
            }
        });
        menu.add(estoque);

        JButton relatorios = criarBotao("Relatórios", null);
        relatorios.addActionListener(e -> {
            if (telaRelatorios == null || !telaRelatorios.isDisplayable()) {
                telaRelatorios = new TelaRelatorios();
            } else {
                telaRelatorios.toFront();
                telaRelatorios.requestFocus();
            }
        });
        menu.add(relatorios);

        JButton sair = criarBotao("Sair", "/imagens/sair.png");
        sair.addActionListener(e -> {

            UIManager.put("OptionPane.background", Color.WHITE);
            UIManager.put("Panel.background", Color.WHITE);

            int opcao = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja realmente sair do Lumière Fashion?",
                    "Lumière Fashion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (opcao == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        menu.add(sair);

        // =========================
        // IMAGEM CENTRAL
        // =========================

        JPanel centro = new JPanel() {
            Image fundo = new ImageIcon(getClass().getResource("/imagens/tela_inicial.png")).getImage();

            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(fundo, 0, 0, getWidth(), getHeight(), this);
            }
        };

        principal.add(menu, BorderLayout.WEST);
        principal.add(centro, BorderLayout.CENTER);

        add(principal);

        setVisible(true);
    }

    private JButton criarBotao(String nome, String caminho) {

        JButton botao = new JButton();
        botao.setLayout(new BorderLayout());
        botao.setBackground(new Color(20,20,20));
        botao.setBorderPainted(false);
        botao.setFocusPainted(false);

        // =========================
        // ÍCONE AJUSTADO
        // =========================

        if (caminho != null) {

            ImageIcon original = new ImageIcon(getClass().getResource(caminho));
            Image imagemOriginal = original.getImage();

            int larguraOriginal = imagemOriginal.getWidth(null);
            int alturaOriginal = imagemOriginal.getHeight(null);

            int tamanhoMaximo = 90;

            double escala = Math.min(
                    (double) tamanhoMaximo / larguraOriginal,
                    (double) tamanhoMaximo / alturaOriginal
            );

            int novaLargura = (int) (larguraOriginal * escala);
            int novaAltura = (int) (alturaOriginal * escala);

            Image imagem = imagemOriginal.getScaledInstance(novaLargura, novaAltura, Image.SCALE_SMOOTH);

            JLabel icone = new JLabel(new ImageIcon(imagem));
            icone.setHorizontalAlignment(SwingConstants.CENTER);
            icone.setVerticalAlignment(SwingConstants.CENTER);

            botao.add(icone, BorderLayout.CENTER);
        }

        // =========================
        // TEXTO
        // =========================

        JLabel texto = new JLabel(nome, SwingConstants.CENTER);
        texto.setForeground(Color.WHITE);
        texto.setFont(new Font("Arial", Font.BOLD, 16));
        texto.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));

        botao.add(texto, caminho != null ? BorderLayout.SOUTH : BorderLayout.CENTER);

        // =========================
        // EFEITO MOUSE
        // =========================

        botao.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                botao.setBackground(new Color(150,110,40));
            }

            public void mouseExited(MouseEvent e) {
                botao.setBackground(new Color(20,20,20));
            }
        });

        return botao;
    }

    public static void main(String[] args) {
        new TelaPrincipal();
    }
}
