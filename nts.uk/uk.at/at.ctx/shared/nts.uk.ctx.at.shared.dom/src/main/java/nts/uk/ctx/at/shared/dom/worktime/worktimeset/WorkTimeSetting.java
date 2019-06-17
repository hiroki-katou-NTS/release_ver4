/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.worktimeset;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.common.color.ColorCode;
import nts.uk.ctx.at.shared.dom.worktime.common.AbolishAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeAggregateRoot;
import nts.uk.shr.com.primitive.Memo;

/**
 * The Class WorkTimeSetting.
 */
// 就業時間帯の設定
@Getter
@NoArgsConstructor
public class WorkTimeSetting extends WorkTimeAggregateRoot implements Cloneable{

	/** The company id. */
	// 会社ID
	private String companyId;

	/** The worktime code. */
	// コード
	private WorkTimeCode worktimeCode;

	/** The work time division. */
	// 勤務区分
	private WorkTimeDivision workTimeDivision;

	/** The abolish atr. */
	// 廃止区分
	private AbolishAtr abolishAtr;

	/** The color code. */
	// 色
	private ColorCode colorCode;

	/** The work time display name. */
	// 表示名
	private WorkTimeDisplayName workTimeDisplayName;

	/** The memo. */
	// メモ
	private Memo memo;

	/** The note. */
	// 備考
	private WorkTimeNote note;

	/**
	 * Instantiates a new work time setting.
	 *
	 * @param memento the memento
	 */
	public WorkTimeSetting(WorkTimeSettingGetMemento memento) {
		this.companyId = memento.getCompanyId();
		this.worktimeCode = memento.getWorktimeCode();
		this.workTimeDivision = memento.getWorkTimeDivision();
		this.abolishAtr = memento.getAbolishAtr();
		this.colorCode = memento.getColorCode();
		this.workTimeDisplayName = memento.getWorkTimeDisplayName();
		this.memo = memento.getMemo();
		this.note = memento.getNote();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(WorkTimeSettingSetMemento memento) {
		memento.setCompanyId(this.companyId);
		memento.setWorktimeCode(this.worktimeCode);
		memento.setWorkTimeDivision(this.workTimeDivision);
		memento.setAbolishAtr(this.abolishAtr);
		memento.setColorCode(this.colorCode);
		memento.setWorkTimeDisplayName(this.workTimeDisplayName);
		memento.setMemo(this.memo);
		memento.setNote(this.note);
	}
	
	/**
	 * Checks if is abolish.
	 *
	 * @return true, if is abolish
	 */
	public boolean isAbolish(){
		return this.abolishAtr == AbolishAtr.ABOLISH;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((worktimeCode == null) ? 0 : worktimeCode.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof WorkTimeSetting))
			return false;
		WorkTimeSetting other = (WorkTimeSetting) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (worktimeCode == null) {
			if (other.worktimeCode != null)
				return false;
		} else if (!worktimeCode.equals(other.worktimeCode))
			return false;
		return true;
	}

	public WorkTimeSetting(String companyId, WorkTimeCode worktimeCode, WorkTimeDivision workTimeDivision,
			AbolishAtr abolishAtr, ColorCode colorCode, WorkTimeDisplayName workTimeDisplayName, Memo memo,
			WorkTimeNote note) {
		super();
		this.companyId = companyId;
		this.worktimeCode = worktimeCode;
		this.workTimeDivision = workTimeDivision;
		this.abolishAtr = abolishAtr;
		this.colorCode = colorCode;
		this.workTimeDisplayName = workTimeDisplayName;
		this.memo = memo;
		this.note = note;
	}
	
	/**
	 * create this Instance
	 * @return new Instance
	 */
	@Override
	public WorkTimeSetting clone() {
		WorkTimeSetting cloned = new WorkTimeSetting();
		try {
			cloned.companyId = this.companyId;
			cloned.worktimeCode = new WorkTimeCode(this.worktimeCode.v());
			cloned.workTimeDivision = this.workTimeDivision.clone();
			cloned.abolishAtr = AbolishAtr.valueOf(this.abolishAtr.value);
			cloned.colorCode = new ColorCode(this.colorCode.v());
			cloned.workTimeDisplayName = this.workTimeDisplayName.clone();
			cloned.memo = new Memo(this.memo.v());
			cloned.note = new WorkTimeNote(this.note.v());
		}	
		catch (Exception e){
			throw new RuntimeException("WorkTimeSetting clone error.");
		}
		return cloned;
	}

}
