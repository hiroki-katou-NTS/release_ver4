package nts.uk.ctx.at.shared.dom.specialholiday.grantinformation;

import java.util.Optional;

import lombok.AllArgsConstructor;
//import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.shared.dom.specialholiday.periodinformation.GrantDeadline;

/**
 * 付与・期限情報
 * @author masaaki_jinno
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GrantRegular extends DomainObject {

	/** 付与するタイミングの種類 */
	private TypeTime typeTime;

	/** 付与基準日 */
	private Optional<GrantDate> grantDate;

	/** 指定日付与 */
	private Optional<FixGrantDate> fixGrantDate;

	/** 付与日テーブル参照付与 */
	private Optional<GrantDeadline> grantPeriodic;

	/** 期間付与 */
	private Optional<PeriodGrantDate> periodGrantDate;

	@Override
	public void validate() {
		super.validate();
	}

	static public GrantRegular of(
		/** 付与するタイミングの種類 */
		TypeTime typeTime
		/** 付与基準日 */
		, Optional<GrantDate> grantDate
		/** 指定日付与 */
		, Optional<FixGrantDate> fixGrantDate
		/** 付与日テーブル参照付与 */
		, Optional<GrantDeadline> grantPeriodic
		/** 期間付与 */
		, Optional<PeriodGrantDate> periodGrantDate
	){
		GrantRegular c = new GrantRegular();
		/** 付与するタイミングの種類 */
		c.typeTime=typeTime;
		/** 付与基準日 */
		c.grantDate=grantDate;
		/** 指定日付与 */
		c.fixGrantDate=fixGrantDate;
		/** 付与日テーブル参照付与 */
		c.grantPeriodic=grantPeriodic;
		/** 期間付与 */
		c.periodGrantDate=periodGrantDate;

		return c;
	}

	public Optional<Integer> getLimitAccumulationDays() {
		if(this.typeTime==TypeTime.REFER_GRANT_DATE_TBL) {
			if(!this.getGrantPeriodic().isPresent())return Optional.empty();
			if(!this.getGrantPeriodic().get().getLimitAccumulationDays().isPresent())return Optional.empty();
			if(!this.getGrantPeriodic().get().getLimitAccumulationDays().get().getLimitCarryoverDays().isPresent())return Optional.empty();

			return Optional.of(this.getGrantPeriodic().get().getLimitAccumulationDays().get().getLimitCarryoverDays().get().v());
		}
		if(this.typeTime==TypeTime.GRANT_SPECIFY_DATE) {
			if(!this.getFixGrantDate().isPresent())return Optional.empty();
			if(!this.getFixGrantDate().get().getGrantPeriodic().getLimitAccumulationDays().isPresent())return Optional.empty();
			if(!this.getFixGrantDate().get().getGrantPeriodic().getLimitAccumulationDays().get().getLimitCarryoverDays().isPresent())return Optional.empty();

			return Optional.of(this.getFixGrantDate().get().getGrantPeriodic().getLimitAccumulationDays().get().getLimitCarryoverDays().get().v());
		}
		return Optional.empty();
	}


}
