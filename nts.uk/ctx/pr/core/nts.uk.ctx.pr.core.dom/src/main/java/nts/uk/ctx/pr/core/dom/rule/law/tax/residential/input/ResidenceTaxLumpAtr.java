package nts.uk.ctx.pr.core.dom.rule.law.tax.residential.input;
/**
 * 
 * @author sonnh1
 *
 */
public enum ResidenceTaxLumpAtr {
	/**
	 * 0 - �ꊇ�������Ȃ�
	 */
	DONTBULKCOLLECTION(0),
	/**
	 * 1 - �ꊇ��������
	 */
	BULKCOLLECTION(1);
	
	public final int value;
	
	ResidenceTaxLumpAtr (int value){
		this.value = value;
	}

}
