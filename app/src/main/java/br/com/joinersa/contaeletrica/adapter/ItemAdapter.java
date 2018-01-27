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

import br.com.joinersa.contaeletrica.entities.Item;
import br.com.joinersa.contaeletrica.R;
import br.com.joinersa.contaeletrica.dao.ItemDAO;
import br.com.joinersa.contaeletrica.interfaces.RecyclerViewOnClickListenerHack;

/**
 * Created by Joiner on 16/07/2016.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
    private List<Item> meusItensList;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;


    public ItemAdapter(List<Item> meusItensList) {
        this.meusItensList = meusItensList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.meu_item_eletro, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Item item = meusItensList.get(position);

        // mudando o formato do númeoro de ponto para vírgula e arredondando.
        // O formato da frança é 1 023,99, separa com espaço o milhar e com vírgula as casas decimais.
        NumberFormat numFormatado = NumberFormat.getNumberInstance(Locale.FRANCE);

        holder.imagem.setBackgroundResource(item.getIdImagem()); // imagem de fundo.
        holder.nome.setText(item.getNome());
        holder.potencia.setText("" + numFormatado.format(item.getPotencia())); // passando o número formatado..
        holder.tempo.setText("" + numFormatado.format(item.getTempo())); // passando o número formatado..
        holder.periodo.setText(item.getPeriodo()); // passando o número formatado..
        holder.quantidade.setText("" + item.getQuantidade());
        holder.consumoMes.setText("" + numFormatado.format(item.getConsumoMes())); // passando o número formatado..
        // mudar cor do item selecionado
        holder.itemView.setBackgroundResource(meusItensList.get(position).selected ? R.color.corItemSelecionado : R.color.corComponenteSelecionavel);
        // setar imagem que mostra que o item está marcado
        holder.imagemChecked.setImageResource(meusItensList.get(position).selected ? R.drawable.ic_check_circle_outline_white_24dp : R.drawable.ic_check_false_24dp);

        // aqui tinha efeito no recycler...

    }

    @Override
    public int getItemCount() {
        return meusItensList.size();
    }


    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r) {
        mRecyclerViewOnClickListenerHack = r;
    }

    // remove um item da lista:
    public void removeListItem(Context context, int position) {
        // remove um item do banco de dados:
        new ItemDAO(context).deletar(meusItensList.get(position));
        // remove do recyclerView. (poderia ser dando um removeall depois um select no banco pra mostrar tudo de novo, mas...deixei assim..)
        meusItensList.remove(position);
        notifyItemRemoved(position);
    }

    // remove todos itens da tabela "itens" do banco de dados e limpa o recyclerView:
    public void removeAllItens(Context context) {
        new ItemDAO(context).deleteAll();
        limpaRecycler();
    }

    // apenas remove todos os elementos da recyclerView
    public void limpaRecycler() {
        meusItensList.clear();
        notifyDataSetChanged();
    }

    // atributos do objeto eletrodoméstico farão referência aos componentes do xml:
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imagem;
        private TextView nome;
        private TextView potencia;
        private TextView tempo;
        private TextView periodo;
        private TextView quantidade;
        private TextView consumoMes;
        private ImageView imagemChecked;

        // referenciando atributos:
        public MyViewHolder(View view) {
            super(view);

            imagem = (ImageView) view.findViewById(R.id.iv_imagem_meu_eletro);
            nome = (TextView) view.findViewById(R.id.tv_nome_meu_eletro);
            potencia = (TextView) view.findViewById(R.id.tv_potencia_meu_eletro);
            tempo = (TextView) view.findViewById(R.id.tv_tempo_meu_eletro);
            periodo = (TextView) view.findViewById(R.id.tv_periodo_meu_eletro);
            quantidade = (TextView) view.findViewById(R.id.tv_quantidade_meu_eletro);
            consumoMes = (TextView) view.findViewById(R.id.tv_consumo_mes_meu_eletro);
            imagemChecked = (ImageView) view.findViewById(R.id.iv_checked);

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
