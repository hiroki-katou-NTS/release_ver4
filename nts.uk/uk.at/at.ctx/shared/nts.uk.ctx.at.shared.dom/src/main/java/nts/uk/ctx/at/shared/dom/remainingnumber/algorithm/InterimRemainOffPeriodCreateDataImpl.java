package nts.uk.ctx.at.shared.dom.remainingnumber.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.adapter.employment.AffPeriodEmpCodeImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employment.SharedSidPeriodDateEmploymentImport;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.CompanyHolidayMngSetting;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.EmploymentHolidayMngSetting;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.service.RemainCreateInforByApplicationData;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.service.RemainCreateInforByRecordData;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.service.RemainCreateInforByScheData;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensLeaveComSetRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensLeaveEmSetRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryLeaveComSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryLeaveEmSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.ComSubstVacation;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.ComSubstVacationRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.EmpSubstVacation;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.EmpSubstVacationRepository;
import nts.uk.ctx.at.shared.dom.worktype.service.WorkTypeIsClosedService;
@Stateless
public class InterimRemainOffPeriodCreateDataImpl implements InterimRemainOffPeriodCreateData {
	@Inject
	private InterimRemainOffDateCreateData createDataService;
	@Inject
	private EmpSubstVacationRepository empSubsRepos;
	@Inject
	private CompensLeaveEmSetRepository empLeaveSetRepos;
	@Inject
	private ShareEmploymentAdapter employmentService;
	@Inject
	private RemainCreateInforByScheData remainScheData;
	@Inject
	private RemainCreateInforByRecordData remainRecordData;
	@Inject
	private RemainCreateInforByApplicationData remainAppData;
	@Inject
	private ComSubstVacationRepository subRepos;
	@Inject
	private CompensLeaveComSetRepository leaveSetRepos;
	@Inject
	private WorkTypeIsClosedService workTypeRepo;
	@Override
	public Map<GeneralDate, DailyInterimRemainMngData> createInterimRemainDataMng(
			InterimRemainCreateDataInputPara inputParam, CompanyHolidayMngSetting comHolidaySetting) {
		Map<GeneralDate, DailyInterimRemainMngData> dataOutput = new HashMap<>();
		//アルゴリズム「社員ID（List）と指定期間から社員の雇用履歴を取得」を実行する
		List<String> lstEmployee = new ArrayList<>();
		lstEmployee.add(inputParam.getSid());
		List<SharedSidPeriodDateEmploymentImport> emloymentHist = employmentService.getEmpHistBySidAndPeriod(lstEmployee, inputParam.getDateData());
		//所属雇用履歴を設定する
		List<AffPeriodEmpCodeImport> lstEmployment = !emloymentHist.isEmpty() ? emloymentHist.get(0).getAffPeriodEmpCodeExports() : new ArrayList<>();
		List<EmploymentHolidayMngSetting> lstEmplSetting = this.lstEmpHolidayMngSetting(inputParam.getCid(), lstEmployment);
		GeneralDate sStartDate = inputParam.getDateData().start();
		GeneralDate sEndDate = inputParam.getDateData().end();
		//対象日の雇用別休暇管理設定を抽出する
		List<AffPeriodEmpCodeImport> lstDateEmployment = lstEmployment.stream()
				.filter(x -> x.getPeriod().start().beforeOrEquals(sStartDate) && x.getPeriod().end().afterOrEquals(sEndDate))
				.collect(Collectors.toList());
		EmploymentHolidayMngSetting employmentHolidaySetting = new EmploymentHolidayMngSetting();
		if(!lstDateEmployment.isEmpty() && lstDateEmployment.size() == 1) {
			AffPeriodEmpCodeImport dateEmployment = lstDateEmployment.get(0);
			List<EmploymentHolidayMngSetting> lstEmploymentSetting = lstEmplSetting.stream()
					.filter(y -> y.getEmploymentCode().equals(dateEmployment.getEmploymentCode()))
					.collect(Collectors.toList());
			if(!lstEmploymentSetting.isEmpty()) {
				employmentHolidaySetting = lstEmploymentSetting.get(0);
			}
		}
		for(int i = 0; sStartDate.daysTo(sEndDate) - i >= 0; i++){
			GeneralDate loopDate = inputParam.getDateData().start().addDays(i);
			if(!inputParam.getAppData().isEmpty()
					&& inputParam.getAppData().get(0).getLstAppDate() != null
					&& !inputParam.getAppData().get(0).getLstAppDate().isEmpty()
					&& inputParam.getAppData().get(0).getLstAppDate().contains(loopDate)) {
				continue;
			}
			if(employmentHolidaySetting == null || employmentHolidaySetting.getEmploymentCode() == null) {
				lstDateEmployment = lstEmployment.stream()
						.filter(x -> x.getPeriod().start().beforeOrEquals(loopDate) && x.getPeriod().end().afterOrEquals(loopDate))
						.collect(Collectors.toList());			
				if(!lstDateEmployment.isEmpty()) {
					AffPeriodEmpCodeImport dateEmployment = lstDateEmployment.get(0);
					List<EmploymentHolidayMngSetting> lstEmploymentSetting = lstEmplSetting.stream()
							.filter(y -> y.getEmploymentCode().equals(dateEmployment.getEmploymentCode()))
							.collect(Collectors.toList());
					if(!lstEmploymentSetting.isEmpty()) {
						employmentHolidaySetting = lstEmploymentSetting.get(0);
					}
				}
			}
			//対象日のデータを抽出する
			InterimRemainCreateInfor dataCreate = this.extractDataOfDate(inputParam.getCid(),loopDate, inputParam);
			
			
			//アルゴリズム「指定日の暫定残数管理データを作成する」
			DailyInterimRemainMngData outPutdata = createDataService.createData(inputParam.getCid(), 
					inputParam.getSid(),
					loopDate,
					inputParam.isDayOffTimeIsUse(),
					dataCreate,
					comHolidaySetting,
					employmentHolidaySetting);
			if(outPutdata != null) {
				dataOutput.put(loopDate, outPutdata);	
			}			
		}
		
		return dataOutput;
	}

