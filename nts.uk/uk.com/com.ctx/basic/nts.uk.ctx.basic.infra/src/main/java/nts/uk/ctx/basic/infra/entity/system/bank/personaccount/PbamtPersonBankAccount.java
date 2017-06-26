package nts.uk.ctx.basic.infra.entity.system.bank.personaccount;


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
@Table(name = "PBAMT_PERSON_BANK_ACCOUNT")
public class PbamtPersonBankAccount extends UkJpaEntity {
	
	@EmbeddedId
	public PbamtPersonBankAccountPK pbamtPersonBankAccountPK;
	
	@Column(name = "STR_YM")
	public int startYearMonth;
	
	@Column(name = "END_YM")
	public int endYearMonth;
	
	@Column(name = "USE_SET1")
	public int useSet1;
	
	@Column(name = "PRIORITY_ORDER1")
	public int priorityOrder1;
	
	@Column(name = "PAYMENT_METHOD1")
	public int paymentMethod1;
	
	@Column(name = "PARTIAL_PAY_SET1")
	public int partialPaySet1;
	
	@Column(name = "FIX_AMOUNT_MNY1")
    public int fixAmountMny1;
	
	@Column(name = "FIX_RATE1")
	public int fixRate1;
	
	@Column(name = "FROM_LINE_BANK_CD1")
	public String fromLineBankCd1;
		
	@Column(name = "TO_BRANCH_ID1")
	public String toBranchId1;
	
	@Column(name = "ACCOUNT_ATR1")
	public int accountAtr1;
	
	@Column(name = "ACCOUNT_NO1")
	public String accountNo1;
	
	@Column(name = "ACCOUNT_HOLDER_KN_NAME1")
	public String accountHolderKnName1;
	
	@Column(name = "ACCOUNT_HOLDER_NAME1")
	public String accountHolderName1;
	
	@Column(name = "USE_SET2")
	public int useSet2;
	
	@Column(name = "PRIORITY_ORDER2")
	public int priorityOrder2;
	
	@Column(name = "PAYMENT_METHOD2")
	public int paymentMethod2;
	
	@Column(name = "PARTIAL_PAY_SET2")
	public int partialPaySet2;
	
	@Column(name = "FIX_AMOUNT_MNY2")
	public int fixAmountMny2;
	
	@Column(name = "FIX_RATE2")
	public int fixRate2;
	
	@Column(name = "FROM_LINE_BANK_CD2")
	public String fromLineBankCd2;
	
	@Column(name = "TO_BRANCH_ID2")
	public String toBranchId2;
	
	@Column(name = "ACCOUNT_ATR2")
	public int accountAtr2;
	
	@Column(name = "ACCOUNT_NO2")
	public String accountNo2;
	
	@Column(name = "ACCOUNT_HOLDER_KN_NAME2")
	public String accountHolderKnName2;
	
	@Column(name = "ACCOUNT_HOLDER_NAME2")
	public String accountHolderName2;
	
	@Column(name ="USE_SET3")
	public int useSet3;
	
	@Column(name = "PRIORITY_ORDER3")
	public int priorityOrder3;
	
	@Column(name = "PAYMENT_METHOD3")
	public int paymentMethod3;
	
	@Column(name = "PARTIAL_PAY_SET3")
	public int partialPaySet3;
	
	@Column(name = "FIX_AMOUNT_MNY3")
	public int fixAmountMny3;
	
	@Column(name = "FIX_RATE3")
	public int fixRate3;
	
	@Column(name = "FROM_LINE_BANK_CD3")
	public String fromLineBankCd3;
		
	@Column(name = "TO_BRANCH_ID3")
	public String toBranchId3;
	
	@Column(name = "ACCOUNT_ATR3")
	public int accountAtr3;
	
	@Column(name = "ACCOUNT_NO3")
	public String accountNo3;
	
	@Column(name = "ACCOUNT_HOLDER_KN_NAME3")
	public String accountHolderKnName3;
	
	@Column(name = "ACCOUNT_HOLDER_NAME3")
	public String accountHolderName3;
	
	@Column(name ="USE_SET4")
	public int useSet4;
	
	@Column(name = "PRIORITY_ORDER4")
	public int priorityOrder4;
	
	@Column(name = "PAYMENT_METHOD4")
	public int paymentMethod4;
	
	@Column(name = "PARTIAL_PAY_SET4")
	public int partialPaySet4;
	
	@Column(name = "FIX_AMOUNT_MNY4")
	public int fixAmountMny4;
	
	@Column(name = "FIX_RATE4")
	public int fixRate4;
	
	@Column(name = "FROM_LINE_BANK_CD4")
	public String fromLineBankCd4;	
	
	@Column(name = "TO_BRANCH_ID4")
	public String toBranchId4;
	
	@Column(name = "ACCOUNT_ATR4")
	public int accountAtr4;
	
	@Column(name = "ACCOUNT_NO4")
	public String accountNo4;
	
	@Column(name = "ACCOUNT_HOLDER_KN_NAME4")
	public String accountHolderKnName4;
	
	@Column(name = "ACCOUNT_HOLDER_NAME4")
	public String accountHolderName4;
	
	@Column(name ="USE_SET5")
	public int useSet5;
	
	@Column(name = "PRIORITY_ORDER5")
	public int priorityOrder5;
	
	@Column(name = "PAYMENT_METHOD5")
	public int paymentMethod5;
	
	@Column(name = "PARTIAL_PAY_SET5")
	public int partialPaySet5;
	
	@Column(name = "FIX_AMOUNT_MNY5")
	public int fixAmountMny5;
	
	@Column(name = "FIX_RATE5")
	public int fixRate5;
	
	@Column(name = "FROM_LINE_BANK_CD5")
	public String fromLineBankCd5;
	
	@Column(name = "TO_BRANCH_ID5")
	public String toBranchId5;
	
	@Column(name = "ACCOUNT_ATR5")
	public int accountAtr5;
	
	@Column(name = "ACCOUNT_NO5")
	public String accountNo5;
	
	@Column(name = "ACCOUNT_HOLDER_KN_NAME5")
	public String accountHolderKnName5;
	
	@Column(name = "ACCOUNT_HOLDER_NAME5")
	public String accountHolderName5;

	@Override
	protected Object getKey() {
		return pbamtPersonBankAccountPK;
	}
}
