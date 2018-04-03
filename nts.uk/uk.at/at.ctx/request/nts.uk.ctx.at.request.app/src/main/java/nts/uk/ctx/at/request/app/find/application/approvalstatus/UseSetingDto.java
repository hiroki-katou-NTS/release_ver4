package nts.uk.ctx.at.request.app.find.application.approvalstatus;

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class UseSetingDto {
	/**
	 * 月別確認を利用する
	 */
	private boolean monthlyConfirm;
	/**
	 * 上司確認を利用する
	 */
	private boolean useBossConfirm;
	/**
	 * 本人確認を利用する
	 */
	private boolean usePersonConfirm;
}
