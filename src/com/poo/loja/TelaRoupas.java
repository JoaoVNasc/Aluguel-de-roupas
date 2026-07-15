package com.poo.loja;

import javax.swing.*;
import java.awt.*;

public class TelaRoupas extends JFrame {

    private final Loja loja = new Loja();

    private JTextField txtId;
    private JTextField txtDescricao;
    private JTextField txtCategoria;
    private JTextField txtQuantidade;
    private JTextField txtValorDiaria;

    private JTextField txtBuscarCategoria;

    private JTextField txtAjusteId;
    private JTextField txtAjusteQtd;
    private JTextField txtAjusteMotivo;

    private JTextArea areaResultado;

    public TelaRoupas() {

        setTitle("Lumière Fashion - Roupas");
        setSize(900,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel principal = new JPanel(new BorderLayout(15,15));
        principal.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        principal.setBackground(new Color(245,245,245));

        //-----------------------------------------
        // TÍTULO
        //-----------------------------------------

        JLabel titulo = new JLabel("GERENCIAMENTO DE ROUPAS");

        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        titulo.setFont(new Font("Arial",Font.BOLD,28));

        titulo.setForeground(new Color(70,70,70));

        principal.add(titulo,BorderLayout.NORTH);

        //-----------------------------------------
        // PAINEL CENTRAL
        //-----------------------------------------

        JPanel centro = new JPanel(new BorderLayout(20,20));
        centro.setOpaque(false);

        //-----------------------------------------
        // CADASTRO
        //-----------------------------------------

        JPanel cadastro = new JPanel(new GridBagLayout());

        cadastro.setBackground(Color.WHITE);

        cadastro.setBorder(
                BorderFactory.createTitledBorder("Cadastrar Roupa")
        );

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8,8,8,8);

        gbc.anchor = GridBagConstraints.WEST;

        txtId = new JTextField(10);
        txtDescricao = new JTextField(25);
        txtCategoria = new JTextField(20);
        txtQuantidade = new JTextField(10);
        txtValorDiaria = new JTextField(10);

        JButton btnCadastrar = new JButton("Cadastrar");
        JButton btnLimpar = new JButton("Limpar");
        JButton btnBuscarId = new JButton("Buscar ID");
        JButton btnEditar = new JButton("Salvar Edição");
        JButton btnRemover = new JButton("Remover");

        btnLimpar.setBackground(new Color(120,120,120));
        btnLimpar.setForeground(Color.WHITE);
        btnLimpar.setFocusPainted(false);

        btnCadastrar.setBackground(new Color(180,140,60));
        btnCadastrar.setForeground(Color.WHITE);
        btnCadastrar.setFocusPainted(false);

        btnBuscarId.setBackground(new Color(90,110,140));
        btnBuscarId.setForeground(Color.WHITE);
        btnBuscarId.setFocusPainted(false);

        btnEditar.setBackground(new Color(90,110,140));
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFocusPainted(false);

        btnRemover.setBackground(new Color(170,60,60));
        btnRemover.setForeground(Color.WHITE);
        btnRemover.setFocusPainted(false);

        gbc.gridx=0;
        gbc.gridy=0;
        cadastro.add(new JLabel("ID:"),gbc);

        gbc.gridx=1;
        cadastro.add(txtId,gbc);

        gbc.gridx=2;
        cadastro.add(btnBuscarId,gbc);

        gbc.gridx=0;
        gbc.gridy=1;
        cadastro.add(new JLabel("Descrição:"),gbc);

        gbc.gridx=1;
        cadastro.add(txtDescricao,gbc);

        gbc.gridx=0;
        gbc.gridy=2;
        cadastro.add(new JLabel("Categoria:"),gbc);

        gbc.gridx=1;
        cadastro.add(txtCategoria,gbc);

        gbc.gridx=0;
        gbc.gridy=3;
        cadastro.add(new JLabel("Quantidade:"),gbc);

        gbc.gridx=1;
        cadastro.add(txtQuantidade,gbc);

        gbc.gridx=0;
        gbc.gridy=4;
        cadastro.add(new JLabel("Valor Diária (R$):"),gbc);

        gbc.gridx=1;
        cadastro.add(txtValorDiaria,gbc);

        gbc.gridx=1;
        gbc.gridy=5;
        cadastro.add(btnCadastrar,gbc);
        gbc.gridx = 2;

        cadastro.add(btnLimpar, gbc);

        gbc.gridx=1;
        gbc.gridy=6;
        cadastro.add(btnEditar,gbc);
        gbc.gridx = 2;

        cadastro.add(btnRemover, gbc);

        //-----------------------------------------
        // AJUSTAR ESTOQUE (ENTRADA/SAÍDA MANUAL)
        //-----------------------------------------

        JPanel ajuste = new JPanel(new FlowLayout(FlowLayout.LEFT));

        ajuste.setBackground(Color.WHITE);

        ajuste.setBorder(
                BorderFactory.createTitledBorder("Ajustar Estoque (Entrada/Saída Manual)")
        );

        txtAjusteId = new JTextField(6);
        txtAjusteQtd = new JTextField(6);
        txtAjusteMotivo = new JTextField(20);

        JButton btnAjustar = new JButton("Aplicar Ajuste");

        btnAjustar.setBackground(new Color(90,110,140));
        btnAjustar.setForeground(Color.WHITE);
        btnAjustar.setFocusPainted(false);

        ajuste.add(new JLabel("ID:"));
        ajuste.add(txtAjusteId);
        ajuste.add(new JLabel("Nova Qtd:"));
        ajuste.add(txtAjusteQtd);
        ajuste.add(new JLabel("Motivo:"));
        ajuste.add(txtAjusteMotivo);
        ajuste.add(btnAjustar);

        //-----------------------------------------
        // BUSCAR
        //-----------------------------------------

        JPanel busca = new JPanel(new FlowLayout(FlowLayout.LEFT));

        busca.setBackground(Color.WHITE);

        busca.setBorder(
                BorderFactory.createTitledBorder("Buscar por Categoria")
        );

        txtBuscarCategoria = new JTextField(20);

        JButton btnBuscar = new JButton("Buscar");

        btnBuscar.setBackground(new Color(180,140,60));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);