	@Override
	public InterimRemainCreateInfor extractDataOfDate(String cid,GeneralDate baseDate,
			InterimRemainCreateDataInputPara inputInfor) {
		InterimRemainCreateInfor detailData = new InterimRemainCreateInfor(Optional.empty(), Optional.empty(), Collections.emptyList());
		//実績を抽出する
		List<RecordRemainCreateInfor> recordData = inputInfor.getRecordData()
				.stream()
				.filter(x -> x.getSid().equals(inputInfor.getSid()) && x.getYmd().equals(baseDate))
				.collect(Collectors.toList());
		if(!recordData.isEmpty()) {
			detailData.setRecordData(Optional.of(recordData.get(0)));
		}
		
		//対象日の予定を抽出する
		List<ScheRemainCreateInfor> scheData = inputInfor.getScheData().stream()
				.filter(z -> z.getSid().equals(inputInfor.getSid()) && z.getYmd().equals(baseDate))
				.collect(Collectors.toList());
		if(!scheData.isEmpty()) {
			detailData.setScheData(Optional.of(scheData.get(0)));
		}
		//対象日の申請を抽出する
		List<AppRemainCreateInfor> appData = inputInfor.getAppData().stream()
				.filter(y -> y.getSid().equals(inputInfor.getSid()) 
						&& (y.getAppDate().equals(baseDate)	
								|| (y.getStartDate().isPresent()
										&& y.getEndDate().isPresent()
										&& y.getStartDate().get().beforeOrEquals(baseDate)
										&& y.getEndDate().get().afterOrEquals(baseDate))
								)
						)
				.collect(Collectors.toList());
		appData = appData.stream().sorted((a,b) -> b.getInputDate().compareTo(a.getInputDate())).collect(Collectors.toList());
		Integer excludeHolidayAtr = null;
		if(!appData.isEmpty() && appData.get(0).getAppType() == ApplicationType.WORK_CHANGE_APPLICATION) {
			excludeHolidayAtr = remainAppData.excludeHolidayAtr(cid, appData.get(0).getAppId());
		}
		//申請：　勤務変更申請、休日を除外する		
		//又は　休暇申請
		if((excludeHolidayAtr != null && excludeHolidayAtr == 1)
				|| (!appData.isEmpty() && appData.get(0).getAppType() == ApplicationType.ABSENCE_APPLICATION)) {
			//申請日は休日かチェック、休日なら申請データをセットしない
			if((detailData.getRecordData().isPresent() && workTypeRepo.checkHoliday(detailData.getRecordData().get().getWorkTypeCode()))
					|| (detailData.getScheData().isPresent() && workTypeRepo.checkHoliday(detailData.getScheData().get().getWorkTypeCode()))) {
				return detailData;
			}
		}
		detailData.setAppData(appData);
		return detailData;
		
	}

