package nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange;

import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.ManagementDataRemainUnit;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.TargetSelectionAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.SeqVacationAssociationInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.empinfo.grantremainingdata.daynumber.ReserveLeaveRemainingDayNumber;

/**
 * @author ThanhNX
 *
 *         紐づけ登録処理
 */
public class TypeRegistrationProcess {

	private TypeRegistrationProcess() {
	};

	// 紐づけ登録処理
	public static Optional<SeqVacationAssociationInfo> process(GeneralDate occurDate, GeneralDate accdigestDate,
			ManagementDataRemainUnit unOffsetDay, TypeOffsetJudgment typeJudgment) {
		// LeaveManagementData
		// 時間代休管理区分をチェック
		return Optional.of(createVacationInfo(occurDate, accdigestDate, unOffsetDay));
	}

	private static SeqVacationAssociationInfo createVacationInfo(GeneralDate occurDate, GeneralDate accdigestDate,
			ManagementDataRemainUnit unOffsetDay) {

		return new SeqVacationAssociationInfo(occurDate, accdigestDate,
				new ReserveLeaveRemainingDayNumber(unOffsetDay.v()), TargetSelectionAtr.AUTOMATIC);
	}
}
