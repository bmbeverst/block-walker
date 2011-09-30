package com.gooogle.code.blockWalker.AI;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLayer;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLoader;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTile;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTiledMap;
import org.anddev.andengine.entity.modifier.IEntityModifier;
import org.anddev.andengine.entity.modifier.PathModifier;
import org.anddev.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.anddev.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.anddev.andengine.entity.modifier.PathModifier.Path;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.util.constants.Constants;
import org.anddev.andengine.util.modifier.IModifier;
import org.anddev.andengine.util.modifier.ease.EaseLinear;
import org.anddev.andengine.util.path.ITiledMap;
import org.anddev.andengine.util.path.astar.AStarPathFinder;

import com.gooogle.code.blockWalker.Resources;

//This class is used primarily for organization
public class UI{
	
	
	private static final long[] ANIMATE_DURATION = new long[] { 200, 200, 200 };
	private static final int TILE_WIDTH = 32;
	// Controls the speed the character moves for touch events
	private static final float TOUCH_SPEED = (float) 0.25;
	
	private static AnimatedSprite player;
	private static org.anddev.andengine.util.path.Path A_path;
	private static Path mCurrentPath;
	private static AStarPathFinder<TMXLayer> finder;
	private static boolean isWalking;
	private static int mWaypointIndex;
	private static PathModifier mMoveModifier;
	private static IEntityModifier mPathTemp;
	
	private static TMXTiledMap mTMXTiledMap;
	private static TMXLayer TMXMapLayer;
	

	private static List<TMXTile> CollideTiles = Resources.getCollideTile();
	
	public UI(Boss pBoss) {	
		player = pBoss;
		
		
	}
	
	//**********************************************************************************************************************
	
	/**
	 */
	public static void GeneralUI() {
		
		// These must be defined for the findpath() method to work
		final ITiledMap<TMXLayer> mTiledMap = new ITiledMap<TMXLayer>() {

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
			public boolean isTileBlocked(TMXLayer pTile, final int pToTileColumn, final int pToTileRow) {

				// Tile in the A* Path
				TMXTile blocked = TMXMapLayer.getTMXTile(pToTileColumn, pToTileRow);

				// Returns true if the tile in the A* Path is contained in the Arraylist CollideTiles
				if (CollideTiles.contains(blocked)) {
					return true;

				}
				// Return false by default = no tiles blocked
				return false;
			}

			// This is the key function to understand for AStar pathing Returns the cost of the next tile in the path
			@Override
			public float getStepCost(final TMXLayer pTile, final int pFromTileColumn, final int pFromTileRow,
					final int pToTileColumn, final int pToTileRow) {
				return 0;

			}

			// If the tile is processed by findpath(), any extra
			// code you might want goes here
			@Override
			public void onTileVisitedByPathFinder(int pTileColumn, int pTileRow) {
				// Do Nothing
			}
		};

		// Declare the AStarPathFinder
		// First Param: above ITiledMap
		// Second Param: Max Search Depth - Care, if this is too
		// small your program will crash
		// Third Param: allow diagonal movement or not
		// Fourth Param: Heuristics you want to use in the A* algorithm(optional)
		finder = new AStarPathFinder<TMXLayer>(mTiledMap, 15, false);
	}

	//***********************************************************************************************************************

	/**
	 * @param pX
	 * @param pY
	 * @param pScene
	 */
	public static void walkTo(final float pX, final float pY, Scene pScene) {

		// If the user is touching the screen Puts the touch events into an array
		final float[] pToTiles = pScene.convertLocalToSceneCoordinates(pX, pY);

		// Gets the tile at the touched location
		final TMXTile tmxTilePlayerTo = TMXMapLayer.getTMXTileAt(pToTiles[Constants.VERTEX_INDEX_X],
				pToTiles[Constants.VERTEX_INDEX_Y]);

		/*********/
		// if is walking and there is a A_path ******************
		if (isWalking == true && A_path != null) {
			walkToNextWayPoint(pX, pY, pScene);
		} else if (A_path == null) {
			float[] playerFootCordinates = player.convertLocalToSceneCoordinates(16, 16);
			TMXTile playerLocationTile  = TMXMapLayer.getTMXTileAt(playerFootCordinates[Constants.VERTEX_INDEX_X],
					playerFootCordinates[Constants.VERTEX_INDEX_Y]);;
			// Sets the A* path from the player location to the touched location.
			A_path = finder.findPath(TMXMapLayer, 20,
			// Sprite's initial tile location
					playerLocationTile.getTileColumn(), playerLocationTile.getTileRow(),
					// Sprite's final tile location
					tmxTilePlayerTo.getTileColumn(), tmxTilePlayerTo.getTileRow());

			// The path with the above parameters should be saved
			loadPathFound();
		}
	}

