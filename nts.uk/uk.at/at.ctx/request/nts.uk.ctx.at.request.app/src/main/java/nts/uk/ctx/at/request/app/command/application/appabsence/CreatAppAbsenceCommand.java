package nts.uk.ctx.at.request.app.command.application.appabsence;

import java.util.List;

import lombok.Data;
import nts.uk.ctx.at.request.app.find.application.appabsence.dto.AppForSpecLeaveDto;
import nts.uk.ctx.at.request.app.find.application.appabsence.dto.AppTimeDigestDto;
import nts.uk.ctx.at.request.app.find.application.appabsence.dto.SubDigestionDto;
import nts.uk.ctx.at.request.app.find.application.appabsence.dto.SubTargetDigestionDto;

@Data
public class CreatAppAbsenceCommand {
	/**
	 * 申請ID
	 */
	private String appID;
	/**
	 * 申請.事前事後区分
	 */
	private int prePostAtr;
	
	/**
	 * 申請.申請日
	 */
	private String startDate;
	
	/**
	 * 申請.申請日
	 */
	private String endDate;

	/**
	 * 申請.申請者
	 */
	private String employeeID;
	
	/**
	 * 定型理由
	 */
	private String appReasonID;

	/**
	 * 申請理由	
	 */
	private String applicationReason;
	/**
	 * 休暇種類
	 */
	private Integer holidayAppType;
	/**
	 * 勤務種類コード
	 */
	private String workTypeCode;
	/**
	 * 就業時間帯コード
	 */
	private String workTimeCode;
	/**
	 * 半日の組み合わせを表示する
	 */
	public boolean halfDayFlg;
	/**
	 * 就業時間帯変更する
	 */
	private boolean changeWorkHour;
	/**
	 * 終日半日休暇区分
	 */
	private int allDayHalfDayLeaveAtr;
	/**
	 * 開始時刻
	 */
	private Integer startTime1;
	/**
	 * 終了時刻
	 */
	private Integer endTime1;
	/**
	 * 開始時刻2
	 */
	private Integer startTime2;
	/**
	 * 終了時刻2
	 */
	private Integer endTime2;
	/**
	 * 消化対象代休管理
	 */
	private List<SubTargetDigestionDto> subTargetDigestions;
	
	/**
	 * 消化対象振休管理
	 */
	private List<SubDigestionDto> subDigestions;
	/**
	 * 特別休暇申請
	 */
	private AppForSpecLeaveDto appForSpecLeave;
	
	/**
	 * 時間消化申請
	 */
	private AppTimeDigestDto appTimeDigest;
	/**
	 * 登録時にメールを送信する
	 */
	private boolean sendMail;
	
	/**
	 * displayEndDateFlg
	 */
	private boolean displayEndDateFlg;
	//specHoliday
	private SpecHolidayCommand specHd;
	
	private boolean checkOver1Year;
	
	private boolean checkContradiction;
}
