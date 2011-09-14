/**
 * Brooks Beverstock bmb2gf
 * Sep 8, 2011
 * keyBoardListener.java
 */
package com.gooogle.code.blockWalker;

import android.view.KeyEvent;

/**
 * @author brooks
 * Sep 8, 2011
 */
public interface OnKeyUpListener {

	/**
	 * @param pKeyCode
	 * @param pEvent
	 * @return
	 */
	boolean onKeyUp(int pKeyCode, KeyEvent pEvent);
}
