/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.command.worktime.difftimeset.dto;

import java.util.List;
import java.util.stream.Collectors;

import nts.uk.ctx.at.shared.app.command.worktime.common.dto.EmTimeZoneSetDto;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeOTTimezoneSet;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimezoneSetting;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimezoneSettingGetMemento;

public class DiffTimezoneSettingDto {

	/** The employment timezone. */
	private List<EmTimeZoneSetDto> employmentTimezones;

	/** The OT timezone. */
	private List<DiffTimeOTTimezoneSetDto> oTTimezones;

	/**
	 * To domain.
	 *
	 * @return the diff timezone setting
	 */
	public DiffTimezoneSetting toDomain() {
		return new DiffTimezoneSetting(new DiffTimezoneSettingImpl(this));
	}

	/**
	 * The Class DiffTimezoneSettingImpl.
	 */
	public class DiffTimezoneSettingImpl implements DiffTimezoneSettingGetMemento {

		/** The dto. */
		private DiffTimezoneSettingDto dto;

		/**
		 * Instantiates a new diff timezone setting impl.
		 *
		 * @param diffTimezoneSettingDto the diff timezone setting dto
		 */
		public DiffTimezoneSettingImpl(DiffTimezoneSettingDto diffTimezoneSettingDto) {
			this.dto = diffTimezoneSettingDto;
		}

		@Override
		public List<EmTimeZoneSet> getEmploymentTimezones() {
			return this.dto.employmentTimezones.stream().map(item -> {
				return new EmTimeZoneSet(item);
			}).collect(Collectors.toList());
		}

		@Override
		public List<DiffTimeOTTimezoneSet> getOTTimezones() {
			return this.dto.oTTimezones.stream().map(item -> {
				return item.toDomain();
			}).collect(Collectors.toList());
		}

	}
}
