package br.com.joinersa.contaeletrica.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.joinersa.contaeletrica.entities.Aparelho;
import br.com.joinersa.contaeletrica.R;
import br.com.joinersa.contaeletrica.interfaces.RecyclerViewOnClickListenerHack;

/**
 * Created by Joiner on 16/07/2016.
 */
public class AparelhoAdapter extends RecyclerView.Adapter<AparelhoAdapter.MyViewHolder> {
    private List<Aparelho> listaAparelhos;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    public AparelhoAdapter(List<Aparelho> listaEletros) {
        this.listaAparelhos = listaEletros;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_eletro, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Aparelho aparelho = listaAparelhos.get(position);

        holder.nome.setText(aparelho.getNome());
        holder.imagem.setImageResource(aparelho.getIdImagem());
        //holder.imagem.setBackgroundResource(eletro.getIdImagem());

        /*
        holder.potencia.setText("" + eletro.getPotencia());
        holder.tempo.setText("" + eletro.getTempo());
        holder.periodo.setText(eletro.getPeriodo());
        holder.consumoMes.setText("" + eletro.getConsumoMes());
        */
        // efeito no RecyclerView:
        /*
        try {
            YoYo.with(Techniques.Bounce).
                    duration(700).
                    playOn(holder.itemView);

        } catch (Exception e) {

        }
        */
    }

    @Override
    public int getItemCount() {
        return listaAparelhos.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r) {
        mRecyclerViewOnClickListenerHack = r;
    }

    // ************* remove item, aqui não é necessário *************
    public void removeListItem(int position) {
        listaAparelhos.remove(position);
        notifyItemRemoved(position);
    }

    public String getNomeEletro(int position) {
        return listaAparelhos.get(position).getNome();
    }

    // Retorna o objeto Eletrodoméstico pressionado. by Joiner:
    public Aparelho getEletrodomestico(int position) {
        return listaAparelhos.get(position);
    }

    // atributos do objeto eletrodoméstico farão referencia aos componentes do xml:
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // atributo nome, pois só tem o nome e uma imagem do eletrodoméstico:
        private TextView nome;
        private ImageView imagem;

        // referenciando atributos:
        public MyViewHolder(View view) {
            super(view);

            nome = (TextView) view.findViewById(R.id.tv_nome_eletro);
            imagem = (ImageView) view.findViewById(R.id.iv_imagem_eletro);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mRecyclerViewOnClickListenerHack != null) {
                mRecyclerViewOnClickListenerHack.onClickListener(v, getAdapterPosition());
            }
        }
    }
}