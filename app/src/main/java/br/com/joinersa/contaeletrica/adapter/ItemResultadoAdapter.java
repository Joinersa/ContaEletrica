package br.com.joinersa.contaeletrica.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import br.com.joinersa.contaeletrica.entities.ItemComCustoMensal;
import br.com.joinersa.contaeletrica.R;
import br.com.joinersa.contaeletrica.dao.ItemDAO;
import br.com.joinersa.contaeletrica.interfaces.RecyclerViewOnClickListenerHack;

/**
 * Created by Joiner on 16/07/2016.
 */
public class ItemResultadoAdapter extends RecyclerView.Adapter<ItemResultadoAdapter.MyViewHolder> {

    private List<ItemComCustoMensal> itensResultadoList;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;


    public ItemResultadoAdapter(List<ItemComCustoMensal> itensResultadoList) {
        this.itensResultadoList = itensResultadoList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itens_resultados, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ItemComCustoMensal item = itensResultadoList.get(position);

        // mudando o formato do númeoro de ponto para vírgula e arredondando.
        // O formato da frança é 1 023,99, separa com espaço o milhar e com vírgula as casas decimais.
        NumberFormat numFormatado = NumberFormat.getNumberInstance(Locale.FRANCE);

        //holder.imagem.setBackgroundResource(item.getIdImagem()); // imagem de fundo.
        holder.imagem.setImageResource(item.getIdImagem());
        holder.nome.setText(item.getNome());
        holder.potencia.setText(numFormatado.format(item.getPotencia()) + " W"); // passando o número formatado..
        holder.tempo.setText("" + numFormatado.format(item.getTempo())); // passando o número formatado..
        holder.periodo.setText(item.getPeriodo()); // passando o número formatado..
        holder.quantidade.setText("" + item.getQuantidade());
        holder.consumoMes.setText(numFormatado.format(item.getConsumoMes()) + " kWh"); // passando o número formatado..
        numFormatado = NumberFormat.getCurrencyInstance();
        holder.custoMensal.setText("R$ " + numFormatado.format(item.getCustoMensal()).replace("R$", "")); // (novo) --- custo mensal de cada item

        //holder.custoMensal.setText();

        // aqui tinha efeito no recycler...

    }

    @Override
    public int getItemCount() {
        return itensResultadoList.size();
    }


    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r) {
        mRecyclerViewOnClickListenerHack = r;
    }

    // remove um item da lista:
    public void removeListItem(Context context, int position) {
        // remove um item do banco de dados:
        new ItemDAO(context).deletar(itensResultadoList.get(position));
        // remove do recyclerView. (poderia ser dando um removeall depois um select no banco pra mostrar tudo de novo, mas...deixei assim..)
        itensResultadoList.remove(position);
        notifyItemRemoved(position);
    }

    // remove todos itens da tabela "itens" do banco de dados e limpa o recyclerView:
    public void removeAllItens(Context context) {
        new ItemDAO(context).deleteAll();
        limpaRecycler();
    }

    // apenas remove todos os elementos da recyclerView
    public void limpaRecycler() {
        itensResultadoList.clear();
        notifyDataSetChanged();
    }

    // atributos do objeto eletrodoméstico farão referências aos componentes do xml:
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imagem;
        private TextView nome;
        private TextView potencia;
        private TextView tempo;
        private TextView periodo;
        private TextView quantidade;
        private TextView consumoMes;

        private TextView custoMensal;

        // referenciando atributos:
        public MyViewHolder(View view) {
            super(view);

            imagem = (ImageView) view.findViewById(R.id.iv_imagem_item_resultado);
            nome = (TextView) view.findViewById(R.id.tv_nome_item_resultado);
            potencia = (TextView) view.findViewById(R.id.tv_potencia_item_resultado);
            tempo = (TextView) view.findViewById(R.id.tv_tempo_item_resultado);
            periodo = (TextView) view.findViewById(R.id.tv_periodo_item_resultado);
            quantidade = (TextView) view.findViewById(R.id.tv_quantidade_item_resultado);
            consumoMes = (TextView) view.findViewById(R.id.tv_consumo_mes_item_resultado);

            custoMensal = (TextView) view.findViewById(R.id.tv_custo_mensal_item_resultado);

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(mRecyclerViewOnClickListenerHack != null){
                mRecyclerViewOnClickListenerHack.onClickListener(v, getAdapterPosition());
            }
        }
    }

}
