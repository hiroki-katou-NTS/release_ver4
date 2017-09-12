/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.app.find.workplace.dto;

import nts.uk.ctx.bs.employee.dom.workplace.HistoryId;
import nts.uk.ctx.bs.employee.dom.workplace.WorkplaceId;
import nts.uk.ctx.bs.employee.dom.workplace.info.OutsideWorkplaceCode;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceCode;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceDisplayName;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceGenericName;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoSetMemento;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceName;

/**
 * The Class WorkplaceInfoDto.
 */
public class WorkplaceInfoDto implements WorkplaceInfoSetMemento {

	/** The company id. */
	// 会社ID
	public String companyId;

	/** The history id. */
	// 履歴ID
	public String historyId;

	/** The workplace id. */
	// 職場ID
	public String workplaceId;

	/** The workplace code. */
	// 職場コード
	public String workplaceCode;

	/** The workplace name. */
	// 職場名称
	public String workplaceName;

	/** The wkp generic name. */
	// 職場総称
	public String wkpGenericName;

	/** The wkp display name. */
	// 職場表示名
	public String wkpDisplayName;

	/** The outside wkp code. */
	// 職場外部コード
	public String outsideWkpCode;

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.workplace.WorkplaceInfoSetMemento#setCompanyId(java.lang.String)
	 */
	@Override
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.workplace.WorkplaceInfoSetMemento#setHistoryId(nts.uk.ctx.bs.employee.dom.workplace.HistoryId)
	 */
	@Override
	public void setHistoryId(HistoryId historyId) {
		this.historyId = historyId.v();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.workplace.WorkplaceInfoSetMemento#setWorkplaceId(nts.uk.ctx.bs.employee.dom.workplace.WorkplaceId)
	 */
	@Override
	public void setWorkplaceId(WorkplaceId workplaceId) {
		this.workplaceId = workplaceId.v();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.workplace.WorkplaceInfoSetMemento#setWorkplaceCode(nts.uk.ctx.bs.employee.dom.workplace.WorkplaceCode)
	 */
	@Override
	public void setWorkplaceCode(WorkplaceCode workplaceCode) {
		this.workplaceCode = workplaceCode.v();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.workplace.WorkplaceInfoSetMemento#setWorkplaceName(nts.uk.ctx.bs.employee.dom.workplace.WorkplaceName)
	 */
	@Override
	public void setWorkplaceName(WorkplaceName workplaceName) {
		this.workplaceName = workplaceName.v();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.workplace.WorkplaceInfoSetMemento#setWkpGenericName(nts.uk.ctx.bs.employee.dom.workplace.WorkplaceGenericName)
	 */
	@Override
	public void setWkpGenericName(WorkplaceGenericName wkpGenericName) {
		this.wkpGenericName = wkpGenericName.v();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.workplace.WorkplaceInfoSetMemento#setWkpDisplayName(nts.uk.ctx.bs.employee.dom.workplace.WorkplaceDisplayName)
	 */
	@Override
	public void setWkpDisplayName(WorkplaceDisplayName wkpDisplayName) {
		this.wkpDisplayName = wkpDisplayName.v();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.workplace.WorkplaceInfoSetMemento#setOutsideWkpCode(nts.uk.ctx.bs.employee.dom.workplace.OutsideWorkplaceCode)
	 */
	@Override
	public void setOutsideWkpCode(OutsideWorkplaceCode outsideWkpCode) {
		this.outsideWkpCode = outsideWkpCode.v();
	}

}
