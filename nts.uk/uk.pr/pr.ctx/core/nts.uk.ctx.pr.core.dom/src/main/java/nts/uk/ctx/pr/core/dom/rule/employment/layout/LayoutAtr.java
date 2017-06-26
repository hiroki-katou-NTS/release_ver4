package nts.uk.ctx.pr.core.dom.rule.employment.layout;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LayoutAtr {

	/** レーザー　A4　縦向き　1人 */
	LASERA4_PORTRAIT_ONE_PERS(0),
	
	/** レーザー　A4　縦向き　2人*/
	LASERA4_PORTRAIT_TWO_PERS(1),
	
	/** レーザー　A4　縦向き　3人*/
	LASERA4_PORTRAIT_THREE_PERS(2),
	
	/** レーザー　A4　横向き　2人*/
	LASERA4_LANDSCAPE_TWO_PERS(3),
	
	/** レーザー(圧着式)　縦向き　1人*/
	LASER_VERTICAL_ONE(4),
	
	/** レーザー(圧着式)　横向き　1人*/
	LASER_SIDEWAYS_ONE(5),
	
	/** ドットプリンタ　連続用紙　1人*/
	DOT_PRINTER_CONTINUOUS_PAPER_ONE(6),
	
	/** PAYS単票*/
	PAYS_SINGLE_VOTE(7),
	
	/** PAYS連続*/
	PAYS_CONTINUOUS(8);

    /**
     * value.
     */
    public final int value;    
}
