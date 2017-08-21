/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.find.worktime;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.i18n.custom.IInternationalization;
import nts.uk.ctx.at.shared.app.find.worktime.dto.WorkTimeDto;
import nts.uk.ctx.at.shared.app.find.worktime.dto.WorkTimeScheduleDto;
import nts.uk.ctx.at.shared.dom.attendance.UseSetting;
import nts.uk.ctx.at.shared.dom.worktime.WorkTime;
import nts.uk.ctx.at.shared.dom.worktime.WorkTimeMethodSet;
import nts.uk.ctx.at.shared.dom.worktime.WorkTimeRepository;
import nts.uk.ctx.at.shared.dom.worktimeset.TimeDayAtr;
import nts.uk.ctx.at.shared.dom.worktimeset.WorkTimeSet;
import nts.uk.ctx.at.shared.dom.worktimeset.WorkTimeSetRepository;
import nts.uk.ctx.at.shared.dom.worktype.DisplayAtr;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * 
 * @author Doan Duy Hung
 *
 */

@Stateless
@Transactional
public class WorkTimeFinder {

	@Inject
	IInternationalization internationalization;

	@Inject
	private WorkTimeRepository workTimeRepository;

	@Inject
	private WorkTimeSetRepository workTimeSetRepository;
	private String[] timeDayAtr = new String[4];
	private String[] workTimeMethodSet = new String[4];

	@PostConstruct
	private void loadInitialData() {

		timeDayAtr[0] = internationalization.getItemName(TimeDayAtr.Enum_DayAtr_PreviousDay.name()).get();
		timeDayAtr[1] = internationalization.getItemName(TimeDayAtr.Enum_DayAtr_Day.name()).get();
		timeDayAtr[2] = internationalization.getItemName(TimeDayAtr.Enum_DayAtr_NextDay.name()).get();
		timeDayAtr[3] = internationalization.getItemName(TimeDayAtr.Enum_DayAtr_SkipDay.name()).get();

		// workTimeMethodSet[0] =
		// internationalization.getItemName(WorkTimeMethodSet.Enum_Fixed_Work.name()).get();
		// workTimeMethodSet[1] =
		// internationalization.getItemName(WorkTimeMethodSet.Enum_Jogging_Time.name()).get();
		workTimeMethodSet[0] = internationalization.getItemName(WorkTimeMethodSet.Enum_Overtime_Work.name()).get();
		workTimeMethodSet[1] = internationalization.getItemName(WorkTimeMethodSet.Enum_Overtime_Work.name()).get();
		workTimeMethodSet[2] = internationalization.getItemName(WorkTimeMethodSet.Enum_Overtime_Work.name()).get();
		workTimeMethodSet[3] = internationalization.getItemName(WorkTimeMethodSet.Enum_Fluid_Work.name()).get();
	}

	/**
	 * Find by company ID.
	 *
	 * @return the list
	 */
	public List<WorkTimeDto> findByCompanyID() {
		String companyID = AppContexts.user().companyId();
		List<WorkTime> workTimeItems = this.workTimeRepository.findByCompanyID(companyID);
		List<WorkTimeSet> workTimeSetItems = this.workTimeSetRepository.findByCompanyID(companyID);
		return getWorkTimeDtos(workTimeItems, workTimeSetItems);
	}

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	public List<WorkTimeDto> findAll() {
		String companyID = AppContexts.user().companyId();
		List<WorkTime> workTimeItems = this.workTimeRepository.findAll(companyID);
		List<WorkTimeSet> workTimeSetItems = this.workTimeSetRepository.findByCompanyID(companyID);
		return getWorkTimeDtos(workTimeItems, workTimeSetItems);
	}

	/**
	 * Find by codes.
	 *
	 * @param codes the codes
	 * @return the list
	 */
	public List<WorkTimeDto> findByCodes(List<String> codes) {
		String companyID = AppContexts.user().companyId();
		if (codes.isEmpty()) {
			return Collections.emptyList();
		} else {
			List<WorkTime> workTimeItems = this.workTimeRepository.findByCodes(companyID, codes);
			List<WorkTimeSet> workTimeSetItems = this.workTimeSetRepository.findByCodeList(companyID, codes);
			return getWorkTimeDtos(workTimeItems, workTimeSetItems);
		}
	}