        busca.add(new JLabel("Categoria:"));

        busca.add(txtBuscarCategoria);

        busca.add(btnBuscar);

        //-----------------------------------------
        // RESULTADO
        //-----------------------------------------

        JPanel lista = new JPanel(new BorderLayout());

        lista.setBackground(Color.WHITE);

        lista.setBorder(
                BorderFactory.createTitledBorder("Roupas")
        );

        areaResultado = new JTextArea();

        areaResultado.setEditable(false);

        areaResultado.setFont(new Font("Monospaced",Font.PLAIN,14));

        JScrollPane scroll = new JScrollPane(areaResultado);

        JButton btnListar = new JButton("Listar Todas");

        btnListar.setBackground(new Color(180,140,60));
        btnListar.setForeground(Color.WHITE);
        btnListar.setFocusPainted(false);

        lista.add(scroll,BorderLayout.CENTER);

        lista.add(btnListar,BorderLayout.SOUTH);

        //-----------------------------------------
        // ORGANIZAÇÃO
        //-----------------------------------------

        JPanel superior = new JPanel(new BorderLayout(15,15));

        superior.setOpaque(false);

        superior.add(cadastro,BorderLayout.CENTER);

        JPanel rodapeSuperior = new JPanel();
        rodapeSuperior.setLayout(new BoxLayout(rodapeSuperior, BoxLayout.Y_AXIS));
        rodapeSuperior.setOpaque(false);
        rodapeSuperior.add(ajuste);
        rodapeSuperior.add(busca);

        superior.add(rodapeSuperior,BorderLayout.SOUTH);

        centro.add(superior,BorderLayout.NORTH);

        centro.add(lista,BorderLayout.CENTER);

        principal.add(centro,BorderLayout.CENTER);

        add(principal);
// =======================================
// EVENTO CADASTRAR
// =======================================

