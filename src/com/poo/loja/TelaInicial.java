package com.poo.loja;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class TelaInicial extends JFrame {


    private JLayeredPane camada;


    public TelaInicial() {


        setTitle("Lumière Fashion");


        setUndecorated(true);


        setExtendedState(JFrame.MAXIMIZED_BOTH);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        camada = new JLayeredPane();


        add(camada);





        // ==========================
        // IMAGEM DE FUNDO
        // ==========================


        ImageIcon imagemOriginal =
                new ImageIcon(
                        TelaInicial.class
                                .getResource(
                                        "/imagens/tela_inicial.png"
                                )
                );



        JLabel imagemLabel =
                new JLabel();



        camada.add(
                imagemLabel,
                Integer.valueOf(0)
        );







        // ==========================
        // BOTÃO ENTRAR
        // ==========================


        JButton entrar =
                new JButton("ENTRAR");



        entrar.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );



        entrar.setForeground(
                Color.WHITE
        );



        entrar.setBackground(
                new Color(
                        180,
                        140,
                        60
                )
        );



        entrar.setFocusPainted(false);



        entrar.setBorder(
                BorderFactory
                        .createEmptyBorder(
                                15,
                                50,
                                15,
                                50
                        )
        );






        // Efeito mouse botão

        entrar.addMouseListener(
                new java.awt.event.MouseAdapter(){


                    public void mouseEntered(
                            java.awt.event.MouseEvent e
                    ){

                        entrar.setBackground(
                                new Color(
                                        220,
                                        170,
                                        80
                                )
                        );

                    }



                    public void mouseExited(
                            java.awt.event.MouseEvent e
                    ){

                        entrar.setBackground(
                                new Color(
                                        180,
                                        140,
                                        60
                                )
                        );

                    }


                }
        );








        // ==========================
        // TRANSIÇÃO CORRIGIDA
        // ==========================


        entrar.addActionListener(e -> {



            entrar.setEnabled(false);




            JPanel escurecimento =
                    new JPanel();



            escurecimento.setBackground(
                    new Color(
                            0,
                            0,
                            0,
                            0
                    )
            );



            escurecimento.setOpaque(true);



            camada.add(
                    escurecimento,
                    Integer.valueOf(2)
            );



            escurecimento.setBounds(
                    0,
                    0,
                    getWidth(),
                    getHeight()
            );






            Timer transicao =
                    new Timer(
                            30,
                            null
                    );



            final int[] alpha =
                    {0};






            transicao.addActionListener(event -> {



                alpha[0] += 15;



                escurecimento.setBackground(
                        new Color(
                                0,
                                0,
                                0,
                                Math.min(alpha[0],255)
                        )
                );



                escurecimento.repaint();






                if(alpha[0] >= 255){



                    transicao.stop();





                    // ==============================
                    // CORREÇÃO PRINCIPAL
                    // ==============================


                    TelaPrincipal principal =
                            new TelaPrincipal();






                    // espera a tela nova aparecer
                    Timer fechar =
                            new Timer(
                                    200,
                                    null
                            );



                    fechar.addActionListener(ev -> {



                        fechar.stop();


                        dispose();



                    });



                    fechar.start();



                }



            });




            transicao.start();



        });








        camada.add(
                entrar,
                Integer.valueOf(1)
        );










        // Ajusta tamanho


        camada.addComponentListener(
                new java.awt.event.ComponentAdapter(){


                    public void componentResized(
                            java.awt.event.ComponentEvent e
                    ){


                        atualizarTela(
                                camada,
                                imagemOriginal,
                                imagemLabel,
                                entrar
                        );


                    }


                }
        );








        // ESC fecha


        addKeyListener(
                new KeyAdapter(){


                    public void keyPressed(
                            KeyEvent e
                    ){


                        if(e.getKeyCode()
                                == KeyEvent.VK_ESCAPE){


                            System.exit(0);


                        }


                    }


                }
        );




        setFocusable(true);



        setVisible(true);






        atualizarTela(
                camada,
                imagemOriginal,
                imagemLabel,
                entrar
        );


    }









    private void atualizarTela(
            JLayeredPane camada,
            ImageIcon imagemOriginal,
            JLabel imagemLabel,
            JButton entrar
    ){



        int largura =
                camada.getWidth();



        int altura =
                camada.getHeight();




        if(largura <= 0 ||
                altura <= 0){

            return;

        }






        Image imagem =
                imagemOriginal
                        .getImage()
                        .getScaledInstance(
                                largura,
                                altura,
                                Image.SCALE_SMOOTH
                        );




        imagemLabel.setIcon(
                new ImageIcon(imagem)
        );




        imagemLabel.setBounds(
                0,
                0,
                largura,
                altura
        );







        entrar.setBounds(
                (largura / 2) - 100,
                altura - 150,
                200,
                60
        );


    }









    public static void main(String[] args){


        new TelaInicial();


    }


}