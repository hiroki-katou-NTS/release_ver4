/**
 * 8:56:53 AM Mar 22, 2018
 */
package nts.uk.ctx.at.record.dom.approvalmanagement.domainservice;

import java.util.ArrayList;
//import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.adapter.company.AffComHistItemImport;
import nts.uk.ctx.at.record.dom.adapter.company.AffCompanyHistImport;
import nts.uk.ctx.at.record.dom.adapter.company.SyCompanyRecordAdapter;
import nts.uk.ctx.at.record.dom.adapter.employee.EmployeeAdapter;
import nts.uk.ctx.at.record.dom.adapter.employee.EmployeeDto;
import nts.uk.ctx.at.record.dom.adapter.employee.RegularSortingTypeImport;
import nts.uk.ctx.at.record.dom.adapter.employee.RegulationInfoEmployeeAdapter;
import nts.uk.ctx.at.record.dom.adapter.employee.SortingConditionOrderImport;
import nts.uk.ctx.at.record.dom.adapter.employment.EmploymentHistAdapter;
import nts.uk.ctx.at.record.dom.adapter.employment.EmploymentHistImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.ApprovalStatusAdapter;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApprovalRootOfEmployeeImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApprovalRootSituation;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApprovalStatus;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApprovalActionByEmpl;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApproverEmployeeState;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalProcessingUseSetting;
import nts.uk.ctx.at.record.dom.approvalmanagement.dtos.ApprovalEmployeeDto;
import nts.uk.ctx.at.record.dom.approvalmanagement.dtos.ClosureDto;
import nts.uk.ctx.at.record.dom.approvalmanagement.dtos.DateApprovalStatusDto;
import nts.uk.ctx.at.record.dom.approvalmanagement.dtos.OneMonthApprovalStatusDto;
import nts.uk.ctx.at.record.dom.approvalmanagement.repository.ApprovalProcessingUseSettingRepository;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.Identification;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentificationRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureHistory;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * @author hungnm
 *
 */
@Stateless
public class OneMonthApprovalSttDomainServiceImpl implements OneMonthApprovalSttDomainService {

	@Inject
	private ApprovalProcessingUseSettingRepository approvalProcessingUseSettingRepository;

	@Inject
	private ClosureRepository closureRepository;

	@Inject
	private ClosureService closureService;

	@Inject
	private ApprovalStatusAdapter approvalStatusAdapter;

	@Inject
	private ClosureEmploymentRepository closureEmploymentRepository;

	@Inject
	private RegulationInfoEmployeeAdapter regulationInfoEmployeeAdapter;
	
	@Inject
	private SyCompanyRecordAdapter syCompanyRecordAdapter;
	
//	@Inject
//	private ShareEmploymentAdapter shareEmploymentAdapter;
	
	@Inject
	private EmployeeAdapter atEmployeeAdapter;
	
	@Inject
	private EmploymentHistAdapter employmentHistAdapter;

	private List<ApprovalEmployeeDto> buildApprovalEmployeeData(List<Identification> listIdentification,List<EmployeeDto> lstEmployee,
			ApprovalRootOfEmployeeImport approvalRootOfEmployeeImport) {

		List<ApprovalEmployeeDto> lstApprovalEmployee = new ArrayList<>();
		List<ApprovalRootSituation> lstApproval = approvalRootOfEmployeeImport.getApprovalRootSituations();

		for (EmployeeDto empQ : lstEmployee) {
			ApprovalEmployeeDto approvalEmployee = new ApprovalEmployeeDto();

			approvalEmployee.setEmployeeId(empQ.getSid());
			approvalEmployee.setEmployeeCode(empQ.getScd());
			approvalEmployee.setEmployeeName(empQ.getBussinessName());
			
			
			List<DateApprovalStatusDto> lstDateApprovalStatusDto = lstApproval.stream()
					.filter(f -> empQ.getSid().equals(f.getTargetID())).map(apv -> {
						ApprovalStatus status = apv.getApprovalStatus();
						ApproverEmployeeState state = apv.getApprovalAtr();
						ApprovalActionByEmpl aproval = status.getApprovalActionByEmpl();

						DateApprovalStatusDto dateApvS = new DateApprovalStatusDto();

						dateApvS.setStatus(3);
						dateApvS.setDate(apv.getAppDate());
						boolean check = false;
						for(Identification identification : listIdentification) {
							if(apv.getAppDate().equals(identification.getProcessingYmd()) && apv.getTargetID().equals(identification.getEmployeeId()) ) {
								check = true;
								break;
							}
						}
						if(check) {
							if (ApproverEmployeeState.COMPLETE.equals(state) 
									|| ApproverEmployeeState.PHASE_PASS.equals(state)){
								dateApvS.setStatus(0);
							}else if(ApproverEmployeeState.PHASE_DURING.equals(state)) {
								if(ApprovalActionByEmpl.APPROVALED.equals(aproval)) {
									dateApvS.setStatus(0);
								}else if(ApprovalActionByEmpl.APPROVAL_REQUIRE.equals(aproval) ) {
									dateApvS.setStatus(1);
								}else {
									dateApvS.setStatus(2);
								}
								
							}else if(ApproverEmployeeState.PHASE_LESS.equals(state)) {
								dateApvS.setStatus(2);
							}
						}else {
							dateApvS.setStatus(4);
						}
						
						return dateApvS;
					}).collect(Collectors.toList());

			if (!lstDateApprovalStatusDto.isEmpty()) {
				approvalEmployee.setLstStatus(lstDateApprovalStatusDto);
				lstApprovalEmployee.add(approvalEmployee);
			}
		}

		return lstApprovalEmployee;
	}

