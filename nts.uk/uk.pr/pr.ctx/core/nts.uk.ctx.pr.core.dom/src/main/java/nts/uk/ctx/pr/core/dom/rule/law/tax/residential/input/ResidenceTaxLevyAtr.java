package nts.uk.ctx.pr.core.dom.rule.law.tax.residential.input;
/**
 * 
 * @author sonnh1
 *
 */
public enum ResidenceTaxLevyAtr {
	/**
	 * 
	 * 0 - ���ʒ���
	 */
	SPECIALCOLLECTION(0),
	/**
	 * 
	 * 1 - ���ʒ���
	 */
	NORMALCOLLECTION(1);
	
	public final int value;
	
	ResidenceTaxLevyAtr(int value) {
		this.value = value;
	}
}
