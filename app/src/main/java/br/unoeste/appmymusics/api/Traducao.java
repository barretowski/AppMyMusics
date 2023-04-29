package br.unoeste.appmymusics.api;

public class Traducao {
    public String id;
    public int lang;
    public String url;

    public Traducao(String id, int lang, String url, String text) {
        this.id = id;
        this.lang = lang;
        this.url = url;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLang() {
        return lang;
    }

    public void setLang(int lang) {
        this.lang = lang;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String text;
}
