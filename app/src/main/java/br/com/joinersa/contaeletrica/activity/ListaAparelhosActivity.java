package br.com.joinersa.contaeletrica.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import br.com.joinersa.contaeletrica.entities.Aparelho;
import br.com.joinersa.contaeletrica.R;
import br.com.joinersa.contaeletrica.adapter.AparelhoAdapter;
import br.com.joinersa.contaeletrica.interfaces.RecyclerViewOnClickListenerHack;

public class ListaAparelhosActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack {

    private RecyclerView recyclerViewListaEletros;
    private List<Aparelho> listaEletro = new ArrayList<>();
    private AparelhoAdapter aparelhoAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_eletros);

        // Toolbar:
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar_lista_eletros);
        //tb.setTitle("Escolha um Aparelho");
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);// bt voltar

        // lista de eletrodomésticos:
        recyclerViewListaEletros = (RecyclerView) findViewById(R.id.rv_lista_eletros);
        // botão pressionado:
        recyclerViewListaEletros.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), recyclerViewListaEletros, this));
        // adapter:
        aparelhoAdapter = new AparelhoAdapter(listaEletro);
        // nao sei pra que serve:
        recyclerViewListaEletros.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewListaEletros.setLayoutManager(layoutManager);
        recyclerViewListaEletros.setItemAnimator(new DefaultItemAnimator());
        recyclerViewListaEletros.setAdapter(aparelhoAdapter);

        // preencher recyclerView com os aparelhos:
        preencherRecyclerView(); // inicializar com valores fixos.

    }

    private void preencherRecyclerView() {
        listaEletro.add(new Aparelho(R.drawable.ar, "Ar Condicionado 7000 BTUs", 900, 8, "Horas/Dia"));
        listaEletro.add(new Aparelho(R.drawable.ar, "Ar Condicionado 7500 BTUs", 1100, 8, "Horas/Dia"));
        listaEletro.add(new Aparelho(R.drawable.ar, "Ar Condicionado 10000 BTUs", 1400, 8, "Horas/Dia"));
        listaEletro.add(new Aparelho(R.drawable.ar, "Ar Condicionado 12000 BTUs", 1600, 8, "Horas/Dia"));
        listaEletro.add(new Aparelho(R.drawable.ar, "Ar Condicionado 15000 BTUs", 2000, 8, "Horas/Dia"));
        listaEletro.add(new Aparelho(R.drawable.ar, "Ar Condicionado 18000 BTUs", 2600, 8, "Horas/Dia"));
        listaEletro.add(new Aparelho(R.drawable.aspirador, "Aspirador de Pó", 1000, 4, "Horas/Semana"));
        listaEletro.add(new Aparelho(R.drawable.cafeteira, "Cafeteira", 600, 30, "Minutos/Dia"));
        listaEletro.add(new Aparelho(R.drawable.chuveiro, "Chuveiro Elétrico", 3500, 15, "Minutos/Dia"));
        listaEletro.add(new Aparelho(R.drawable.computador, "Computador", 250, 4, "Horas/Dia"));
        listaEletro.add(new Aparelho(R.drawable.dvd, "DVD", 40, 6, "Horas/Semana"));
        listaEletro.add(new Aparelho(R.drawable.ferro, "Ferro Elétrico", 1000, 30, "Minutos/Semana"));
        listaEletro.add(new Aparelho(R.drawable.frigobar, "Frigobar", 70, 10, "Horas/Dia"));
        listaEletro.add(new Aparelho(R.drawable.furadeira, "Furadeira", 650, 2, "Horas/Mês"));
        listaEletro.add(new Aparelho(R.drawable.geladeira, "Geladeira", 225, 10, "Horas/Dia"));
        listaEletro.add(new Aparelho(R.drawable.geladeira_duplex, "Geladeira Duplex", 350, 10, "Horas/Dia"));
        listaEletro.add(new Aparelho(R.drawable.impressora, "Impressora", 80, 30, "Minutos/Dia"));
        listaEletro.add(new Aparelho(R.drawable.lampada, "Lâmpada Fluorescente", 15, 8, "Horas/Dia"));
        listaEletro.add(new Aparelho(R.drawable.lavadoura, "Lavadoura", 880, 2, "Horas/Semana"));
        listaEletro.add(new Aparelho(R.drawable.liquidificador, "Liquidificador", 290, 15, "Minutos/Dia"));
        listaEletro.add(new Aparelho(R.drawable.microondas, "Microondas", 1250, 20, "Minutos/Dia"));
        listaEletro.add(new Aparelho(R.drawable.notebook, "Notebook", 90, 4, "Horas/Dia"));
        listaEletro.add(new Aparelho(R.drawable.receptor, "Receptor de TV", 20, 4, "Horas/Dia"));
        listaEletro.add(new Aparelho(R.drawable.sanduicheira, "Sanduicheira", 800, 30, "Minutos/Dia"));
        listaEletro.add(new Aparelho(R.drawable.tv_lcd, "Televisor LCD 14\"", 50, 4, "Horas/Dia"));
        listaEletro.add(new Aparelho(R.drawable.tv_lcd, "Televisor LCD 22\"", 80, 4, "Horas/Dia"));
        listaEletro.add(new Aparelho(R.drawable.tv_lcd, "Televisor LCD 32\"", 130, 4, "Horas/Dia"));
        listaEletro.add(new Aparelho(R.drawable.tv_lcd, "Televisor LCD 42\"", 200, 4, "Horas/Dia"));
        listaEletro.add(new Aparelho(R.drawable.tv_led, "Televisor LED 32\"", 95, 4, "Horas/Dia"));
        listaEletro.add(new Aparelho(R.drawable.tv_led, "Televisor LED 46\"", 155, 4, "Horas/Dia"));
        listaEletro.add(new Aparelho(R.drawable.tv_led, "Televisor LED 55\"", 200, 4, "Horas/Dia"));
        listaEletro.add(new Aparelho(R.drawable.tv_tubo, "Televisor Tubo 14\"", 50, 4, "Horas/Dia"));
        listaEletro.add(new Aparelho(R.drawable.tv_tubo, "Televisor Tubo 21\"", 80, 4, "Horas/Dia"));
        listaEletro.add(new Aparelho(R.drawable.tv_tubo, "Televisor Tubo 29\"", 100, 4, "Horas/Dia"));
        listaEletro.add(new Aparelho(R.drawable.ventilador, "Ventilador", 100, 8, "Horas/Dia"));
        listaEletro.add(new Aparelho(R.drawable.ventilador_de_teto, "Ventilador de Teto", 120, 8, "Horas/Dia"));
        listaEletro.add(new Aparelho(R.drawable.outro, "Outro", 0, 0, ""));
        // atualiza a lista.
        aparelhoAdapter.notifyDataSetChanged();
    }

    // click em um item da lista de aparelhos:
    @Override
    public void onClickListener(View view, int position) {
        // dados que serão enviados para próxima activity:
        Bundle parans = new Bundle();
        parans.putInt("idImagem", aparelhoAdapter.getEletrodomestico(position).getIdImagem());
        parans.putString("nome", aparelhoAdapter.getEletrodomestico(position).getNome());
        parans.putFloat("potencia", aparelhoAdapter.getEletrodomestico(position).getPotencia());
        parans.putFloat("tempo", aparelhoAdapter.getEletrodomestico(position).getTempo());
        parans.putString("periodo", aparelhoAdapter.getEletrodomestico(position).getPeriodo());
        parans.putInt("quantidade", 1); // valor que não está no objeto eletrodoméstico. Mas por 'padrão' será 1.

        parans.putString("activity_chamada", "lista_eletros"); // para "dizer" pra outra activity que não foi a main que a chamou, e sim essa activity.

        Intent intent = new Intent(ListaAparelhosActivity.this, ItemActivity.class);
        intent.putExtras(parans);
        startActivityForResult(intent, 0); // recebe dados da activity que essa vai chamar.

    }

    // longo click em um item da lista:
    @Override
    public void onLongPressClickListener(View view, int position) {
        //Toast.makeText(getApplicationContext(), "onLongPressClickListener(): " + position, Toast.LENGTH_SHORT).show();
    }

    // método que recebe dados da activity que essa activity chamará.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) { // se o usuário salvar o item na outra activity.
            setResult(RESULT_OK); // valor que será lido na MainActivity
            finish(); // finaliza essa activity
        }
    }

    public static class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener {

        private Context mContext;
        private GestureDetector mGestureDetector;
        private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

        public RecyclerViewTouchListener(Context c, final RecyclerView rv, RecyclerViewOnClickListenerHack rvoclh) {
            mContext = c;
            mRecyclerViewOnClickListenerHack = rvoclh;

            mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);

                    View cv = rv.findChildViewUnder(e.getX(), e.getY());

                    if (cv != null && mRecyclerViewOnClickListenerHack != null) {
                        mRecyclerViewOnClickListenerHack.onLongPressClickListener(cv, rv.getChildAdapterPosition(cv));
                    }
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {

                    View cv = rv.findChildViewUnder(e.getX(), e.getY());

                    if (cv != null && mRecyclerViewOnClickListenerHack != null) {
                        mRecyclerViewOnClickListenerHack.onClickListener(cv, rv.getChildAdapterPosition(cv));
                    }

                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            mGestureDetector.onTouchEvent(e);

            /*
            fabAdd.hide();
            fabCalcular.hide();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fabAdd.show();
                    fabCalcular.show();
                }
            }, 1200);
            */

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

}
