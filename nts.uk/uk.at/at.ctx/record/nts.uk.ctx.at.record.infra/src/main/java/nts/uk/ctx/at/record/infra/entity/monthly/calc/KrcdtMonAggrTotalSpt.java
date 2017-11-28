package nts.uk.ctx.at.record.infra.entity.monthly.calc;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.infra.entity.monthly.KrcdtMonAttendanceTime;
import nts.uk.ctx.at.record.infra.entity.monthly.KrcdtMonAttendanceTimePK;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 集計総拘束時間
 * @author shuichu_ishida
 */
@Entity
@Table(name = "KRCDT_MON_AGGR_TOTAL_SPT")
@NoArgsConstructor
@AllArgsConstructor
public class KrcdtMonAggrTotalSpt extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/** プライマリキー */
	@EmbeddedId
	public KrcdtMonAttendanceTimePK PK;
	
	/** 拘束残業時間 */
	@Column(name = "SPENT_OVER_TIME")
	public int overTimeSpentAtWork;
	
	/** 拘束深夜時間 */
	@Column(name = "SPENT_MIDNIGHT_TIME")
	public int midnightTimeSpentAtWork;
	
	/** 拘束休出時間 */
	@Column(name = "SPENT_HOLIDAY_TIME")
	public int holidayTimeSpentAtWork;
	
	/** 拘束差異時間 */
	@Column(name = "SPENT_VARIENCE_TIME")
	public int varienceTimeSpentAtWork;
	
	/** 総拘束時間 */
	@Column(name = "TOTAL_SPENT_TIME")
	public int totalTimeSpentAtWork;

	/** マッチング：月別実績の勤怠時間 */
	@OneToOne
	@JoinColumns({
    	@JoinColumn(name = "SID", referencedColumnName = "KRCDT_MON_ATTENDANCE_TIME.SID", insertable = false, updatable = false),
		@JoinColumn(name = "YM", referencedColumnName = "KRCDT_MON_ATTENDANCE_TIME.YM", insertable = false, updatable = false),
		@JoinColumn(name = "CLOSURE_ID", referencedColumnName = "KRCDT_MON_ATTENDANCE_TIME.CLOSURE_ID", insertable = false, updatable = false),
		@JoinColumn(name = "CLOSURE_DAY", referencedColumnName = "KRCDT_MON_ATTENDANCE_TIME.CLOSURE_DAY", insertable = false, updatable = false),
		@JoinColumn(name = "IS_LAST_DAY", referencedColumnName = "KRCDT_MON_ATTENDANCE_TIME.IS_LAST_DAY", insertable = false, updatable = false)
	})
	public KrcdtMonAttendanceTime krcdtMonAttendanceTime;
	
	/**
	 * キー取得
	 */
	@Override
	protected Object getKey() {		
		return this.PK;
	}
}
