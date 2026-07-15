package com.poo.loja;

public class Cliente {

    private int id;
    private String nome;
    private String telefone;
    private String endereco;

    public Cliente(int id,
                   String nome,
                   String telefone,
                   String endereco) {

        if (id <= 0 ||
                nome == null || nome.trim().isEmpty() ||
                telefone == null || telefone.trim().isEmpty() ||
                endereco == null || endereco.trim().isEmpty()) {

            throw new IllegalArgumentException("Dados inválidos para cliente.");
        }

        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getTelefone(){

        return telefone;

    }

    public String getEndereco(){

        return endereco;

    }

    // =========================
    // SETTERS (Tópico 3 - Atualização de Dados Cadastrais)
    // =========================

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome inválido.");
        }
        this.nome = nome;
    }

    public void setTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            throw new IllegalArgumentException("Telefone inválido.");
        }
        this.telefone = telefone;
    }

    public void setEndereco(String endereco) {
        if (endereco == null || endereco.trim().isEmpty()) {
            throw new IllegalArgumentException("Endereço inválido.");
        }
        this.endereco = endereco;
    }

    @Override
    public String toString(){

        return "Cliente [ID=" + id +
                ", Nome=\"" + nome +
                "\", Telefone=\"" + telefone +
                "\", Endereço=\"" + endereco + "\"]";

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente)) return false;
        Cliente c = (Cliente) o;
        return id == c.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    // Persistência
    public String toFileString(){

        return id + ";" +
                nome + ";" +
                telefone + ";" +
                endereco;

    }

    public static Cliente fromFileString(String linha) {
        String[] partes = linha.split(";", -1);

        int id = Integer.parseInt(partes[0].trim());
        String nome = (partes.length > 1 && !partes[1].trim().isEmpty()) ? partes[1] : "Não informado";
        String telefone = (partes.length > 2 && !partes[2].trim().isEmpty()) ? partes[2] : "Não informado";
        String endereco = (partes.length > 3 && !partes[3].trim().isEmpty()) ? partes[3] : "Não informado";

        return new Cliente(id, nome, telefone, endereco);
    }
}
