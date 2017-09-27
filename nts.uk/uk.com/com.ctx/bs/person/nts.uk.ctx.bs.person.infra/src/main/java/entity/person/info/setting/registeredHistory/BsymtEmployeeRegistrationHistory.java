package entity.person.info.setting.registeredHistory;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BSYMT_EMP_REG_HISTORY")
public class BsymtEmployeeRegistrationHistory extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public BsymtEmployeeRegistrationHistoryPk bsydtEmployeeRegistrationHistoryPk;

	@Basic(optional = false)
	@Column(name = "CID")
	public String CompanyId;

	@Basic(optional = true)
	@Column(name = "REG_DATE")
	public String RegisteredDate;

	@Basic(optional = false)
	@Column(name = "LAST_REG_SID")
	public String LastRegEmployeeID;

	@Override
	protected Object getKey() {
		return bsydtEmployeeRegistrationHistoryPk;
	}

}
