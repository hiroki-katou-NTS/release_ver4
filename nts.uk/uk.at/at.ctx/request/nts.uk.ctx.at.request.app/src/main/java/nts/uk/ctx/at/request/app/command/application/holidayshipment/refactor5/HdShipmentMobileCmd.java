package nts.uk.ctx.at.request.app.command.application.holidayshipment.refactor5;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.uk.ctx.at.request.app.command.application.holidayshipment.refactor5.command.AbsenceLeaveAppCmd;
import nts.uk.ctx.at.request.app.command.application.holidayshipment.refactor5.command.RecruitmentAppCmd;
import nts.uk.ctx.at.request.app.find.application.holidayshipment.refactor5.dto.DisplayInforWhenStarting;
import nts.uk.ctx.at.shared.app.find.remainingnumber.paymana.PayoutSubofHDManagementDto;
import nts.uk.ctx.at.shared.app.find.remainingnumber.subhdmana.dto.LeaveComDayOffManaDto;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@Getter
public class HdShipmentMobileCmd {
	/**
	 * 画面モード
	 */
	private boolean newMode;
	
	/**
	 * 振出申請
	 */
	public RecruitmentAppCmd rec;
	
	/**
	 * 振休申請
	 */
	public AbsenceLeaveAppCmd abs;
	
	/**
	 * 振休振出申請起動時の表示情報
	 */
	private DisplayInforWhenStarting displayInforWhenStarting;
	
	/**
	 * 振出_古いの休出代休紐付け管理<List>
	 */
	private List<LeaveComDayOffManaDto> recOldHolidayMngLst;
	
	/**
	 * 振出_休出代休紐付け管理<List>
	 */
	private List<LeaveComDayOffManaDto> recHolidayMngLst;
	
	/**
	 * 振休_古いの休出代休紐付け管理<List>
	 */
	private List<LeaveComDayOffManaDto> absOldHolidayMngLst;
	
	/**
	 * 振休_古いの振出振休紐付け管理<List>
	 */
	private List<PayoutSubofHDManagementDto> absOldWorkMngLst;
	
	/**
	 * 振休_休出代休紐付け管理<List>
	 */
	private List<LeaveComDayOffManaDto> absHolidayMngLst;
	
	/**
	 * 振休_振出振休紐付け管理<List>
	 */
	private List<PayoutSubofHDManagementDto> absWorkMngLst;
}
