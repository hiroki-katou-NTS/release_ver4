package nts.uk.ctx.at.request.dom.applicationapproval.setting.request.gobackdirectlycommon.primitive;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(400)
public class CommentContent extends StringPrimitiveValue<CommentContent> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CommentContent(String rawValue) {
		super(rawValue);
	}

}
