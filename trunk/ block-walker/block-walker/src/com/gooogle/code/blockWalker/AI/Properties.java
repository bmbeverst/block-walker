package com.gooogle.code.blockWalker.AI;

/**
 * @author brooks
 * Sep 20, 2011
 */
class AIProperties {
	
	private final String mName;
	private final String mValue;
	
	
	/**
	 * @param mName
	 * @param mValue
	 */
	public AIProperties(String mName, String mValue) {
		super();
		this.mName = mName;
		this.mValue = mValue;
	}
	/**
	 * @return the mName
	 */
	final String getmName() {
		return mName;
	}
	/**
	 * @return the mValue
	 */
	final String getmValue() {
		return mValue;
	}
	
}
