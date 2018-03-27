package nts.uk.ctx.at.request.app.find.application.overtime.dto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.request.app.find.application.common.ApplicationDto_New;
import nts.uk.ctx.at.request.app.find.application.lateorleaveearly.ApplicationReasonDto;
import nts.uk.ctx.at.request.dom.application.overtime.AppOverTime;
import nts.uk.ctx.at.request.dom.application.overtime.service.AppOvertimeReference;
import nts.uk.ctx.at.request.dom.application.overtime.service.SiftType;
import nts.uk.ctx.at.request.dom.application.overtime.service.WorkTypeOvertime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OverTimeDto {
	
	private Long version;
	/**
	 * application
	 */
	private ApplicationDto_New application;
	/**
	 * 会社ID
	 * companyID
	 */
	private String companyID;
	/**
	 * 申請ID
	 */
	private String appID;
	
	/**
	 * overtimeInstructInformation
	 */
	private String overtimeInstructInformation;
	/**
	 * displayOvertimeInstructInforFlg
	 */
	private boolean displayOvertimeInstructInforFlg;
	/**
	 * 
	 */
	private String employeeID;
	
	/**
	 * 申請者
	 */
	private String employeeName;
	/**
	 * 残業区分
	 */
	private int overtimeAtr;
	/**
	 * 残業申請時間設定
	 */
	private List<OvertimeInputDto> overTimeInputs;
	/**
	 *  事前事後区分表示 
	 */
	private int displayPrePostFlg;
	/**
	 * workType
	 */
	private WorkTypeOvertime workType;
	
	/**
	 * workTypes
	 */
	private List<String> workTypes;
	
	/** siftType */
	private SiftType siftType;
	
	/**
	 * siftTypes
	 */
	private List<String> siftTypes;

	/**
	 * 勤務時間From1
	 */
	private Integer workClockFrom1;
	/**
	 * 勤務時間To1
	 */
	private Integer workClockTo1;
	/**
	 * 勤務時間From2
	 */
	private Integer workClockFrom2;
	/**
	 * 勤務時間To2
	 */
	private Integer workClockTo2;
	/**
	 * 乖離定型理由
	 */
	private String divergenceReasonID;
	/**
	 * 乖離理由
	 */
	private String divergenceReasonContent;
	
	/**
	 * 計算残業時間
	 */
	private int calculationOverTime;
	/**
	 * フレックス超過時間
	 */
	private Integer flexExessTime;
	/**
	 * 就業時間外深夜時間
	 */
	private Integer overTimeShiftNight;
	/**
	 * 時刻計算利用
	 */
	private boolean displayCaculationTime;
	
	/**
	 * 休憩時間取得表示する
	 */
	private boolean displayRestTime;
	
	/**
	 * 加給時間を取得
	 */
	private boolean displayBonusTime;
	
	/**
	 * applicationReasonDtos
	 */
	private List<ApplicationReasonDto> applicationReasonDtos;
	/**
	 * typicalReasonDisplayFlg
	 */
	private boolean typicalReasonDisplayFlg;
	
	/**
	 * displayAppReasonContentFlg
	 */
	private boolean displayAppReasonContentFlg;
	
	/**
	 * divergenceReasonDtos
	 */
	private List<DivergenceReasonDto> divergenceReasonDtos;
	/**
	 * displayDivergenceReason
	 */
	private boolean displayDivergenceReasonForm;
	
	/**
	 * displayDivergenceReasonInput
	 */
	private boolean displayDivergenceReasonInput;
	
	/**
	 * appOvertimeNightFlg
	 */
	private int appOvertimeNightFlg;
	
	
	
	/**
	 * 参照ラベル
	 */
	private boolean referencePanelFlg;
	/**
	 * 事前申請ラベル
	 */
	private boolean preAppPanelFlg;
	/**
	 * allPreAppPanelFlg
	 */
	private boolean allPreAppPanelFlg;
	/**
	 * 時間外表示区分
	 */
	private boolean extratimeDisplayFlag;
	
	/**
	 * manualSendMailAtr
	 */
	private boolean manualSendMailAtr;
	
	/**
	 * preAppOvertimeDto
	 */
	private PreAppOvertimeDto preAppOvertimeDto;
	
	/**
	 * appOvertimeReference
	 */
	private AppOvertimeReference appOvertimeReference;
	
	private boolean performanceDisplayAtr;
	
	private boolean preDisplayAtr;
	/**
	 * flexFLag
	 */
	private boolean flexFLag;
	
	/**
	 * prePostCanChangeFlg
	 */
	private boolean prePostCanChangeFlg;
	
	public static OverTimeDto fromDomain(AppOverTime appOverTime){
		return new OverTimeDto(
				appOverTime.getVersion(),
				ApplicationDto_New.fromDomain(appOverTime.getApplication()), 
				appOverTime.getCompanyID(), 
				appOverTime.getAppID(), 
				"", 
				false, 
				"", 
				"", 
				appOverTime.getOverTimeAtr().value, 
				CollectionUtil.isEmpty(appOverTime.getOverTimeInput())
					? Collections.emptyList() 
					: appOverTime.getOverTimeInput().stream().map(x -> OvertimeInputDto.fromDomain(x)).collect(Collectors.toList()), 
				0, 
				new WorkTypeOvertime(appOverTime.getWorkTypeCode().v(), ""),
				Collections.emptyList(),
				new SiftType(appOverTime.getSiftCode().v(),""),
				Collections.emptyList(),
				appOverTime.getWorkClockFrom1(), 
				appOverTime.getWorkClockTo1(),  
				appOverTime.getWorkClockFrom2(), 
				appOverTime.getWorkClockTo2(), 
				"", 
				appOverTime.getDivergenceReason(), 
				0, 
				appOverTime.getFlexExessTime(), 
				appOverTime.getOverTimeShiftNight(), 
				false, 
				false, 
				false, 
				Collections.emptyList(), 
				false, 
				false, 
				Collections.emptyList(),
				false, 
				false, 
				0, 
				false,
				false,
				false,
				false,
				false, 
				null,
				null,
				false,
				false,
				false,false);
	}
	
}
