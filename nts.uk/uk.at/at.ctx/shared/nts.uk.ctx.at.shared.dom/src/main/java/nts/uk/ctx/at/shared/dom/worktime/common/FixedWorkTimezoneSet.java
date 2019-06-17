/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.val;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;

/**
 * The Class FixedWorkTimezoneSet.
 */
// 固定勤務時間帯設定
@Getter
@Setter
@NoArgsConstructor
public class FixedWorkTimezoneSet extends WorkTimeDomainObject implements Cloneable{

	/** The lst working timezone. */
	// 就業時間帯
	private List<EmTimeZoneSet> lstWorkingTimezone;

	/** The lst OT timezone. */
	// 残業時間帯
	private List<OverTimeOfTimeZoneSet> lstOTTimezone;
	
	/** The Constant EMPLOYMENT_TIME_FRAME_NO_ONE. */
	public static final int EMPLOYMENT_TIME_FRAME_NO_ONE = 1;
	
	/** The Constant WORK_TIME_ZONE_NO_ONE. */
	public static final int WORK_TIME_ZONE_NO_ONE = 1;

	/**
	 * Instantiates a new fixed work timezone set.
	 *
	 * @param memento the memento
	 */
	public FixedWorkTimezoneSet(FixedWorkTimezoneSetGetMemento memento) {
		this.lstWorkingTimezone = memento.getLstWorkingTimezone();
		this.lstOTTimezone = memento.getLstOTTimezone();
	}

	/**
	 * Gets the over time of time zone set.
	 *
	 * @param workTimezoneNo the work timezone no
	 * @return the over time of time zone set
	 */
	public OverTimeOfTimeZoneSet getOverTimeOfTimeZoneSet(int workTimezoneNo) {
		return this.lstOTTimezone.stream().filter(overtime -> overtime.getWorkTimezoneNo().v() == workTimezoneNo)
				.findFirst().get();
	}
	
	/**
	 * Gets the em time zone set.
	 *
	 * @param employmentTimeFrameNo the employment time frame no
	 * @return the em time zone set
	 */
	public EmTimeZoneSet getEmTimeZoneSet(int employmentTimeFrameNo) {
		return this.lstWorkingTimezone.stream()
				.filter(timezone -> timezone.getEmploymentTimeFrameNo().v() == employmentTimeFrameNo).findFirst().get();
	}
	/* (non-Javadoc)
	 * @see nts.arc.layer.dom.DomainObject#validate()
	 */
	@Override
	public void validate() {
		this.checkOverlap();
		
		super.validate();
	}

	/**
	 * Checks if is in over timezone.
	 *
	 * @param timezone the timezone
	 * @return true, if is in over timezone
	 */
	public boolean isInOverTimezone(TimeZone timezone) {
		return this.lstOTTimezone.stream().anyMatch(ot -> timezone.isBetweenOrEqual(ot.getTimezone()));
	}

	/**
	 * Checks if is in em timezone.
	 *
	 * @param timezone the timezone
	 * @return true, if is in em timezone
	 */
	public boolean isInEmTimezone(TimeZone timezone) {
		return this.lstWorkingTimezone.stream().anyMatch(ot -> timezone.isBetweenOrEqual(ot.getTimezone()));
	}

	/**
	 * Check over time and em time overlap.
	 */
	public boolean isOverTimeAndEmTimeOverlap() {
		if (CollectionUtil.isEmpty(this.lstWorkingTimezone) || CollectionUtil.isEmpty(this.lstOTTimezone)) {
			return false;
		}
		
		return this.lstOTTimezone.stream()
				.anyMatch(ot -> this.lstWorkingTimezone.stream().anyMatch(em -> ot.getTimezone().isOverlap(em.getTimezone())));
	}

	/**
	 * Check overlap.
	 */
	private void checkOverlap() {
		if (!CollectionUtil.isEmpty(this.lstWorkingTimezone)) {
			val size = this.lstWorkingTimezone.size();
			for (int i = 0; i < size; i++) {
				for (int j = i + 1; j < size; j++) {
					if (this.lstWorkingTimezone.get(i).getTimezone()
							.isOverlap(this.lstWorkingTimezone.get(j).getTimezone())) {
						this.bundledBusinessExceptions.addMessage("Msg_515", "KMK003_86");
					}
				}
			}
		}

		if (!CollectionUtil.isEmpty(this.lstOTTimezone)) {
			val size = this.lstOTTimezone.size();
			for (int i = 0; i < size; i++) {
				for (int j = i + 1; j < size; j++) {
					if (this.lstOTTimezone.get(i).getTimezone()
							.isOverlap(this.lstOTTimezone.get(j).getTimezone())) {
						this.bundledBusinessExceptions.addMessage("Msg_515", "KMK003_89");
					}
				}
			}
		}

	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(FixedWorkTimezoneSetSetMemento memento){
		memento.setLstWorkingTimezone(this.lstWorkingTimezone);
		memento.setLstOTTimezone(this.lstOTTimezone);
	}
	
	/**
	 * Restore data.
	 *
	 * @param other the other
	 */
	public void restoreData(FixedWorkTimezoneSet other) {
		// restore 就業時間帯
		Map<EmTimeFrameNo, EmTimeZoneSet> mapEmTimezone = other.getLstWorkingTimezone().stream().collect(
				Collectors.toMap(item -> ((EmTimeZoneSet) item).getEmploymentTimeFrameNo(), Function.identity()));
		if (mapEmTimezone.isEmpty()) {
			this.lstWorkingTimezone = new ArrayList<>();
		} else {
			this.lstWorkingTimezone.forEach(emTimezoneOther -> {
				emTimezoneOther.restoreData(mapEmTimezone.get(emTimezoneOther.getEmploymentTimeFrameNo()));
			});
		}	
		
		// restore OTTimezone
		Map<EmTimezoneNo, OverTimeOfTimeZoneSet> mapOverTimezone = other.getLstOTTimezone().stream().collect(
				Collectors.toMap(item -> ((OverTimeOfTimeZoneSet) item).getWorkTimezoneNo(), Function.identity()));
		if (mapOverTimezone.isEmpty()) {
			this.lstOTTimezone = new ArrayList<>();
		} else {
			this.lstOTTimezone.forEach(overTimezoneOther -> {
				overTimezoneOther.restoreData(mapOverTimezone.get(overTimezoneOther.getWorkTimezoneNo()));
			});
		}	
	}
	
	/**
	 * Restore default data.
	 */
	public void restoreDefaultData() {
		this.lstWorkingTimezone = new ArrayList<>();
		this.lstOTTimezone = new ArrayList<>();
	}
	
	/**
	 * Correct default data.
	 */
	public void correctDefaultData() {
		this.lstOTTimezone.forEach(item -> item.correctDefaultData());
	}
	
	@Override
	public FixedWorkTimezoneSet clone() {
		FixedWorkTimezoneSet cloned = new FixedWorkTimezoneSet();
		try {
			cloned.lstWorkingTimezone = this.lstWorkingTimezone.stream().map(c -> c.clone()).collect(Collectors.toList());
			cloned.lstOTTimezone = this.lstOTTimezone.stream().map(c -> c.clone()).collect(Collectors.toList());
		}
		catch (Exception e){
			throw new RuntimeException("FixedWorkTimezoneSet clone error.");
		}
		return cloned;
	}
}
