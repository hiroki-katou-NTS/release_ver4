package nts.uk.ctx.at.schedule.infra.entity.schedule.alarm.continuouswork;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.val;
import nts.arc.layer.infra.data.jdbc.map.JpaEntityMapper;
import nts.uk.ctx.at.schedule.dom.schedule.alarm.continuouswork.NumberOfConsecutiveDays;
import nts.uk.ctx.at.schedule.dom.schedule.alarm.continuouswork.continuousattendance.MaxNumberDaysOfContinuousAttendance;
import nts.uk.ctx.at.schedule.dom.schedule.alarm.continuouswork.continuousattendance.MaxNumberDaysOfContinuousAttendanceCom;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;

/**
 * 
 * @author hiroko_miura
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "KSCMT_ALCHK_CONSECUTIVE_WORK_CMP")
public class KscmtAlchkConsecutiveWorkCmp extends ContractUkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final JpaEntityMapper<KscmtAlchkConsecutiveWorkCmp> MAPPER = new JpaEntityMapper<>(KscmtAlchkConsecutiveWorkCmp.class);

	/**
	 * 会社ID
	 */
	@Id
	@Column(name = "CID")
	public String companyId;
	
	/**
	 * 会社の連続出勤できる上限日数
	 */
	@Column(name = "MAX_CONSECUTIVE_DAYS")
	public int maxConsDays;
	
	@Override
	protected Object getKey() {
		return this.companyId;
	}
	
	/**
	 * convert to entity
	 * @param domain
	 * @return
	 */
	public static KscmtAlchkConsecutiveWorkCmp of (String companyId, MaxNumberDaysOfContinuousAttendanceCom domain) {
		val entity = new KscmtAlchkConsecutiveWorkCmp();
		entity.companyId = companyId;
		entity.maxConsDays = domain.getNumberOfDays().getNumberOfDays().v();
		return entity;
	}
	
	/**
	 * convert to domain
	 * @return
	 */
	public MaxNumberDaysOfContinuousAttendanceCom toDomain() {
		return new MaxNumberDaysOfContinuousAttendanceCom(
				new MaxNumberDaysOfContinuousAttendance(new NumberOfConsecutiveDays(this.maxConsDays)));
	}
}
