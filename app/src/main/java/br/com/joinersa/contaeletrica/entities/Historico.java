package br.com.joinersa.contaeletrica.entities;

/**
 * Created by joiner on 12/08/16.
 */
public class Historico {

    private int mes;
    private int ano;
    private float consumoMes;
    private float custoMes;

    public Historico() {
    }

    public Historico(int mes, int ano, float consumoMes) {
        this.mes = mes;
        this.ano = ano;
        this.consumoMes = consumoMes;
    }

    public Historico(int mes, int ano, float consumoMes, float custoMes) {
        this.mes = mes;
        this.ano = ano;
        this.consumoMes = consumoMes;
        this.custoMes = custoMes;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public float getConsumoMes() {
        return consumoMes;
    }

    public void setConsumoMes(float consumoMes) {
        this.consumoMes = consumoMes;
    }

    public float getCustoMes() {
        return custoMes;
    }

    public void setCustoMes(float custoMes) {
        this.custoMes = custoMes;
    }
}
