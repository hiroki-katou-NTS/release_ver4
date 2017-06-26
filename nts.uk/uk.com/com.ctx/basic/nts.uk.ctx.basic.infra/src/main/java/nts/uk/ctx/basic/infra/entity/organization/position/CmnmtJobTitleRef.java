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
import nts.uk.shr.infra.data.entity.UkJpaEntity;
@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="CMNMT_JOB_TITLE_REF")
public class CmnmtJobTitleRef extends UkJpaEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	@EmbeddedId
    public CmnmtJobTitleRefPK cmnmtJobTitleRefPK;

	@Basic(optional = false)
	@Column(name = "REF_SET")
	public int referenceSettings;

	@Override
	protected Object getKey() {
		// TODO Auto-generated method stub
		return cmnmtJobTitleRefPK;
	}
	
	
}