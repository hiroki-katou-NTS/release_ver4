package nts.uk.ctx.basic.infra.entity.system.bank.linebank;

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
@Table(name="CBKMT_LINE_BANK")
public class CbkmtLineBank extends UkJpaEntity {

	@EmbeddedId
	public CbkmtLineBankPK cbkmtLineBankPK;
	
	@Column(name="LINE_BANK_NAME")
	public String lineBankName;
	
	@Column(name="BRANCH_ID")
	public String branchId;
	
	@Column(name="ACCOUNT_ATR")
	public int accountAtr;
	
	@Column(name="ACCOUNT_NO")
	public String accountNo;
	
	@Column(name="REQUESTER_NAME")
	public String requesterName;
	
	@Column(name="CONSIGNOR_CD1")
	public String consignorCode1;
	
	@Column(name="CONSIGNOR_MEMO1")
	public String consignorMemo1;

	@Column(name="CONSIGNOR_CD2")
	public String consignorCode2;
	
	@Column(name="CONSIGNOR_MEMO2")
	public String consignorMemo2;
	
	@Column(name="CONSIGNOR_CD3")
	public String consignorCode3;
	
	@Column(name="CONSIGNOR_MEMO3")
	public String consignorMemo3;
	
	@Column(name="CONSIGNOR_CD4")
	public String consignorCode4;
	
	@Column(name="CONSIGNOR_MEMO4")
	public String consignorMemo4;
	
	@Column(name="CONSIGNOR_CD5")
	public String consignorCode5;
	
	@Column(name="CONSIGNOR_MEMO5")
	public String consignorMemo5;
	
	@Column(name="MEMO")
	public String memo;

	@Override
	protected CbkmtLineBankPK getKey() {
		return this.cbkmtLineBankPK;
	}
	
}