        btnCadastrar.addActionListener(e -> {

            try {

                double valorDiaria = txtValorDiaria.getText().trim().isEmpty()
                        ? 0.0
                        : Double.parseDouble(txtValorDiaria.getText().trim().replace(",", "."));

                Roupa roupa = new Roupa(
                        Integer.parseInt(txtId.getText()),
                        txtDescricao.getText(),
                        txtCategoria.getText(),
                        Integer.parseInt(txtQuantidade.getText()),
                        valorDiaria
                );

                JOptionPane.showMessageDialog(
                        this,
                        loja.cadastrarRoupa(roupa)
                );

                limparCampos();
                areaResultado.setText(loja.listarRoupa());

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Preencha todos os campos corretamente!"
                );

            }

        });


// =======================================
// EVENTO BUSCAR POR ID (carregar para edição)
// =======================================

        btnBuscarId.addActionListener(e -> {

            try {

                int id = Integer.parseInt(txtId.getText());
                Roupa roupa = loja.buscarRoupaPorId(id);

                if (roupa == null) {
                    JOptionPane.showMessageDialog(this, "Roupa não encontrada.");
                    return;
                }

                txtDescricao.setText(roupa.getDescricao());
                txtCategoria.setText(roupa.getCategoria());
                txtQuantidade.setText(String.valueOf(roupa.getQuantidade()));
                txtValorDiaria.setText(String.valueOf(roupa.getValorDiaria()));

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this, "Informe um ID válido.");

            }

        });


// =======================================
// EVENTO SALVAR EDIÇÃO
// =======================================

        btnEditar.addActionListener(e -> {

            try {

                int id = Integer.parseInt(txtId.getText());

                double valorDiaria = txtValorDiaria.getText().trim().isEmpty()
                        ? 0.0
                        : Double.parseDouble(txtValorDiaria.getText().trim().replace(",", "."));

                String resposta = loja.atualizarRoupa(
                        id,
                        txtDescricao.getText(),
                        txtCategoria.getText(),
                        valorDiaria
                );

                JOptionPane.showMessageDialog(this, resposta);
                areaResultado.setText(loja.listarRoupa());

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this, "Informe dados válidos (ID e Valor Diária).");

            }

        });


// =======================================
// EVENTO REMOVER
// =======================================

        btnRemover.addActionListener(e -> {

            try {

                int id = Integer.parseInt(txtId.getText());

                int confirmacao = JOptionPane.showConfirmDialog(
                        this,
                        "Tem certeza que deseja remover a roupa ID " + id + "?",
                        "Confirmar remoção",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirmacao != JOptionPane.YES_OPTION) return;

                String resposta = loja.removerRoupa(id);

                JOptionPane.showMessageDialog(this, resposta);
                areaResultado.setText(loja.listarRoupa());

                limparCampos();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this, "Informe um ID válido.");

            }

        });


// =======================================
// EVENTO AJUSTAR ESTOQUE (ENTRADA/SAÍDA MANUAL)
// =======================================

        btnAjustar.addActionListener(e -> {

            try {

                int id = Integer.parseInt(txtAjusteId.getText());
                int novaQtd = Integer.parseInt(txtAjusteQtd.getText());

                String resposta = loja.ajustarEstoque(
                        id,
                        novaQtd,
                        txtAjusteMotivo.getText()
                );

                JOptionPane.showMessageDialog(this, resposta);
                areaResultado.setText(loja.listarRoupa());

                txtAjusteId.setText("");
                txtAjusteQtd.setText("");
                txtAjusteMotivo.setText("");

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this, "Informe ID e quantidade válidos.");

            }

        });


// =======================================
// EVENTO BUSCAR
// =======================================

        btnBuscar.addActionListener(e -> {

            areaResultado.setText(
                    loja.buscarPorCategoria(
                            txtBuscarCategoria.getText()
                    )
            );

        });


// =======================================
// EVENTO LISTAR
// =======================================

        btnListar.addActionListener(e -> {

            areaResultado.setText(
                    loja.listarRoupa()
            );

        });

        btnLimpar.addActionListener(e -> {

            limparCampos();
            txtBuscarCategoria.setText("");
            txtAjusteId.setText("");
            txtAjusteQtd.setText("");
            txtAjusteMotivo.setText("");
            areaResultado.setText("");

        });

        setVisible(true);
    }

    private void limparCampos() {
        txtId.setText("");
        txtDescricao.setText("");
        txtCategoria.setText("");
        txtQuantidade.setText("");
        txtValorDiaria.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TelaRoupas::new);
    }
}
