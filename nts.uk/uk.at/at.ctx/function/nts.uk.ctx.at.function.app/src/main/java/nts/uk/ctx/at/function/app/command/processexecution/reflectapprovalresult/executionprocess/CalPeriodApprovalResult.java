package nts.uk.ctx.at.function.app.command.processexecution.reflectapprovalresult.executionprocess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.dom.statement.EmployeeGeneralInfoAdapter;
import nts.uk.ctx.at.function.dom.statement.dtoimport.EmployeeGeneralInfoImport;
import nts.uk.ctx.at.record.dom.adapter.generalinfo.dtoimport.ExWorkplaceHistItemImport;
import nts.uk.ctx.at.record.dom.affiliationinformation.wkplaceinfochangeperiod.WkplaceInfoChangePeriod;
import nts.uk.ctx.at.record.dom.affiliationinformation.wktypeinfochangeperiod.WkTypeInfoChangePeriod;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.schedule.dom.adapter.generalinfo.workplace.ExWorkplaceHistItemImported;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository;
import nts.uk.ctx.at.schedule.dom.schedule.schedulemaster.requestperiodchange.RequestPeriodChangeService;
import nts.uk.ctx.at.shared.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmpDto;
import nts.uk.ctx.at.shared.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmpHisAdaptor;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 承認結果の再反映期間を計算する
 * @author tutk
 *
 */
@Stateless
public class CalPeriodApprovalResult {
	
	@Inject
	private WorkInformationRepository workInformationRepo;
	
	@Inject
	private BasicScheduleRepository basicScheduleRepo;
	
	@Inject
	private EmployeeGeneralInfoAdapter employeeGeneralInfoAdapter;
	
	@Inject
	private WkplaceInfoChangePeriod wkplaceInfoChangePeriod;
	
	@Inject
	private RequestPeriodChangeService requestPeriodChangeService;
	
	@Inject
	private BusinessTypeOfEmpHisAdaptor businessTypeOfEmpHisAdaptor;
	
	@Inject
	private WkTypeInfoChangePeriod wkTypeInfoChangePeriod;
	
