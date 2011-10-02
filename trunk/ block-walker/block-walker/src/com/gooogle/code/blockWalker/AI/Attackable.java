/**
 * 
 */
package com.gooogle.code.blockWalker.AI;

import org.anddev.andengine.entity.shape.IShape;

/**
 * @author brooks
 */
public interface Attackable extends IShape {
	/**
	 * Entities that can be attacked.
	 */
	public void attacked();
	
	/**
	 * @return boolean
	 */
	public boolean isBoss();
}
