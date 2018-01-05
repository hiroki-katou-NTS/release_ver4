package nts.uk.ctx.at.shared.infra.entity.specialholiday;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.infra.entity.specialholiday.yearservicecom.KshstYearServiceCom;
import nts.uk.ctx.at.shared.infra.entity.specialholiday.yearserviceper.KshstYearServicePer;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KSHST_GRANT_PERIODIC")
public class KshstGrantPeriodic extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;
		/* 主キー */
		@EmbeddedId
		public KshstGrantPeriodicPK kshstGrantPeriodicPK;
		
		/* 付与日数 */
		@Column(name = "SPLIT_ACQUISITION")
		public int splitAcquisition;
		
		/* 固定付与日数 */
		@Column(name = "GRANT_DAY")
		public int grantDay;
		
		/* 付与日数定期方法 */
		@Column(name = "GRANT_PERIODIC_METHOD")
		public int grantPerioricMethod;
		
		@OneToOne(optional = false)
		@JoinColumns({
			@JoinColumn(name = "CID", referencedColumnName="CID", insertable = false, updatable = false),
			@JoinColumn(name = "SPHD_CD", referencedColumnName="SPHD_CD", insertable = false, updatable = false)
		})
		
		public KshstSpecialHoliday specialHoliday;
		
		@OneToOne(cascade = CascadeType.ALL, mappedBy="grantPeriodic", orphanRemoval = true)
		public KshstYearServiceCom yearServiceCom;
		
		@OneToMany(cascade = CascadeType.ALL, mappedBy="grantPeriodic", orphanRemoval = true)
		public List<KshstYearServicePer> yearServicePer;
	
	@Override
	protected Object getKey() {
		return kshstGrantPeriodicPK;
	}

}