	//***********************************************************************************************************************

	private static void walkToNextWayPoint(final float pX, final float pY, final Scene pScene) {

		player.unregisterEntityModifier(mMoveModifier);

		// mPathTemp is another global PathModifier
		player.unregisterEntityModifier(mPathTemp);

		final Path lPath = mCurrentPath.deepCopy();
		// create a new path with length 2 from current sprite position to next
		// original path waypoint
		final Path path = new Path(2);
		path.to(player.getX(), player.getY()).to(lPath.getCoordinatesX()[mWaypointIndex + 1],
				lPath.getCoordinatesY()[mWaypointIndex + 1]);

		// recalculate the speed.TILE_WIDTH is the tmx tile width, use yours
		//Adjust the speed for different control options
		float TileSpeed = 0;
		TileSpeed = path.getLength() * TOUCH_SPEED / (TILE_WIDTH);

		// Create the modifier of this subpath
		mPathTemp = new PathModifier(TileSpeed, path, new IEntityModifierListener() {

			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {

			}

			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {

			}
		}, new IPathModifierListener() {

			public void onPathWaypointStarted(final PathModifier pPathModifier, final IEntity pEntity,
					int pWaypointIndex) {

			}

			public void onPathWaypointFinished(PathModifier pPathModifier, IEntity pEntity, int pWaypointIndex) {

			}

			public void onPathStarted(PathModifier pPathModifier, IEntity pEntity) {

			}

			public void onPathFinished(PathModifier pPathModifier, IEntity pEntity) {
					player.stopAnimation();
				A_path = null;
				walkTo(pX, pY, pScene);
			}
		});

		player.registerEntityModifier(mPathTemp);
	}

	//***********************************************************************************************************************

	private static void loadPathFound() {

		if (A_path != null) {
			// Global var
			mCurrentPath = new Path(A_path.getLength());
			int tilewidth = mTMXTiledMap.getTileWidth();
			int tileheight = mTMXTiledMap.getTileHeight();

			for (int i = 0; i < A_path.getLength(); i++) {
				mCurrentPath.to(A_path.getTileColumn(i) * tilewidth, A_path.getTileRow(i) * tileheight);
			}
			doPath();
		}
	}

	//***********************************************************************************************************************

	private static void doPath() {

		//Adjust the speed for different control options
		float Speed = 0;
		Speed = TOUCH_SPEED;

		// Create this mMoveModifier as Global, there is TOUCH_SPEED too -> player
		// speed
		mMoveModifier = new PathModifier(Speed * A_path.getLength(), mCurrentPath, new IEntityModifierListener() {

			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {

			}

			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {

			}
		}, new PathModifier.IPathModifierListener() {

			public void onPathWaypointStarted(final PathModifier pPathModifier, final IEntity pEntity,
					int pWaypointIndex) {

				
					switch (A_path.getDirectionToNextStep(pWaypointIndex)) {
					case DOWN:
						player.animate(ANIMATE_DURATION, 0, 2, true);
						break;

					case RIGHT:
						player.animate(ANIMATE_DURATION, 6, 8, true);
						break;

					case UP:
						player.animate(ANIMATE_DURATION, 9, 11, true);
						break;

					case LEFT:
						player.animate(ANIMATE_DURATION, 3, 5, true);
						break;

					default:
						break;
					}
				
				// Keep the waypointIndex in a Global Var
				mWaypointIndex = pWaypointIndex;

			}

			public void onPathWaypointFinished(PathModifier pPathModifier, IEntity pEntity, int pWaypointIndex) {
			}

			public void onPathStarted(PathModifier pPathModifier, IEntity pEntity) {
				// Set a global var
				isWalking = true;
			}

			public void onPathFinished(PathModifier pPathModifier, IEntity pEntity) {
				// Stop walking and set A_path to null
				isWalking = false;
				A_path = null;
				player.stopAnimation();
			}

		}, EaseLinear.getInstance());

		player.registerEntityModifier(mMoveModifier);
	}
}
