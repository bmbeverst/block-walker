/**
 * Brooks Beverstock bmb2gf Sep 8, 2011 keyBoardListener.java
 */
package com.gooogle.code.blockWalker;

import android.view.KeyEvent;

/**
 * @author brooks Sep 8, 2011
 */
public interface OnKeyUpListener {
	
	/**
	 * Used by Resource to broad cast events
	 * 
	 * @param pKeyCode
	 * @param pEvent
	 * @return treu is handeled
	 */
	boolean onKeyUp(int pKeyCode, KeyEvent pEvent);
}
