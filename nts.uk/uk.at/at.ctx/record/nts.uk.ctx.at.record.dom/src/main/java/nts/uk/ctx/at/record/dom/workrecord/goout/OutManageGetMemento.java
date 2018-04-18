/**
 * 
 */
package nts.uk.ctx.at.record.dom.workrecord.goout;

/**
 * The Interface OutManageGetMemento.
 *
 * @author Hoangdd
 */
public interface OutManageGetMemento {
	
	/**
	 * Gets the company ID.
	 *
	 * @return the company ID
	 */
	String getCompanyID();
	
	/**
	 * Gets the max usage.
	 *
	 * @return the max usage
	 */
	MaxGoOut getMaxUsage();
	
	/**
	 * Gets the inits the value reason go out.
	 *
	 * @return the inits the value reason go out
	 */
	GoingOutReason getInitValueReasonGoOut();
}

