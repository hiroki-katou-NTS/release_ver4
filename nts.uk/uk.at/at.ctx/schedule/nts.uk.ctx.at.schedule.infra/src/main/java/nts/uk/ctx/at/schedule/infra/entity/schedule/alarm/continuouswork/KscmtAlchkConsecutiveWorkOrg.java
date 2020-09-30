package nts.uk.ctx.at.schedule.infra.entity.schedule.alarm.continuouswork;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.val;
import nts.arc.layer.infra.data.jdbc.map.JpaEntityMapper;
import nts.uk.ctx.at.schedule.dom.schedule.alarm.consecutivework.ConsecutiveNumberOfDays;
import nts.uk.ctx.at.schedule.dom.schedule.alarm.consecutivework.consecutivework.MaxDaysOfConsecutiveAttendance;
import nts.uk.ctx.at.schedule.dom.schedule.alarm.consecutivework.consecutivework.MaxDaysOfConsecutiveAttendanceOrganization;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.workplace.TargetOrgIdenInfor;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.workplace.TargetOrganizationUnit;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;

/**
 * 
 * @author hiroko_miura
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "KSCMT_ALCHK_CONSECUTIVE_WORK_ORG")
public class KscmtAlchkConsecutiveWorkOrg extends ContractUkJpaEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final JpaEntityMapper<KscmtAlchkConsecutiveWorkOrg> MAPPER = new JpaEntityMapper<>(KscmtAlchkConsecutiveWorkOrg.class);
	
	@EmbeddedId
	public KscmtAlchkConsecutiveWorkOrgPk pk;

	/**
	 * 組織の連続出勤できる上限日数
	 */
	@Column(name = "MAX_CONSECUTIVE_DAYS")
	public int maxConsDays;
	
	@Override
	protected Object getKey() {
		return this.pk;
	}
	
	
	/**
	 * convert to entity
	 * @param domain
	 * @param companyId
	 * @return
	 */
	public static KscmtAlchkConsecutiveWorkOrg of (MaxDaysOfConsecutiveAttendanceOrganization domain, String companyId) {
		val entity = new KscmtAlchkConsecutiveWorkOrg();
		entity.pk.companyId = companyId;
		entity.pk.targetUnit = domain.getTargeOrg().getUnit().value;
		entity.pk.targetId = domain.getTargeOrg().getTargetId();
		entity.maxConsDays = domain.getNumberOfDays().getNumberOfDays().v();
		return entity;
	}
	
	/**
	 * convert to domain
	 * @return
	 */
	public MaxDaysOfConsecutiveAttendanceOrganization toDomain () {
		val targetOrg = TargetOrgIdenInfor.createFromTargetUnit(TargetOrganizationUnit.valueOf(this.pk.targetUnit), this.pk.targetId);
		
		return new MaxDaysOfConsecutiveAttendanceOrganization(
				  targetOrg
				, new MaxDaysOfConsecutiveAttendance(new ConsecutiveNumberOfDays(this.maxConsDays)));
	}
}
