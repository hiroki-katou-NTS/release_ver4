package nts.uk.ctx.at.function.infra.entity.alarm.checkcondition;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class KrcmtFixedConditionWorkRecordPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "DAILY_ALARM_CON_ID")
	public String dailyAlarmConID;

	@Column(name = "FIX_CON_WORK_RECORD_NO")
	public int fixConWorkRecordNo;

}
