package com.poo.loja;

public class Cliente extends Entidade {

    private String nome;

    public Cliente(int id, String nome) {
        super(id);
        if (id <= 0 || nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Dados inválidos para cliente.");
        }
        this.id = id;
        this.nome = nome;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }

    @Override
    public String toString() {
        return "Cliente [ID=" + id + ", Nome=\"" + nome + "\"]";
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
    public String toFileString() {
        return id + ";" + nome;
    }

    public static Cliente fromFileString(String linha) {
        String[] partes = linha.split(";");
        return new Cliente(
                Integer.parseInt(partes[0]),
                partes[1]
        );
    }
}