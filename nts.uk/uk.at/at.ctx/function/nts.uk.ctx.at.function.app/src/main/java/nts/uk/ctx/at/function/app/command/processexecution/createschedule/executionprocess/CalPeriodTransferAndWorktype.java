package nts.uk.ctx.at.function.app.command.processexecution.createschedule.executionprocess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.app.command.processexecution.ApprovalPeriodByEmp;
import nts.uk.ctx.at.function.dom.statement.EmployeeGeneralInfoAdapter;
import nts.uk.ctx.at.function.dom.statement.dtoimport.EmployeeGeneralInfoImport;
import nts.uk.ctx.at.schedule.dom.adapter.generalinfo.workplace.ExWorkplaceHistItemImported;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository;
import nts.uk.ctx.at.schedule.dom.schedule.schedulemaster.requestperiodchange.RequestPeriodChangeService;
import nts.uk.ctx.at.shared.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmpDto;
import nts.uk.ctx.at.shared.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmpHisAdaptor;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 異動者・勤務種別変更者の作成対象期間の計算（個人別）
 * 
 * @author tutk
 *
 */
@Stateless
public class CalPeriodTransferAndWorktype {

	@Inject
	private EmployeeGeneralInfoAdapter employeeGeneralInfoAdapter;

	@Inject
	private BusinessTypeOfEmpHisAdaptor businessTypeOfEmpHisAdaptor;

	@Inject
	private BasicScheduleRepository basicScheRepo;

	@Inject
	private RequestPeriodChangeService requestPeriodChangeService;

	/**
	 * @param companyId
	 *            会社ID
	 * @param listEmp
	 *            社員ID(List)
	 * @param period
	 *            期間
	 * @param isTransfer
	 *            異動時に再作成（TRUE、FALSE）
	 * @param isWorkType
	 *            勤務種別変更時に再作成（TRUE、FALSE）
	 * @return List<ApprovalPeriodByEmp> 承認結果の反映対象期間（List）
	 */
	public List<ApprovalPeriodByEmp> calPeriodTransferAndWorktype(String companyId, List<String> listEmp,
			DatePeriod period, boolean isTransfer, boolean isWorkType) {

		List<ApprovalPeriodByEmp> listApprovalPeriodByEmp = new ArrayList<>();
		List<ExWorkplaceHistItemImported> workplaceItems = new ArrayList<>();
		List<BusinessTypeOfEmpDto> listBusinessTypeOfEmpDto = new ArrayList<>();

		// INPUT．「異動時に再作成」をチェックする
		if (isTransfer) {
			// 社員ID（List）と期間から個人情報を取得する - RQ401
			EmployeeGeneralInfoImport employeeGeneralInfoImport = employeeGeneralInfoAdapter
					.getEmployeeGeneralInfo(listEmp, period, false, false, false, true, false); // 職場を取得するか = True
			if (!employeeGeneralInfoImport.getExWorkPlaceHistoryImports().isEmpty()) {
				nts.uk.ctx.at.function.dom.statement.dtoimport.ExWorkPlaceHistoryImport exWorkPlaceHistoryImportFn = employeeGeneralInfoImport
						.getExWorkPlaceHistoryImports().get(0);
				workplaceItems = exWorkPlaceHistoryImportFn.getWorkplaceItems().stream()
						.map(c -> new ExWorkplaceHistItemImported(c.getHistoryId(), c.getPeriod(), c.getWorkplaceId()))
						.collect(Collectors.toList());
			}
		}
		// INPUT．「勤務種別変更時に再作成」をチェックする
		if (isWorkType) {
			// <<Public>> 社員ID(List)、期間で期間分の勤務種別情報を取得する
			listBusinessTypeOfEmpDto = businessTypeOfEmpHisAdaptor.findByCidSidBaseDate(companyId, listEmp, period);
		}
		// INPUT．「社員ID(List)」をループする
		for (String empId : listEmp) {

			// ドメインモデル「勤務予定基本情報」を取得する - 年月日が最も大きいレコードを取得する
			GeneralDate basicScheduleDateHighest = basicScheRepo.findMaxDateByListSid(Arrays.asList(empId));

			// 「勤務予定基本情報」を取得できない場合次の社員へ
			if (basicScheduleDateHighest == null)
				continue;

			// INPUT「開始日」と取得したデータの年月日から「対象期間」を求める
			DatePeriod newPeriod = new DatePeriod(period.start(), basicScheduleDateHighest);

			// INPUT．「異動時に再作成」をチェックする
			List<DatePeriod> dataWorkplace = new ArrayList<>();
			if (isTransfer) {
				// 職場・勤務種別変更期間を求める
				dataWorkplace = requestPeriodChangeService.getPeriodChange(empId, newPeriod, workplaceItems,
						Collections.emptyList(), true, isTransfer);
			}
			List<DatePeriod> dataWorkType = new ArrayList<>();
			// INPUT．「勤務種別変更時に再作成」をチェックする
			if (isWorkType) {
				// 職場・勤務種別変更期間を求める
				dataWorkType = requestPeriodChangeService.getPeriodChange(empId, newPeriod, Collections.emptyList(),
						listBusinessTypeOfEmpDto, false, isWorkType);
			}
			// 計算するする「期間」がない場合次の社員へ
			if (dataWorkplace.isEmpty() && dataWorkType.isEmpty())
				continue;

			// 求めた「期間」の重複しているきかんを取除く
			// 取り除いた期間（List）を返す
			List<DatePeriod> listDatePeriodAll = new ArrayList<>();
			listDatePeriodAll.addAll(createListAllPeriod(dataWorkplace, dataWorkType));
			listApprovalPeriodByEmp.add(new ApprovalPeriodByEmp(empId, listDatePeriodAll));
		}
		// 「承認結果の反映対象期間（List）」を返す
		return listApprovalPeriodByEmp;
	}

	private List<DatePeriod> createListAllPeriod(List<DatePeriod> list1, List<DatePeriod> list2) {
		List<DatePeriod> listResult = new ArrayList<>();
		List<DatePeriod> listAll = new ArrayList<>();
		listAll.addAll(list1);
		listAll.addAll(list2);
		listAll.sort((x, y) -> x.start().compareTo(y.start()));

		for (int i = 0; i < listAll.size(); i++) {
			DatePeriod merged = new DatePeriod(listAll.get(i).start(), listAll.get(i).end());
			for (int j = i + 1; j < listAll.size(); j++) {
				DatePeriod next = listAll.get(j);
				if (merged.contains(next.start()) && merged.contains(next.end())) {
					i++;
				} else if (merged.contains(next.start()) || merged.end().addDays(1).equals(next.start())) {
					merged = merged.cutOffWithNewEnd(next.end());
					i++;
				} else {
					break;
				}
			}
			listResult.add(merged);
		}
		return listResult;
	}
}
