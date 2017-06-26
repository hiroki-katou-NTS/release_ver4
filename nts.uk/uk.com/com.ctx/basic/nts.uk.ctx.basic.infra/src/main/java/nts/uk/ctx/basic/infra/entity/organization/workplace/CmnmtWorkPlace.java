package nts.uk.ctx.basic.infra.entity.organization.workplace;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CMNMT_WORK_PLACE")
public class CmnmtWorkPlace implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CmnmtWorkPlacePK cmnmtWorkPlacePK;

	@Column(name = "STR_D")
	@Temporal(TemporalType.DATE)
	private GeneralDate startDate;

	@Column(name = "END_D")
	@Temporal(TemporalType.DATE)
	private GeneralDate endDate;

	@Column(name = "WKPNAME")
	private String name;

	@Column(name = "WKPNAME_TOTAL")
	private String genericName;

	@Column(name = "HIERARCHY_ID")
	private String hierarchyId;

	@Column(name = "WKP_OUT_CD")
	private String externalCode;

	@Column(name = "PARENT_CHILD_ATR1")
	private String parentChildAttribute1;

	@Column(name = "PARENT_CHILD_ATR2")
	private String parentChildAttribute2;

	@Column(name = "PARENT_WKPCD1")
	private String parentWorkCode1;

	@Column(name = "PARENT_WKPCD2")
	private String parentWorkCode2;

	public CmnmtWorkPlacePK getCmnmtWorkPlacePK() {
		return cmnmtWorkPlacePK;
	}

	public void setCmnmtWorkPlacePK(CmnmtWorkPlacePK cmnmtWorkPlacePK) {
		this.cmnmtWorkPlacePK = cmnmtWorkPlacePK;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
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

	public String getParentChildAttribute1() {
		return parentChildAttribute1;
	}

	public void setParentChildAttribute1(String parentChildAttribute1) {
		this.parentChildAttribute1 = parentChildAttribute1;
	}

	public String getParentChildAttribute2() {
		return parentChildAttribute2;
	}

	public void setParentChildAttribute2(String parentChildAttribute2) {
		this.parentChildAttribute2 = parentChildAttribute2;
	}

	public String getParentWorkCode1() {
		return parentWorkCode1;
	}

	public void setParentWorkCode1(String parentWorkCode1) {
		this.parentWorkCode1 = parentWorkCode1;
	}

	public String getParentWorkCode2() {
		return parentWorkCode2;
	}

	public void setParentWorkCode2(String parentWorkCode2) {
		this.parentWorkCode2 = parentWorkCode2;
	}

}
