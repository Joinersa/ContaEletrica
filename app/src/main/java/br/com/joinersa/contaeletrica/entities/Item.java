package br.com.joinersa.contaeletrica.entities;

/**
 * Created by Joiner on 16/07/2016.
 */
public class Item extends Aparelho {
    public boolean selected; // flag para indicar se o item está selecionado
    private long id;
    private int quantidade;
    private float consumoMes;

    public Item() {
    }

    public Item(int idImagem, String nome, float potencia, float tempo, String periodo, int quantidade, float consumoMes) {
        super(idImagem, nome, potencia, tempo, periodo);
        this.quantidade = quantidade;
        this.consumoMes = consumoMes;
    }

    public Item(long id, String nome, float potencia, float tempo, String periodo, int quantidade, float consumoMes) {
        super(nome, potencia, tempo, periodo);
        this.id = id;
        this.quantidade = quantidade;
        this.consumoMes = consumoMes;
    }

    public Item(long id, int idImagem, String nome, float potencia, float tempo, String periodo, int quantidade, float consumoMes) {
        super(idImagem, nome, potencia, tempo, periodo);
        this.id = id;
        this.quantidade = quantidade;
        this.consumoMes = consumoMes;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public float getConsumoMes() {
        return consumoMes;
    }

    public void setConsumoMes(float consumoMes) {
        this.consumoMes = consumoMes;
    }
}
