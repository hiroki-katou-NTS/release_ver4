package nts.uk.ctx.sys.portal.dom.placement;

import lombok.EqualsAndHashCode;
import lombok.Value;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.sys.portal.dom.placement.primitive.Column;
import nts.uk.ctx.sys.portal.dom.placement.primitive.Row;

/**
 * @author LamDT
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class Placement extends DomainObject {

	/** Company ID */
	String companyID;

	/** Placement GUID */
	String placementID;

	/** Layout GUID */
	String layoutID;

	/** Toppage Part GUID */
	String toppagePartID;

	/** Column */
	Column column;

	/** Row */
	Row row;

	/** ExternalUrl */
	ExternalUrl externalUrl;
	
	public static Placement createFromJavaType(String companyId, String placementID, String layoutID, String toppagePartID, int column, int row, String url, int width, int height) {
		return new Placement(
			companyId, placementID, layoutID, toppagePartID,
			new Column(column), new Row(row),
			ExternalUrl.createFromJavaType(url, width, height)
		);
	}
}