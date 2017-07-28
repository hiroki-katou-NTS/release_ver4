/**
 * 
 */
package nts.uk.ctx.bs.person.dom.person.layoutitemclassification;

import lombok.AllArgsConstructor;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * @author laitv
 *
 */
@AllArgsConstructor
@StringMaxLength(1)
public enum LayoutItemType {

	/*0 :  項目*/
	ITEM(0), 
	
	/*1 :  一覧表*/
	LIST(1),

	/*2 :  区切り線*/
	SeparatorLine(2);
	
	public final int value;

}
