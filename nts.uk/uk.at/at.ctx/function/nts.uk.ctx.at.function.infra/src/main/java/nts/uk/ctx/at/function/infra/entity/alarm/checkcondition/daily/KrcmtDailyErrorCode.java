package nts.uk.ctx.at.function.infra.entity.alarm.checkcondition.daily;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@NoArgsConstructor
@Entity
@Table(name = "KRCMT_DAILY_ERROR_CODE_PK")
public class KrcmtDailyErrorCode extends UkJpaEntity implements Serializable  {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	
	public KrcmtDailyErrorCodePK krcmtDailyErrorCodePK;
	
	@ManyToOne
	@JoinColumn(name="DAILY_ALARM_CON_ID", referencedColumnName="DAILY_ALARM_CON_ID", insertable = false, updatable = false)
	public KrcmtDailyAlarmCondition errorcode;
	
	@Override
	protected Object getKey() {
		return krcmtDailyErrorCodePK;
	}

	public KrcmtDailyErrorCode(KrcmtDailyErrorCodePK krcmtDailyErrorCodePK) {
		super();
		this.krcmtDailyErrorCodePK = krcmtDailyErrorCodePK;
	}
	
	public static KrcmtDailyErrorCode toEntity(String dailyAlarmConID, String errorAlarmID) {
		return new KrcmtDailyErrorCode(new KrcmtDailyErrorCodePK(dailyAlarmConID, errorAlarmID));
	}
	
	public static List<KrcmtDailyErrorCode> toEntity(String dailyAlarmConID, List<String> errorAlarmCode) {
		List<KrcmtDailyErrorCode> data = new ArrayList<>();
		for(String errorAlarmID : errorAlarmCode ) {
			data.add(KrcmtDailyErrorCode.toEntity(dailyAlarmConID, errorAlarmID));
		}
		return data;
	}
	
}
