/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.command.worktime.difftimeset.dto;

import nts.uk.ctx.at.shared.dom.worktime.common.AmPmAtr;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeHalfDayGetMemento;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeHalfDayWorkTimezone;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeRestTimezone;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimezoneSetting;

public class DiffTimeHalfDayWorkTimezoneDto {
	
	/** The rest timezone. */
	private DiffTimeRestTimezoneDto restTimezone;

	/** The work timezone. */
	private DiffTimezoneSettingDto workTimezone;

	/** The Am pm cls. */
	private Integer AmPmAtr;
	
	
	public DiffTimeHalfDayWorkTimezone toDomain() {
		return new DiffTimeHalfDayWorkTimezone(new DiffTimeHalfDayWorkTimezoneImpl(this));
	}
	public class DiffTimeHalfDayWorkTimezoneImpl implements DiffTimeHalfDayGetMemento {

		/** The dto. */
		private DiffTimeHalfDayWorkTimezoneDto dto;
		
		public DiffTimeHalfDayWorkTimezoneImpl(DiffTimeHalfDayWorkTimezoneDto diffTimeHalfDayWorkTimezoneDto) {
			this.dto = diffTimeHalfDayWorkTimezoneDto;
		}

		@Override
		public DiffTimeRestTimezone getRestTimezone() {
			return this.dto.restTimezone.toDomain();
		}

		@Override
		public DiffTimezoneSetting getWorkTimezone() {
			return this.dto.workTimezone.toDomain();
		}

		@Override
		public AmPmAtr getAmPmAtr() {
			// TODO Auto-generated method stub
			return null;
		}

	}
}
