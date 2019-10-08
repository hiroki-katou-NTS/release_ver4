package nts.uk.ctx.at.record.pubimp.workrecord.remainingnumbermanagement;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.pub.workrecord.remainingnumbermanagement.AnnualHolidayManagementPub;
import nts.uk.ctx.at.record.pub.workrecord.remainingnumbermanagement.AttendRateAtNextHolidayExport;
import nts.uk.ctx.at.record.pub.workrecord.remainingnumbermanagement.NextAnnualLeaveGrantExport;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmpEmployeeAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmployeeImport;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnLeaEmpBasicInfoRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnualLeaveEmpBasicInfo;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.GetClosureStartForEmployee;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.GrantHdTblSet;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.LengthServiceRepository;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.YearHolidayRepository;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.export.GetNextAnnualLeaveGrant;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class AnnualHolidayManagementPubImpl implements AnnualHolidayManagementPub {
	@Inject
	AnnLeaEmpBasicInfoRepository annLeaEmpBasicInfoRepository;
	
	@Inject
	YearHolidayRepository yearHolidayRepository;
	
	@Inject
	GetNextAnnualLeaveGrant nextAnnualLeaveGrant;
	
	@Inject
	LengthServiceRepository lengthServiceRepository;
	
	@Inject
	EmpEmployeeAdapter empEmployeeAdapter;
	
	@Inject
	private GetClosureStartForEmployee getClosureStartForEmployee;
	
	/**
	 * RequestList210
	 * 次回年休付与日を取得する
	 * 
	 * @param cId
	 * @param sId
	 * @return
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<NextAnnualLeaveGrantExport> acquireNextHolidayGrantDate(String companyId, String employeeId, Optional<GeneralDate> referenceDate) {
		// ドメインモデル「年休社員基本情報」を取得
		Optional<AnnualLeaveEmpBasicInfo> annualLeaveEmpBasicInfo = annLeaEmpBasicInfoRepository.get(employeeId);
		
		if(!annualLeaveEmpBasicInfo.isPresent()) {
			return Collections.emptyList();
		}
		
		// 次回年休付与の計算範囲を作成
		Optional<DatePeriod> period = createCalRangeNextYearHdGrant(referenceDate);
		
		// 次回年休付与を計算
		List<NextAnnualLeaveGrantExport> result = calculateNextHolidayGrant(companyId, employeeId, period, annualLeaveEmpBasicInfo);
		
		// 次回年休付与を返す
		return result;
	}
	
	/**
	 * 次回年休付与の計算範囲を作成
	 * 
	 * @param referenceDate
	 * @return
	 */
	private Optional<DatePeriod> createCalRangeNextYearHdGrant(Optional<GeneralDate> referenceDate) {
		Optional<DatePeriod> result = Optional.empty();
		
		// パラメータ「基準日」が存在するかチェック
		if(referenceDate.isPresent()) {	
			// 基準日から1年間の期間を返す
			result = Optional.of(new DatePeriod(referenceDate.get(), referenceDate.get().addYears(1))) ;
		}
		
		return result;
	}

	/**
	 * 次回年休付与を計算
	 * 
	 * @param companyId
	 * @param employeeId
	 * @param period
	 * @param annualLeaveEmpBasicInfo
	 * @return
	 */
	private List<NextAnnualLeaveGrantExport> calculateNextHolidayGrant(String companyId, String employeeId, 
			Optional<DatePeriod> period, Optional<AnnualLeaveEmpBasicInfo> annualLeaveEmpBasicInfo) {
		boolean isSingleDay = false;
		Optional<DatePeriod> periodDate = period;
		
		// Imported(就業)「社員」を取得する
		EmployeeImport employee = empEmployeeAdapter.findByEmpId(employeeId);
		
		// パラメータ「期間」をチェック
		if(!period.isPresent()) {
			isSingleDay = true;
			
			// 社員に対応する締め開始日を取得する
			Optional<GeneralDate> closureStartDate = getClosureStartForEmployee.algorithm(employeeId);
			
			periodDate = Optional.ofNullable(new DatePeriod(GeneralDate.ymd(closureStartDate.get().year(), 
					closureStartDate.get().month(), closureStartDate.get().day()), GeneralDate.ymd(9999, 12, 31)));
		}
		
		// ドメインモデル「年休付与テーブル設定」を取得する
		Optional<GrantHdTblSet> grantHdTblSet = yearHolidayRepository.findByCode(companyId, annualLeaveEmpBasicInfo.get().getGrantRule().getGrantTableCode().v());
		
		if(!grantHdTblSet.isPresent()) {
			return Collections.emptyList();
		}
		
		// 次回年休付与を取得する
		List<NextAnnualLeaveGrantExport> nextAnnualLeaveGrantData = nextAnnualLeaveGrant.algorithm(companyId, grantHdTblSet.get().getYearHolidayCode().v(), 
				employee.getEntryDate(), annualLeaveEmpBasicInfo.get().getGrantRule().getGrantStandardDate(), periodDate.get(), isSingleDay)
				.stream().map(x -> new NextAnnualLeaveGrantExport(
						x.getGrantDate(), 
						x.getGrantDays(),
						x.getTimes(),
						x.getTimeAnnualLeaveMaxDays(),
						x.getTimeAnnualLeaveMaxTime(),
						x.getHalfDayAnnualLeaveMaxTimes()))
				.collect(Collectors.toList());
		
		// List<次回年休付与>を返す
		return nextAnnualLeaveGrantData;
	}
	
	/**
	 * RequestList323
	 * 次回年休付与時点の出勤率・出勤日数・所定日数・年間所定日数を取得する
	 * 
	 * @param companyId
	 * @param employeeId
	 * @return
	 */

	@Override
	public Optional<AttendRateAtNextHolidayExport> getDaysPerYear(String companyId, String employeeId) {
		// ドメインモデル「年休社員基本情報」を取得
		Optional<AnnualLeaveEmpBasicInfo> annualLeaveEmpBasicInfo = annLeaEmpBasicInfoRepository.get(employeeId);
		if(!annualLeaveEmpBasicInfo.isPresent()){
			return Optional.empty();
		}
		// 次回年休付与を計算
//		List<NextAnnualLeaveGrantExport> result = calculateNextHolidayGrant(companyId, employeeId, Optional.empty(), annualLeaveEmpBasicInfo);
		return Optional.empty();
	}
	
	
	
}
