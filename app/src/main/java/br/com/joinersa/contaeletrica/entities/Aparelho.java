package br.com.joinersa.contaeletrica.entities;

/**
 * Created by Joiner on 16/07/2016.
 */
public class Aparelho {
    private int idImagem;
    private String nome;
    private float potencia;
    private float tempo;
    private String periodo;

    public Aparelho() {}

    public Aparelho(String nome, float potencia, float tempo, String periodo) {
        this.nome = nome;
        this.potencia = potencia;
        this.tempo = tempo;
        this.periodo = periodo;
    }

    public Aparelho(int idImagem, String nome, float potencia, float tempo, String periodo) {
        this.idImagem = idImagem;
        this.nome = nome;
        this.potencia = potencia;
        this.tempo = tempo;
        this.periodo = periodo;
    }

    public int getIdImagem() {
        return idImagem;
    }

    public void setIdImagem(int idImagem) {
        this.idImagem = idImagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getPotencia() {
        return potencia;
    }

    public void setPotencia(float potencia) {
        this.potencia = potencia;
    }

    public float getTempo() {
        return tempo;
    }

    public void setTempo(float tempo) {
        this.tempo = tempo;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

}
