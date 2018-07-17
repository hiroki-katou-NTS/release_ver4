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
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensLeaveComSetRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensLeaveEmSetRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryLeaveComSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryLeaveEmSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.ComSubstVacation;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.ComSubstVacationRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.EmpSubstVacation;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.EmpSubstVacationRepository;
@Stateless
public class InterimRemainOffPeriodCreateDataImpl implements InterimRemainOffPeriodCreateData{
	@Inject
	private InterimRemainOffDateCreateData createDataService;
	@Inject
	private ComSubstVacationRepository subRepos;
	@Inject
	private CompensLeaveComSetRepository leaveSetRepos;
	@Inject
	private EmpSubstVacationRepository empSubsRepos;
	@Inject
	private CompensLeaveEmSetRepository empLeaveSetRepos;
	@Inject
	private ShareEmploymentAdapter employmentService;
	@Override
	public Map<GeneralDate, DailyInterimRemainMngData> createInterimRemainDataMng(
			InterimRemainCreateDataInputPara inputParam) {
		Map<GeneralDate, DailyInterimRemainMngData> dataOutput = new HashMap<>();
		//雇用履歴と休暇管理設定を取得する
		Optional<ComSubstVacation> comSetting = subRepos.findById(inputParam.getCid());
		CompensatoryLeaveComSetting leaveComSetting = leaveSetRepos.find(inputParam.getCid());
		CompanyHolidayMngSetting comHolidaySetting = new CompanyHolidayMngSetting(inputParam.getCid(), comSetting, leaveComSetting);
		//アルゴリズム「社員ID（List）と指定期間から社員の雇用履歴を取得」を実行する
		List<String> lstEmployee = new ArrayList<>();
		lstEmployee.add(inputParam.getSid());
		List<SharedSidPeriodDateEmploymentImport> emloymentHist = employmentService.getEmpHistBySidAndPeriod(lstEmployee, inputParam.getDateData());
		List<AffPeriodEmpCodeImport> lstEmployment = new ArrayList<>();
		//所属雇用履歴を設定する
		if(!emloymentHist.isEmpty()) {
			lstEmployment = emloymentHist.get(0).getAffPeriodEmpCodeExports();
		}
		
		List<EmploymentHolidayMngSetting> lstEmplSetting = this.lstEmpHolidayMngSetting(inputParam.getCid(), lstEmployment);
		for(int i = 0; inputParam.getDateData().start().daysTo(inputParam.getDateData().end()) + i <= 0; i++){			
			GeneralDate loopDate = inputParam.getDateData().start().addDays(i);
			//対象日の雇用別休暇管理設定を抽出する
			List<AffPeriodEmpCodeImport> lstDateEmployment = lstEmployment.stream()
					.filter(x -> x.getPeriod().start().beforeOrEquals(loopDate) && x.getPeriod().end().afterOrEquals(loopDate))
					.collect(Collectors.toList());
			EmploymentHolidayMngSetting employmentHolidaySetting = new EmploymentHolidayMngSetting();
			if(!lstDateEmployment.isEmpty()) {
				AffPeriodEmpCodeImport dateEmployment = lstDateEmployment.get(0);
				List<EmploymentHolidayMngSetting> lstEmploymentSetting = lstEmplSetting.stream()
						.filter(y -> y.getEmploymentCode().equals(dateEmployment.getEmploymentCode()))
						.collect(Collectors.toList());
				if(!lstEmploymentSetting.isEmpty()) {
					employmentHolidaySetting = lstEmploymentSetting.get(0);
				}
			}
			
			//対象日のデータを抽出する
			InterimRemainCreateInfor dataCreate = this.extractDataOfDate(loopDate, inputParam);
			
			
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
	public InterimRemainCreateInfor extractDataOfDate(GeneralDate baseDate,
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
		//対象日の申請を抽出する
		List<AppRemainCreateInfor> appData = inputInfor.getAppData().stream()
				.filter(y -> y.getSid().equals(inputInfor.getSid()) && y.getAppDate().equals(baseDate))
				.collect(Collectors.toList());
		detailData.setAppData(appData);
		//対象日の予定を抽出する
		List<ScheRemainCreateInfor> scheData = inputInfor.getScheData().stream()
				.filter(z -> z.getSid().equals(inputInfor.getSid()) && z.getYmd().equals(baseDate))
				.collect(Collectors.toList());
		if(!scheData.isEmpty()) {
			detailData.setScheData(Optional.of(scheData.get(0)));
		}
	
		return detailData;
		
	}

	@Override
	public List<EmploymentHolidayMngSetting> lstEmpHolidayMngSetting(String cid, List<AffPeriodEmpCodeImport> lstEmployment) {
		List<EmploymentHolidayMngSetting> lstEmplSetting = new ArrayList<>();
		//雇用別休暇管理設定(List)を作成する
		for (AffPeriodEmpCodeImport emplData : lstEmployment) {
			//ドメインモデル「雇用振休管理設定」を取得する
			Optional<EmpSubstVacation> optEmpSubData = empSubsRepos.findById(cid, emplData.getEmploymentCode());
			//ドメインモデル「雇用代休管理設定」を取得する
			CompensatoryLeaveEmSetting empSetting = empLeaveSetRepos.find(cid, emplData.getEmploymentCode());
			EmploymentHolidayMngSetting employmentSetting = new EmploymentHolidayMngSetting(emplData.getEmploymentCode(), optEmpSubData, empSetting);
			lstEmplSetting.add(employmentSetting);
		}
		return lstEmplSetting;
	}

	

}
