package com.gooogle.code.blockWalker.AI;

import java.util.ArrayList;
import java.util.List;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLayer;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXObject;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXObjectGroup;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTile;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTiledMap;
import org.anddev.andengine.entity.modifier.IEntityModifier;
import org.anddev.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.anddev.andengine.entity.modifier.PathModifier;
import org.anddev.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.anddev.andengine.entity.modifier.PathModifier.Path;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.constants.Constants;
import org.anddev.andengine.util.modifier.IModifier;
import org.anddev.andengine.util.modifier.ease.EaseLinear;
import org.anddev.andengine.util.path.ITiledMap;
import org.anddev.andengine.util.path.astar.AStarPathFinder;

import com.gooogle.code.blockWalker.Resources;

/**
 * @author brooks
 * Oct 1, 2011
 */
// This class is used primarily for organization
public class AstartPathing {
	
	protected static final float TILE_WIDTH = 32;
	protected static final float TILE_HEIGHT = 32;
	protected static final float SPEED = (float) 0.25;
	
	private static List<TMXTile> CollideTiles = new ArrayList<TMXTile>();
	private static org.anddev.andengine.util.path.Path A_path = null;
	private static AStarPathFinder<TMXLayer> finder;
	private static Path mCurrentPath;
	private static boolean isWalking;
	private static PathModifier mMoveModifier;
	private static IEntityModifier mPathTemp;
	private static int mWaypointIndex;
	private static TMXTiledMap mTMXTiledMap;
	private static TMXLayer TMXMapLayer;
	private static Boss boss;
	private static Scene mScene = Resources.getmScene();
	
	/**
	 * @param pBoss
	 */
	public static void setBoss(Boss pBoss) {
		boss = pBoss;
		// Declare the AStarPathFinder
		// First Param: above ITiledMap
		// Second Param: Max Search Depth - Care, if this is too
		// small your program will crash
		// Third Param: allow diagonal movement or not
		// Fourth Param: Heuristics you want to use in the A*
		// algorithm(optional)
		finder = new AStarPathFinder<TMXLayer>(new AImap(), 30, false);
	}
	
	/**
	 * @param map
	 */
	public static void setTMXTiledMap(TMXTiledMap map) {
		CollideTiles.clear();
		mTMXTiledMap = map;
		TMXMapLayer = mTMXTiledMap.getTMXLayers().get(0);
		for (final TMXObjectGroup group : mTMXTiledMap.getTMXObjectGroups()) {
			
			// Gets the object layer with these properties. Use if
			// you have many object layers, Otherwise this can be
			// removed
			if (group.getTMXObjectGroupProperties().containsTMXProperty(
					"wall", "true")) {
				
				for (final TMXObject object : group.getTMXObjects()) {
					
					int ObjectX = Math.round(object.getX()
							- (TILE_WIDTH / 2.0f));//remove + x to make greedy
					int ObjectY = Math.round(object.getY()
							- (TILE_HEIGHT / 2.0f));
					// Gets the number of rows and columns in the
					// object
					int ObjectHeight = Math.round((object.getHeight() + TILE_HEIGHT/2)
							/ TILE_HEIGHT);
					int ObjectWidth = Math
							.round((object.getWidth() + TILE_HEIGHT/2)/ TILE_WIDTH);
					
					// Gets the tiles the object covers and puts it
					// into the Arraylist CollideTiles
					for (int TileRow = 0; TileRow < ObjectHeight; TileRow++) {
						for (int TileColumn = 0; TileColumn < ObjectWidth; TileColumn++) {
							TMXTile tempTile = TMXMapLayer.getTMXTileAt(ObjectX
									+ (TileColumn * TILE_WIDTH), ObjectY
									+ (TileRow * TILE_HEIGHT));
							CollideTiles.add(tempTile);
						}
					}
					
				}// end object for
			}//end if
		}//end for
		Debug.d(CollideTiles.size() + " Collide Size");
	}
	
