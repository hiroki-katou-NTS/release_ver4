package nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.monthlycheckcondition.checkremainnumber;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem.CompareRange;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.checkremainnumber.CheckConValueRemainingNumber;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;

@Entity
@NoArgsConstructor
@Table(name = "KRCMT_ALST_CHKMON_UDREMVR")
public class KrcmtAlstChkmonUdremvr extends ContractUkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ERAL_CHECK_ID")
	public String errorAlarmCheckID;

	@Column(name = "COMPARE_OPERATOR")
	public int compareOperator;
	
	@Column(name = "START_VALUE_DAY")
	public BigDecimal startValueDay;
	
	@Column(name = "START_VALUE_TIME")
	public Integer startValueTime;
	
	@Column(name = "END_VALUE_DAY")
	public BigDecimal endValueDay;
	
	@Column(name = "END_VALUE_TIME")
	public Integer endValueTime;
	
	@OneToOne
	@JoinColumn(name = "ERAL_CHECK_ID", referencedColumnName = "ERAL_CHECK_ID", insertable = false, updatable = false)
	public KrcmtCheckRemainNumberMon comparerange;
	
	@Override
	protected Object getKey() {
		return errorAlarmCheckID;
	}

	public KrcmtAlstChkmonUdremvr(String errorAlarmCheckID, int compareOperator, BigDecimal startValueDay, Integer startValueTime, BigDecimal endValueDay, Integer endValueTime) {
		super();
		this.errorAlarmCheckID = errorAlarmCheckID;
		this.compareOperator = compareOperator;
		this.startValueDay = startValueDay;
		this.startValueTime = startValueTime;
		this.endValueDay = endValueDay;
		this.endValueTime = endValueTime;
	}
	
	public static KrcmtAlstChkmonUdremvr toEntity(String errorAlarmCheckID,CompareRange<CheckConValueRemainingNumber> domain) {
		return new KrcmtAlstChkmonUdremvr(
				errorAlarmCheckID,
				domain.getCompareOperator().value,
				domain.getStartValue().getDaysValue(),
				domain.getStartValue().getTimeValue() ==null?null:(!domain.getStartValue().getTimeValue().isPresent()?null:domain.getStartValue().getTimeValue().get()),
				domain.getEndValue().getDaysValue(),
				domain.getEndValue().getTimeValue()==null?null:(!domain.getEndValue().getTimeValue().isPresent()?null:domain.getEndValue().getTimeValue().get())
				);
		
	}
	
	public CompareRange<CheckConValueRemainingNumber> toDomain() {
		CheckConValueRemainingNumber data1 = new CheckConValueRemainingNumber(this.startValueDay, 
				this.startValueTime==null?null:Optional.of(this.startValueTime.intValue())
				);
		CheckConValueRemainingNumber data2 = new CheckConValueRemainingNumber(this.endValueDay, 
				this.endValueTime==null?null:Optional.of(this.endValueTime.intValue())
				);
		CompareRange<CheckConValueRemainingNumber> data = new CompareRange<>(this.compareOperator);
		data.setStartValue(data1);
		data.setEndValue(data2);
		
		return data;
	}
	

}
