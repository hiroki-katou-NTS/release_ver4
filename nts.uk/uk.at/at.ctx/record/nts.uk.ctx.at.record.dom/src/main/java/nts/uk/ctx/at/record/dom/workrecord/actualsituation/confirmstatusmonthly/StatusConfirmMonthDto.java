package nts.uk.ctx.at.record.dom.workrecord.actualsituation.confirmstatusmonthly;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 * 月の実績の確認状況
 * @author tutk
 *
 */
@Getter
@NoArgsConstructor
public class StatusConfirmMonthDto {
	
	/**確認状況*/
	private List<ConfirmStatusResult> listConfirmStatus = new ArrayList<>();

	
	public StatusConfirmMonthDto(List<ConfirmStatusResult> listConfirmStatus) {
		super();
		this.listConfirmStatus = listConfirmStatus;
	}

	
}