	// ***********************************************************************************************************************
	
	/**
	 * @param pX
	 * @param pY
	 */
	public static void walkTo(final float pX, final float pY) {
		
		// If the user is touching the screen Puts the touch events into an
		// array
		final float[] pToTiles = Resources.getmScene().convertLocalToSceneCoordinates(pX, pY);
		
		// Gets the tile at the touched location
		final TMXTile tmxTilePlayerTo = TMXMapLayer.getTMXTileAt(
				pToTiles[Constants.VERTEX_INDEX_X],
				pToTiles[Constants.VERTEX_INDEX_Y]);
		
		/*********/
		// if is walking and there is a A_path ******************
		if ((isWalking == true) && (A_path != null)) {
			walkToNextWayPoint(pX, pY, Resources.getmScene());
		} else if (A_path == null) {
			// Sets the A* path from the player location to the touched
			// location.
			float[] playerFootCordinates = boss
					.convertLocalToSceneCoordinates(16, 16);
			TMXTile playerLocationTile = TMXMapLayer.getTMXTileAt(
					playerFootCordinates[Constants.VERTEX_INDEX_X],
					playerFootCordinates[Constants.VERTEX_INDEX_Y]);
			
			if (tmxTilePlayerTo != null) {
				A_path = finder.findPath(TMXMapLayer,
						20,
						// Sprite's initial tile location
						playerLocationTile.getTileColumn(),
						playerLocationTile.getTileRow(),
						// Sprite's final tile location
						tmxTilePlayerTo.getTileColumn(),
						tmxTilePlayerTo.getTileRow());
			}
			// The path with the above parameters should be saved
			loadPathFound();
		}
	}
	
	// ***********************************************************************************************************************
	
	private static void walkToNextWayPoint(final float pX, final float pY,
			final Scene pScene) {
		
		boss.unregisterEntityModifier(mMoveModifier);
		
		// mPathTemp is another global PathModifier
		boss.unregisterEntityModifier(mPathTemp);
		
		final Path lPath = mCurrentPath.deepCopy();
		// create a new path with length 2 from current sprite position to next
		// original path waypoint
		final Path path = new Path(2);
		path.to(boss.getX(), boss.getY()).to(
				lPath.getCoordinatesX()[mWaypointIndex + 1],
				lPath.getCoordinatesY()[mWaypointIndex + 1]);
		
		// recalculate the speed.TILE_WIDTH is the tmx tile width, use yours
		// Adjust the speed for different control options
		float TileSpeed = (path.getLength() * SPEED) / (TILE_WIDTH);
		
		// Create the modifier of this subpath
		mPathTemp = new PathModifier(TileSpeed, path,
				new IEntityModifierListenerImplementation(), new FinishedMod(
						pY, pScene, pX));
		
		boss.registerEntityModifier(mPathTemp);
	}
	
	// ***********************************************************************************************************************
	
	private static void loadPathFound() {
		
		if (A_path != null) {
			// Global var
			mCurrentPath = new Path(A_path.getLength());
			int tilewidth = mTMXTiledMap.getTileWidth();
			int tileheight = mTMXTiledMap.getTileHeight();
			
			for (int i = 0; i < A_path.getLength(); i++) {
				mCurrentPath.to(A_path.getTileColumn(i) * tilewidth,
						A_path.getTileRow(i) * tileheight);
			}
			doPath();
		}
	}
	
	// ***********************************************************************************************************************
	
	private static void doPath() {
		
		// Create this mMoveModifier as Global, there is TOUCH_SPEED too ->
		// player
		// speed
		mMoveModifier = new PathModifier(SPEED * A_path.getLength(),
				mCurrentPath, new IEntityModifierListenerImplementation(),
				new Animator(), EaseLinear.getInstance());
		
		boss.registerEntityModifier(mMoveModifier);
	}
	
