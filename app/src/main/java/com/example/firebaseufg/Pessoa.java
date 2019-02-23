package com.example.firebaseufg;

public class Pessoa {
    private String uid;
    private String nome;
    private String email;
    private String apelido;
    private String senha;

    public Pessoa() {
    }

    public Pessoa(String uid) {
        this.uid = uid;
    }

    public Pessoa(String nome, String email, String apelido, String senha) {
        this.nome = nome;
        this.email = email;
        this.apelido = apelido;
        this.senha = senha;
    }

    public Pessoa(String uid, String nome, String email, String apelido, String senha) {
        this.uid = uid;
        this.nome = nome;
        this.email = email;
        this.apelido = apelido;
        this.senha = senha;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return nome;
    }
}
