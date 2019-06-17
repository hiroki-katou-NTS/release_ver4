/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.worktimeset;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;

/**
 * The Class WorkTimeDisplayName.
 */
//就業時間帯の表示名
@Getter
@NoArgsConstructor
@Builder
public class WorkTimeDisplayName extends WorkTimeDomainObject implements Cloneable{
	
	/** The work time name. */
	//名称
	private WorkTimeName workTimeName;
	
	/** The work time ab name. */
	//略名
	private WorkTimeAbName workTimeAbName;
	
	/** The work time symbol. */
	//記号
	private WorkTimeSymbol workTimeSymbol;

	public WorkTimeDisplayName(WorkTimeName workTimeName, WorkTimeAbName workTimeAbName,
			WorkTimeSymbol workTimeSymbol) {
		super();
		this.workTimeName = workTimeName;
		this.workTimeAbName = workTimeAbName;
		this.workTimeSymbol = workTimeSymbol;
	}
	
	@Override
	public WorkTimeDisplayName clone() {
		WorkTimeDisplayName cloned = new WorkTimeDisplayName();
		try {
			cloned.workTimeName = new WorkTimeName(this.workTimeName.v());
			cloned.workTimeAbName = new WorkTimeAbName(this.workTimeAbName.v());
			cloned.workTimeSymbol = new WorkTimeSymbol(workTimeSymbol.v());
		}
		catch (Exception e){
			throw new RuntimeException("WorkTimeDisplayName clone error.");
		}
		return cloned;
	}
}
