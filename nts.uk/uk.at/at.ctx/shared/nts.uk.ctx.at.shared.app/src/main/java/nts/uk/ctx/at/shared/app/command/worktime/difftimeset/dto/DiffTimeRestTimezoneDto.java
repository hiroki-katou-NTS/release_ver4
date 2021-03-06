/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.command.worktime.difftimeset.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeDeductTimezone;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeRestTimezone;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeRestTimezoneGetMemento;

/**
 * The Class DiffTimeRestTimezoneDto.
 */

@Getter
@Setter
public class DiffTimeRestTimezoneDto {

	/** The rest timezone. */
	private List<DiffTimeDeductTimezoneDto> restTimezones;

	/**
	 * To domain.
	 *
	 * @return the diff time rest timezone
	 */
	public DiffTimeRestTimezone toDomain() {
		return new DiffTimeRestTimezone(new DiffTimeRestTimezoneImpl(this));
	}

	/**
	 * The Class DiffTimeRestTimezoneImpl.
	 */
	public class DiffTimeRestTimezoneImpl implements DiffTimeRestTimezoneGetMemento {

		/** The dto. */
		private DiffTimeRestTimezoneDto dto;

		/**
		 * Instantiates a new diff time rest timezone impl.
		 *
		 * @param diffTimeRestTimezoneDto the diff time rest timezone dto
		 */
		public DiffTimeRestTimezoneImpl(DiffTimeRestTimezoneDto diffTimeRestTimezoneDto) {
			this.dto = diffTimeRestTimezoneDto;
		}

		@Override
		public List<DiffTimeDeductTimezone> getRestTimezones() {
			return this.dto.restTimezones.stream()
					.sorted((item1,item2)->item1.getStart().compareTo(item2.getStart()))
					.map(item -> {
				return item.toDomain();
			}).collect(Collectors.toList());
		}

	}
}
