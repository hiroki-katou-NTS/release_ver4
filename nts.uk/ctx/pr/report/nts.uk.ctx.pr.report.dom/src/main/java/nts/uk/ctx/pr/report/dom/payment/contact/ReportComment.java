/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.payment.contact;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * The Class CommentInitial.
 */
@StringMaxLength(100)
public class ReportComment extends StringPrimitiveValue<ReportComment> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new comment initial.
	 *
	 * @param rawValue
	 *            the raw value
	 */
	public ReportComment(String rawValue) {
		super(rawValue);
	}

}
