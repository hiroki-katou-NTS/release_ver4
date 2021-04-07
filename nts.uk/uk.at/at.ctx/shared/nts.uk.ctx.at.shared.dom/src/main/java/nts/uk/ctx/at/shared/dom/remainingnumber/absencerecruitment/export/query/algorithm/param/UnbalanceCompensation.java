package nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.algorithm.param;

import java.util.Optional;

import lombok.Getter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.DigestionAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.HolidayAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.AccumulationAbsenceDetail;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.LeaveOccurrDetail;

/**
 * @author ThanhNX
 *
 *         振出の未相殺
 */
public class UnbalanceCompensation extends LeaveOccurrDetail {

	/**
	 * 法定内外区分
	 */
	@Getter
	private HolidayAtr legalInExClassi;

	public UnbalanceCompensation(AccumulationAbsenceDetail detail, GeneralDate deadline, DigestionAtr digestionCate, Optional<GeneralDate> extinctionDate,
			HolidayAtr legalInExClassi) {
		super(detail, deadline, digestionCate, extinctionDate);
		this.legalInExClassi = legalInExClassi;
	}

}
