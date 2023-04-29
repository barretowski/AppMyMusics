package br.unoeste.appmymusics.api;

public class Busca {
    public String id;
    public String nome;
    public String url;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Busca(String id, String nome, String url) {
        this.id = id;
        this.nome = nome;
        this.url = url;
    }
}
