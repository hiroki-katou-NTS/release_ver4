package nts.uk.ctx.bs.person.infra.entity.person.info.setting.reghistory;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.person.dom.person.info.setting.reghistory.EmpRegHistory;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * @author sonnlb
 *
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PPEDT_EMP_REG_HISTORY")
public class PpedtEmployeeRegistrationHistory extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public PpedtEmployeeRegistrationHistoryPk ppedtEmployeeRegistrationHistoryPk;

	/**
	 * 会社ID
	 */

	@Basic(optional = false)
	@Column(name = "CID")
	public String companyId;

	/**
	 * 登録日時
	 */

	@Basic(optional = true)
	@Column(name = "REG_DATE")
	public GeneralDate registeredDate;

	/**
	 * 最後に登録した社員ID
	 */

	@Basic(optional = false)
	@Column(name = "LAST_REG_SID")
	public String lastRegEmployeeID;

	@Override
	protected Object getKey() {
		return ppedtEmployeeRegistrationHistoryPk;
	}

	public static PpedtEmployeeRegistrationHistory fromDomain(EmpRegHistory domain) {
		return new PpedtEmployeeRegistrationHistory(
				new PpedtEmployeeRegistrationHistoryPk(domain.getRegisteredEmployeeID()), domain.getCompanyId(),
				domain.getRegisteredDate(), domain.getLastRegEmployeeID());

	}

	public PpedtEmployeeRegistrationHistory updateFromDomain(EmpRegHistory domain) {

		this.registeredDate = domain.getRegisteredDate();
		this.lastRegEmployeeID = domain.getLastRegEmployeeID();
		return this;

	}

}
