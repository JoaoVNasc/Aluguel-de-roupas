package com.poo.loja;

import javax.swing.*;
import java.awt.*;

public class TelaClientes extends JFrame {

    private final Loja loja = new Loja();

    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtTelefone;
    private JTextField txtEndereco;

    private JTextArea areaResultado;

    public TelaClientes() {

        setTitle("Lumière Fashion - Clientes");
        setSize(900,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel principal = new JPanel(new BorderLayout(15,15));
        principal.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        principal.setBackground(new Color(245,245,245));

        JLabel titulo = new JLabel("GERENCIAMENTO DE CLIENTES");
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(new Color(70,70,70));

        principal.add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(20,20));
        centro.setOpaque(false);

        JPanel cadastro = new JPanel(new GridBagLayout());
        cadastro.setBackground(Color.WHITE);
        cadastro.setBorder(BorderFactory.createTitledBorder("Cadastrar / Atualizar Cliente"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.anchor = GridBagConstraints.WEST;

        txtId = new JTextField(10);
        txtNome = new JTextField(25);
        txtTelefone = new JTextField(20);
        txtEndereco = new JTextField(30);

        JButton btnBuscar = new JButton("Buscar");
        JButton btnCadastrar = new JButton("Cadastrar");
        JButton btnAtualizar = new JButton("Salvar Alterações");
        JButton btnLimpar = new JButton("Limpar");

        btnBuscar.setBackground(new Color(90,110,140));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);

        btnCadastrar.setBackground(new Color(180,140,60));
        btnCadastrar.setForeground(Color.WHITE);
        btnCadastrar.setFocusPainted(false);

        btnAtualizar.setBackground(new Color(90,110,140));
        btnAtualizar.setForeground(Color.WHITE);
        btnAtualizar.setFocusPainted(false);

        btnLimpar.setBackground(new Color(120,120,120));
        btnLimpar.setForeground(Color.WHITE);
        btnLimpar.setFocusPainted(false);

        gbc.gridx=0;
        gbc.gridy=0;
        cadastro.add(new JLabel("ID:"),gbc);

        gbc.gridx=1;
        cadastro.add(txtId,gbc);

        gbc.gridx=2;
        cadastro.add(btnBuscar,gbc);

        gbc.gridx=0;
        gbc.gridy=1;
        cadastro.add(new JLabel("Nome:"),gbc);

        gbc.gridx=1;
        cadastro.add(txtNome,gbc);

        gbc.gridx=0;
        gbc.gridy=2;
        cadastro.add(new JLabel("Telefone:"),gbc);

        gbc.gridx=1;
        cadastro.add(txtTelefone,gbc);

        gbc.gridx=0;
        gbc.gridy=3;
        cadastro.add(new JLabel("Endereço:"),gbc);

        gbc.gridx=1;
        cadastro.add(txtEndereco,gbc);

        gbc.gridx=1;
        gbc.gridy=4;
        cadastro.add(btnCadastrar,gbc);

        gbc.gridx=2;
        cadastro.add(btnAtualizar,gbc);

        gbc.gridx=1;
        gbc.gridy=5;
        cadastro.add(btnLimpar,gbc);

        JPanel lista = new JPanel(new BorderLayout());
        lista.setBackground(Color.WHITE);
        lista.setBorder(BorderFactory.createTitledBorder("Clientes"));

        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font("Monospaced",Font.PLAIN,14));

        JScrollPane scroll = new JScrollPane(areaResultado);

        JButton btnListar = new JButton("Listar Todos");

        btnListar.setBackground(new Color(180,140,60));
        btnListar.setForeground(Color.WHITE);
        btnListar.setFocusPainted(false);

        lista.add(scroll,BorderLayout.CENTER);
        lista.add(btnListar,BorderLayout.SOUTH);

        centro.add(cadastro,BorderLayout.NORTH);
        centro.add(lista,BorderLayout.CENTER);

        principal.add(centro,BorderLayout.CENTER);

        add(principal);

        // EVENTOS

        btnCadastrar.addActionListener(e -> {

            try {

                Cliente cliente = new Cliente(

                        Integer.parseInt(txtId.getText()),
                        txtNome.getText(),
                        txtTelefone.getText(),
                        txtEndereco.getText()

                );

                JOptionPane.showMessageDialog(
                        this,
                        loja.cadastrarCliente(cliente)
                );

                limparCampos();
                areaResultado.setText(loja.listarClientes());

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Preencha todos os campos corretamente!"
                );

            }

        });

        btnBuscar.addActionListener(e -> {

            try {

                int id = Integer.parseInt(txtId.getText());
                Cliente cliente = loja.buscarClientePorId(id);

                if (cliente == null) {
                    JOptionPane.showMessageDialog(this, "Cliente não encontrado.");
                    return;
                }

                txtNome.setText(cliente.getNome());
                txtTelefone.setText(cliente.getTelefone());
                txtEndereco.setText(cliente.getEndereco());

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this, "Informe um ID válido.");

            }

        });

        btnAtualizar.addActionListener(e -> {

            try {

                int id = Integer.parseInt(txtId.getText());

                String resposta = loja.atualizarCliente(
                        id,
                        txtNome.getText(),
                        txtTelefone.getText(),
                        txtEndereco.getText()
                );

                JOptionPane.showMessageDialog(this, resposta);

                limparCampos();
                areaResultado.setText(loja.listarClientes());

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this, "Informe um ID válido.");

            }

        });

        btnListar.addActionListener(e -> {

            areaResultado.setText(
                    loja.listarClientes()
            );

        });

        btnLimpar.addActionListener(e -> {

            limparCampos();
            areaResultado.setText("");

        });

        setVisible(true);

    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtTelefone.setText("");
        txtEndereco.setText("");
    }

}
