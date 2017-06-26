package nts.uk.ctx.basic.infra.entity.organization.position;



import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Getter
@Setter
@Entity
@Table(name="CMNMT_JOB_HIST")
@AllArgsConstructor
@NoArgsConstructor
public class CmnmtJobHist extends UkJpaEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@EmbeddedId
    public CmnmtJobHistPK cmnmtJobHistPK;

	@Basic(optional = false)
	@Column(name = "STR_D")
	public GeneralDate startDate;
	
	@Basic(optional = false)
	@Column(name = "END_D")
	public GeneralDate endDate;

	@Override
	protected CmnmtJobHistPK getKey() {
		return this.cmnmtJobHistPK;
	}



}