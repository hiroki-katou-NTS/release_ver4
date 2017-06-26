package nts.uk.ctx.pr.core.dom.rule.law.tax.residential.input;
/**
 * 
 * @author sonnh1
 *
 */
public enum ResidenceTaxLumpAtr {
	/**
	 * 0 - ˆêŠ‡’¥Žû‚µ‚È‚¢
	 */
	DONTBULKCOLLECTION(0),
	/**
	 * 1 - ˆêŠ‡’¥Žû‚·‚é
	 */
	BULKCOLLECTION(1);
	
	public final int value;
	
	ResidenceTaxLumpAtr (int value){
		this.value = value;
	}

}
