package br.com.joinersa.contaeletrica.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import br.com.joinersa.contaeletrica.entities.Item;
import br.com.joinersa.contaeletrica.R;
import br.com.joinersa.contaeletrica.dao.ItemDAO;

public class ItemActivity extends AppCompatActivity {

    private LinearLayout eletroSelecionado, potenciaSelecionada, tempoPeriodoSelecionado, quantidadeSelecionada;
    private TextView subtituloEleto, subtituloPotencia, subtituloTempo, subtituloPeriodo, subtituloQuantidade;
    private int idImagem; // irá receber a referência da imagem da outra activity.
    private Button btSalvarItem;
    private Intent dados;

    private TextView potenciaDigitada;
    private TextView tempoDigitado;
    private TextView quantidadeDigitada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar_item);
        //tb.setTitle("Item");
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);// bt voltar


        // ----------------------- inicializando layouts clicáveis (Includes no xml): --------------
        eletroSelecionado = (LinearLayout) findViewById(R.id.inc_eletro_selecionado); // pegar ID do include do eletrodoméstico, servirá para evento de click e outros.
        potenciaSelecionada = (LinearLayout) findViewById(R.id.inc_potencia_selecionada);
        tempoPeriodoSelecionado = (LinearLayout) findViewById(R.id.inc_tempo_periodo_selecionados);
        quantidadeSelecionada = (LinearLayout) findViewById(R.id.inc_quantidade_selecionada);
        // -----------------------------------------------------------------------------------------

        // ----------- inicializa TextView's onde ficarão as características do item ---------------
        subtituloEleto = (TextView) eletroSelecionado.findViewById(R.id.tv_subtitulo_componente_selecionavel);
        subtituloPotencia = (TextView) potenciaSelecionada.findViewById(R.id.tv_subtitulo_componente_selecionavel);
        subtituloTempo = (TextView) tempoPeriodoSelecionado.findViewById(R.id.tv_subtitulo_1_componente_selecionavel_2);// textos que estão dentro dos layouts componetes_selecionaveis..
        subtituloPeriodo = (TextView) tempoPeriodoSelecionado.findViewById(R.id.tv_subtitulo_2_componente_selecionavel_2);
        subtituloQuantidade = (TextView) quantidadeSelecionada.findViewById(R.id.tv_subtitulo_componente_selecionavel);
        // -----------------------------------------------------------------------------------------

        // --------------------- Setando valores que vieram da activity anterior -------------------
        dados = getIntent();
        idImagem = dados.getExtras().getInt("idImagem"); // só será útil se for a activity ListaAparelhosActivity que chamou essa activity. Caso não exista a chave "idImagem", a variável idImagem fica com zero.
        //System.out.println("************************** ID: " + idImagem);
        subtituloEleto.setText("" + dados.getExtras().getString("nome"));
        subtituloPotencia.setText("" + dados.getExtras().getFloat("potencia"));
        subtituloTempo.setText("" + dados.getExtras().getFloat("tempo"));
        subtituloPeriodo.setText(dados.getExtras().getString("periodo"));
        subtituloQuantidade.setText("" + dados.getExtras().getInt("quantidade"));

        // caso essa chamada seja da Main é porque o item deve ser editado.. logo a activity será para edição do item.
        if (dados.getExtras().getString("activity_chamada").equals("main")) {
            //Log.i("Chamada", "Chamada da Main");
            tb.setTitle("Editar Item"); // muda o título da toolbar
            eletroSelecionado.setEnabled(false); // desativa componete para que não seja clicável
            // muda cor do título do item, uma forma de mostrar que o componente não pode ser clicado:
            ((TextView) eletroSelecionado.findViewById(R.id.tv_titulo_componente_selecionavel)).
                    setTextColor(ContextCompat.getColor(this, R.color.corAtributosEletro));
        }

        // -----------------------------------------------------------------------------------------


        // eletrodoméstico: ------------------------------------------------------------------------
        TextView tituloEletro = (TextView) eletroSelecionado.findViewById(R.id.tv_titulo_componente_selecionavel);// pegar o ID do título do primeiro include, o do eletrodoméstico
        tituloEletro.setText("Aparelho"); // setar o título do eletrodoméstico do primeiro include
        // Clique para selecionar um eletrodoméstico e setar no subtítulo do include do eletrodoméstico:
        eletroSelecionado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setResult(RESULT_CANCELED); // resultado cancelado que será recebido na activity anterior.
                finish(); // volta pra activity anterior sem salvar o item, podendo o usuário escolher outro aparelho.

            }
        });
        // -----------------------------------------------------------------------------------------


        // potencia: -------------------------------------------------------------------------------
        TextView tituloPotencia = (TextView) potenciaSelecionada.findViewById(R.id.tv_titulo_componente_selecionavel);
        tituloPotencia.setText("Potência (Watt)");

        potenciaSelecionada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(ItemActivity.this);
                // infla layout de entrada da potencia
                final View viewPotencia = getLayoutInflater().inflate(R.layout.potencia_do_item, null, false);
                alert.setView(viewPotencia);
                alert.setTitle("Potência");

                // chamar teclado
                showKeyboard(getApplicationContext());

                potenciaDigitada = (TextView) viewPotencia.findViewById(R.id.et_potencia_de_entrada);
                //potenciaDigitada.requestFocus();
                //potenciaDigitada.setSelectAllOnFocus(true);

                alert.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        subtituloPotencia.setText(potenciaDigitada.getText());
                        // ocutar teclado:
                        hideKeyboard(getApplicationContext(), potenciaDigitada);

                    }
                });

                alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ocutar teclado:
                        hideKeyboard(getApplicationContext(), potenciaDigitada);
                    }
                });


                // ------------- esconder teclado quando houver um clique fora do dialog -----------
                alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        // ocutar teclado:
                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        //System.out.println("################################ CANCELADO!!");
                    }
                });
                // ---------------------------------------------------------------------------------


                //alert.show();

                AlertDialog ad = alert.create();
                ad.show(); // mostrar dialog
                // mudar cor dos textos dos botões Salvar e Cancelar:
                Button bt = ad.getButton(DialogInterface.BUTTON_POSITIVE);
                bt.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
                bt = ad.getButton(DialogInterface.BUTTON_NEGATIVE);
                bt.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));

            }
        });
        // -----------------------------------------------------------------------------------------


        // tempo de uso (com período): -------------------------------------------------------------
        TextView tituloTempo = (TextView) tempoPeriodoSelecionado.findViewById(R.id.tv_titulo_componente_selecionavel_2);
        tituloTempo.setText("Tempo de Uso");

        tempoPeriodoSelecionado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Dialog Builder para entrada de dados do usuário:
                final AlertDialog.Builder alert = new AlertDialog.Builder(ItemActivity.this);

                // layout que ficará dentro do AlertDialog, com campo de tempo e radios de períodos:
                final View viewTempoPeriodo = getLayoutInflater().inflate(R.layout.tempo_periodo_do_item, null, false);
                // abrir teclado automaticamaente:
                // chamar teclado
                showKeyboard(getApplicationContext());
                //final InputMethodManager imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
                //imm.toggleSoftInput(InputMethodManager.RESULT_SHOWN, 0);

                alert.setView(viewTempoPeriodo);
                //alert.setTitle("Tempo de uso");

                // grupo de radio butões:
                final RadioGroup rgOpcoesPeriodo = (RadioGroup) viewTempoPeriodo.findViewById(R.id.rg_opcoes_de_periodo);


                // ------------------ capturar o valor do tempo: ---------------------------
                tempoDigitado = (TextView) viewTempoPeriodo.findViewById(R.id.et_tempo_de_entrada);
                // -------------------------------------------------------------------------


                // cancelar operação e ocutar teclado:
                alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ocutar teclado:
                        //imm.toggleSoftInput(InputMethodManager.RESULT_UNCHANGED_SHOWN, 0);
                        hideKeyboard(getApplicationContext(), tempoDigitado);
                    }
                });


                // ------------- esconder teclado quando houver um clique fora do dialog -----------
                alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        // ocutar teclado:
                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        //System.out.println("################################ CANCELADO!!");
                    }
                });
                // ---------------------------------------------------------------------------------


                // Dialog para entrada de dados do usuário:
                final AlertDialog alertDialog = alert.create();


                // ******* verifica que está sendo digitado o EditText, a entrada do tempo *********
                tempoDigitado.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        //
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        Log.i("onTextChanged()", "Editando...");
                        try {
                            float valorTempoDigitado = Float.parseFloat(s.toString());
                            Log.i("Valor", "" + valorTempoDigitado);
                            // verifica se é viável ativar os períodos de acordo com o tempo digitado:
                            if (valorTempoDigitado >= 1) {
                                // minutos por dia:
                                if (valorTempoDigitado <= 1440) {
                                    ((RadioButton) viewTempoPeriodo.findViewById(R.id.rb_minutos_por_dia)).setEnabled(true);
                                } else {
                                    ((RadioButton) viewTempoPeriodo.findViewById(R.id.rb_minutos_por_dia)).setEnabled(false);
                                }

                                // Minutos por semana:
                                if (valorTempoDigitado <= 10080) {
                                    ((RadioButton) viewTempoPeriodo.findViewById(R.id.rb_minutos_por_semana)).setEnabled(true);
                                } else {
                                    ((RadioButton) viewTempoPeriodo.findViewById(R.id.rb_minutos_por_semana)).setEnabled(false);
                                }

                                // Horas por dia:
                                if (valorTempoDigitado <= 24) {
                                    //Log.i("Menor Igual A 24", "Entrou");
                                    ((RadioButton) viewTempoPeriodo.findViewById(R.id.rb_horas_por_dia)).setEnabled(true);
                                } else {
                                    ((RadioButton) viewTempoPeriodo.findViewById(R.id.rb_horas_por_dia)).setEnabled(false);
                                }

                                // Horas por semana:
                                if (valorTempoDigitado <= 168) {
                                    ((RadioButton) viewTempoPeriodo.findViewById(R.id.rb_horas_por_semana)).setEnabled(true);
                                } else {
                                    ((RadioButton) viewTempoPeriodo.findViewById(R.id.rb_horas_por_semana)).setEnabled(false);
                                }

                                // Horas por mês:
                                if (valorTempoDigitado <= 720) { // considerando um mês de 30 dias ---- se fosse um mês de 31 seria 744
                                    ((RadioButton) viewTempoPeriodo.findViewById(R.id.rb_horas_por_mes)).setEnabled(true);
                                } else {
                                    ((RadioButton) viewTempoPeriodo.findViewById(R.id.rb_horas_por_mes)).setEnabled(false);
                                }

                            } else {
                                ((RadioButton) viewTempoPeriodo.findViewById(R.id.rb_minutos_por_dia)).setEnabled(false);
                                ((RadioButton) viewTempoPeriodo.findViewById(R.id.rb_minutos_por_semana)).setEnabled(false);
                                ((RadioButton) viewTempoPeriodo.findViewById(R.id.rb_horas_por_dia)).setEnabled(false);
                                ((RadioButton) viewTempoPeriodo.findViewById(R.id.rb_horas_por_semana)).setEnabled(false);
                                ((RadioButton) viewTempoPeriodo.findViewById(R.id.rb_horas_por_mes)).setEnabled(false);
                            }

                        } catch (NumberFormatException e) {
                            System.err.println("Erro: " + e.getMessage());
                            ((RadioButton) viewTempoPeriodo.findViewById(R.id.rb_minutos_por_dia)).setEnabled(false);
                            ((RadioButton) viewTempoPeriodo.findViewById(R.id.rb_minutos_por_semana)).setEnabled(false);
                            ((RadioButton) viewTempoPeriodo.findViewById(R.id.rb_horas_por_dia)).setEnabled(false);
                            ((RadioButton) viewTempoPeriodo.findViewById(R.id.rb_horas_por_semana)).setEnabled(false);
                            ((RadioButton) viewTempoPeriodo.findViewById(R.id.rb_horas_por_mes)).setEnabled(false);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                // *********************************************************************************
                // ---------------------------------------------------------------------------------


                // captura qual radioButton foi selecionado, ou seja, qual período foi selecionado:
                rgOpcoesPeriodo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        Log.i("onCheckedChanged()", "Radio clicado");

                        RadioButton rb = null;

                        int op = rgOpcoesPeriodo.getCheckedRadioButtonId(); // radio selecionado... periodo selecionado.
                        switch (op) {

                            case R.id.rb_minutos_por_dia:
                                rb = (RadioButton) viewTempoPeriodo.findViewById(op);
                                break;
                            case R.id.rb_minutos_por_semana:
                                rb = (RadioButton) viewTempoPeriodo.findViewById(op);
                                break;
                            case R.id.rb_horas_por_dia:
                                rb = (RadioButton) viewTempoPeriodo.findViewById(op);
                                break;
                            case R.id.rb_horas_por_semana:
                                rb = (RadioButton) viewTempoPeriodo.findViewById(op);
                                break;
                            case R.id.rb_horas_por_mes:
                                rb = (RadioButton) viewTempoPeriodo.findViewById(op);
                                break;
                        }

                        subtituloTempo.setText(tempoDigitado.getText()); // setar tempo
                        subtituloPeriodo.setText(rb.getText()); // setar periodo

                        // ocutar teclado:
                        //imm.toggleSoftInput(InputMethodManager.RESULT_UNCHANGED_SHOWN, 0);
                        hideKeyboard(getApplicationContext(), tempoDigitado);

                        alertDialog.dismiss(); // desfaz o alertDialog ao escolher uma opcao do radio group
                    }
                });

                alertDialog.show(); // mostrar alert

                // mudar cor do texto do botão Cancelar:
                Button bt = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                bt.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
            }
        });
        // -----------------------------------------------------------------------------------------

        // Quantidade: -----------------------------------------------------------------------------
        TextView tituloQuantidade = (TextView) quantidadeSelecionada.findViewById(R.id.tv_titulo_componente_selecionavel);
        tituloQuantidade.setText("Quantidade");

        // click no campo de seleção de quantidade:
        quantidadeSelecionada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(ItemActivity.this);
                final View viewQuantidade = getLayoutInflater().inflate(R.layout.quantidade_do_item, null, false);// inflar layout de entrada da quantidade
                alert.setView(viewQuantidade);

                // inicializando o campo de texto que receberá a quantidade:
                quantidadeDigitada = (TextView) viewQuantidade.findViewById(R.id.et_quantidade_de_entrada);

                // abrir teclado automaticamaente:
                showKeyboard(getApplicationContext());
                //final InputMethodManager imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
                //imm.toggleSoftInput(InputMethodManager.RESULT_SHOWN, 0);

                alert.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // setar no subtitulo do item selecionado a quantidade inserida no alertDialog:
                        subtituloQuantidade.setText(quantidadeDigitada.getText());
                        // ocutar teclado:
                        hideKeyboard(getApplicationContext(), quantidadeDigitada);
                        //imm.toggleSoftInput(InputMethodManager.RESULT_UNCHANGED_SHOWN, 0);
                    }
                });
                alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ocutar teclado:
                        hideKeyboard(getApplicationContext(), quantidadeDigitada);
                        //imm.toggleSoftInput(InputMethodManager.RESULT_UNCHANGED_SHOWN, 0);
                    }
                });

                // ------------- esconder teclado quando houver um clique fora do dialog -----------
                alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        // ocutar teclado:
                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        //System.out.println("################################ CANCELADO!!");
                    }
                });
                // ---------------------------------------------------------------------------------

                //alert.show();

                // mudar cor dos textos dos botões Salvar e Cancelar:
                AlertDialog ad = alert.create();
                ad.show();
                Button bt = ad.getButton(DialogInterface.BUTTON_POSITIVE);
                bt.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
                bt = ad.getButton(DialogInterface.BUTTON_NEGATIVE);
                bt.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
            }
        });

        // -----------------------------------------------------------------------------------------

        // ------------------- Botão de salvar o item escolhido: -----------------------------------
        btSalvarItem = (Button) findViewById(R.id.bt_salvar_item);
        assert btSalvarItem != null; // assertiva... if(btSalvarItem != null) { faça o que está em baixo... }
        btSalvarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (dados.getExtras().getString("activity_chamada").equals("main")) { // a chamada foi da MainActivity ---- Irá dar um Update no banco, pois está sendo editado um item.

                        // ---------- alterando dados do banco ---------------------
                        new ItemDAO(ItemActivity.this).atualizar(new Item(
                                dados.getExtras().getLong("id"), // é com o id que será possível de ser alterado o item na classe do ItemDAO.
                                //idImagem, // não é necssário passar o id da imagem, pois a imagem de um item não será alterada.
                                subtituloEleto.getText().toString(),
                                Float.parseFloat(subtituloPotencia.getText().toString()),
                                Float.parseFloat(subtituloTempo.getText().toString()),
                                subtituloPeriodo.getText().toString(),
                                Integer.parseInt(subtituloQuantidade.getText().toString()),
                                consumoMes()
                        ));
                        // -------------------------------------------------------

                    } else { // A chamada foi da ListaAparelhosActivity ---- Irá dar um Insert no banco, pois está sendo inserido um novo item.

                        // ---------- inserir dados no banco ---------------------
                        new ItemDAO(ItemActivity.this).inserir(new Item(
                                idImagem,
                                subtituloEleto.getText().toString(),
                                Float.parseFloat(subtituloPotencia.getText().toString()),
                                Float.parseFloat(subtituloTempo.getText().toString()),
                                subtituloPeriodo.getText().toString(),
                                Integer.parseInt(subtituloQuantidade.getText().toString()),
                                consumoMes()
                        ));
                        // -------------------------------------------------------

                    }

                    setResult(RESULT_OK); // avisa na activity anterior que os dados foram salvos ao clicar no botão salvar
                } catch (Exception ex) {
                    setResult(RESULT_CANCELED); // envia mensagem cancelada, ao cair em uma exceção
                    ex.printStackTrace();
                }
                finish(); // finalizar activity
            }
        });
        // -----------------------------------------------------------------------------------------
    }


    // método que ocutar teclado
    public static void hideKeyboard(Context context, View editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    // método que mostrar teclado
    public static void showKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.RESULT_SHOWN, 0);//imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }


    private float consumoMes() {

        float consumo;

        if (subtituloPeriodo.getText().equals("Minutos/Dia") || subtituloPeriodo.getText().equals("Minutos/Semana")) {

            float horas = (Float.parseFloat(subtituloTempo.getText().toString())) / 60; // transformando de minutos para hora

            if (subtituloPeriodo.getText().equals("Minutos/Dia")) {
                consumo = (Float.parseFloat(subtituloPotencia.getText().toString()) / 1000) * horas * 30 * Float.parseFloat(subtituloQuantidade.getText().toString());
            } else {
                consumo = (Float.parseFloat(subtituloPotencia.getText().toString()) / 1000) * horas * 4 * Float.parseFloat(subtituloQuantidade.getText().toString());
            }

        } else {

            float horas = (Float.parseFloat(subtituloTempo.getText().toString()));

            if (subtituloPeriodo.getText().equals("Horas/Dia")) {
                consumo = (Float.parseFloat(subtituloPotencia.getText().toString()) / 1000) * horas * 30 * Float.parseFloat(subtituloQuantidade.getText().toString());
            } else if (subtituloPeriodo.getText().equals("Horas/Semana")) {
                consumo = (Float.parseFloat(subtituloPotencia.getText().toString()) / 1000) * horas * 4 * Float.parseFloat(subtituloQuantidade.getText().toString());
            } else { // horas por mês
                consumo = (Float.parseFloat(subtituloPotencia.getText().toString()) / 1000) * horas * Float.parseFloat(subtituloQuantidade.getText().toString());
            }
        }

        return consumo;
    }
    /*
    private void enableComponentesSelecionaveis(boolean b) {
        // desabilita componentes para que não sejam clicáveis antes da escolha de um eletrodoméstico.
        potenciaSelecionada.setEnabled(b);
        tempoPeriodoSelecionado.setEnabled(b);
        quantidadeSelecionada.setEnabled(b);
        TextView t1 = (TextView) potenciaSelecionada.findViewById(R.id.tv_titulo_componente_selecionavel);
        TextView t2 = (TextView) tempoPeriodoSelecionado.findViewById(R.id.tv_titulo_componente_selecionavel_2);
        TextView t3 = (TextView) quantidadeSelecionada.findViewById(R.id.tv_titulo_componente_selecionavel);
        // muda a cor das letras dos componentes para cinza, assim dará uma impressão de desabilitado.
        if (b) {
            t1.setTextColor(Color.parseColor("#222222"));
            t2.setTextColor(Color.parseColor("#222222"));
            t3.setTextColor(Color.parseColor("#222222"));
        } else {
            t1.setTextColor(Color.parseColor("#999999"));
            t2.setTextColor(Color.parseColor("#999999"));
            t3.setTextColor(Color.parseColor("#999999"));
        }
    }
    */

    /*
    // recebe o resultado da activity que foi chamada por essa activity.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    btSalvarItem.setEnabled(true);
                    idImagem = data.getExtras().getInt("idImagem"); // id da imagem que será passada pro banco de dados.
                    subtituloEleto.setText("" + data.getExtras().getString("nome"));
                    subtituloPotencia.setText("" + data.getExtras().getFloat("potencia"));
                    subtituloTempo.setText("" + data.getExtras().getFloat("tempo"));
                    subtituloPeriodo.setText(data.getExtras().getString("periodo"));
                    subtituloQuantidade.setText("" + data.getExtras().getInt("quantidade"));
                }
        }
        // habilita componentes para serem editados e inseridos atributos ao eletrodoméstico escolhido.
        //enableComponentesSelecionaveis(true);
    }
    */

    @Override
    protected void onPause() {
        super.onPause();
        // ocutar teclado caso aplicativo seja minimizado. (solução Joiner - não sei se é gambiarra):
        if (potenciaDigitada != null) {
            hideKeyboard(getApplicationContext(), potenciaDigitada);
        }

        if (tempoDigitado != null) {
            hideKeyboard(getApplicationContext(), tempoDigitado);
        }

        if (quantidadeDigitada != null) {
            hideKeyboard(getApplicationContext(), quantidadeDigitada);
        }

        Log.d("onPause", "PAUSADA!");
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
