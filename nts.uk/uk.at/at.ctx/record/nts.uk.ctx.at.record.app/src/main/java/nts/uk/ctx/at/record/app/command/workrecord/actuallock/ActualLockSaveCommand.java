/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.app.command.workrecord.actuallock;

import lombok.Data;
import nts.arc.time.GeneralDateTime;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.workrecord.actuallock.ActualLockGetMemento;
import nts.uk.ctx.at.record.dom.workrecord.actuallock.ActualLockHistoryGetMemento;
import nts.uk.ctx.at.record.dom.workrecord.actuallock.LockStatus;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.context.AppContexts;

/**
 * Instantiates a new actual lock history save command.
 */
@Data
public class ActualLockSaveCommand implements ActualLockHistoryGetMemento, ActualLockGetMemento {
	
	/** The closure id. */
	private int closureId;
	
	/** The daily lock state. */
	private int dailyLockState;
	
	/** The monthly lock state. */
	private int monthlyLockState;
	
	private int yearMonth;
	
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.workrecord.actuallock.ActualLockHistoryGetMemento#getCompanyId()
	 */
	@Override
	public String getCompanyId() {
		return AppContexts.user().companyId();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.workrecord.actuallock.ActualLockHistoryGetMemento#getClosureId()
	 */
	@Override
	public ClosureId getClosureId() {
		return ClosureId.valueOf(this.closureId);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.workrecord.actuallock.ActualLockHistoryGetMemento#getDailyLockState()
	 */
	@Override
	public LockStatus getDailyLockState() {
		return LockStatus.valueOf(this.dailyLockState);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.workrecord.actuallock.ActualLockHistoryGetMemento#getMonthyLockState()
	 */
	@Override
	public LockStatus getMonthyLockState() {
		return LockStatus.valueOf(this.monthlyLockState);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.workrecord.actuallock.ActualLockHistoryGetMemento#getTargetMonth()
	 */
	@Override
	public YearMonth getTargetMonth() {
		return new YearMonth(this.yearMonth);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.workrecord.actuallock.ActualLockHistoryGetMemento#getLockDateTime()
	 */
	@Override
	public GeneralDateTime getLockDateTime() {
		return GeneralDateTime.now();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.workrecord.actuallock.ActualLockHistoryGetMemento#getUpdater()
	 */
	@Override
	public String getUpdater() {
		return AppContexts.user().employeeId();
	}

}
