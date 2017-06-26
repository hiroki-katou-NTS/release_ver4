package nts.uk.ctx.pr.core.dom.paymentdata;

/**
 * Enum use or not. 
 *
 */
public enum UseOrNot {
	DO_NOT_USE(0),
	USE(1);
	
	public final int value;

	private UseOrNot(int value) {
		this.value = value;
	}
}
