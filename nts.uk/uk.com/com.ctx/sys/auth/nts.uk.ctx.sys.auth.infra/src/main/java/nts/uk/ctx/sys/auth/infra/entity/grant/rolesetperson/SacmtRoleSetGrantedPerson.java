package nts.uk.ctx.sys.auth.infra.entity.grant.rolesetperson;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.layer.infra.data.entity.type.GeneralDateToDBConverter;
import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 
 * @author HungTT
 *
 */

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SACMT_ROLESET_PERSON")
public class SacmtRoleSetGrantedPerson extends UkJpaEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SID")
	public String employeeId;

	@Basic(optional = false)
	@Column(name = "ROLESET_CD")
	public String roleSetCd;

	@Basic(optional = false)
	@Column(name = "CID")
	public String companyId;
	
	@Basic(optional = false)
	@Column(name = "START_DATE")
    @Convert(converter = GeneralDateToDBConverter.class)
	public GeneralDate startDate;
    
	@Basic(optional = false)
    @Column(name = "END_DATE")
    @Convert(converter = GeneralDateToDBConverter.class)
	public GeneralDate endDate;

	@Override
	protected Object getKey() {
		return this.employeeId;
	}
	
}
