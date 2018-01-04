package nts.uk.ctx.at.shared.infra.entity.specialholiday;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KSHST_GRANT_REGULAR")
public class KshstGrantRegular extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/* 主キー */
	@EmbeddedId
	public KshstGrantRegularPK kshstGrantRegularPK;

	/* 付与開始日 */
	@Column(name = "GRANT_START_DATE")
	public GeneralDate grantStartDate;

	/* 月数 */
	@Column(name = "MONTHS")
	public int months;

	/* 年数 */
	@Column(name = "YEARS")
	public int years;

	/* 付与日定期方法 */
	@Column(name = "GRANT_REGULAR_METHOD")
	public int grantRegularMethod;

	@OneToOne(optional = false)
	@JoinColumns({
		@JoinColumn(name = "CID", referencedColumnName="CID", insertable = false, updatable = false),
		@JoinColumn(name = "SPHD_CD", referencedColumnName="SPHD_CD", insertable = false, updatable = false)
	})
	public KshstSpecialHoliday specialHoliday;
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy="grantRegular", orphanRemoval = true)
	public KshstGrantDateCom grantDateCom;
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy="grantRegular", orphanRemoval = true)
	public KshstGrantDatePer grantDatePer;

	@Override
	protected Object getKey() {
		// TODO Auto-generated method stub
		return kshstGrantRegularPK;
	}
}
