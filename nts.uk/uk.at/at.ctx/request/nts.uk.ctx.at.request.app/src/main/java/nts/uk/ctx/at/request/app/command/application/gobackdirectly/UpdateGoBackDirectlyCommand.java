package nts.uk.ctx.at.request.app.command.application.gobackdirectly;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.request.app.command.application.common.UpdateApplicationCommand;
import nts.uk.ctx.at.request.app.command.application.common.appapprovalphase.AppApprovalPhaseCmd;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGoBackDirectlyCommand {
	
	/**
	 * version
	 */
	Long version;
	/**
	 * application ID
	 */
	String appID;
	/**
	 * 勤務種類
	 */
	String workTypeCD;
	/**
	 * 就業時間帯
	 */
	String siftCd;
	/**
	 * 勤務を変更する
	 */
	int workChangeAtr;
	/**
	 * 勤務直行1
	 */
	int goWorkAtr1;
	/**
	 * 勤務直帰1
	 */
	int backHomeAtr1;
	/**
	 * 勤務時間開始1
	 */
	int workTimeStart1;
	/**
	 * 勤務時間終了1
	 */
	int workTimeEnd1;
	/**
	 * 勤務場所選択1
	 */
	String workLocationCD1;
	/**
	 * 勤務直行2
	 */
	int goWorkAtr2;
	/**
	 * 勤務直帰2
	 */
	int backHomeAtr2;
	/**
	 * 勤務時間開始2
	 */
	int workTimeStart2;
	/**
	 * 勤務時間終了2
	 */
	int workTimeEnd2;
	/**
	 * 勤務場所選択２
	 */
	String workLocationCD2;
	
	String employeeID;
	
	int prePostAtr;
	
	String appDate;
	
	int employeeRouteAtr;
	
	UpdateApplicationCommand appCommand;
	
	List<AppApprovalPhaseCmd> appApprovalPhaseCmds;
	
}
