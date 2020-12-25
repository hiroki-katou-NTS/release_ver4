package nts.uk.ctx.at.request.app.find.application.holidayshipment.refactor5.dto;

import java.util.List;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.shared.app.find.remainingnumber.paymana.PayoutSubofHDManagementDto;
import nts.uk.ctx.at.shared.app.find.remainingnumber.subhdmana.dto.LeaveComDayOffManaDto;
import nts.uk.ctx.at.shared.app.find.worktype.WorkTypeDto;

/**
 * @author thanhpv
 * 振休申請起動時の表示情報 || 振出申請起動時の表示情報
 */
@Getter
@Setter
@NoArgsConstructor
public class DisplayInformationApplication {
	/** 勤務種類リスト */
	private List<WorkTypeDto> workTypeList;
	/** 休出代休紐付け管理 */
	private List<LeaveComDayOffManaDto> leaveComDayOffMana;
	/** 初期選択勤務種類 */
	private String workType;
	/** 初期選択就業時間帯 */
	private String workTime;
	/** 開始時刻 */
	private Integer startTime;
	/** 終了時刻 */
	private Integer endTime;
	/** 開始時刻2 */
	private Integer startTime2;
	/** 終了時刻2 */
	private Integer endTime2;
	
	/** 振出振休紐付け管理 --> only AbsApplication <=> 振出申請起動時の表示情報*/ 
	private List<PayoutSubofHDManagementDto> payoutSubofHDManagements;
	
}


