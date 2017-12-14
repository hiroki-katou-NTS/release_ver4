/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.dom.singlesignon;

/**
 * The Interface WindowAccountSetMemento.
 */
public interface WindowAccountSetMemento {

	/**
	 * Sets the user id.
	 *
	 * @param userId the new user id
	 */
	void setUserId(String userId);
	
	/**
	 * Sets the hot name.
	 *
	 * @param hotName the new hot name
	 */
	void setHostName(String hostName);
	
	/**
	 * Sets the user name.
	 *
	 * @param userName the new user name
	 */
	void setUserName(String userName);
	
	/**
	 * Sets the no.
	 *
	 * @param no the new no
	 */
	void setNo(Integer no);
	
	/**
	 * Sets the use division.
	 *
	 * @param useDivision the new use division
	 */
	void setUseAtr(UseAtr useAtr);
	
	
	
}
