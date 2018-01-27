package br.com.joinersa.contaeletrica.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.joinersa.contaeletrica.Concessionaria;
import br.com.joinersa.contaeletrica.R;

public class ConcessionariaActivity extends AppCompatActivity {

    private LinearLayout concessionariaSelecionada;
    private TextView subtituloNomeConcessionaria, subtituloValorConcessionaria;
    private static final String PREF_CONCESSIONARIA = "preferencia";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concessionaria);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_concessionaria);
        //toolbar.setTitle("Configuração");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);// bt voltar


        concessionariaSelecionada = (LinearLayout) findViewById(R.id.inc_concessionaria_selecionada);

        TextView tituloConcessionaria = (TextView) concessionariaSelecionada.findViewById(R.id.tv_titulo_componente_selecionavel_3);
        tituloConcessionaria.setText("Concessionária");


        SharedPreferences sp = getSharedPreferences(PREF_CONCESSIONARIA, Context.MODE_PRIVATE);

        subtituloNomeConcessionaria = (TextView) concessionariaSelecionada.findViewById(R.id.tv_subtitulo_1_componente_selecionavel_3);
        subtituloValorConcessionaria = (TextView) concessionariaSelecionada.findViewById(R.id.tv_subtitulo_2_componente_selecionavel_3);

        if(sp.contains("valor")) {
            subtituloNomeConcessionaria.setText(sp.getString("concessionaria", ""));
            subtituloValorConcessionaria.setText("R$ " + sp.getFloat("valor", 0) + " / kWh");
        }



        // click para escolha de uma concessionária:
        concessionariaSelecionada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.titulo_alertdialog, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(ConcessionariaActivity.this);
                //builder.setTitle("Concessionárias");
                builder.setCustomTitle(view); // título personalizado..
                builder.setSingleChoiceItems(Concessionaria.getArrayNomes(), -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SharedPreferences preferences = getSharedPreferences(PREF_CONCESSIONARIA, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("concessionaria", Concessionaria.getEnumConcessionaria(which).nome);
                        editor.putFloat("valor", Concessionaria.getEnumConcessionaria(which).valor);
                        editor.commit();

                        subtituloNomeConcessionaria.setText(Concessionaria.getEnumConcessionaria(which).nome);
                        subtituloValorConcessionaria.setText("R$ " + Concessionaria.getEnumConcessionaria(which).valor + " / kWh");

                        dialog.dismiss();
                    }
                });
                builder.show();

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return true;

    }

}
