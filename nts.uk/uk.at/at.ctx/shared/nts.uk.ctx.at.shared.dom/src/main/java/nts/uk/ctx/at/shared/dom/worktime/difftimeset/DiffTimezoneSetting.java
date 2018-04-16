/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.difftimeset;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Getter;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimeFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;

/**
 * The Class DiffTimezoneSetting.
 */
// 時差勤務時間帯設定
@Getter
public class DiffTimezoneSetting extends WorkTimeDomainObject {

	/** The employment timezone. */
	// 就業時間帯
	private List<EmTimeZoneSet> employmentTimezones;

	/** The OT timezone. */
	// 残業時間帯
	private List<DiffTimeOTTimezoneSet> oTTimezones;

	/**
	 * Instantiates a new diff timezone setting.
	 *
	 * @param memento the memento
	 */
	public DiffTimezoneSetting(DiffTimezoneSettingGetMemento memento) {
		this.employmentTimezones = memento.getEmploymentTimezones();
		this.oTTimezones = memento.getOTTimezones();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(DiffTimezoneSettingSetMemento memento) {
		memento.setEmploymentTimezones(this.employmentTimezones);
		memento.setOTTimezones(this.oTTimezones);
	}
	
	/**
	 * Restore data.
	 *
	 * @param other
	 *            the other
	 */
	public void correctData(DiffTimezoneSetting other) {
		// restore 就業時間帯
		Map<EmTimeFrameNo, EmTimeZoneSet> mapEmTimezone = other.getEmploymentTimezones().stream().collect(
				Collectors.toMap(item -> ((EmTimeZoneSet) item).getEmploymentTimeFrameNo(), Function.identity()));
		this.employmentTimezones.forEach(emTimezoneOther -> {
			emTimezoneOther.restoreData(mapEmTimezone.get(emTimezoneOther.getEmploymentTimeFrameNo()));
		});

		// restore 残業時間帯
		this.oTTimezones = other.getOTTimezones();
	}
	
	/**
	 * Restore default data.
	 */
	public void correctDefaultData() {
		this.employmentTimezones = new ArrayList<>();
		this.oTTimezones = new ArrayList<>();
	}
	
	@Override
	public void validate() {
		
		// Validate overlap employmentTimezones
		for (int i = 0; i < this.employmentTimezones.size(); i++) {
			EmTimeZoneSet em = this.employmentTimezones.get(i);
			for (int j = i + 1; j < this.employmentTimezones.size(); j++) {
				EmTimeZoneSet em2 = this.employmentTimezones.get(j);
				// check overlap
				if (em.getTimezone().isOverlap(em2.getTimezone())) {
					this.bundledBusinessExceptions.addMessage("Msg_515","KMK003_86");
				}
			}
		}
		
		// validate overlap oTTimezones
		for (int i = 0; i < this.oTTimezones.size(); i++) {
			DiffTimeOTTimezoneSet em = this.oTTimezones.get(i);
			for (int j = i + 1; j < this.oTTimezones.size(); j++) {
				DiffTimeOTTimezoneSet em2 = this.oTTimezones.get(j);
				// check overlap
				if (em.getTimezone().isOverlap(em2.getTimezone())) {
					this.bundledBusinessExceptions.addMessage("Msg_515","KMK003_89");
				}
			}
		}
		
		// validate worktime vs OT time
		//validate msg_845
		this.checkOverTimeAndEmTimeOverlap();
		super.validate();
	}
	
	/**
	 * Check over time and em time overlap.
	 */
	private void checkOverTimeAndEmTimeOverlap() {
		if (this.oTTimezones.stream().anyMatch(ot -> CollectionUtil.isEmpty(this.employmentTimezones)
				|| this.employmentTimezones.stream().anyMatch(em -> ot.getTimezone().isOverlap(em.getTimezone())))) {
			this.bundledBusinessExceptions.addMessage("Msg_845", "KMK003_89");
		}
	}
}
