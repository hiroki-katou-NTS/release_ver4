package nts.uk.ctx.sys.portal.dom.toppage;

import lombok.Getter;
import nts.uk.ctx.sys.portal.dom.primitive.CompanyId;
import nts.uk.ctx.sys.portal.dom.primitive.LayoutId;
import nts.uk.ctx.sys.portal.dom.primitive.PGType;

/**
 * The Class Layout.
 */
@Getter
public class Layout {

	/** The layout id. */
	private LayoutId layoutId;

	/** The pg area. */
	private PGType pgType;

	/** The company id. */
	private CompanyId companyId;

	/** The Layout code. */
	private Placement placement;
}
