/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.dom.workrecord.closure;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.DomainObject;

/**
 * The Class ClosureHistorry.
 */
// 締め変更履歴
@Getter
@Setter
public class ClosureHistory extends DomainObject{
	
	/** The close name. */
	// 名称: 締め名称
	private CloseName closeName;
	
	/** The closure id. */
	// 締めＩＤ
	private ClosureId closureId;
	
	/** The closure year. */
	// 終了年月: 年月
	private ClosureYearMonth endDate;
	
	/** The closure date. */
	// 締め日: 日付
	private ClosureDate closureDate;
	
	/** The start date. */
	// 開始年月: 年月
	private ClosureYearMonth startDate;

	
	/**
	 * Instantiates a new closure history.
	 *
	 * @param memento the memento
	 */
	public ClosureHistory(ClosureHistoryGetMemento memento){
		this.closeName = memento.getCloseName();
		this.closureId = memento.getClosureId();
		this.endDate = memento.getEndDate();
		this.closureDate = memento.getClosureDate();
		this.startDate = memento.getStartDate();
	}
	
	
	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(ClosureHistorySetMemento memento){
		memento.setCloseName(this.closeName);
		memento.setClosureId(this.closureId);
		memento.setEndDate(this.endDate);
		memento.setClosureDate(this.closureDate);
		memento.setStartDate(this.startDate);
	}
}
