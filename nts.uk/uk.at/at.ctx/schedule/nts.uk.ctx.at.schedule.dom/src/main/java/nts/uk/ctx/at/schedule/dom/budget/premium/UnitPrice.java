package nts.uk.ctx.at.schedule.dom.budget.premium;

/**
 * 
 * @author Doan Duy Hung
 *
 */

public enum UnitPrice {
	
	// 単価１
	Price_1(0),
	
	// 単価2
	Price_2(1), 
	
	// 単価3
	Price_3(2),
	
	// 基準単価
	Standard(3),
	
	// 契約単価
	Contract(4);
	
	public final int value;
	
	UnitPrice(int value){
		this.value = value;
	}
}