	public OneMonthApprovalStatusDto getDatePeriod(int closureId) {
		OneMonthApprovalStatusDto result = new OneMonthApprovalStatusDto();
		YearMonth currentYearMonth = GeneralDate.today().yearMonth();
		DatePeriod datePeriod = closureService.getClosurePeriod(closureId, currentYearMonth);
		result.setStartDate(datePeriod.start());
		result.setEndDate(datePeriod.end());
		return result;
	}

	public OneMonthApprovalStatusDto getDatePeriod(int closureId, int currentYearMonth) {
		OneMonthApprovalStatusDto result = new OneMonthApprovalStatusDto();
		DatePeriod datePeriod = closureService.getClosurePeriod(closureId, YearMonth.of(currentYearMonth));
		result.setStartDate(datePeriod.start());
		result.setEndDate(datePeriod.end());
		return result;
	}

	public OneMonthApprovalStatusDto getOneMonthApprovalStatus(Integer closureIdParam, GeneralDate startDateParam,
			GeneralDate endDateParam) {
		YearMonth currentYearMonth = GeneralDate.today().yearMonth();
		List<Identification> listIdentification = new ArrayList<>();
		OneMonthApprovalStatusDto oneMonthApprovalStatusDto = new OneMonthApprovalStatusDto();
		// 対応するドメインモデル「承認処理の利用設定」を取得する
		Optional<ApprovalProcessingUseSetting> approvalProcUseSet = approvalProcessingUseSettingRepository
				.findByCompanyId(AppContexts.user().companyId());
		// 取得したドメインモデル「承認処理の利用設定．日の承認者確認を利用する」をチェックする
		boolean useDayApprovalComfirmCheck = false;
		if (approvalProcUseSet.isPresent()) {
			useDayApprovalComfirmCheck = approvalProcUseSet.get().getUseDayApproverConfirm();
		}
		
		if (useDayApprovalComfirmCheck) {
			List<YearMonth> lstYearMonth = new ArrayList<>();
			int currentClosure = 0;
			if (closureIdParam == null) {
				// ドメインモデル「締め」をすべて取得する
				List<Closure> lstClosure = closureRepository.findAllUse(AppContexts.user().companyId());
				// アルゴリズム「当月の名前を取得する」を実行する
				List<ClosureHistory> lstClosureHst = closureRepository
						.findByCurrentMonth(AppContexts.user().companyId(), currentYearMonth);
				// convert to closure dtos
				List<ClosureDto> lstClosureDto = new ArrayList<>();
				for (int c = 0; c < lstClosure.size(); c++) {
					for (int h = 0; h < lstClosureHst.size(); h++) {
						if (lstClosure.get(c).getClosureId().value == lstClosureHst.get(h).getClosureId().value) {
							lstClosureDto.add(new ClosureDto(lstClosureHst.get(h).getClosureId().value,
									lstClosureHst.get(h).getClosureName().v(),
									lstClosure.get(c).getClosureMonth().getProcessingYm().v().intValue()));
							lstYearMonth.add(lstClosure.get(c).getClosureMonth().getProcessingYm());
						}
					}
				}
				oneMonthApprovalStatusDto.setLstClosure(lstClosureDto);
				// 取得したドメインモデル「締め」の先頭を選択状態にする
				currentClosure = lstClosureDto.get(0).closureId;
			} else {
				currentClosure = closureIdParam;
			}
			DatePeriod datePeriod = null;
			if (startDateParam == null) {
				// アルゴリズム「当月の期間を算出する」を実行する
				datePeriod = closureService.getClosurePeriod(currentClosure,
						lstYearMonth.isEmpty() ? currentYearMonth : lstYearMonth.get(0));
			} else {
				datePeriod = new DatePeriod(startDateParam, endDateParam);
			}
			oneMonthApprovalStatusDto.setStartDate(datePeriod.start());
			oneMonthApprovalStatusDto.setEndDate(datePeriod.end());
			// Imported「（就業．勤務実績）基準社員の承認対象者」をすべて取得する
			// Imported（就業）「基準社員の承認対象者」を取得する
			ApprovalRootOfEmployeeImport approvalRootOfEmployeeImport = approvalStatusAdapter.getApprovalRootOfEmloyee(
					datePeriod.start(), datePeriod.end(), AppContexts.user().employeeId(),
					AppContexts.user().companyId(), 1);
			
			List<ApprovalRootSituation> approvalRootSituations = new ArrayList<>();
			List<String> lstEmploymentCd = new ArrayList<>();
			// fix bug 91363
			List<String> lstEmployees= new ArrayList<>();
			if (approvalRootOfEmployeeImport == null
					|| approvalRootOfEmployeeImport.getApprovalRootSituations().size() == 0) {
				//oneMonthApprovalStatusDto.setMessageID("Msg_874");
				//エラーメッセージ（Msg_916）を表示する
				oneMonthApprovalStatusDto.setMessageID("Msg_916");
				return oneMonthApprovalStatusDto;
				//throw new BusinessException("Msg_874");
			} else {
				List<ApprovalRootSituation> listApp =approvalRootOfEmployeeImport.getApprovalRootSituations();
				Set<String> listAppId = approvalRootOfEmployeeImport.getApprovalRootSituations().stream().map(c->c.getTargetID()).collect(Collectors.toSet());
				//対象期間に在職しているかチェックする
				//社員ID（List）と指定期間から所属会社履歴項目を取得 【Request：No211】
				List<AffCompanyHistImport> listAffCompanyHistImport  = this.syCompanyRecordAdapter
				.getAffCompanyHistByEmployee(new ArrayList<>(listAppId), datePeriod);
				if(listAffCompanyHistImport.isEmpty() || listAffCompanyHistImport.stream().flatMap(x -> x.getLstAffComHistItem().stream()).collect(Collectors.toList()).isEmpty()) {
					oneMonthApprovalStatusDto.setMessageID("Msg_875");
					return oneMonthApprovalStatusDto;
				}
				//日の本人確認を取得する
				listIdentification = this.getIdentification(new ArrayList<>(listAppId), datePeriod);
				//取得した「所属会社履歴項目」に当てはまらない対象者、対象日の「ルート状況」を取り除く
				for(String approvalId  : listAppId) {
					//loop find approvalID
					for(AffCompanyHistImport affCompanyHistImport : listAffCompanyHistImport ) {
						
						if(approvalId.equals(affCompanyHistImport.getEmployeeId())) {
							List<GeneralDate> listDate = new ArrayList<>();
							//loop list period 
							List<AffComHistItemImport> listAffComHistItemImport = affCompanyHistImport.getLstAffComHistItem();
							for(AffComHistItemImport  affComHistItem : listAffComHistItemImport) {
								GeneralDate startDate = affComHistItem.getDatePeriod().start();
								GeneralDate endDate = affComHistItem.getDatePeriod().end();
								if(startDate.after(datePeriod.end()) || endDate.before(datePeriod.start())) {
									break;
								}
								GeneralDate dateS = datePeriod.start();
								GeneralDate dateE = datePeriod.end();
								if(startDate.afterOrEquals(datePeriod.start())) {
									dateS = startDate;
								}
								if(endDate.beforeOrEquals(datePeriod.end())) {
									dateE = endDate;
								}
								
								for(GeneralDate date = dateS;date.beforeOrEquals(dateE);) {
									listDate.add(date);
									date = date.addDays(1);
								}
							}
							//get item by date and emp id
							for(ApprovalRootSituation approval :listApp) {
								for(GeneralDate date : listDate) {
									if(approval.getTargetID().equals(approvalId) && approval.getAppDate().equals(date)) {
										approvalRootSituations.add(approval);
									}
								}
							}
							break;
						}
					}
				}
			}
			
			approvalRootOfEmployeeImport.setApprovalRootSituations(approvalRootSituations);
			
			//クエリ「社員を並び替える(任意)」を実行する (Sort employee)
			String companyId = AppContexts.user().companyId();
			List<String> employeeList = approvalRootOfEmployeeImport.getApprovalRootSituations().stream().map(item->{
				return item.getTargetID();
			}).collect(Collectors.toList());
			// list order conditions
			lstEmployees = this.regulationInfoEmployeeAdapter.sortEmployees(companyId, employeeList,
					this.createListConditions(), this.convertFromDateToDateTime(datePeriod.end()));
			
			// アルゴリズム「表示する承認者の集計」を実行する
			// 対応するImported「（就業）社員」をすべて取得する -requestList31-2
//			Map<String, String> mapEmp = this.shareEmploymentAdapter.findEmpHistoryVer2(companyId, lstEmployees, datePeriod.end())
//					.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getEmploymentCode()));
			// アルゴリズム「社員をしぼり込む」を実行する
			// ドメインモデル「雇用に紐づく就業締め」を取得する
			List<ClosureEmployment> lstClosureEmployment = closureEmploymentRepository
					.findByClosureId(AppContexts.user().companyId(), currentClosure);
			lstEmploymentCd.addAll(lstClosureEmployment.stream().map(closureEmployment -> {
				return closureEmployment.getEmploymentCD();
			}).collect(Collectors.toList()));
			
			// 対応するすべての社員を取得する
//			List<String> listSid = mapEmp.entrySet().stream().filter(x -> lstEmploymentCd.contains(x.getValue()))
//					.map(x -> {
//						return x.getKey();
//					}).collect(Collectors.toList());
			
			//対象期間に対象の締めに紐付いた雇用に属しているかチェックする
			List<EmploymentHistImport> lstEmpHist = employmentHistAdapter.findBySidDatePeriod(lstEmployees, datePeriod)
					.stream().filter(x -> lstEmploymentCd.contains(x.getEmploymentCode())).collect(Collectors.toList());
			ApprovalRootOfEmployeeImport approvalRootOfEmployeeImportTemp = new ApprovalRootOfEmployeeImport(approvalRootOfEmployeeImport.getEmployeeStandard(), approvalRootSituations);
			List<ApprovalRootSituation> approvalRootSituationsTemp = new ArrayList<>();
			approvalRootOfEmployeeImport.getApprovalRootSituations().stream().forEach(x -> {
				val lstFilter = lstEmpHist.stream().filter(y -> {
					return y.getEmployeeId().equals(x.getTargetID())
							&& x.getAppDate().afterOrEquals(y.getPeriod().start())
							&& x.getAppDate().beforeOrEquals(y.getPeriod().end());
				}).collect(Collectors.toList());
				if(!lstFilter.isEmpty()) {
					approvalRootSituationsTemp.add(x);
				}
			});
			approvalRootOfEmployeeImportTemp.setApprovalRootSituations(approvalRootSituationsTemp);
			//  
			// RequestList228
			List<EmployeeDto> listEmployeeInfo = atEmployeeAdapter.getByListSID(lstEmployees);
			
			List<ApprovalEmployeeDto> buildApprovalEmployeeData = buildApprovalEmployeeData(listIdentification,listEmployeeInfo,
					approvalRootOfEmployeeImportTemp);
			if (buildApprovalEmployeeData.isEmpty()) {
				oneMonthApprovalStatusDto.setMessageID("Msg_875");
				return oneMonthApprovalStatusDto;
				//throw new BusinessException("Msg_875");
			}
			// fix bug 91363
			List<ApprovalEmployeeDto> buildApprovalEmployeeDataResult = new ArrayList<>();
			lstEmployees.forEach(item -> {
				if(buildApprovalEmployeeData.stream().filter(o -> o.getEmployeeId().equals(item)).findFirst().isPresent()){
				buildApprovalEmployeeDataResult.add(buildApprovalEmployeeData.stream().filter(o -> o.getEmployeeId().equals(item)).findFirst().get());
				}
			});
			oneMonthApprovalStatusDto.setLstEmployee(buildApprovalEmployeeDataResult);
			
		} else {
			throw new BusinessException("Msg_873");
		}
		return oneMonthApprovalStatusDto;
	}
	
