package nts.uk.ctx.at.request.infra.entity.application.gobackdirectly;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Entity
@Table(name="KRQDT_GO_BACK_DIRECTLY")
public class KrqdtGoBackDirectly extends UkJpaEntity{
	@EmbeddedId
	public KrqdtGoBackDirectlyPK krqdtGoBackDirectlyPK;
	
	@Column(name="WORK_TYPE_CD")
	public String workTypeCD;
	
	@Column(name="SIFT_CD")
	public String siftCD;
	
	@Column(name="WORK_CHANGE_ATR")
	public int workChangeAtr;
	
	@Column(name="WORK_TIME_START1")
	public int workTimeStart1;
	
	@Column(name="WORK_TIME_End1")
	public int workTimeEnd1;
	
	@Column(name="WORK_LOCATION_CD1")
	public String workLocationCd1;
	
	@Column(name="GO_WORK_ATR1")
	public int goWorkAtr1;

	@Column(name="BACK_HOME_ATR1")
	public int backHomeAtr1;
	
	@Column(name="WORK_TIME_START2")
	public int workTimeStart2;
	
	@Column(name="WORK_TIME_End2")
	public int workTimeEnd2;

	@Column(name="WORK_LOCATION_CD2")
	public String workLocationCd2;
	
	@Column(name="GO_WORK_ATR2")
	public int goWorkAtr2;

	@Column(name="BACK_HOME_ATR2")
	public int backHomeAtr2;
	
	@Override
	protected Object getKey() {
		return krqdtGoBackDirectlyPK;
	}

}
