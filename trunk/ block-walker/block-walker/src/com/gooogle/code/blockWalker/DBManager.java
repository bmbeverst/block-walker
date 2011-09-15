package com.gooogle.code.blockWalker;

import org.anddev.andengine.util.Debug;

import android.content.Context;
import android.content.SharedPreferences;

public class DBManager {
	 
    private SharedPreferences mDB;
    private SharedPreferences.Editor mDBEditor;
    private Context mcontext;
   
    private static final String SCORE_LABEL = "score";
    private static final String MAP_LABEL = "score";

   
    public DBManager(Context pcontext) {
    	mcontext=pcontext;
    }
   
    public boolean saveScore(final int pScore) {

        this.mDB = mcontext.getSharedPreferences("DB_score", Context.MODE_PRIVATE);
        this.mDBEditor = this.mDB.edit();
        this.mDBEditor.putInt(SCORE_LABEL, pScore);
        return this.mDBEditor.commit();
    }
     
    public int loadScore() {

        this.mDB = mcontext.getSharedPreferences("DB_score", Context.MODE_PRIVATE);
        this.mDBEditor = this.mDB.edit();
        return this.mDB.getInt(SCORE_LABEL, 0);
    }
    

    
    public boolean saveMap(final String pMap) {
        this.mDB = mcontext.getSharedPreferences("DB_map", Context.MODE_PRIVATE);
        this.mDBEditor = this.mDB.edit();
        this.mDBEditor.putString(MAP_LABEL, pMap);
        Debug.d("MAP SAVED !");
        return this.mDBEditor.commit();
    }
     
    public String loadMap() {
        this.mDB = mcontext.getSharedPreferences("DB_map", Context.MODE_PRIVATE);
        this.mDBEditor = this.mDB.edit();
        Debug.d("MAP LOADED !");

        return this.mDB.getString(MAP_LABEL, "stage1.tmx");
    }
}