	/**
	 * find list Work Time Dto by code list
	 * 
	 * @param codeList
	 *            code list
	 * @return list Work Time Dto
	 */
	public List<WorkTimeDto> findByCodeList(List<String> codeList) {
		String companyID = AppContexts.user().companyId();
		if (codeList.isEmpty()) {
			return Collections.emptyList();
		} else {
			List<WorkTime> workTimeItems = this.workTimeRepository.findByCodeList(companyID, codeList);
			List<WorkTimeSet> workTimeSetItems = this.workTimeSetRepository.findByCodeList(companyID, codeList);
			return getWorkTimeDtos(workTimeItems, workTimeSetItems);
		}
	}

	/**
	 * find list Work Time Dto by input time and code list
	 * 
	 * @param codeList
	 *            code list
	 * @param startAtr
	 *            start time option
	 * @param startTime
	 *            start time
	 * @param endAtr
	 *            end time option
	 * @param endTime
	 *            end time
	 * @return list Work Time Dto
	 */
	public List<WorkTimeDto> findByTime(List<String> codeList, int startAtr, int startTime, int endAtr, int endTime) {
		if (codeList.isEmpty()) {
			return Collections.emptyList();
		} else {
			String companyID = AppContexts.user().companyId();
			List<WorkTime> workTimeItems = new ArrayList<>();
			List<WorkTimeSet> workTimeSetItems = new ArrayList<>();
			// when both start time and end time is valid
			if ((startTime > -1) && (endTime > -1)) {
				// compare start time and end time
				if (((24 * 60 * startAtr) + startTime) > ((24 * 60 * endAtr) + endTime))
					throw new BusinessException("Msg_54");
				workTimeItems = this.workTimeRepository.findByCodeList(companyID, codeList);
				workTimeSetItems = this.workTimeSetRepository.findByStartAndEnd(companyID, codeList, startAtr,
						startTime, endAtr, endTime);
				// when only start time is select
			} else if ((startTime > -1) && (endTime <= -1)) {
				workTimeItems = this.workTimeRepository.findByCodeList(companyID, codeList);
				workTimeSetItems = this.workTimeSetRepository.findByStart(companyID, codeList, startAtr, startTime);
				// when only end time is select
			} else if ((startTime <= -1) && (endTime > -1)) {
				workTimeItems = this.workTimeRepository.findByCodeList(companyID, codeList);
				workTimeSetItems = this.workTimeSetRepository.findByEnd(companyID, codeList, endAtr, endTime);
				// when both start time and end time is invalid
			} else {
				throw new BusinessException("Msg_53");
			}
			return getWorkTimeDtos(workTimeItems, workTimeSetItems);
		}
	}

