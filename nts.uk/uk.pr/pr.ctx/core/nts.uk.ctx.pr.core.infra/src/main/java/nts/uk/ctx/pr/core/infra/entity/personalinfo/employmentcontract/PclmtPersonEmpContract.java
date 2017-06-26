package nts.uk.ctx.pr.core.infra.entity.personalinfo.employmentcontract;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import nts.arc.layer.infra.data.entity.type.GeneralDateToDBConverter;
import nts.arc.time.GeneralDate;

@Entity
@Table(name="PCLMT_PERSON_EMP_CONTRACT")
public class PclmtPersonEmpContract {

	@EmbeddedId
	public PclmtPersonEmpContractPK pclmtPersonEmpContractPK;
	
	@Column(name ="END_D")
	@Convert(converter = GeneralDateToDBConverter.class)
	public GeneralDate endD;
	
	@Column(name ="EXP_D")
	@Convert(converter = GeneralDateToDBConverter.class)
	public GeneralDate expD;
	
	@Column(name ="ISSUE_DATE")
	@Convert(converter = GeneralDateToDBConverter.class)
	public GeneralDate issueDate;
	
	@Column(name ="ISSUE_STS")
	public int issueSts;
	
	@Column(name ="EMPCD")
	public String empCd;
	
	@Column(name ="PAYROLL_CALC_ATR")
	public int payrollCalcAtr;
	
	@Column(name ="PAYROLL_SYSTEM")
	public int payrollSystem;
	
	@Column(name ="LABOR_ASSOCIATION_ATR")
	public int laborAssociationAtr;
	
	@Column(name ="CONTRACT_PERIOD_ATR")
	public int contractPriodAtr;

//	@Column(name ="QUIT_PROCEDURE")
//	public String quitProcedure;
	
//	@Column(name ="FIRING_REASON")
//	public String firingReason;
	
//	@Column(name ="QUIT_REMARK")
//	public String quitRemark;
//	
//	@Column(name ="QUIT_FIRING_ATR")
//	public String quitFiringAtr;
//	
//	@Column(name ="QUIT_FIRING_REASON_ATR")
//	public String quitFiringReasonAtr;
//	
//	@Column(name ="FIRING_REASON_A")
//	public String firingReasonA;
//	
//	@Column(name ="FIRING_REASON_B")
//	public String firingReasonB;
//	
//	@Column(name ="FIRING_REASON_C")
//	public String firingReasonC;
//	
//	@Column(name ="FIRING_REASON_D")
//	public String firingReasonD;
//	
//	@Column(name ="FIRING_REASON_E")
//	public String firingReasonE;
//	
//	@Column(name ="FIRING_REASON_F")
//	public String firingReasonF;
	
	@Column(name ="OTHER_REMARK")
	public String otherRemark;
	
	@Column(name ="AUTO_CONTRACT_RENEWAL")
	public int autoContractRenewal;
	
	@Column(name ="FIRING_PRE_PAYDATE")
	@Convert(converter = GeneralDateToDBConverter.class)
	public GeneralDate firingPrePaydate;
	
}
