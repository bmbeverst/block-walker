package com.gooogle.code.blockWalker;

import org.anddev.andengine.util.Debug;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Decy Oct 2, 2011
 */
public class DBManager {
	
	private SharedPreferences mDB;
	private SharedPreferences.Editor mDBEditor;
	private Context mcontext;
	
	// private static final String SCORE_LABEL = "score";
	private static final String MAP_LABEL = "map";
	private static final String LIFE_LABEL = "life";
	private static final String ENERGY_LABEL = "energy";
	
	/**
	 * @param pcontext
	 */
	public DBManager(Context pcontext) {
		mcontext = pcontext;
	}
	
	// public boolean saveScore(final int pScore) {
	//
	// this.mDB = mcontext.getSharedPreferences("DB_score",
	// Context.MODE_PRIVATE);
	// this.mDBEditor = this.mDB.edit();
	// this.mDBEditor.putInt(SCORE_LABEL, pScore);
	// return this.mDBEditor.commit();
	// }
	//
	// public int loadScore() {
	//
	// this.mDB = mcontext.getSharedPreferences("DB_score",
	// Context.MODE_PRIVATE);
	// this.mDBEditor = this.mDB.edit();
	// return this.mDB.getInt(SCORE_LABEL, 0);
	// }
	
	/**
	 * @param option
	 * @param pMap
	 * @param pLife
	 * @param pEnergy
	 * @return boolean
	 */
	public boolean saveAll(int option, final String pMap, final int pLife,
			final int pEnergy) {
		this.mDB = mcontext.getSharedPreferences("DB" + option,
				Context.MODE_PRIVATE);
		
		this.mDB = mcontext.getSharedPreferences("DB" + option,
				Context.MODE_PRIVATE);
		this.mDBEditor = this.mDB.edit();
		this.mDBEditor.putString(MAP_LABEL, pMap);
		this.mDBEditor.putInt(LIFE_LABEL, pLife);
		this.mDBEditor.putInt(ENERGY_LABEL, pEnergy);
		Debug.d("ALL SAVED !");
		return this.mDBEditor.commit();
	}
	
	/**
	 * @param option
	 * @return String
	 */
	public String loadMap(int option) {
		this.mDB = mcontext.getSharedPreferences("DB" + option,
				Context.MODE_PRIVATE);
		this.mDBEditor = this.mDB.edit();
		Debug.d("MAP LOADED !");
		return this.mDB.getString(MAP_LABEL, "final0.tmx");
	}
	
	/**
	 * @param option
	 * @return int
	 */
	public int loadLife(int option) {
		this.mDB = mcontext.getSharedPreferences("DB" + option,
				Context.MODE_PRIVATE);
		this.mDBEditor = this.mDB.edit();
		Debug.d("LIFE LOADED !");
		return this.mDB.getInt(LIFE_LABEL, 3);
	}
	
	/**
	 * @param option
	 * @return int
	 */
	public int loadEnergy(int option) {
		this.mDB = mcontext.getSharedPreferences("DB" + option,
				Context.MODE_PRIVATE);
		this.mDBEditor = this.mDB.edit();
		Debug.d("ENERGY LOADED !");
		return this.mDB.getInt(ENERGY_LABEL, 100);
	}
}