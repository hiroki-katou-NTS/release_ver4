package nts.uk.ctx.pr.core.dom.retirement.payment;

/**
 * 振込選択
 * @author Doan Duy Hung
 *
 */

public enum BankTransferOption {
	
	/**
	 * 選択しない
	 */
	NoSelected(0),
	
	/**
	 * 支給１
	 */
	BankTransferOption1(1),
	
	/**
	 * 支給2
	 */
	BankTransferOption2(2),
	
	/**
	 * 支給3
	 */
	BankTransferOption3(3),
	
	/**
	 * 支給4
	 */
	BankTransferOption4(4),
	
	/**
	 * 支給5
	 */
	BankTransferOption5(5);
	
	public final int value;
	
	BankTransferOption(int value){
		this.value = value;
	}
	
}
