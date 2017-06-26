package nts.uk.ctx.pr.core.finder.itemmaster;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO: Item master
 * @author chinhbv
 *
 */
@Data
@AllArgsConstructor
public class ItemMasterDto {
	/**
	 * item code
	 */
	private String code;
	
	/**
	 * item name
	 */
	private String name;
	
	/**
	 * category
	 * 0-PAYMENT
	 * 1-DEDUCTION
	 * 2-PERSONAL_TIME
	 * 3-ARTICLES
	 * 9-OTHER
	 */
	private int categoryAtr;
	
	/**
	 * 0-表示しない
	 * 1-表示する
	 */
	private int displaySet;
}
