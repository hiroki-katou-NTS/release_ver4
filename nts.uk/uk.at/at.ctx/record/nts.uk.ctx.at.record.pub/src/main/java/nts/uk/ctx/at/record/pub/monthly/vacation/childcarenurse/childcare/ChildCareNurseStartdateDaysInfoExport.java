package nts.uk.ctx.at.record.pub.monthly.vacation.childcarenurse.childcare;

import lombok.Data;
import nts.uk.ctx.at.record.dom.remainingnumber.childcarenurse.childcare.ChildCareNurseRemainingNumber;
import nts.uk.ctx.at.record.dom.remainingnumber.childcarenurse.childcare.ChildCareNurseStartdateDaysInfo;
import nts.uk.ctx.at.record.dom.remainingnumber.childcarenurse.childcare.ChildCareNurseStartdateInfo;
import nts.uk.ctx.at.record.dom.remainingnumber.childcarenurse.childcare.ChildCareNurseUpperLimit;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.usenumber.DayNumberOfUse;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.usenumber.TimeOfUse;

import java.util.Optional;


/**
 * 起算日からの休暇情報
 * @author yuri_tamakoshi
 */
@Data
public class ChildCareNurseStartdateDaysInfoExport {
	/** 子の看護休暇情報（本年）*/
	private ChildCareNurseStartdateInfoExport thisYear;
	/** 子の看護休暇情報（翌年）*/
	private Optional<ChildCareNurseStartdateInfoExport> nextYear;

	/**
	 * コンストラクタ　ChildCareNurseStartdateDaysInfoExport
	 */
	public ChildCareNurseStartdateDaysInfoExport(){
		this.thisYear = new ChildCareNurseStartdateInfoExport();
		this.nextYear = Optional.empty();
	}

	/**
	 * ファクトリー
	 * @param thisYear 子の看護休暇情報（本年）
	 * @param nextYear 子の看護休暇情報（翌年）
	 * @return 起算日からの休暇情報
	 */
	public static ChildCareNurseStartdateDaysInfoExport of(
			ChildCareNurseStartdateInfoExport thisYear,
			Optional <ChildCareNurseStartdateInfoExport> nextYear){

		ChildCareNurseStartdateDaysInfoExport domain = new ChildCareNurseStartdateDaysInfoExport();
		domain.thisYear = thisYear;
		domain.nextYear = nextYear;
		return domain;
	}

	public ChildCareNurseStartdateDaysInfo toDomain() {
		return ChildCareNurseStartdateDaysInfo.of(
				ChildCareNurseStartdateInfo.of(
						thisYear.getUsedDays(),
						ChildCareNurseRemainingNumber.of(
								new DayNumberOfUse(thisYear.getRemainingNumber().getUsedDays()),
								thisYear.getRemainingNumber().getUsedTime().map(i -> new TimeOfUse(i))
						),
						new ChildCareNurseUpperLimit(thisYear.getLimitDays())
				),
				nextYear.map(i -> ChildCareNurseStartdateInfo.of(
						i.getUsedDays(),
						ChildCareNurseRemainingNumber.of(
								new DayNumberOfUse(i.getRemainingNumber().getUsedDays()),
								i.getRemainingNumber().getUsedTime().map(j -> new TimeOfUse(j))
						),
						new ChildCareNurseUpperLimit(i.getLimitDays())
				))
		);
	}
}
