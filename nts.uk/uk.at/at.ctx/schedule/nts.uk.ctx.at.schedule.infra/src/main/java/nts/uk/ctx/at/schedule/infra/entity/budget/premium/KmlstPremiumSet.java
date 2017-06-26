package nts.uk.ctx.at.schedule.infra.entity.budget.premium;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name="KMLST_PREMIUM_SET")
public class KmlstPremiumSet extends UkJpaEntity{
	@EmbeddedId
	public KmlspPremiumSetPK kmlspPremiumSet;
	
	@Column(name="PREMIUM_RATE")
	public Integer premiumRate;
	

	@ManyToOne
	@PrimaryKeyJoinColumns({
		@PrimaryKeyJoinColumn(name="CID",referencedColumnName="CID"), 
		@PrimaryKeyJoinColumn(name="HIS_ID",referencedColumnName="HIS_ID")
	})
	private KmlmtPersonCostCalculation kmlmtPersonCostCalculation;
	
	@ManyToOne
	@PrimaryKeyJoinColumns(value = {
		@PrimaryKeyJoinColumn(name="CID",referencedColumnName="CID"),
		@PrimaryKeyJoinColumn(name="PREMIUM_ID",referencedColumnName="ID")
    })
	public KmnmtPremiumItem kmnmtPremiumItem;
	
	@OneToMany(targetEntity = KmldtPremiumAttendance.class, cascade = CascadeType.ALL, mappedBy = "kmlstPremiumSet")
	@JoinTable(name = "KMLDT_PREMIUM_ATTENDANCE")
	public List<KmldtPremiumAttendance> kmldtPremiumAttendances;
	
	@Override
	protected Object getKey() {
		return kmlspPremiumSet;
	}
}
