/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.find.worktime.worktimeset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.uk.ctx.at.shared.app.find.worktime.dto.WorkTimeSettingInfoDto;
import nts.uk.ctx.at.shared.app.find.worktime.predset.PredetemineTimeSetFinder;
import nts.uk.ctx.at.shared.app.find.worktime.worktimeset.dto.SimpleWorkTimeSettingDto;
import nts.uk.ctx.at.shared.app.find.worktime.worktimeset.dto.WorkTimeSettingDto;
import nts.uk.ctx.at.shared.app.find.worktime_old.dto.WorkTimeDto;
import nts.uk.ctx.at.shared.dom.worktime.common.AbolishAtr;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.TimezoneUse;
import nts.uk.ctx.at.shared.dom.worktime.predset.UseSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDailyAtr;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingCondition;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;
import nts.uk.shr.com.time.TimeWithDayAttr;
import nts.uk.shr.infra.i18n.resource.I18NResourcesForUK;

/**
 * The Class WorkTimeSettingFinder.
 */
@Stateless
public class WorkTimeSettingFinder {

	/** The work time setting repository. */
	@Inject
	private WorkTimeSettingRepository workTimeSettingRepository;

	/** The pred finder. */
	@Inject
	private PredetemineTimeSetFinder predetemineTimeSetFinder;

	/** The Predetemine time setting repository. */
	@Inject
	private PredetemineTimeSettingRepository predetemineTimeSettingRepository;

	/** The i 18 n. */
	@Inject
	private I18NResourcesForUK i18n;

	/**
	 * Find all simple.
	 *
	 * @return the list
	 */
	public List<SimpleWorkTimeSettingDto> findAllSimple() {
		String companyId = AppContexts.user().companyId();
		List<WorkTimeSetting> lstWorktimeSetting = workTimeSettingRepository
				.findByCompanyId(companyId);
		return lstWorktimeSetting.stream().map(item -> {
			return SimpleWorkTimeSettingDto.builder()
					.isAbolish(item.getAbolishAtr() == AbolishAtr.ABOLISH)
					.worktimeCode(item.getWorktimeCode().v())
					.workTimeName(item.getWorkTimeDisplayName().getWorkTimeName().v()).build();
		}).collect(Collectors.toList());
	}

	/**
	 * Find with condition.
	 *
	 * @param condition
	 *            the condition
	 * @return the list
	 */
	public List<SimpleWorkTimeSettingDto> findWithCondition(WorkTimeSettingCondition condition) {
		String companyId = AppContexts.user().companyId();
		List<WorkTimeSetting> lstWorktimeSetting = workTimeSettingRepository
				.findWithCondition(companyId, condition);
		return lstWorktimeSetting.stream().map(item -> {
			return SimpleWorkTimeSettingDto.builder()
					.isAbolish(item.getAbolishAtr() == AbolishAtr.ABOLISH)
					.worktimeCode(item.getWorktimeCode().v())
					.workTimeName(item.getWorkTimeDisplayName().getWorkTimeName().v()).build();
		}).collect(Collectors.toList());
	}

