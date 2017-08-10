/**
 * 
 */
package find.layout.classification.definition;

import lombok.Value;
import nts.uk.ctx.bs.person.dom.person.layoutitemclassification.definition.LayoutPersonInfoClsDefinition;

/**
 * @author laitv
 *
 */
@Value
public class LayoutPersonInfoClsDefDto {

	private String layoutID;
	private int layoutDisPOrder;
	private int dispOrder;
	private String personInfoItemDefinitionID;

	public static LayoutPersonInfoClsDefDto fromDomain(LayoutPersonInfoClsDefinition domain) {
		return new LayoutPersonInfoClsDefDto(domain.getLayoutID(), domain.getLayoutDisPOrder().v().intValue(),
				domain.getDispOrder().v().intValue(), domain.getPersonInfoItemDefinitionID());
	}
}
