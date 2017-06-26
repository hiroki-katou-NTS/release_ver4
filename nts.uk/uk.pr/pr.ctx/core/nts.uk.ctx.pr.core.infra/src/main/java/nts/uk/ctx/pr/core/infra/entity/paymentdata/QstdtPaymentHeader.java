package nts.uk.ctx.pr.core.infra.entity.paymentdata;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.layer.infra.data.entity.type.GeneralDateToDBConverter;
import nts.arc.time.GeneralDate;

@Entity
@Table(name = "QSTDT_PAYMENT_HEADER")
@AllArgsConstructor
@NoArgsConstructor
public class QstdtPaymentHeader {
	
	@EmbeddedId
	public QstdtPaymentHeaderPK qstdtPaymentHeaderPK;
	
	@Column(name = "INV_SCD")
	public String employeeCode;
	
	@Column(name = "CNAME")
	public String companyName;
	
	@Column(name = "NAME_OFFICIAL")
	public String employeeName;
	
	@Basic(optional = false)
	@Column(name = "STD_DATE")
	@Convert(converter = GeneralDateToDBConverter.class)
	public GeneralDate standardDate;
	
	@Column(name = "EMPCD")
	public String employmentCode;
	
	@Column(name = "EMPNAME")
	public String employmentName;
	
	@Column(name = "DEPCD")
	public String departmentCode;
	
	@Column(name = "DEPNAME")
	public String departmentName;
	
	@Column(name = "DEP_OUTCD")
	public String externalDepartmentCode;
	
	@Column(name = "CLSCD")
	public String classificationCode;
	
	@Column(name = "CLSNAME")
	public String classificationName;
	
	@Column(name = "JOBCD")
	public String positionCode;
	
	@Column(name = "JOBNAME")
	public String positionName;
	
	@Basic(optional = false)
	@Column(name = "STMT_CD")
	public String specificationCode;
	
	@Column(name = "STMT_NAME")
	public String specificationName;
	
	@Column(name = "RESIDENCE_CD")
	public String residenceCode;
	
	@Column(name = "RESIDENCE_NAME")
	public String residenceName;
	
	@Basic(optional = false)
	@Column(name = "HEALTH_INSU_GRADE")
	public int healthInsuranceGrade;
	
	@Basic(optional = false)
	@Column(name = "HEALTH_INSU_AVG_EARN")
	public int healthInsuranceAverageEarn;
	
	@Basic(optional = false)
	@Column(name = "AGED_CONT_INSU_ATR")
	public int ageContinuationInsureAtr;
	
	@Basic(optional = false)
	@Column(name = "TENURE_ATR")
	public int tenureAtr;
	
	@Basic(optional = false)
	@Column(name = "TAX_ATR")
	public int taxAtr;
	
	@Basic(optional = false)
	@Column(name = "PENSION_INSU_GRADE")
	public int pensionInsuranceGrade;
	
	@Basic(optional = false)
	@Column(name = "PENSION_AVG_EARN")
	public int pensionAverageEarn;
	
	@Basic(optional = false)
	@Column(name = "EMPINSU_ATR")
	public int employmentInsuranceAtr;
	
	@Basic(optional = false)
	@Column(name = "DEPENDENT_NUMBER")
	public int dependentNumber;
	
	@Basic(optional = false)
	@Column(name = "WORK_INS_CALC_ATR")
	public int workInsuranceCalculateAtr;
	
	@Basic(optional = false)
	@Column(name = "INSURED_ATR")
	public int insuredAtr;
	
	@Basic(optional = false)
	@Column(name = "BONUS_TAX_RATE")
	public int bonusTaxRate;
	
	@Basic(optional = false)
	@Column(name = "CALC_FLG")
	public int calcFlag;
	
	@Basic(optional = false)
	@Column(name = "MAKE_METHOD_FLG")
	public int makeMethodFlag;
	
	@Basic(optional = false)
	@Column(name = "PR_POS1_CTG_ATR")
	public int printPositionCategoryATR1;
	
	@Basic(optional = false)
	@Column(name = "PR_POS2_CTG_ATR")
	public int printPositionCategoryATR2;
	
	@Basic(optional = false)
	@Column(name = "PR_POS3_CTG_ATR")
	public int printPositionCategoryATR3;
	
	@Basic(optional = false)
	@Column(name = "PR_POS4_CTG_ATR")
	public int printPositionCategoryATR4;
	
	@Basic(optional = false)
	@Column(name = "PR_POS5_CTG_ATR")
	public int printPositionCategoryATR5;
	
	@Basic(optional = false)
	@Column(name = "PR_POS1_CTG_LINES")
	public int printPositionCategoryLines1;
	
	@Basic(optional = false)
	@Column(name = "PR_POS2_CTG_LINES")
	public int printPositionCategoryLines2;
	
	@Basic(optional = false)
	@Column(name = "PR_POS3_CTG_LINES")
	public int printPositionCategoryLines3;
	
	@Basic(optional = false)
	@Column(name = "PR_POS4_CTG_LINES")
	public int printPositionCategoryLines4;
	
	@Basic(optional = false)
	@Column(name = "PR_POS5_CTG_LINES")
	public int printPositionCategoryLines5;
	
	@Column(name = "MEMO")
	public String comment;
}
