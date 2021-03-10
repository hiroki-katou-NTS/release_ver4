package nts.uk.ctx.at.shared.dom.specialholiday.periodinformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
//import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.DomainObject;

/**
 * 期限情報
 * @author masaaki_jinno
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GrantDeadline extends DomainObject {

	/** 期限指定方法 */
	private TimeLimitSpecification timeSpecifyMethod;

	/** 有効期限 */
	private Optional<SpecialVacationDeadline> expirationDate;

	/** 蓄積上限 */
	private Optional<LimitAccumulationDays> limitAccumulationDays;


	@Override
	public void validate() {
		super.validate();
	}

	/**
	 * Validate input data
	 */
	public List<String> validateInput() {
		List<String> errors = new ArrayList<>();

		// 0年0ヶ月は登録不可
		if(this.getTimeSpecifyMethod() == TimeLimitSpecification.AVAILABLE_GRANT_DATE_DESIGNATE) {
			if (this.expirationDate.isPresent()) {
				if (this.expirationDate.get().getMonths().v() == 0 && this.expirationDate.get().getYears().v() == 0) {
					errors.add("Msg_95");
				}
			}
		}

		return errors;
	}

	/**
	 * Create from Java Type
	 * @return
	 */
	public static GrantDeadline createFromJavaType(
			int timeSpecifyMethod,
			Optional<SpecialVacationDeadline> deadline,
			Integer limit,
			Integer limitCarryoverDays) {

		Optional<LimitAccumulationDays> accumulationDays = Optional.empty();
		Optional<LimitCarryoverDays> carryOverDays = Optional.empty();
		if(limitCarryoverDays != null)
			carryOverDays = Optional.of(new LimitCarryoverDays(limitCarryoverDays));

		if(limit != null)
			accumulationDays = Optional.of(new LimitAccumulationDays(limit==1, carryOverDays));

		return new GrantDeadline(
				EnumAdaptor.valueOf(timeSpecifyMethod, TimeLimitSpecification.class),
				deadline,
				accumulationDays);

	}
}
