package br.com.joinersa.contaeletrica.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.joinersa.contaeletrica.bdcore.BDCore;
import br.com.joinersa.contaeletrica.entities.Historico;

/**
 * Created by joiner on 11/08/16.
 */
public class HistoricoDAO {

    private SQLiteDatabase bd;
    private static final String PREF_CONCESSIONARIA = "preferencia";



    public HistoricoDAO(Context context) {
        BDCore auxBd = new BDCore(context);
        bd = auxBd.getWritableDatabase();
    }

    public void inserir(Historico historico) {
        ContentValues valores = new ContentValues();
        valores.put("mes", historico.getMes());
        valores.put("ano", historico.getAno());
        valores.put("consumo_mes", historico.getConsumoMes());

        //System.out.println("TESTE = " + bd.insert("historico", null, valores));

        /*
        if (bd.insert("historico", null, valores) == -1) {
            Log.i("EXCEÇÃO", ".Inserir em HistoricoDAO!");
            atualizar(historico);
        } */

        try {
            bd.insertOrThrow("historico", null, valores);
        } catch (SQLiteConstraintException e) {
            atualizar(historico);
        }

    }

    public void atualizar(Historico historico) {
        ContentValues valores = new ContentValues();
        valores.put("consumo_mes", historico.getConsumoMes());

        bd.update("historico", valores, "mes = ? AND ano = ?", new String[]{"" + historico.getMes(), "" + historico.getAno()});
    }

    public void deletar(Historico historico) {
        bd.delete("historico", "mes = " + historico.getMes() + " AND ano = " + historico.getAno(), null);
    }

    public void deleteAll() {
        bd.execSQL("delete from historico");
    }


    public List<Historico> buscar(Context context, String ano) {

        SharedPreferences preferences = context.getSharedPreferences(PREF_CONCESSIONARIA, Context.MODE_PRIVATE);
        float taxa = preferences.getFloat("valor", 0);

        List<Historico> list = new ArrayList<>();
        String[] colunas = new String[] {"mes", "ano", "consumo_mes"};

        Cursor cursor = bd.query("historico", colunas, "ano = " + ano, null, null, null, null);

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Historico historico = new Historico();
                historico.setMes(cursor.getInt(0));
                historico.setAno(cursor.getInt(1));
                historico.setConsumoMes(cursor.getFloat(2));
                historico.setCustoMes(cursor.getFloat(2) * taxa);
                list.add(historico);
            } while (cursor.moveToNext());
        }
        return list;
    }

    /**
     * Método que verifica se a tabela ESTATISTICA está vazia.
     * @return
     * Caso a tabela esteja vazia retorna true, caso contrário retorna false.
     */
    public boolean isEmpty() {
        long numOfEntries = DatabaseUtils.queryNumEntries(bd, "historico");
        if(numOfEntries == 0l) {
            // Tabela vazia
            return true;
        } else {
            // Tabela ja contem dados.
            return false;
        }
    }

    public List<String> getAnos() {

        List<String> list = new ArrayList<>();
        String[] colunas = new String[] {"ano"};

        Cursor cursor = bd.query(true, "historico", colunas, null, null, null, null, "ano DESC", null);

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                list.add("" + cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return list;
    }

}
