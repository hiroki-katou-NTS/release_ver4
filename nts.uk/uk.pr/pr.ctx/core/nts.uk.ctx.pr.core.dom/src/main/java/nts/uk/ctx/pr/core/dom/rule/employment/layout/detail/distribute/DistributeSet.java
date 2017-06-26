package nts.uk.ctx.pr.core.dom.rule.employment.layout.detail.distribute;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DistributeSet {
	/**  0:按分しない*/
	NOT_PROPORTIONAL(0),
	/**  1:按分する*/
	PROPORTIONAL(1),
	/**  2:月1回支給*/
	MONTHLY_PAYMENT(2);

	public final int value;

}
