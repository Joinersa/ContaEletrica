package br.com.joinersa.contaeletrica.entities;

/**
 * Created by Joiner on 16/07/2016.
 */
public class ItemComCustoMensal extends Item {

    private float custoMensal;

    public ItemComCustoMensal() {
    }

    public ItemComCustoMensal(int idImagem, String nome, float potencia, float tempo, String periodo, int quantidade, float consumoMes, float custoMensal) {
        super(idImagem, nome, potencia, tempo, periodo, quantidade, consumoMes);
        this.custoMensal = custoMensal;
    }

    public float getCustoMensal() {
        return custoMensal;
    }

    public void setCustoMensal(float custoMensal) {
        this.custoMensal = custoMensal;
    }
}
