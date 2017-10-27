package nts.uk.ctx.at.shared.dom.worktype;

import lombok.AllArgsConstructor;

/**
 * The Enum Work Type Set Check
 * 
 * @author sonnh
 *
 */
@AllArgsConstructor
public enum WorkTypeSetCheck {

	/**
	 * しません
	 */
	NO_CHECK(0),
	
	/**
	 * します
	 */
	CHECK(1);
	
	public final int value;

}