	/**
	 * Find all.
	 *
	 * @return the work time setting dto
	 */
	public WorkTimeSettingInfoDto findByCode(String worktimeCode) {
		String companyId = AppContexts.user().companyId();
		WorkTimeSettingInfoDto dto = new WorkTimeSettingInfoDto();
		dto.setPredseting(this.predetemineTimeSetFinder.findByWorkTimeCode(worktimeCode));
		WorkTimeSetting worktimeSetting = workTimeSettingRepository
				.findByCode(companyId, worktimeCode).get();
		WorkTimeSettingDto worktimeSettingDto = new WorkTimeSettingDto();
		worktimeSetting.saveToMemento(worktimeSettingDto);
		dto.setWorktimeSetting(worktimeSettingDto);
		return dto;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static final int FIRST_ITEM = 0;

	public static final int TWO_ITEM = 1;

	public static final int TWO_TIMEZONE = 2;

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	public List<WorkTimeDto> findAll() {
		String companyID = AppContexts.user().companyId();
		List<WorkTimeSetting> workTimeItems = this.workTimeSettingRepository
				.findByCompanyId(companyID);
		List<PredetemineTimeSetting> workTimeSetItems = this.predetemineTimeSettingRepository
				.findByCompanyID(companyID);
		return getWorkTimeDtos(workTimeItems, workTimeSetItems);
	}

	/**
	 * Find by codes.
	 *
	 * @param codes
	 *            the codes
	 * @return the list
	 */
	public List<WorkTimeDto> findByCodes(List<String> codes) {
		String companyID = AppContexts.user().companyId();
		if (codes.isEmpty()) {
			return Collections.emptyList();
		} else {
			List<WorkTimeSetting> workTimeItems = this.workTimeSettingRepository
					.findByCodes(companyID, codes);
			List<PredetemineTimeSetting> workTimeSetItems = this.predetemineTimeSettingRepository
					.findByCodeList(companyID, codes);
			return getWorkTimeDtos(workTimeItems, workTimeSetItems);
		}
	}

	/**
	 * Find by time.
	 *
	 * @param codeList
	 *            the code list
	 * @param startAtr
	 *            the start atr
	 * @param startTime
	 *            the start time
	 * @param endAtr
	 *            the end atr
	 * @param endTime
	 *            the end time
	 * @return the list
	 */
	public List<WorkTimeDto> findByTime(List<String> codeList, int startAtr, int startTime,
			int endAtr, int endTime) {
		if (codeList.isEmpty()) {
			return Collections.emptyList();
		} else {
			String companyID = AppContexts.user().companyId();
			List<WorkTimeSetting> workTimeItems = new ArrayList<>();
			List<PredetemineTimeSetting> workTimeSetItems = new ArrayList<>();
			// when both start time and end time is valid
			if ((startTime > -1) && (endTime > -1)) {
				// compare start time and end time
				if (((24 * 60 * startAtr) + startTime) > ((24 * 60 * endAtr) + endTime))
					throw new BusinessException("Msg_54");
				workTimeItems = this.workTimeSettingRepository.findByCodes(companyID, codeList);
				workTimeSetItems = this.predetemineTimeSettingRepository.findByStartAndEnd(
						companyID, codeList, startAtr * 24 * 60 + startTime,
						endAtr * 24 * 60 + endTime);
				// when only start time is select
			} else if ((startTime > -1) && (endTime <= -1)) {
				workTimeItems = this.workTimeSettingRepository.findByCodes(companyID, codeList);
				workTimeSetItems = this.predetemineTimeSettingRepository.findByStart(companyID,
						codeList, startAtr * 24 * 60 + startTime);
				// when only end time is select
			} else if ((startTime <= -1) && (endTime > -1)) {
				workTimeItems = this.workTimeSettingRepository.findByCodes(companyID, codeList);
				workTimeSetItems = this.predetemineTimeSettingRepository.findByEnd(companyID,
						codeList, endAtr * 24 * 60 + endTime);
				// when both start time and end time is invalid
			} else {
				throw new BusinessException("Msg_53");
			}
			return getWorkTimeDtos(workTimeItems, workTimeSetItems);
		}
	}

	/**
	 * Gets the work time dtos.
	 *
	 * @param workTimeItems
	 *            the work time items
	 * @param workTimeSetItems
	 *            the work time set items
	 * @return the work time dtos
	 */
	private List<WorkTimeDto> getWorkTimeDtos(List<WorkTimeSetting> workTimeItems,
			List<PredetemineTimeSetting> workTimeSetItems) {
		List<WorkTimeDto> workTimeDtos = new ArrayList<>();
		if (workTimeItems.isEmpty() || workTimeSetItems.isEmpty()) {
			workTimeDtos = Collections.emptyList();
		} else {
			for (PredetemineTimeSetting item : workTimeSetItems) {
				WorkTimeSetting currentWorkTime = workTimeItems.stream()
						.filter(x -> x.getWorktimeCode().equals(item.getWorkTimeCode())).findAny()
						.get();
				if (item.getPrescribedTimezoneSetting().getLstTimezone().isEmpty()) {
					continue;
				} else if (this.checkNotUse(item)) {
					continue;
				} else {
					TimezoneUse timezone1 = item.getPrescribedTimezoneSetting().getLstTimezone()
							.get(FIRST_ITEM);
					TimezoneUse timezone2 = null;
					// if have 2 timezone
					if (item.getPrescribedTimezoneSetting().getLstTimezone()
							.size() >= TWO_TIMEZONE) {
						timezone2 = item.getPrescribedTimezoneSetting().getLstTimezone()
								.get(TWO_ITEM);
					} 
					if (currentWorkTime.getWorkTimeDivision().getWorkTimeDailyAtr().equals(WorkTimeDailyAtr.REGULAR_WORK)) {
						workTimeDtos
								.add(new WorkTimeDto(currentWorkTime.getWorktimeCode().v(),
										currentWorkTime.getWorkTimeDisplayName().getWorkTimeName().v(),
										(timezone1 != null) ? createWorkTimeField(timezone1.getUseAtr(),
												timezone1.getStart(), timezone1.getEnd()) : null,
										(timezone2 != null)
												? createWorkTimeField(timezone2.getUseAtr(),
														timezone2.getStart(), timezone2.getEnd())
												: null,
										i18n.localize(currentWorkTime.getWorkTimeDivision()
												.getWorkTimeMethodSet().nameId).get(),
										currentWorkTime.getNote().v()));
					} else {
						workTimeDtos
						.add(new WorkTimeDto(currentWorkTime.getWorktimeCode().v(),
								currentWorkTime.getWorkTimeDisplayName().getWorkTimeName().v(),
								(timezone1 != null) ? createWorkTimeField(timezone1.getUseAtr(),
										timezone1.getStart(), timezone1.getEnd()) : null,
								(timezone2 != null)
										? createWorkTimeField(timezone2.getUseAtr(),
												timezone2.getStart(), timezone2.getEnd())
										: null,
								i18n.localize(currentWorkTime.getWorkTimeDivision()
										.getWorkTimeDailyAtr().nameId).get(),
								currentWorkTime.getNote().v()));
					}
				}
			};
		}
		return workTimeDtos;
	}

	/**
	 * Check not use.
	 *
	 * @param workTimeSet
	 *            the work time set
	 * @return true, if successful
	 */
	private boolean checkNotUse(PredetemineTimeSetting workTimeSet) {
		for (TimezoneUse timezone : workTimeSet.getPrescribedTimezoneSetting().getLstTimezone()) {
			if (timezone.getUseAtr().equals(UseSetting.NOT_USE))
				return true;
		}
		return false;
	}

	/**
	 * Creates the work time field.
	 *
	 * @param useAtr
	 *            the use atr
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @return the string
	 */
	private String createWorkTimeField(UseSetting useAtr, TimeWithDayAttr start,
			TimeWithDayAttr end) {
		if (useAtr.equals(UseSetting.USE)) {
			return start.dayAttr().description + start.getRawTimeWithFormat() + " ~ "
					+ end.dayAttr().description + end.getRawTimeWithFormat();
		} else
			return null;
	}

	/**
	 * Find by id.
	 *
	 * @param workTimeCode
	 *            the work time code
	 * @return the work time dto
	 */
	public WorkTimeDto findById(String workTimeCode) {
		// get login user
		LoginUserContext loginUserContext = AppContexts.user();

		// get company id
		String companyId = loginUserContext.companyId();

		// call repository find by id
		Optional<WorkTimeSetting> opWorkTime = this.workTimeSettingRepository.findByCode(companyId,
				workTimeCode);

		WorkTimeDto dto = new WorkTimeDto(null, null, null, null, null, null);
		// check exist data
		if (opWorkTime.isPresent()) {
			dto.setCode(opWorkTime.get().getWorktimeCode().v());
			dto.setName(opWorkTime.get().getWorkTimeDisplayName().getWorkTimeName().v());
		}
		return dto;
	}
}
