package nts.uk.ctx.sys.portal.dom.toppage;


import lombok.Getter;
import nts.uk.ctx.sys.portal.dom.enums.TopPagePartType;
import nts.uk.ctx.sys.portal.dom.primitive.TopPagePartCode;
import nts.uk.ctx.sys.portal.dom.primitive.TopPagePartName;

/**
 * The Class TopPagePart.
 */
@Getter
public class TopPagePart {
	
	/** The top page part code. */
	private TopPagePartCode topPagePartCode;

	/** The top page part name. */
	private TopPagePartName topPagePartName;

	/** The top page part type. */
	private TopPagePartType topPagePartType;

	/** The size. */
	private Size size;
}
