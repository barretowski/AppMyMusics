package br.unoeste.appmymusics.api;

import java.util.ArrayList;

public class Musica {
    public String id;
    public String name;
    public String url;
    public int lang;
    public String text;
    public ArrayList<Traducao> translate;

    public Musica(String id, String name, String url, int lang, String text, ArrayList<Traducao> translate) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.lang = lang;
        this.text = text;
        this.translate = translate;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLang() {
        return lang;
    }

    public void setLang(int lang) {
        this.lang = lang;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<Traducao> getTranslate() {
        return translate;
    }

    public void setTranslate(ArrayList<Traducao> translate) {
        this.translate = translate;
    }


}
