/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.repository.executionlog;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.executionlog.CreateMethodAtr;
import nts.uk.ctx.at.schedule.dom.executionlog.ExecutionContentSetMemento;
import nts.uk.ctx.at.schedule.dom.executionlog.ImplementAtr;
import nts.uk.ctx.at.schedule.dom.executionlog.ProcessExecutionAtr;
import nts.uk.ctx.at.schedule.dom.executionlog.ReCreateAtr;
import nts.uk.ctx.at.schedule.infra.entity.executionlog.KscmtScheduleExcLog;
import nts.uk.ctx.at.schedule.infra.entity.executionlog.KscmtScheduleExcLogPK;

/**
 * The Class JpaExecutionContentSetMemento.
 */
public class JpaExecutionContentSetMemento implements ExecutionContentSetMemento{
	
	//YYYYMMDD
	/** The Constant MUL_YEAR. */
	public static final int MUL_YEAR = 10000;
	
	/** The Constant MUL_MONTH. */
	public static final int MUL_MONTH = 100;
	
	/** The Constant TRUE_VALUE. */
	public static final int TRUE_VALUE = 1;
	
	/** The Constant FALSE_VALUE. */
	public static final int FALSE_VALUE = 0;
	
	/** The entity. */
	private KscmtScheduleExcLog entity; 

	/**
	 * Instantiates a new jpa execution content set memento.
	 *
	 * @param entity the entity
	 */
	public JpaExecutionContentSetMemento(KscmtScheduleExcLog entity) {
		if(entity.getKscmtScheduleExcLogPK() == null){
			entity.setKscmtScheduleExcLogPK(new KscmtScheduleExcLogPK());
		}
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.executionlog.ExecutionContentSetMemento#
	 * setCopyStartDate(nts.arc.time.GeneralDate)
	 */
	@Override
	public void setCopyStartDate(GeneralDate copyStartDate) {
		this.entity.setCopyStartYmd(copyStartDate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.executionlog.ExecutionContentSetMemento#
	 * setCreateMethodAtr(nts.uk.ctx.at.schedule.dom.executionlog.
	 * CreateMethodAtr)
	 */
	@Override
	public void setCreateMethodAtr(CreateMethodAtr createMethodAtr) {
		this.entity.setCreateMethodAtr(createMethodAtr.value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.executionlog.ExecutionContentSetMemento#
	 * setConfirm(java.lang.Boolean)
	 */
	@Override
	public void setConfirm(Boolean confirm) {
		this.entity.setConfirm(confirm ? TRUE_VALUE : FALSE_VALUE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.executionlog.ExecutionContentSetMemento#
	 * setImplementAtr(nts.uk.ctx.at.schedule.dom.executionlog.ImplementAtr)
	 */
	@Override
	public void setImplementAtr(ImplementAtr implementAtr) {
		this.entity.setImplementAtr(implementAtr.value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.executionlog.ExecutionContentSetMemento#
	 * setProcessExecutionAtr(nts.uk.ctx.at.schedule.dom.executionlog.
	 * ProcessExecutionAtr)
	 */
	@Override
	public void setProcessExecutionAtr(ProcessExecutionAtr processExecutionAtr) {
		this.entity.setProcessExeAtr(processExecutionAtr.value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.executionlog.ExecutionContentSetMemento#
	 * setReCreateAtr(nts.uk.ctx.at.schedule.dom.executionlog.ReCreateAtr)
	 */
	@Override
	public void setReCreateAtr(ReCreateAtr reCreateAtr) {
		this.entity.setReCreateAtr(reCreateAtr.value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.executionlog.ExecutionContentSetMemento#
	 * setResetMasterInfo(java.lang.Boolean)
	 */
	@Override
	public void setResetMasterInfo(Boolean resetMasterInfo) {
		this.entity.setReMasterInfo(resetMasterInfo ? TRUE_VALUE : FALSE_VALUE);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.executionlog.ExecutionContentSetMemento#
	 * setResetAbsentHolidayBusines(java.lang.Boolean)
	 */
	@Override
	public void setResetAbsentHolidayBusines(Boolean resetAbsentHolidayBusines) {
		this.entity.setReAbstHdBusines(resetAbsentHolidayBusines ? TRUE_VALUE : FALSE_VALUE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.executionlog.ExecutionContentSetMemento#
	 * setResetWorkingHours(java.lang.Boolean)
	 */
	@Override
	public void setResetWorkingHours(Boolean resetWorkingHours) {
		this.entity.setReWorkingHours(resetWorkingHours ? TRUE_VALUE : FALSE_VALUE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.executionlog.ExecutionContentSetMemento#
	 * setResetTimeAssignment(java.lang.Boolean)
	 */
	@Override
	public void setResetTimeAssignment(Boolean resetTimeAssignment) {
		this.entity.setReTimeAssignment(resetTimeAssignment ? TRUE_VALUE : FALSE_VALUE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.executionlog.ExecutionContentSetMemento#
	 * setResetDirectLineBounce(java.lang.Boolean)
	 */
	@Override
	public void setResetDirectLineBounce(Boolean resetDirectLineBounce) {
		this.entity.setReDirLineBounce(resetDirectLineBounce ? TRUE_VALUE : FALSE_VALUE);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.executionlog.ExecutionContentSetMemento#
	 * setResetTimeChildCare(java.lang.Boolean)
	 */
	@Override
	public void setResetTimeChildCare(Boolean resetTimeChildCare) {
		this.entity.setReTimeChildCare(resetTimeChildCare ? TRUE_VALUE : FALSE_VALUE);

	}

	

}
