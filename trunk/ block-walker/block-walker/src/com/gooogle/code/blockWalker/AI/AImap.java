/**
 * Brooks Beverstock bmb2gf
 * Sep 20, 2011
 * AImap.java
 */
package com.gooogle.code.blockWalker.AI;

import static org.anddev.andengine.util.constants.Constants.VERTEX_INDEX_X;
import static org.anddev.andengine.util.constants.Constants.VERTEX_INDEX_Y;

import java.util.LinkedList;

import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTiledMap;
import org.anddev.andengine.util.path.ITiledMap;

/**
 * @author brooks
 * Sep 20, 2011
 */
public class AImap extends Entity implements ITiledMap{
	

	private final AITile[][] mAITiles;
	private int mTileColumns;
	private int mTileRows;
	private float mTileWidth;
	private int mTileHeight;
	
	
	/**
	 * @param pMap map that is to be converted to AI readable version.
	 */
	public AImap(TMXTiledMap pMap) {
		mTileColumns = pMap.getTileColumns();
		mTileRows = pMap.getTileRows();
		mTileWidth = pMap.getTileWidth();
		mTileHeight = pMap.getTileHeight();
		mAITiles = new AITile[mTileColumns][mTileRows];
		for(int y = 0; y < mTileRows; y++) {
			for(int x = 0; x < mTileColumns; x++) {
				mAITiles[mTileColumns][mTileRows] = new AITile();
			}
		}
	}
	
	@Override
	public int getTileColumns() {
		return mTileColumns;
	}

	@Override
	public int getTileRows() {
		return mTileRows;
	}

	@Override
	public void onTileVisitedByPathFinder(int pTileColumn, int pTileRow) {
		//Do nothing?
	}

	@Override
	public boolean isTileBlocked(Object pEntity, int pTileColumn, int pTileRow) {
		AITile temp = mAITiles[pTileColumn][pTileRow];
		if(temp != null) {
			return temp.conatinsProperty("blocked");
		}
		return false;
	}

	@Override
	public float getStepCost(Object pEntity, int pFromTileColumn,
			int pFromTileRow, int pToTileColumn, int pToTileRow) {
		// TODO Auto-generated method stub
		return 0;
	}
	AITile getAITileAt(final float pX, final float pY) {
		final float[] localCoords = this.convertSceneToLocalCoordinates(pX, pY);

		final int tileColumn = (int)(localCoords[VERTEX_INDEX_X] / mTileWidth);
		if(tileColumn < 0 || tileColumn > this.mTileColumns - 1) {
			return null;
		}
		final int tileRow = (int)(localCoords[VERTEX_INDEX_Y] / mTileHeight);
		if(tileRow < 0 || tileRow > this.mTileRows - 1) {
			return null;
		}

		return mAITiles[tileRow][tileColumn];
	}
	
	class AITile {

		private LinkedList<String> mProperties = new LinkedList<String>();

		boolean conatinsProperty(String pProp) {
			return mProperties.contains(pProp);
		}
		
		boolean addProperty(String pProp) {
			return mProperties.add(pProp);
		}
	}
	
}
