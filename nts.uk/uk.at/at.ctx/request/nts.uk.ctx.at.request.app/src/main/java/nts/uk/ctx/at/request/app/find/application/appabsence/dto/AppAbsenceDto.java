package nts.uk.ctx.at.request.app.find.application.appabsence.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.request.app.find.application.common.ApplicationDto_New;
import nts.uk.ctx.at.request.app.find.application.lateorleaveearly.ApplicationReasonDto;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.EmployeeOvertimeDto;
import nts.uk.ctx.at.request.app.find.setting.company.request.applicationsetting.apptypesetting.DisplayReasonDto;
import nts.uk.ctx.at.request.dom.application.appabsence.AbsenceWorkType;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsence;
import nts.uk.ctx.at.request.dom.application.appabsence.service.CheckDispHolidayType;
import nts.uk.ctx.at.request.dom.application.appabsence.service.NumberOfRemainOutput;
import nts.uk.ctx.at.shared.dom.specialholiday.specialholidayevent.service.DateSpecHdRelationOutput;
import nts.uk.ctx.at.shared.dom.specialholiday.specialholidayevent.service.MaxDaySpecHdOutput;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppAbsenceDto {
	/**
	 * version
	 */
	private long version;
	/**
	 * application
	 */
	private ApplicationDto_New application;
	/**
	 * 会社ID
	 */
	private String companyID;
	/**
	 * 申請ID
	 */
	private String appID;
	/**
	 * employeeID
	 */
	private String employeeID;
	/**
	 * employeeName
	 */
	private String employeeName;
	
	/**
	 * employees
	 */
	private List<EmployeeOvertimeDto> employees;
	/**
	 * 休暇種類
	 */
	private int holidayAppType;
	/**
	 * 勤務種類コード
	 */
	private String workTypeCode;
	
	/**
	 * workTypeName
	 */
	private String workTypeName;
	/**
	 * 就業時間帯コード
	 */
	private String workTimeCode;
	
	/**
	 * workTimeName
	 */
	private String workTimeName;
	/**
	 * 半日の組み合わせを表示する
	 */
	public boolean halfDayFlg;
	/**
	 * 就業時間帯変更する
	 */
	private boolean changeWorkHourFlg;
	/**
	 * 終日半日休暇区分
	 */
	private Integer allDayHalfDayLeaveAtr;
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
	 * manualSendMailFlg
	 */
	private boolean manualSendMailFlg;
	/**
	 * applicationReasonDtos
	 */
	private List<ApplicationReasonDto> applicationReasonDtos;
	
	/**
	 * prePostFlg
	 */
	private boolean prePostFlg;
	
	private List<Integer> holidayAppTypes;
	
	/**
	 * workTypes
	 */
	private List<AbsenceWorkType> workTypes;
	
	/**
	 * workTimeCodes
	 */
	private List<String> workTimeCodes;
	
	private int initMode;
	/**
	 * mailFlg
	 */
	private boolean mailFlg;
	
	/**
	 * displayWorkChangeFlg : dùng cho màn B
	 */
	private boolean displayWorkChangeFlg;
	
	/**
	 * holidayAppTypeName
	 */
	private List<HolidayAppTypeName> holidayAppTypeName;
	
	private boolean appReasonRequire;
	
	/**
	 * 承認処理時に自動でメールを送信する
	 */
	private boolean sendMailWhenApprovalFlg;
	/**
	 * 新規登録時に自動でメールを送信する
	 */
	private boolean sendMailWhenRegisterFlg;
	
	private CheckDispHolidayType checkDis;
	private MaxDaySpecHdOutput maxDayObj;
	//事象に応じた特休フラグ(true, false)
	private boolean specHdForEventFlag;
	private List<DateSpecHdRelationOutput> lstRela;
	/* 上限日数の設定方法 */
	private Integer maxNumberDayType;
	/* 忌引とする */
	private Integer makeInvitation;
	private AppForSpecLeaveDto specHdDto;
	private List<DisplayReasonDto> displayReasonDtoLst;
	//ドメインモデル「申請種類別設定」.事前事後区分を変更できる
	private boolean prPostChange;
	private SettingNo65 setingNo65;
	//No.376
	private NumberOfRemainOutput numberRemain;
	
	// 終日・半日選択表示区分
	private int dayDispSet;
		
	public static AppAbsenceDto fromDomain(AppAbsence app){
		return new AppAbsenceDto(app.getVersion(),
								ApplicationDto_New.fromDomain(app.getApplication()),
								app.getCompanyID(),
								app.getAppID(),
								app.getApplication().getEmployeeID(),
								"",
								null,
								app.getHolidayAppType().value,
								app.getWorkTypeCode() == null ? null : app.getWorkTypeCode().toString(),
								"",
								app.getWorkTimeCode() == null ? null : app.getWorkTimeCode().toString(),
								"",
								app.isHalfDayFlg(),
								app.isChangeWorkHour(),
								app.getAllDayHalfDayLeaveAtr().value,
								app.getStartTime1() == null ? null :app.getStartTime1().v(),
								app.getEndTime1() == null ? null :app.getEndTime1().v(),
								app.getStartTime2() == null ? null :app.getStartTime2().v(),
								app.getEndTime2() == null ? null :app.getEndTime2().v(),
								null,
								null,
								null,
								null,
								false,
								null,
								true,
								null,
								null,
								null,
								0,
								false,
								false,null,true, false, false, null, null, false,
								new ArrayList<>(),
								null,
								null,
								null,
								Collections.emptyList(),
								false,
								null,
								null,
								0);
	}
}

