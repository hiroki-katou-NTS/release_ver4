package nts.uk.ctx.at.record.pubimp.dailyperform;

import java.util.List;
import java.util.Optional;
//import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.ApprovalStatusAdapter;
//import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApprovalRootStateStatusImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApproveRootStatusForEmpImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApprovalStatusForEmployee;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonCheckParameter;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonProcessCheckService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonReflectParameter;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.DegreeReflectionAtr;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ExecutionType;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ReflectedStateRecord;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ScheAndRecordSameChangeFlg;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.absence.AbsenceReflectService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.absenceleave.AbsenceLeaveReflectService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.goback.ChangeAppGobackAtr;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.goback.GobackAppParameter;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.goback.GobackReflectParameter;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.goback.PreGoBackReflectService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.goback.PriorStampAtr;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.goback.ScheTimeReflectAtr;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.holidayworktime.HolidayWorktimeAppPara;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.holidayworktime.HolidayWorktimePara;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.holidayworktime.PreHolidayWorktimeReflectService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime.AfterOvertimeReflectService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime.OverTimeRecordAtr;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime.OvertimeAppParameter;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime.OvertimeParameter;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime.PreOvertimeReflectService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.recruitment.RecruitmentRelectRecordService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.workchange.PreWorkchangeReflectService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.workchange.WorkChangeCommonReflectPara;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.workrecord.actuallock.DetermineActualResultLock;
import nts.uk.ctx.at.record.dom.workrecord.actuallock.LockStatus;
import nts.uk.ctx.at.record.dom.workrecord.actuallock.PerformanceType;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.Identification;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentificationRepository;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.AppCommonPara;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.AppReflectProcessRecordPub;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.CommonReflectPubParameter;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.ConfirmStatusCheck;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.HolidayWorkReflectPubPara;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.ObjectCheck;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.PrePostRecordAtr;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.ReflectRecordAtr;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.WorkChangeCommonReflectPubPara;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.goback.GobackReflectPubParameter;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.overtime.OvertimeAppPubParameter;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.overtime.PreOvertimePubParameter;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.ApplicationType;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.ScheRemainCreateInfor;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.service.RemainCreateInforByScheData;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class AppReflectProcessRecordPubImpl implements AppReflectProcessRecordPub{
	@Inject
	private PreGoBackReflectService preGobackReflect;
	@Inject
	private PreOvertimeReflectService preOvertimeReflect;
	@Inject
	private AfterOvertimeReflectService afterOvertimeReflect;
	@Inject
	private AbsenceReflectService absenceReflect;
	@Inject
	private PreHolidayWorktimeReflectService holidayworkService;
	@Inject
	private PreWorkchangeReflectService workChangeService;
	@Inject
	private AbsenceLeaveReflectService absenceLeaveService;
	@Inject
	private RecruitmentRelectRecordService recruitmentService;
	@Inject
	private WorkInformationRepository workRepository;
	@Inject
	private CommonProcessCheckService processCheckService;
	@Inject
	private DetermineActualResultLock resultLock;
	@Inject
	private ClosureService closureService;
	@Inject
	private RemainCreateInforByScheData scheData;
	@Inject
	private ApprovalStatusAdapter appAdapter;
	@Inject
	private IdentificationRepository identificationRepository;
	@Override
	public boolean appReflectProcess(AppCommonPara para) {
		boolean output = true;		
		ScheRemainCreateInfor scheInfor = null;
		if(para.isChkRecord()) {
			//ドメインモデル「日別実績の勤務情報」を取得する
			Optional<WorkInfoOfDailyPerformance> optDaily = workRepository.find(para.getSid(), para.getYmd());
			if(!optDaily.isPresent()) {
				return false;
			}
		} else {
			//ドメインモデル「勤務予定基本情報」を取得する(get domain model)
			List<ScheRemainCreateInfor> lstSche = scheData.createRemainInfor(para.getCid(), para.getSid(), 
					new DatePeriod(para.getYmd(), para.getYmd()));
			if(lstSche.isEmpty()) {
				return false;
			}
			scheInfor = lstSche.get(0);
		}
		
		//WorkInfoOfDailyPerformance dailyInfor = optDaily.get();
		//反映状況によるチェック
		CommonCheckParameter checkPara = new CommonCheckParameter(para.isChkRecord() ? DegreeReflectionAtr.RECORD : DegreeReflectionAtr.SCHEDULE,
				ExecutionType.EXCECUTION,
				EnumAdaptor.valueOf(para.getStateReflectionReal().value, ReflectedStateRecord.class),
				EnumAdaptor.valueOf(para.getStateReflection().value, ReflectedStateRecord.class));
		boolean chkProcess = processCheckService.commonProcessCheck(checkPara);
		if(chkProcess) {
			//ドメインモデル「申請承認設定」.データが確立されている場合の承認済申請の反映のチェックをする
			if(para.getReflectAtr() == ReflectRecordAtr.REFLECT) {
				return output;
			}
			//アルゴリズム「実績ロックされているか判定する」を実行する
			Closure closureData = closureService.getClosureDataByEmployee(para.getSid(), para.getYmd());
			if(closureData == null) {
				return false;
			}
			LockStatus lockStatus = resultLock.getDetermineActualLocked(para.getCid(),
					para.getYmd(),
					closureData.getClosureId().value,
					PerformanceType.DAILY);
			if(lockStatus == LockStatus.LOCK) {
				return false;
			}
			//確定状態によるチェック
			ConfirmStatusCheck chkParam = new ConfirmStatusCheck(para.getCid(), 
					para.getSid(),
					para.getYmd(), 
					para.getPrePostAtr(),
					para.getAppType(), 
					para.isChkRecord() ? ObjectCheck.DAILY : ObjectCheck.SCHE, 
					para.isRecordReflect(),
					para.isScheReflect(),
					para.isChkRecord() ? false : scheInfor.isConfirmedAtr());
			return this.checkConfirmStatus(chkParam);
		}
		return output;
	}

	@Override
	public boolean preGobackReflect(GobackReflectPubParameter para) {
		return preGobackReflect.gobackReflect(this.toDomainGobackReflect(para));		
	}

	@Override
	public boolean afterGobackReflect(GobackReflectPubParameter para) {		
		return preGobackReflect.afterGobackReflect(this.toDomainGobackReflect(para));		
	}
	private GobackReflectParameter toDomainGobackReflect(GobackReflectPubParameter para) {
		GobackAppParameter appPara = new GobackAppParameter(EnumAdaptor.valueOf(para.getGobackData().getChangeAppGobackAtr().value, ChangeAppGobackAtr.class),
				para.getGobackData().getWorkTimeCode(),
				para.getGobackData().getWorkTypeCode(),
				para.getGobackData().getStartTime1(),
				para.getGobackData().getEndTime1(),
				para.getGobackData().getStartTime2(),
				para.getGobackData().getEndTime2());
		GobackReflectParameter gobackPara = new GobackReflectParameter(para.getEmployeeId(),
				para.getDateData(),
				para.isOutResReflectAtr(),
				EnumAdaptor.valueOf(para.getPriorStampAtr().value, PriorStampAtr.class),
				EnumAdaptor.valueOf(para.getScheAndRecordSameChangeFlg().value, ScheAndRecordSameChangeFlg.class),
				EnumAdaptor.valueOf(para.getScheTimeReflectAtr().value, ScheTimeReflectAtr.class),
				para.isScheReflectAtr(),
				appPara);
		return gobackPara;
		
	}

	@Override
	public boolean preOvertimeReflect(PreOvertimePubParameter param) {
		return preOvertimeReflect.overtimeReflect(this.toDomainOvertimeReflect(param));		
	}

	@Override
	public boolean afterOvertimeReflect(PreOvertimePubParameter param) {
		return afterOvertimeReflect.reflectAfterOvertime(this.toDomainOvertimeReflect(param));		
	}

	private OvertimeParameter toDomainOvertimeReflect(PreOvertimePubParameter param) {
		OvertimeAppPubParameter overtimeInfor = param.getOvertimePara();
		OvertimeAppParameter appOver = new OvertimeAppParameter(
				overtimeInfor.getWorkTypeCode(), 
				overtimeInfor.getWorkTimeCode(),
				overtimeInfor.getStartTime1(), 
				overtimeInfor.getEndTime1(),
				overtimeInfor.getStartTime2(),
				overtimeInfor.getEndTime2(),
				overtimeInfor.getMapOvertimeFrame(),
				overtimeInfor.getOverTimeShiftNight(),
				overtimeInfor.getFlexExessTime(),
				EnumAdaptor.valueOf(overtimeInfor.getOverTimeAtr().value, OverTimeRecordAtr.class),
				param.getOvertimePara().getAppReason());
		OvertimeParameter overtimePara = new OvertimeParameter(param.getEmployeeId(), 
				param.getDateInfo(), 
				param.isActualReflectFlg(), 
				param.isScheReflectFlg(), 
				param.isTimeReflectFlg(), 
				param.isAutoClearStampFlg(), 
				EnumAdaptor.valueOf(param.getScheAndRecordSameChangeFlg().value, ScheAndRecordSameChangeFlg.class),
				param.isScheTimeOutFlg(),
				appOver);
		return overtimePara;
	}

	@Override
	public boolean absenceReflect(WorkChangeCommonReflectPubPara param, boolean isPre) {
		return absenceReflect.absenceReflect(new WorkChangeCommonReflectPara(this.toRecordPara(param.getCommon()), param.getExcludeHolidayAtr()), isPre);		
	}

	@Override
	public boolean holidayWorkReflect(HolidayWorkReflectPubPara param, boolean isPre) {
		HolidayWorktimeAppPara appPara = new HolidayWorktimeAppPara(param.getHolidayWorkPara().getWorkTypeCode(),
				param.getHolidayWorkPara().getWorkTimeCode(),
				param.getHolidayWorkPara().getMapWorkTimeFrame(),
				param.getHolidayWorkPara().getNightTime(),
				param.getHolidayWorkPara().getStartTime(),
				param.getHolidayWorkPara().getEndTime());
		HolidayWorktimePara para = new HolidayWorktimePara(param.getEmployeeId(),
				param.getBaseDate(),
				param.isHolidayWorkReflectFlg(), 
				EnumAdaptor.valueOf(param.getScheAndRecordSameChangeFlg().value, ScheAndRecordSameChangeFlg.class),
				param.isScheReflectFlg(),
				appPara);
		return holidayworkService.preHolidayWorktimeReflect(para, isPre);
	}

	@Override
	public boolean workChangeReflect(WorkChangeCommonReflectPubPara param, boolean isPre) {
		
		return workChangeService.workchangeReflect(new WorkChangeCommonReflectPara(this.toRecordPara(param.getCommon()), param.getExcludeHolidayAtr()), isPre);
		
	}
	
	private CommonReflectParameter toRecordPara(CommonReflectPubParameter param) {
		CommonReflectParameter outputData = new CommonReflectParameter(param.getEmployeeId(), param.getBaseDate(), EnumAdaptor.valueOf(param.getScheAndRecordSameChangeFlg().value, ScheAndRecordSameChangeFlg.class),
			 	param.isScheTimeReflectAtr(),
			 	param.getWorkTypeCode(),
			 	param.getWorkTimeCode(),			 	
			 	param.getStartDate(),
			 	param.getEndDate(),
			 	param.getStartTime(),
			 	param.getEndTime());
		return outputData;
	}

	@Override
	public boolean absenceLeaveReflect(CommonReflectPubParameter param, boolean isPre) {
		return absenceLeaveService.reflectAbsenceLeave(this.toRecordPara(param), isPre);
	}

	@Override
	public boolean recruitmentReflect(CommonReflectPubParameter param, boolean isPre) {
		return recruitmentService.recruitmentReflect(this.toRecordPara(param), isPre);
	}

	@Override
	public boolean isRecordData(String employeeId, GeneralDate baseDate) {
		//日別実績の勤務情報
		Optional<WorkInfoOfDailyPerformance> optDailyPerfor = workRepository.find(employeeId, baseDate); 
		if(!optDailyPerfor.isPresent()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean checkConfirmStatus(ConfirmStatusCheck chkParam) {
		boolean output = true;
		//申請の種類と事前事後区分をチェックする
		if(chkParam.getPrePost() == PrePostRecordAtr.PREDICT
				&& chkParam.getAppType() == ApplicationType.OVER_TIME_APPLICATION) {
			return output;
		}
		//INPUT．対象をチェックする
		if(chkParam.getChkObject() == ObjectCheck.DAILY) {
			//ドメインモデル「反映情報」．実績強制反映をチェックする
			if(chkParam.isRecordReflect()) {
				return output;
			}
			List<ApproveRootStatusForEmpImport> lstRootStatus = appAdapter.getApprovalByEmplAndDate(chkParam.getAppDate(), chkParam.getAppDate(),
					chkParam.getSid(), chkParam.getCid(), 1);
			if(lstRootStatus.isEmpty() 
					|| lstRootStatus.get(0).getApprovalStatus() == ApprovalStatusForEmployee.UNAPPROVED) {
				List<Identification> findByEmployeeID = identificationRepository.findByEmployeeID(chkParam.getSid(), chkParam.getAppDate(), chkParam.getAppDate());
				if(!findByEmployeeID.isEmpty()) {
					return false; 
				}
				return output;
			}
			return false;
		} else {
			//ドメインモデル「反映情報」．予定強制反映をチェックする
			if(chkParam.isScheReflect()) {
				return output;
			} 
			if(chkParam.isConfirmedAtr()) {
				return false;
			}
		}
		return output;
	}
}
