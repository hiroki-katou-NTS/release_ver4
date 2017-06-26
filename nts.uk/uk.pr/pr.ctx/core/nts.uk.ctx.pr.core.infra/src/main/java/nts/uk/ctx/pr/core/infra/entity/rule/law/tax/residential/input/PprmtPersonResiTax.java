package nts.uk.ctx.pr.core.infra.entity.rule.law.tax.residential.input;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="PPRMT_PERSON_RESITAX")
public class PprmtPersonResiTax {
	@EmbeddedId
	public PprmtPersonResiTaxPK pprmtPersonResiTaxPK;
	@Column(name="RESIDENCE_CD")
	public String residenceCode;
	@Column(name="RESIDENCE_TAX_BN")
	public BigDecimal residenceTaxBn;
	@Column(name="RESIDENCE_TAX_AVE")
	public BigDecimal residenceTaxAve;
	@Column(name="RESIDENCE_TAX06")
	public BigDecimal residenceTax06;
	@Column(name="RESIDENCE_TAX07")
	public BigDecimal residenceTax07;
	@Column(name="RESIDENCE_TAX08")
	public BigDecimal residenceTax08;
	@Column(name="RESIDENCE_TAX09")
	public BigDecimal residenceTax09;
	@Column(name="RESIDENCE_TAX10")
	public BigDecimal residenceTax10;
	@Column(name="RESIDENCE_TAX11")
	public BigDecimal residenceTax11;
	@Column(name="RESIDENCE_TAX12")
	public BigDecimal residenceTax12;
	@Column(name="RESIDENCE_TAX01")
	public BigDecimal residenceTax01;
	@Column(name="RESIDENCE_TAX02")
	public BigDecimal residenceTax02;
	@Column(name="RESIDENCE_TAX03")
	public BigDecimal residenceTax03;
	@Column(name="RESIDENCE_TAX04")
	public BigDecimal residenceTax04;
	@Column(name="RESIDENCE_TAX05")
	public BigDecimal residenceTax05;
	@Column(name="RESIDENCE_TAX_LUMP_ATR")
	public int residenceTaxLumpAtr;
	@Column(name="RESIDENCE_TAX_LUMP_YM")
	public int residenceTaxLumpYm;
	@Column(name="RESIDENCE_TAX_LEVY_ATR")
	public int residenceTaxLevyAtr;
}