	/**
	 * get WorkTimeDto list by WorkTime list and WorkTimeSet list
	 * 
	 * @param workTimeItems
	 *            WorkTime list
	 * @param workTimeSetItems
	 *            WorkTimeSet list
	 * @return WorkTimeDto list
	 */
	private List<WorkTimeDto> getWorkTimeDtos(List<WorkTime> workTimeItems, List<WorkTimeSet> workTimeSetItems) {
		List<WorkTimeDto> workTimeDtos = new ArrayList<>();
		if (workTimeItems.isEmpty() || workTimeSetItems.isEmpty()) {
			workTimeDtos = Collections.emptyList();
		} else {
			for (WorkTimeSet item : workTimeSetItems) {
				int index = workTimeSetItems.indexOf(item);
				WorkTime currentWorkTime = workTimeItems.get(index);
				WorkTimeSet currentWorkTimeSet = workTimeSetItems.get(index);
				if ((currentWorkTimeSet.getWorkTimeDay1() == null) && (currentWorkTimeSet.getWorkTimeDay2() == null)) {
					continue;
				} else if (currentWorkTimeSet.getWorkTimeDay1().getUse_atr().equals(UseSetting.UseAtr_NotUse)
						&& currentWorkTimeSet.getWorkTimeDay2().getUse_atr().equals(UseSetting.UseAtr_NotUse)) {
					continue;
				} else {
					workTimeDtos.add(new WorkTimeDto(currentWorkTime.getSiftCD().v(),
							currentWorkTime.getWorkTimeDisplayName().getWorkTimeName().v(),
							(!(currentWorkTimeSet.getWorkTimeDay1() == null))
									? createWorkTimeField(currentWorkTimeSet.getWorkTimeDay1().getUse_atr(),
											currentWorkTimeSet.getWorkTimeDay1().getA_m_StartCLock(),
											currentWorkTimeSet.getWorkTimeDay1().getA_m_StartAtr(),
											currentWorkTimeSet.getWorkTimeDay1().getP_m_EndClock(),
											currentWorkTimeSet.getWorkTimeDay1().getP_m_EndAtr())
									: null,
							(!(currentWorkTimeSet.getWorkTimeDay2() == null))
									? createWorkTimeField(currentWorkTimeSet.getWorkTimeDay2().getUse_atr(),
											currentWorkTimeSet.getWorkTimeDay2().getA_m_StartCLock(),
											currentWorkTimeSet.getWorkTimeDay2().getA_m_StartAtr(),
											currentWorkTimeSet.getWorkTimeDay2().getP_m_EndClock(),
											currentWorkTimeSet.getWorkTimeDay2().getP_m_EndAtr())
									: null,
							workTimeMethodSet[currentWorkTime.getWorkTimeDivision().getWorkTimeMethodSet().value],
							currentWorkTime.getNote().v()));
				}
			}
			;
		}
		return workTimeDtos;
	}

	/**
	 * format to String form input time day
	 * 
	 * @param useAtr
	 *            time day use atr
	 * @param start
	 *            time day start time
	 * @param startAtr
	 *            time day start atr
	 * @param end
	 *            time day end time
	 * @param endAtr
	 *            time day end atr
	 * @return result string
	 * @throws ParseException
	 */
	private String createWorkTimeField(UseSetting useAtr, int start, TimeDayAtr startAtr, int end, TimeDayAtr endAtr) {
		if (useAtr.equals(UseSetting.UseAtr_Use)) {
			return timeDayAtr[startAtr.value] + formatTime(start) + " ~ " + timeDayAtr[endAtr.value] + formatTime(end);
		} else
			return null;
	}

	/**
	 * format int Time to string HH:mm format
	 * 
	 * @param time
	 *            int Time
	 * @return string HH:mm format
	 */
	private String formatTime(int time) {
		return String.format("%02d:%02d", time / 60, time % 60);
	}

	/**
	 * find list WorkTimeScheduleDto (WorkTimeDto) by companyId and DisplayAtr =
	 * DISPLAY (added by sonnh1)
	 * 
	 * @return List WorkTimeScheduleDto
	 */
	public List<WorkTimeScheduleDto> findByCIdAndDisplayAtr() {
		String companyID = AppContexts.user().companyId();
		return this.workTimeRepository.findByCIdAndDisplayAtr(companyID, DisplayAtr.DisplayAtr_Display.value).stream()
				.map(x -> WorkTimeScheduleDto.fromDomain(x)).collect(Collectors.toList());
	}

	
	/**
	 * Find by id.
	 *
	 * @param workTimeCode the work time code
	 * @return the work time dto
	 */
	public WorkTimeDto findById(String workTimeCode){
		// get login user
		LoginUserContext loginUserContext = AppContexts.user();
		
		// get company id
		String companyId = loginUserContext.companyId();
		
		// call repository find by id
		Optional<WorkTime> opWorkTime = this.workTimeRepository.findByCode(companyId, workTimeCode);

		WorkTimeDto dto = new WorkTimeDto(null, null, null, null, null, null);
		// check exist data
		if(opWorkTime.isPresent()){
			dto.setCode(opWorkTime.get().getSiftCD().v());
			dto.setName(opWorkTime.get().getWorkTimeDisplayName().getWorkTimeName().v());
		}
		return dto;
	}
}
