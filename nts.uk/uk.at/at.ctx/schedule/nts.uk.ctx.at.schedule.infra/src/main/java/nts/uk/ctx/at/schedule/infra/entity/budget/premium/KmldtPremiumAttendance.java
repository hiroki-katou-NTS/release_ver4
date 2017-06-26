package nts.uk.ctx.at.schedule.infra.entity.budget.premium;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="KMLDT_PREMIUM_ATTENDANCE")
public class KmldtPremiumAttendance extends UkJpaEntity{
	@EmbeddedId
	public KmldpPremiumAttendancePK kmldpPremiumAttendancePK;
	
	@ManyToOne
	@PrimaryKeyJoinColumns({
		@PrimaryKeyJoinColumn(name="CID",referencedColumnName="CID"), 
		@PrimaryKeyJoinColumn(name="HIS_ID",referencedColumnName="HIS_ID"),
		@PrimaryKeyJoinColumn(name="PREMIUM_ID",referencedColumnName="PREMIUM_ID")
	})
	private KmlstPremiumSet kmlstPremiumSet;
	
	@Override
	protected Object getKey() {
		return kmldpPremiumAttendancePK;
	}
}
