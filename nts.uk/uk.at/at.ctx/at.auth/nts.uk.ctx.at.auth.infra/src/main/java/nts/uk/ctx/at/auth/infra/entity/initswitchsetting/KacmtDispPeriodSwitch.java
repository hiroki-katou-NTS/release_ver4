/**
 * 
 */
package nts.uk.ctx.at.auth.infra.entity.initswitchsetting;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.auth.dom.initswitchsetting.InitDisplayPeriodSwitchSet;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * @author hieult
 *
 */
@NoArgsConstructor
@Entity
@Table(name = "KACMT_DISP_PERIOD_SWITCH")
@Setter
public class KacmtDispPeriodSwitch extends UkJpaEntity implements Serializable{/* (non-Javadoc)
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	public KacmtDispPeriodSwitchPK kacmtDispPeriodSwitchPK;
	@Column(name = "DAYS")
	public int day;
	@Override
	protected Object getKey() {
		
		return kacmtDispPeriodSwitchPK;
	}
	
	public InitDisplayPeriodSwitchSet toDomain(){
		 return new InitDisplayPeriodSwitchSet(
				 this.kacmtDispPeriodSwitchPK.companyID,
				 this.kacmtDispPeriodSwitchPK.roleID,
				 this.day);
	}
	public static KacmtDispPeriodSwitch toEntity(InitDisplayPeriodSwitchSet domain){
		return new KacmtDispPeriodSwitch(
				new KacmtDispPeriodSwitchPK(domain.getCompanyID(),domain.getRoleID()),
				domain.getDay());
	}
	public KacmtDispPeriodSwitch (KacmtDispPeriodSwitchPK pK, int day){
		super();
		this.kacmtDispPeriodSwitchPK = pK;
		this.day = day;
	}

	

}
