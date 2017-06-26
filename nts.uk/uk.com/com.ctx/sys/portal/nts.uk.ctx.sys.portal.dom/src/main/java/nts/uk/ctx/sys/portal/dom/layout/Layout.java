package nts.uk.ctx.sys.portal.dom.layout;

import lombok.EqualsAndHashCode;
import lombok.Value;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.sys.portal.dom.enums.PGType;

@Value
@EqualsAndHashCode(callSuper = false)
public class Layout extends AggregateRoot {

	/** Company ID */
	String companyID;

	/** Layout GUID */
	String layoutID;

	/** PG Type */
	PGType pgType;

	public static Layout createFromJavaType(String companyID, String layoutID, int pgType) {
		return new Layout(companyID, layoutID, EnumAdaptor.valueOf(pgType, PGType.class));
	}

}