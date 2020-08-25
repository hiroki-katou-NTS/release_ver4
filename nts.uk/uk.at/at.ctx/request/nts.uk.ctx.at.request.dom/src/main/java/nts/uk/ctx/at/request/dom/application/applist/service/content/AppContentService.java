package nts.uk.ctx.at.request.dom.application.applist.service.content;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import nts.uk.ctx.at.request.dom.application.AppReason;
import nts.uk.ctx.at.request.dom.application.Application;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.ReflectedState;
import nts.uk.ctx.at.request.dom.application.appabsence.HolidayAppType;
import nts.uk.ctx.at.request.dom.application.applist.extractcondition.AppListExtractCondition;
import nts.uk.ctx.at.request.dom.application.applist.extractcondition.ApplicationListAtr;
import nts.uk.ctx.at.request.dom.application.applist.service.datacreate.StampAppOutputTmp;
import nts.uk.ctx.at.request.dom.application.applist.service.detail.ScreenAtr;
import nts.uk.ctx.at.request.dom.application.applist.service.param.ListOfApplication;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalBehaviorAtrImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalPhaseStateImport_New;
import nts.uk.ctx.at.request.dom.setting.DisplayAtr;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.approvallistsetting.ApprovalListDisplaySetting;
import nts.uk.ctx.at.request.dom.setting.company.appreasonstandard.AppStandardReasonCode;
import nts.uk.ctx.at.request.dom.setting.company.appreasonstandard.ReasonForFixedForm;
import nts.uk.ctx.at.shared.dom.attendance.AttendanceItem;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.enumcommon.NotUseAtr;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * refactor 4
 * UKDesign.UniversalK.就業.KAF_申請.CMM045_申請一覧・承認一覧.A:申請一覧画面ver4.アルゴリズム.申請内容ver4
 * @author Doan Duy Hung
 *
 */
public interface AppContentService {
	
	/**
	 * refactor 4
	 * UKDesign.UniversalK.就業.KAF_申請.CMM045_申請一覧・承認一覧.A:申請一覧画面ver4.アルゴリズム.申請内容ver4.申請内容（遅刻早退取消）.申請内容（遅刻早退取消）
	 * @param appReason 申請理由
	 * @param appReasonDisAtr 申請理由表示区分
	 * @param screenID ScreenID
	 * @param itemContentLst <List>（項目名、勤務NO、区分（遅刻早退）、時刻、取消）
	 * @param appType 申請種類
	 * @param appStandardReasonCD 定型理由コード
	 * @return
	 */
	public String getArrivedLateLeaveEarlyContent(AppReason appReason, DisplayAtr appReasonDisAtr, ScreenAtr screenAtr, List<ArrivedLateLeaveEarlyItemContent> itemContentLst,
			ApplicationType appType, AppStandardReasonCode appStandardReasonCD);
	
	/**
	 * refactor 4
	 * UKDesign.UniversalK.就業.KAF_申請.CMM045_申請一覧・承認一覧.A:申請一覧画面.アルゴリズム.各申請データを作成.申請内容.申請内容定型理由取得.申請内容定型理由取得
	 * @param appType 申請種類
	 * @param appStandardReasonCD 定型理由コード
	 * @param opHolidayAppType 休暇申請の種類(Optional)
	 * @return
	 */
	public ReasonForFixedForm getAppStandardReasonContent(ApplicationType appType, AppStandardReasonCode appStandardReasonCD, Optional<HolidayAppType> opHolidayAppType);
	
	/**
	 * refactor 4
	 * UKDesign.UniversalK.就業.KAF_申請.CMM045_申請一覧・承認一覧.A:申請一覧画面.アルゴリズム.各申請データを作成.申請内容.申請内容（残業申請、休日出勤申請）.申請内容の申請理由.申請内容の申請理由
	 * @param appReasonDisAtr 申請理由表示区分
	 * @param appReason 申請理由
	 * @param screenID ScreenID
	 * @param appStandardReasonCD 定型理由コード
	 * @param appType 申請種類
	 * @param opHolidayAppType 休暇申請の種類(Optional)
	 * @return
	 */
	public String getAppReasonContent(DisplayAtr appReasonDisAtr, AppReason appReason, ScreenAtr screenAtr, AppStandardReasonCode appStandardReasonCD,
			ApplicationType appType, Optional<HolidayAppType> opHolidayAppType);
	
	/**
	 * refactor 4
	 * UKDesign.UniversalK.就業.KAF_申請.CMM045_申請一覧・承認一覧.A:申請一覧画面ver4.アルゴリズム.申請一覧の申請名称を取得する.申請一覧申請種類のプログラムID.申請一覧申請種類のプログラムID
	 * @return
	 */
	public List<AppTypeMapProgramID> getListProgramIDOfAppType(); 
	