	/**
	 * 
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param startDate 開始日
	 * @param isRecreateTransfer 異動時に再作成
	 * @param isRecreateTypeChangePerson 勤務種別変更時に再作成
	 * @return
	 */
	public List<DatePeriod> calPeriodApprovalResult(String companyId,String employeeId,GeneralDate startDate,boolean isRecreateTransfer,boolean isRecreateTypeChangePerson){
		GeneralDate maxDate = null;
		GeneralDate workInfoDataDateHighest = null;
		GeneralDate basicScheduleDateHighest = null;
		//ドメインモデル「日別実績の勤務情報」を取得する
		List<WorkInfoOfDailyPerformance> workInfoData =  workInformationRepo.findByEmployeeId(employeeId);
		if(!workInfoData.isEmpty()) {
			workInfoData = workInfoData.stream().sorted((x,y)->y.getYmd().compareTo(x.getYmd())).collect(Collectors.toList());
			//年月日が最も大きいレコードを取得する
			workInfoDataDateHighest =  workInfoData.get(0).getYmd();
		}
		
		//ドメインモデル「勤務予定基本情報」を取得する
		basicScheduleDateHighest = basicScheduleRepo.findMaxDateByListSid(Arrays.asList(employeeId));
		maxDate = getMaxDate(workInfoDataDateHighest, basicScheduleDateHighest);
		if(maxDate == null) {
			return Collections.emptyList();
		}
		//INPUT「開始日」と取得したデータの年月日から「対象期間」を求める
		DatePeriod newPeriod = new DatePeriod(startDate,maxDate);
		
		List<DatePeriod> listDatePeriodWorkplace = new ArrayList<>();
		List<DatePeriod> listDatePeriodWorktype = new ArrayList<>();
		List<DatePeriod> listDatePeriodAll = new ArrayList<>();
		
		//INPUT．「異動時に再作成」をチェックする
		if(isRecreateTransfer) {
			//社員ID（List）と期間から個人情報を取得する - RQ401	
			EmployeeGeneralInfoImport employeeGeneralInfoImport = employeeGeneralInfoAdapter.getEmployeeGeneralInfo(Arrays.asList(employeeId), newPeriod, false, false, false, true, false); //職場を取得するか　=　True
			if(!employeeGeneralInfoImport.getExWorkPlaceHistoryImports().isEmpty()) {
				nts.uk.ctx.at.function.dom.statement.dtoimport.ExWorkPlaceHistoryImport exWorkPlaceHistoryImportFn = employeeGeneralInfoImport.getExWorkPlaceHistoryImports().get(0);
				List<ExWorkplaceHistItemImport> workplaceItems = exWorkPlaceHistoryImportFn
						.getWorkplaceItems().stream()
						.map(c -> new ExWorkplaceHistItemImport(c.getHistoryId(), c.getPeriod(),
								c.getWorkplaceId()))
						.collect(Collectors.toList());
				List<ExWorkplaceHistItemImported> workplaceItems2 = exWorkPlaceHistoryImportFn
						.getWorkplaceItems().stream()
						.map(c -> new ExWorkplaceHistItemImported(c.getHistoryId(), c.getPeriod(),
								c.getWorkplaceId()))
						.collect(Collectors.toList());
				// 職場情報変更期間を求める
				listDatePeriodWorkplace = wkplaceInfoChangePeriod.getWkplaceInfoChangePeriod(employeeId, newPeriod,
						workplaceItems, isRecreateTransfer);
				// 職場・勤務種別変更期間を求める
				List<DatePeriod> dataWorkplace = requestPeriodChangeService.getPeriodChange(employeeId, newPeriod,workplaceItems2, Collections.emptyList(), true, isRecreateTransfer);
				listDatePeriodWorkplace.addAll(dataWorkplace);
			}
		}
		
		//INPUT．「勤務種別変更時に再作成」をチェックする
		if(isRecreateTypeChangePerson) {	
			//<<Public>> 社員ID(List)、期間で期間分の勤務種別情報を取得する
			List<BusinessTypeOfEmpDto> listBusinessTypeOfEmpDto = businessTypeOfEmpHisAdaptor.findByCidSidBaseDate(companyId, Arrays.asList(employeeId), newPeriod);
			//勤務種別情報変更期間を求める
			listDatePeriodWorktype = wkTypeInfoChangePeriod.getWkTypeInfoChangePeriod(employeeId, newPeriod, listBusinessTypeOfEmpDto, isRecreateTypeChangePerson);
			//職場・勤務種別変更期間を求める
			List<DatePeriod> dataWorkType = requestPeriodChangeService.getPeriodChange(employeeId, newPeriod, Collections.emptyList(),listBusinessTypeOfEmpDto,false, isRecreateTypeChangePerson);
			listDatePeriodWorktype.addAll(dataWorkType);
		}
		//求めた「期間」の重複しているきかんを取除く
		//取り除いた期間（List）を返す
		listDatePeriodAll.addAll(createListAllPeriod(listDatePeriodWorkplace,listDatePeriodWorktype));
		return listDatePeriodAll;
	}
	
	private GeneralDate getMaxDate(GeneralDate workInfoDataDateHighest,GeneralDate basicScheduleDateHighest ) {
		if(workInfoDataDateHighest != null && basicScheduleDateHighest != null ) {
			if(workInfoDataDateHighest.before(basicScheduleDateHighest)) {
				return workInfoDataDateHighest;
			}
			return basicScheduleDateHighest;
		}

		if(workInfoDataDateHighest !=null) {
			return workInfoDataDateHighest;
		} 

		return basicScheduleDateHighest;
	}
	
	private List<DatePeriod> createListAllPeriod(List<DatePeriod> list1,List<DatePeriod> list2){
		List<DatePeriod> listResult = new ArrayList<>();
		List<DatePeriod> listAll = new ArrayList<>();
		listAll.addAll(list1);
		listAll.addAll(list2);
		listAll.sort((x, y) -> x.start().compareTo(y.start()));
		
		for(int i = 0;i< listAll.size();i++) {
			DatePeriod merged = new DatePeriod(listAll.get(i).start(),listAll.get(i).end());
			for (int j = i + 1; j < listAll.size(); j++) {
				DatePeriod next = listAll.get(j);
				if (merged.contains(next.start()) && merged.contains(next.end())){
					i++;
				}else if(merged.contains(next.start())||merged.end().addDays(1).equals(next.start())) {
					merged = merged.cutOffWithNewEnd(next.end());
					i++;
				}else {
					break;
				}
			}
			listResult.add(merged);
		}
		return listResult;
	}
}
