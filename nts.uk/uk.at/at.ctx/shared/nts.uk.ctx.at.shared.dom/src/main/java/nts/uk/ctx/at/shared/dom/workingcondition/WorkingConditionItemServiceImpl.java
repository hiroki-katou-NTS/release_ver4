/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.workingcondition;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.worktype.WorkAtr;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeSet;
import nts.uk.ctx.at.shared.dom.worktype.holidayset.HolidaySettingRepository;

/**
 * The Class WorkingConditionItemServiceIpm.
 */
@Stateless
public class WorkingConditionItemServiceImpl implements WorkingConditionItemService {

	/** The Constant STATUTORY_HOLIDAYS. */
	/* 法定内休日 */
	public static final int STATUTORY_HOLIDAYS = 0;

	/** The Constant NON_STATUTORY_HOLIDAYS. */
	/* 法定外休日 */
	public static final int NON_STATUTORY_HOLIDAYS = 1;

	/** The Constant PUBLIC_HOLIDAY. */
	/* 祝日 */
	public static final int PUBLIC_HOLIDAY = 2;

	/** The repository working condition item. */
	@Inject
	private WorkingConditionItemRepository repositoryWorkingConditionItem;

	/** The work type repository. */
	@Inject
	private WorkTypeRepository workTypeRepository;
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemService#
	 * getHolidayWorkSchedule(java.lang.String, java.lang.String,
	 * nts.arc.time.GeneralDate, java.lang.String)
	 */
	@Override
	public Optional<SingleDaySchedule> getHolidayWorkSchedule(String companyId, String employeeId,
			GeneralDate baseDate, String workTypeCode) {

		Optional<WorkingConditionItem> optWorkingCondItem = this.repositoryWorkingConditionItem
				.getBySidAndStandardDate(employeeId, baseDate);

		// check list working condition item is present
		if (optWorkingCondItem.isPresent()) {
			// get Working Condition Item
			WorkingConditionItem domain = optWorkingCondItem.get();
			Optional<SingleDaySchedule> optpublicHoliday = domain.getWorkCategory()
					.getPublicHolidayWork();

			// check public holiday is present
			if (!optpublicHoliday.isPresent()) {
				WorkTypeSet workTypeSet = this.workTypeRepository.findByPK(companyId, workTypeCode).get().getWorkTypeSetByAtr(WorkAtr.OneDay).get();
				// filter by holiday Setting atr
				switch (workTypeSet.getHolidayAtr()) {
				case STATUTORY_HOLIDAYS:
					if (this.checkInLawBreakTime(domain.getWorkCategory())) {
						return domain.getWorkCategory().getInLawBreakTime();
					}
					return Optional.of(domain.getWorkCategory().getHolidayWork());
				case NON_STATUTORY_HOLIDAYS:
					if (this.checkInLawBreakTime(domain.getWorkCategory())) {
						return domain.getWorkCategory().getOutsideLawBreakTime();
					}
					return Optional.of(domain.getWorkCategory().getHolidayWork());
				case PUBLIC_HOLIDAY:
					if (this.checkInLawBreakTime(domain.getWorkCategory())) {
						return domain.getWorkCategory().getHolidayAttendanceTime();
					}
					return Optional.of(domain.getWorkCategory().getHolidayWork());
				default:
					return null;
				}

			}
			return optpublicHoliday;
		}

		return null;
	}

	/**
	 * Check in law break time.
	 *
	 * @param persWorkCategory
	 *            the pers work category
	 * @return the boolean
	 */
	private Boolean checkInLawBreakTime(PersonalWorkCategory persWorkCategory) {
		if (persWorkCategory.getInLawBreakTime().isPresent()) {
			return true;
		}

		return false;
	}

}
