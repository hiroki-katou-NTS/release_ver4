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
	// 休日出勤時の勤務情報を取得する
	@Override
	public Optional<SingleDaySchedule> getHolidayWorkSchedule(String companyId, String employeeId,
			GeneralDate baseDate, String workTypeCode) {
		
		Optional<WorkingConditionItem> optWorkingCondItem = this.repositoryWorkingConditionItem
				.getBySidAndStandardDate(employeeId, baseDate);

		// check list working condition item is present
		if (optWorkingCondItem.isPresent()) {
			// get Working Condition Item
			WorkingConditionItem domain = optWorkingCondItem.get();
			// ドメインモデル「個人勤務日区分別勤務」を取得する (Lấy 「個人勤務日区分別勤務」)
			Optional<SingleDaySchedule> optpublicHoliday = domain.getWorkCategory()
					.getPublicHolidayWork();

			// check public holiday is present
			if (!optpublicHoliday.isPresent()) {
				// ドメインモデル「休日設定」を取得する (lấy dữ liệu 「休日設定」)
				Optional<WorkTypeSet> workTypeSet = this.workTypeRepository.findByPK(companyId, workTypeCode).get().getWorkTypeSetByAtr(WorkAtr.OneDay);
				
				if (workTypeSet.isPresent()) {
					// 取得できた場合
					// filter by holiday Setting atr
					switch (workTypeSet.get().getHolidayAtr()) {
					// 法定内休日
					case STATUTORY_HOLIDAYS:
						return domain.getWorkCategory().getInLawBreakTime().isPresent()
								? domain.getWorkCategory().getInLawBreakTime()
								: Optional.of(domain.getWorkCategory().getHolidayWork());
					// 法定外休日			
					case NON_STATUTORY_HOLIDAYS:
						return domain.getWorkCategory().getOutsideLawBreakTime().isPresent()
								? domain.getWorkCategory().getOutsideLawBreakTime()
								: Optional.of(domain.getWorkCategory().getHolidayWork());
					// 祝日			
					case PUBLIC_HOLIDAY:
						return domain.getWorkCategory().getHolidayAttendanceTime().isPresent()
								? domain.getWorkCategory().getHolidayAttendanceTime()
								: Optional.of(domain.getWorkCategory().getHolidayWork());
					default:
						return Optional.empty();
					}
				}else{
					// 取得できない場合
					// 休日出勤時の勤務情報が存在するか確認する 
					// (【条件】 個人勤務日区分別勤務．休日出勤時)
					return Optional.of(domain.getWorkCategory().getHolidayWork());
				}
			}
			
			return optpublicHoliday;
		}

		return Optional.empty();
	}

}
