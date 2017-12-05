/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.command.worktime.difftimeset.dto;

import java.util.List;
import java.util.stream.Collectors;

import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeDeductTimezone;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeRestTimezone;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeRestTimezoneGetMemento;

public class DiffTimeRestTimezoneDto {

	/** The rest timezone. */
	private List<DiffTimeDeductTimezoneDto> restTimezones;

	public DiffTimeRestTimezone toDomain() {
		return new DiffTimeRestTimezone(new DiffTimeRestTimezoneImpl(this));
	}

	public class DiffTimeRestTimezoneImpl implements DiffTimeRestTimezoneGetMemento {

		private DiffTimeRestTimezoneDto dto;

		public DiffTimeRestTimezoneImpl(DiffTimeRestTimezoneDto diffTimeRestTimezoneDto) {
			this.dto = diffTimeRestTimezoneDto;
		}

		@Override
		public List<DiffTimeDeductTimezone> getRestTimezones() {
			return this.dto.restTimezones.stream().map(item -> {
				return item.toDomain();
			}).collect(Collectors.toList());
		}

	}
}
