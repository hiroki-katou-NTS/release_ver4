package nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.MngDataStatus;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.OccurrenceDigClass;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.DigestionAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.ManagementDataRemainUnit;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.AccumulationAbsenceDetail;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.AccumulationAbsenceDetail.AccuVacationBuilder;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.AccumulationAbsenceDetail.NumberConsecuVacation;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.UnbalanceVacation;
import nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.LeaveManagementData;

/**
 * @author ThanhNX
 *
 *         2.未使用の休出(確定)を取得する
 */
@Stateless
public class GetUnusedLeaveFixed {

	private GetUnusedLeaveFixed() {
	};

	public static List<AccumulationAbsenceDetail> getUnbalanceUnused(Require require, String companyId,
			String employeeId, GeneralDate startDateAggr) {
		// アルゴリズム「確定休出から未使用の休出を取得する」を実行する
		List<LeaveManagementData> lstUnBalPay = getUnbalancePayment(require, companyId, employeeId, startDateAggr);
		if (lstUnBalPay.isEmpty())
			return Collections.emptyList();

		// アルゴリズム「暫定代休と紐付けをしない確定休出を取得する」を実行する
		return lstUnBalPay.stream().map(x -> acquireTemporaryHoliday(x)).filter(x -> x.isPresent()).map(x -> x.get())
				.collect(Collectors.toList());
	}

	// 2-1.確定休出から未使用の休出を取得する
	public static List<LeaveManagementData> getUnbalancePayment(Require require, String companyId, String employeeId,
			GeneralDate startDateAggr) {
		List<LeaveManagementData> lstConfirmBreakData = require.getBySidYmd(companyId, employeeId, startDateAggr,
				DigestionAtr.UNUSED);
		if (lstConfirmBreakData.size() > 0) {
			return lstConfirmBreakData;
		}
		return Collections.emptyList();
	}

	// 2-3.暫定代休と紐付けをしない確定休出を取得する
	public static Optional<AccumulationAbsenceDetail> acquireTemporaryHoliday(LeaveManagementData leaveManagement) {
		double unUseDays = leaveManagement.getUnUsedDays().v();
		Integer unUseTimes = leaveManagement.getUnUsedTimes().v();
		// 未使用日数と未使用時間をチェックする
		if (unUseDays <= 0 && unUseTimes <= 0) {
			return Optional.empty();
		}

		AccumulationAbsenceDetail result = new AccuVacationBuilder(leaveManagement.getSID(),
				leaveManagement.getComDayOffDate(), OccurrenceDigClass.OCCURRENCE, MngDataStatus.CONFIRMED,
				leaveManagement.getID()).numberOccurren(
						new NumberConsecuVacation(new ManagementDataRemainUnit(leaveManagement.getOccurredDays().v()),
								Optional.of(new AttendanceTime(leaveManagement.getOccurredTimes().v()))))
						.unbalanceNumber(new NumberConsecuVacation(
								new ManagementDataRemainUnit(leaveManagement.getUnUsedDays().v()),
								Optional.of(new AttendanceTime(leaveManagement.getUnUsedTimes().v()))))
						.unbalanceVacation(new UnbalanceVacation(leaveManagement.getExpiredDate(),
								leaveManagement.getSubHDAtr(), leaveManagement.getDisapearDate(),
								leaveManagement.getFullDayTime(), leaveManagement.getHalfDayTime()))
						.build();
		return Optional.of(result);
	}

	public static interface Require {

		// LeaveManaDataRepository DigestionAtr.UNUSED
		List<LeaveManagementData> getBySidYmd(String cid, String sid, GeneralDate ymd, DigestionAtr state);

	}
}
