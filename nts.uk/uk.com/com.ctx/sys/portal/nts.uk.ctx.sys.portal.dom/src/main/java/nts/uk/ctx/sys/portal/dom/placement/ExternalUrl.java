package nts.uk.ctx.sys.portal.dom.placement;

import lombok.EqualsAndHashCode;
import lombok.Value;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.sys.portal.dom.placement.primitive.Url;
import nts.uk.ctx.sys.portal.dom.toppagepart.Size;

/**
 * @author LamDT
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class ExternalUrl extends DomainObject {

	/** Url */
	Url url;

	/** Size */
	Size size;
	
	public static ExternalUrl createFromJavaType(String url, int width, int height) {
		return new ExternalUrl(new Url(url), Size.createFromJavaType(width, height));
	}
}