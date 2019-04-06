package nts.uk.ctx.at.record.dom.workrecord.actualsituation.checktrackrecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.ApprovalStatusAdapter;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApprovalRootOfEmployeeImport;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalProcessingUseSetting;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.finddata.IFindDataDCRecord;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.TimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.TimeOfMonthlyRepository;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.approvalstatusmonthly.ApprovalStatusMonthly;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.approvalstatusmonthly.ApprovalStatusResult;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.confirmstatusmonthly.AvailabilityAtr;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 承認すべき月の実績があるかチェックする [RQ594]
 * 
 * @author tutk
 *
 */

@Stateless
public class CheckTrackRecord {

	@Inject
	private IFindDataDCRecord iFindDataDCRecord;

	@Inject
	private ApprovalStatusAdapter approvalStatusAdapter;

	@Inject
	private TimeOfMonthlyRepository timeRepo;

	@Inject
	private ClosureRepository closureRepository;

	@Inject
	private ApprovalStatusMonthly approvalStatusMonthly;

	public boolean checkTrackRecord(String companyId, String employeeId,
			List<CheckTargetItemDto> listCheckTargetItemExport) {
		String loginId = AppContexts.user().employeeId();
		iFindDataDCRecord.clearAllStateless();
		// ドメインモデル「承認処理の利用設定」を取得する
		Optional<ApprovalProcessingUseSetting> optApprovalUse = iFindDataDCRecord.findApprovalByCompanyId(companyId);

		if (!optApprovalUse.isPresent()) {
			return false;
		}
		// 取得した「承認処理の利用設定．月の承認者確認を利用する」をチェックする
		if (!optApprovalUse.get().getUseMonthApproverConfirm()) {
			return false;
		}
		List<DatePeriod> listDatePeriod = new ArrayList<>();
		for (CheckTargetItemDto checkTargetItemExport : listCheckTargetItemExport) {
			Optional<Closure> closure = closureRepository.findById(companyId, checkTargetItemExport.getClosureId());
			if (closure.isPresent()) {
				listDatePeriod.addAll(closure.get().getPeriodByYearMonth(checkTargetItemExport.getYearMonth()));
			}
		}
		if (listDatePeriod.isEmpty())
			return false;
		GeneralDate minDate = listDatePeriod.get(0).start();
		GeneralDate maxDate = listDatePeriod.get(0).end();
		for (DatePeriod datePeriod : listDatePeriod) {
			if (datePeriod.start().before(minDate)) {
				minDate = datePeriod.start();
			}
			if (datePeriod.end().after(maxDate)) {
				maxDate = datePeriod.end();
			}
		}
		DatePeriod periord = new DatePeriod(minDate, maxDate);
		// 対応するImported「基準社員の承認対象者」を取得する RQ643
		ApprovalRootOfEmployeeImport approvalRootOfEmployeeImport = approvalStatusAdapter
				.getApprovalRootOfEmloyeeNew(periord.start(), periord.end(), loginId, companyId, 2); // 2 : 月別確認
		if(approvalRootOfEmployeeImport == null) 
			return false;
		// 対応する月別実績を取得する
		List<TimeOfMonthly> timeOfMonthly = timeRepo.findBySidsAndYearMonths(
				approvalRootOfEmployeeImport.getApprovalRootSituations().stream()
						.map(c -> c.getTargetID()).collect(Collectors.toList()),
				listCheckTargetItemExport.stream().map(c -> c.getYearMonth()).collect(Collectors.toList()));
		List<AttendanceTimeOfMonthly> listAttendanceTimeOfMonthly = timeOfMonthly.stream()
				.filter(x -> x.getAttendanceTime().isPresent()).map(c -> c.getAttendanceTime().get())
				.collect(Collectors.toList());

		for (CheckTargetItemDto checkTargetItemExport : listCheckTargetItemExport) {
			List<AttendanceTimeOfMonthly> listCheckTarget = listAttendanceTimeOfMonthly.stream()
					.filter(c -> c.getClosureId().value == checkTargetItemExport.getClosureId())
					.collect(Collectors.toList());
			for (AttendanceTimeOfMonthly attendanceTimeOfMonthly : listCheckTarget) {
				// 処理中の社員の「月別実績」が取得できているかチェックする
				Optional<ApprovalStatusResult> approvalStatusResult = approvalStatusMonthly.getApprovalStatus(companyId,
						employeeId, attendanceTimeOfMonthly.getEmployeeId(), attendanceTimeOfMonthly.getYearMonth(),
						attendanceTimeOfMonthly.getClosureId().value, attendanceTimeOfMonthly.getClosureDate(),
						attendanceTimeOfMonthly.getDatePeriod());
				if (approvalStatusResult.isPresent()) {
					if(approvalStatusResult.get().getImplementaPropriety() == AvailabilityAtr.CAN_RELEASE) {
						return true;
					}
				}		
			}
		}

		return false;
	}
}
