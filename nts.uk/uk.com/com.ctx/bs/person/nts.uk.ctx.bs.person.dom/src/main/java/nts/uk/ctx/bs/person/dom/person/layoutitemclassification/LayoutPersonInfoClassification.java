/**
 * 
 */
package nts.uk.ctx.bs.person.dom.person.layoutitemclassification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;

/**
 * @author laitv
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LayoutPersonInfoClassification extends AggregateRoot {

	private String layoutID;
	private DispOrder dispOrder;
	private String personInfoCategoryID;
	private LayoutItemType layoutItemType;

	public static LayoutPersonInfoClassification createFromJaveType(String layoutID, int dispOrder,
			String personInfoCategoryID, int layoutItemType) {
		
		return new LayoutPersonInfoClassification(layoutID, new DispOrder(dispOrder),
				personInfoCategoryID, EnumAdaptor.valueOf(layoutItemType, LayoutItemType.class));
	}
}
