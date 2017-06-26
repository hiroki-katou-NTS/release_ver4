package nts.uk.ctx.sys.portal.dom.toppagepart;

import lombok.EqualsAndHashCode;
import lombok.Value;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.sys.portal.dom.toppagepart.primitive.Height;
import nts.uk.ctx.sys.portal.dom.toppagepart.primitive.Width;

/**
 * @author LamDT
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class Size extends DomainObject {

	/** Width */
	Width width;

	/** Height */
	Height height;

	public static Size createFromJavaType(int width, int height) {
		return new Size(new Width(width), new Height(height));
	}
}