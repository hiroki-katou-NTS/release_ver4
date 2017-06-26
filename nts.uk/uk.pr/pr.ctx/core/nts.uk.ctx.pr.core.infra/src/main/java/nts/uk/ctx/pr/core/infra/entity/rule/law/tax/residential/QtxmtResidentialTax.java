package nts.uk.ctx.pr.core.infra.entity.rule.law.tax.residential;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "QTXMT_RESIDENTIAL_TAX")
public class QtxmtResidentialTax extends UkJpaEntity implements Serializable{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	public QtxmtResidentialTaxPk qtxmtResidentialTaxPk;
	
	@Basic(optional = false)
	@Column(name = "EXCLUS_VER")
	public int exclusVer;
	
	@Basic(optional = false)
	@Column(name ="RESI_TAX_AUTONOMY")
	public String resiTaxAutonomy;
	
	@Basic(optional = false)
	@Column(name ="RESI_TAX_AUTONOMY_KN_NAME")
	public String resiTaxAutonomyKana;

	@Basic(optional = false)
	@Column(name ="PREFECTURE_CD")
	public String prefectureCode;
	
	@Basic(optional = true)
	@Column(name ="RESI_TAX_REPORT_CD")
	public String resiTaxReportCode;
	
	@Basic(optional = true)
	@Column(name ="REGISTERED_NAME")
	public String registeredName;
	
	@Basic(optional = true)
	@Column(name ="COMPANY_ACCOUNT_NO")
	public String companyAccountNo;
	
	@Basic(optional = true)
	@Column(name ="COMPANY_SPECIFIED_NO")
	public String companySpecifiedNo;
	
	@Basic(optional = true)
	@Column(name ="CORDINATE_POSTAL_CODE")
	public String cordinatePostalCode;
	
	@Basic(optional = true)
	@Column(name ="CORDINATE_POST_OFFICE")
	public String cordinatePostOffice;	
	
	@Basic(optional = true)
	@Column(name ="MEMO")
	public String memo;

	@Override
	protected Object getKey() {
		return qtxmtResidentialTaxPk;
	}
	

}
