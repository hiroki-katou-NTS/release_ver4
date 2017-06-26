package nts.uk.ctx.basic.dom.organization.employment;


import lombok.AllArgsConstructor;
@AllArgsConstructor
public enum ManageOrNot {
	/**1:管理する	 */
	MANAGE(1),
	/**0:管理しない  */
	NOT_MANAGE(0);
	/**
     * value.
     */
    public final int value; 
    }
