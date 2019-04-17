package nts.uk.ctx.at.request.app.command.application.holidayshipment;

import lombok.Getter;

@Getter
public class SaveHolidayShipmentCommand {

	/**
	 * 振出
	 */
	private RecruitmentAppCommand recCmd;

	/**
	 * 振休
	 */
	private AbsenceLeaveAppCommand absCmd;

	/**
	 * 申請組み合わせ
	 */
	private int comType;

	/**
	 * 使用済日数
	 */
	private int usedDays;

	/**
	 * 申請 ITEM
	 */
	private ApplicationCommand appCmd;
	
	private boolean checkOver1Year;
	
	private Boolean isNotSelectYes;
	
	private Boolean isScreenB;
}
