package br.com.joinersa.contaeletrica.bdcore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Joiner on 16/07/2016.
 */
public class BDCore extends SQLiteOpenHelper {

    private static final String NOME_BD = "contaeletrica";
    private static final int VERSAO_BD = 1;

    public BDCore(Context context) {
        super(context, NOME_BD, null, VERSAO_BD);
    }
    // public Item(int idImagem, String nome, float potencia, float tempo, String periodo, long id, int quantidade, float consumoMes)
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE itens(_id INTEGER primary key autoincrement, id_imagem INTEGER not null, nome TEXT not null, " +
                "potencia REAL not null, tempo REAL not null, periodo TEXT not null, quantidade INTEGER not null, " +
                "consumo_mes REAL not null)");

        db.execSQL("CREATE TABLE `historico` (" +
                "`mes` INTEGER NOT NULL," +
                "`ano` INTEGER NOT NULL," +
                "`consumo_mes` REAL NOT NULL," +
                "PRIMARY KEY(`mes`,`ano`))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table itens");
        db.execSQL("drop table historico");
        onCreate(db);
    }
}