	@Inject
	private IdentificationRepository identificationRepo; 
	
	/**日の本人確認を取得する*/
	public List<Identification> getIdentification(List<String> employeeId,DatePeriod datePeriod) {
		List<Identification> listIdentificationRepo = identificationRepo.findByListEmployeeID(employeeId, datePeriod.start(), datePeriod.end());
		return listIdentificationRepo;
	}
	
	//並び替え条件　=<職場(inlevel)、1>、<分類コード(ASC)、2>、<職位(序列)、3>、<社員コード(ASC)、4>
	private List<SortingConditionOrderImport> createListConditions()
	{
		List<SortingConditionOrderImport> lstCondition = new ArrayList<>();
		lstCondition.add(new SortingConditionOrderImport(1,RegularSortingTypeImport.WORKPLACE));
		lstCondition.add(new SortingConditionOrderImport(2,RegularSortingTypeImport.CLASSIFICATION));
		lstCondition.add(new SortingConditionOrderImport(3,RegularSortingTypeImport.POSITION));
		//fix bug 101289
		//lstCondition.add(new SortingConditionOrderImport(4,RegularSortingTypeImport.EMPLOYMENT));
		return lstCondition;
	}
	
	//convert from Date to DateTime
	private GeneralDateTime convertFromDateToDateTime(GeneralDate date) {
		return GeneralDateTime.ymdhms(date.year(), date.month(), date.day(), 0, 0, 0);
	}
}