	/**
	 * refactor 4
	 * UKDesign.UniversalK.就業.KAF_申請.CMM045_申請一覧・承認一覧.A:申請一覧画面ver4.アルゴリズム.申請内容ver4.申請内容（打刻申請）.申請内容（打刻申請）
	 * @param appReasonDisAtr 申請理由表示区分
	 * @param appReason 申請理由
	 * @param screenAtr ScreenID
	 * @param stampAppOutputTmpLst 打刻申請出力用Tmp
	 * @param appType 申請種類
	 * @param appStandardReasonCD 定型理由コード
	 * @return
	 */
	public String getAppStampContent(DisplayAtr appReasonDisAtr, AppReason appReason, ScreenAtr screenAtr, List<StampAppOutputTmp> stampAppOutputTmpLst,
			ApplicationType appType, AppStandardReasonCode appStandardReasonCD);
	
	/**
	 * refactor 4
	 * UKDesign.UniversalK.就業.KAF_申請.CMM045_申請一覧・承認一覧.A:申請一覧画面.アルゴリズム.各申請データを作成.申請内容.申請内容（勤務変更申請、直行直帰申請）.申請内容（勤務変更申請、直行直帰申請）
	 * @param appType 申請種類
	 * @param workTypeName 勤務種類名称
	 * @param workTimeName 就業時間帯名称
	 * @param goWorkAtr1 勤務直行1
	 * @param workTimeStart1 勤務時間開始1
	 * @param goBackAtr1 勤務直帰1
	 * @param workTimeEnd1 勤務時間終了1
	 * @param breakTimeStart1 休憩時間開始1
	 * @param breakTimeEnd1 休憩時間終了1
	 * @param appReasonDisAtr 申請理由表示区分
	 * @param appReason 申請理由
	 * @param application 申請
	 * @return
	 */
	public String getWorkChangeGoBackContent(ApplicationType appType, String workTypeName, String workTimeName, NotUseAtr goWorkAtr1, TimeWithDayAttr workTimeStart1, 
			NotUseAtr goBackAtr1, TimeWithDayAttr workTimeEnd1, TimeWithDayAttr breakTimeStart1, TimeWithDayAttr breakTimeEnd1, DisplayAtr appReasonDisAtr,
			AppReason appReason, Application application);
	
	/**
	 * UKDesign.UniversalK.就業.KAF_申請.CMM045_申請一覧・承認一覧.A:申請一覧画面.アルゴリズム.各申請データを作成.各申請データを作成
	 * @param application 申請
	 * @param companyID 会社ID
	 * @param lstWkTime 就業時間帯リスト
	 * @param lstWkType 勤務種類リスト
	 * @param attendanceItemLst 勤怠項目リスト
	 * @param mode モード
	 * @param approvalListDisplaySetting 承認一覧表示設定
	 * @param listOfApp 申請一覧
	 * @param mapApproval Map＜ルートインスタンスID、承認フェーズList＞
	 * @param device デバイス：PC or スマートフォン
	 * @param appListExtractCondition 申請一覧抽出条件
	 * @return
	 */
	public ListOfApplication createEachAppData(Application application, String companyID, List<WorkTimeSetting> lstWkTime, List<WorkType> lstWkType, 
			List<AttendanceItem> attendanceItemLst, ApplicationListAtr mode, ApprovalListDisplaySetting approvalListDisplaySetting, ListOfApplication listOfApp, 
			Map<String,List<ApprovalPhaseStateImport_New>> mapApproval, int device, AppListExtractCondition appListExtractCondition);
	
	/**
	 * UKDesign.UniversalK.就業.KAF_申請.CMM045_申請一覧・承認一覧.A:申請一覧画面.アルゴリズム.各申請データを作成.承認状況照会内容.承認状況照会内容
	 * @param approvalPhaseLst 承認フェーズList 
	 * @return
	 */
	public String getApprovalStatusInquiryContent(List<ApprovalPhaseStateImport_New> approvalPhaseLst);
	
	/**
	 * UKDesign.UniversalK.就業.KAF_申請.CMM045_申請一覧・承認一覧.A:申請一覧画面.アルゴリズム.各申請データを作成.反映状態（承認一覧モード）.反映状態（承認一覧モード）
	 * @param reflectedState 申請の実績反映状態
	 * @param phaseAtr 承認フェーズの承認区分
	 * @param frameAtr 承認枠の承認区分
	 * @return
	 */
	public String getReflectStatusApprovalListMode(ReflectedState reflectedState, ApprovalBehaviorAtrImport_New phaseAtr, ApprovalBehaviorAtrImport_New frameAtr);
}
