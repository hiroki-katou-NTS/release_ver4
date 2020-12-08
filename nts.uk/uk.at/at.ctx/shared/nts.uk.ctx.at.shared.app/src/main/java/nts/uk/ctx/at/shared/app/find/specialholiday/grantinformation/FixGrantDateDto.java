package nts.uk.ctx.at.shared.app.find.specialholiday.grantinformation;

import lombok.AllArgsConstructor;
import lombok.Value;
import nts.uk.ctx.at.shared.dom.specialholiday.grantinformation.FixGrantDate;

@AllArgsConstructor
@Value
public class FixGrantDateDto {
	/** 周期*/
	private int interval;

	/** 固定付与日数 */
	private int grantDays;

	public FixGrantDateDto() {
		interval = 0;
		grantDays = 0;
	}

	public static FixGrantDateDto fromDomain(FixGrantDate fixGrantDate) {
		if(fixGrantDate == null) {
			return null;
		}
		// 要修正 jinno
		// return new FixGrantDateDto(fixGrantDate.getInterval().v(), fixGrantDate.getGrantDays().v());
		return new FixGrantDateDto(0, 0);

	}
}
