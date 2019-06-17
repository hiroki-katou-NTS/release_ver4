/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.common.timerounding.TimeRoundingSetting;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;

/**
 * The Class OtherEmTimezoneLateEarlySet.
 */
//就業時間帯の遅刻・早退別設定
@Getter
@NoArgsConstructor
public class OtherEmTimezoneLateEarlySet extends WorkTimeDomainObject implements Cloneable{

	/** The del time rounding set. */
	//控除時間丸め設定
	private TimeRoundingSetting delTimeRoundingSet; 
	
	/** The stamp exactly time is late early. */
	//時間丁度の打刻は遅刻・早退とする
	private boolean stampExactlyTimeIsLateEarly;
	
	/** The grace time set. */
	//猶予時間設定
	private GraceTimeSetting graceTimeSet;
	
	/** The record time rounding set. */
	//計上時間丸め設定
	private TimeRoundingSetting recordTimeRoundingSet;
	
	/** The late early atr. */
	//遅刻早退区分
	private LateEarlyAtr lateEarlyAtr;
	
	/**
	 * Instantiates a new other em timezone late early set.
	 *
	 * @param memento the memento
	 */
	public OtherEmTimezoneLateEarlySet(OtherEmTimezoneLateEarlySetGetMemento memento) {
		this.delTimeRoundingSet = memento.getDelTimeRoundingSet();
		this.stampExactlyTimeIsLateEarly = memento.getStampExactlyTimeIsLateEarly();
		this.graceTimeSet = memento.getGraceTimeSet();
		this.recordTimeRoundingSet = memento.getRecordTimeRoundingSet();
		this.lateEarlyAtr =memento.getLateEarlyAtr();
	}
	
	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(OtherEmTimezoneLateEarlySetSetMemento memento){
		memento.setDelTimeRoundingSet(this.delTimeRoundingSet);
		memento.setStampExactlyTimeIsLateEarly(this.stampExactlyTimeIsLateEarly);
		memento.setGraceTimeSet(this.graceTimeSet);
		memento.setRecordTimeRoundingSet(this.recordTimeRoundingSet);
		memento.setLateEarlyAtr(this.lateEarlyAtr);
	}
	
	/**
	 * 丸め設定を取得する
	 * 引数によって、控除か計上のどちらの丸めを取得したか判断する
	 * @param isDeduction　控除である
	 * @return　丸め設定
	 */
	public TimeRoundingSetting getRoundingSetByDedAtr(boolean isDeduction) {
		if(isDeduction) {
			return delTimeRoundingSet;
		}
		else {
			return recordTimeRoundingSet;
		}
	}
	
	@Override
	public OtherEmTimezoneLateEarlySet clone() {
		OtherEmTimezoneLateEarlySet cloned = new OtherEmTimezoneLateEarlySet();
		try {
			cloned.delTimeRoundingSet = this.delTimeRoundingSet.clone();
			cloned.stampExactlyTimeIsLateEarly = this.stampExactlyTimeIsLateEarly ? true : false ;
			cloned.graceTimeSet = this.graceTimeSet.clone();
			cloned.recordTimeRoundingSet = this.recordTimeRoundingSet.clone();
			cloned.lateEarlyAtr = LateEarlyAtr.valueOf(this.lateEarlyAtr.value);
		}
		catch (Exception e){
			throw new RuntimeException("OtherEmTimezoneLateEarlySet clone error.");
		}
		return cloned;
	}
}