	private static class IEntityModifierListenerImplementation implements
			IEntityModifierListener {
		@Override
		public void onModifierStarted(IModifier<IEntity> pModifier,
				IEntity pItem) {/* Not Used */
		}
		
		@Override
		public void onModifierFinished(IModifier<IEntity> pModifier,
				IEntity pItem) {/* Not Used */
		}
	}
	
	private static final class AImap implements ITiledMap<TMXLayer> {
		// Pretty self explanatory
		@Override
		public int getTileColumns() {
			return mTMXTiledMap.getTileColumns();
		}
		
		// Pretty self explanatory
		@Override
		public int getTileRows() {
			return mTMXTiledMap.getTileRows();
		}
		
		// Lets you customize what blocks you want to be considered blocked
		@Override
		public boolean isTileBlocked(TMXLayer pTile, final int pToTileColumn,
				final int pToTileRow) {
			
			// Tile in the A* Path
			TMXTile blocked = TMXMapLayer.getTMXTile(pToTileColumn, pToTileRow);
			
			// Returns true if the tile in the A* Path is contained in the
			// Arraylist CollideTiles
			if (CollideTiles.contains(blocked)) {
				//Debug.d("Blocked");
				return true;
			}
			
			// Return false by default = no tiles blocked
			return false;
		}
		
		// This is the key function to understand for AStar pathing Returns the
		// cost of the next tile in the path
		@Override
		public float getStepCost(final TMXLayer pTile,
				final int pFromTileColumn, final int pFromTileRow,
				final int pToTileColumn, final int pToTileRow) {
			return 0;
			
		}
		
		// If the tile is processed by findpath(), any extra
		// code you might want goes here
		@Override
		public void onTileVisitedByPathFinder(int pTileColumn, int pTileRow) {
			// Do Nothing
		}
	}
	
	private static final class FinishedMod implements IPathModifierListener {
		private final float mY;
		private final float mX;
		
		private FinishedMod(float pY, Scene pScene, float pX) {
			this.mY = pY;
			this.mX = pX;
		}
		
		@Override
		public void onPathWaypointStarted(final PathModifier pPathModifier,
				final IEntity pEntity, int pWaypointIndex) {/* Not Used */
		}
		
		@Override
		public void onPathWaypointFinished(PathModifier pPathModifier,
				IEntity pEntity, int pWaypointIndex) {/* Not Used */
		}
		
		@Override
		public void onPathStarted(PathModifier pPathModifier, IEntity pEntity) {/*
																				 * Not
																				 * Used
																				 */
		}
		
		@Override
		public void onPathFinished(PathModifier pPathModifier, IEntity pEntity) {
			boss.idle();
			A_path = null;
			walkTo(mX, mY);
		}
	}
	
	private static final class Animator implements
			PathModifier.IPathModifierListener {
		@Override
		public void onPathWaypointStarted(final PathModifier pPathModifier,
				final IEntity pEntity, int pWaypointIndex) {
			
			switch (A_path.getDirectionToNextStep(pWaypointIndex)) {
				case DOWN:
					boss.down();
					break;
				
				case RIGHT:
					boss.right();
					break;
				
				case UP:
					boss.up();
					break;
				
				case LEFT:
					boss.left();
					break;
				
				default:
					break;
			}
			
			// Keep the waypointIndex in a Global Var
			mWaypointIndex = pWaypointIndex;
			
		}
		
		@Override
		public void onPathWaypointFinished(PathModifier pPathModifier,
				IEntity pEntity, int pWaypointIndex) {/* Not Used */
		}
		
		@Override
		public void onPathStarted(PathModifier pPathModifier, IEntity pEntity) {
			// Set a global var
			isWalking = true;
		}
		
		@Override
		public void onPathFinished(PathModifier pPathModifier, IEntity pEntity) {
			// Stop walking and set A_path to null
			isWalking = false;
			A_path = null;
			boss.idle();
		}
	}
}