	@Override
	public List<EmploymentHolidayMngSetting> lstEmpHolidayMngSetting(String cid, List<AffPeriodEmpCodeImport> lstEmployment) {
		List<EmploymentHolidayMngSetting> lstEmplSetting = new ArrayList<>();
		//雇用別休暇管理設定(List)を作成する
		lstEmployment.stream().forEach(emplData -> {
			//ドメインモデル「雇用振休管理設定」を取得する
			Optional<EmpSubstVacation> optEmpSubData = empSubsRepos.findById(cid, emplData.getEmploymentCode());
			//ドメインモデル「雇用代休管理設定」を取得する
			CompensatoryLeaveEmSetting empSetting = empLeaveSetRepos.find(cid, emplData.getEmploymentCode());
			EmploymentHolidayMngSetting employmentSetting = new EmploymentHolidayMngSetting(emplData.getEmploymentCode(), optEmpSubData, empSetting);
			lstEmplSetting.add(employmentSetting);
		});
		return lstEmplSetting;
	}

	@Override
	public Map<GeneralDate, DailyInterimRemainMngData> createInterimRemainByScheRecordApp(
			InterimRemainCreateDataInputPara param) {
		//Input「予定」がNULLかどうかチェック
		if(param.getScheData().isEmpty()) {
			//(Imported)「残数作成元の勤務予定を取得する」
			param.setScheData(remainScheData.createRemainInfor(param.getCid(), param.getSid(), param.getDateData()));
		}
		//Input「実績」がNULLかどうかチェック
		if(param.getRecordData().isEmpty()) {
			param.setRecordData(remainRecordData.lstRecordRemainData(param.getCid(), param.getSid(), param.getDateData()));
		}
		//(Imported)「残数作成元の申請を取得する」
		List<AppRemainCreateInfor> lstAppData = remainAppData.lstRemainDataFromApp(param.getCid(), param.getSid(), param.getDateData());
		//Input「申請」がNULLかどうかチェック
		if(!lstAppData.isEmpty()) {
			lstAppData.addAll(param.getAppData());
			param.setAppData(lstAppData);
		}
		Optional<ComSubstVacation> comSetting = subRepos.findById(param.getCid());
		CompensatoryLeaveComSetting leaveComSetting = leaveSetRepos.find(param.getCid());
		CompanyHolidayMngSetting comHolidaySetting = new CompanyHolidayMngSetting(param.getCid(), comSetting, leaveComSetting);
		InterimRemainCreateDataInputPara createDataParam = new InterimRemainCreateDataInputPara(param.getCid(),
				param.getSid(),
				param.getDateData(),
				param.getRecordData(),
				param.getScheData(),
				param.getAppData(),
				param.isDayOffTimeIsUse());
		return this.createInterimRemainDataMng(createDataParam, comHolidaySetting);
	}

	

}
