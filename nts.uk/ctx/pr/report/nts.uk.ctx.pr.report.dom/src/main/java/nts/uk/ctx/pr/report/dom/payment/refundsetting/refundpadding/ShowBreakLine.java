package nts.uk.ctx.pr.report.dom.payment.refundsetting.refundpadding;

import lombok.AllArgsConstructor;

/** 部門出力 */
@AllArgsConstructor
public enum ShowBreakLine {
	/**
	 * 1.表示�?�る
	 */
	DISPLAY(1),
	/**
	 * 2.表示�?��?��?�
	 */
	NOT_DISPLAY(2);

	public final int value;
}
