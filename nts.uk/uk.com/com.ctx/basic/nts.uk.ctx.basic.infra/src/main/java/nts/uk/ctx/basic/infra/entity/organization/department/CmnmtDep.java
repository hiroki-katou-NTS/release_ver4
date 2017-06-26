package nts.uk.ctx.basic.infra.entity.organization.department;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CMNMT_DEP")
public class CmnmtDep implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CmnmtDepPK cmnmtDepPK;

	@Column(name = "STR_D")
	@Temporal(TemporalType.DATE)
	@NotNull
	private GeneralDate startDate;

	@Column(name = "END_D")
	@Temporal(TemporalType.DATE)
	@NotNull
	private GeneralDate endDate;

	@Column(name = "DEPNAME")
	private String depName;

	@Column(name = "DEPNAME_TOTAL")
	private String depNameTotal;

	@Column(name = "HIERARCHY_ID")
	@NotNull
	private String hierarchyId;

	@Column(name = "DEP_OUT_CD")
	private String externalCode;

	public CmnmtDepPK getCmnmtDepPK() {
		return cmnmtDepPK;
	}

	public void setCmnmtDepPK(CmnmtDepPK cmnmtDepPK) {
		this.cmnmtDepPK = cmnmtDepPK;
	}

	public GeneralDate getStartDate() {
		return startDate;
	}

	public void setStartDate(GeneralDate startDate) {
		this.startDate = startDate;
	}

	public GeneralDate getEndDate() {
		return endDate;
	}

	public void setEndDate(GeneralDate endDate) {
		this.endDate = endDate;
	}

	public String getHierarchyId() {
		return hierarchyId;
	}

	public void setHierarchyId(String hierarchyId) {
		this.hierarchyId = hierarchyId;
	}

	public String getExternalCode() {
		return externalCode;
	}

	public void setExternalCode(String externalCode) {
		this.externalCode = externalCode;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public String getDepNameTotal() {
		return depNameTotal;
	}

	public void setDepNameTotal(String depNameTotal) {
		this.depNameTotal = depNameTotal;
	}

}
