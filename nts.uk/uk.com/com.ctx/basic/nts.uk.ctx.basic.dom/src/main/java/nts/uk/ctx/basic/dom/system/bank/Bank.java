package nts.uk.ctx.basic.dom.system.bank;

import lombok.Getter;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.AggregateRoot;
import nts.gul.text.StringUtil;
import nts.uk.shr.com.primitive.Memo;

public class Bank extends AggregateRoot {
	/**
	 * Company code
	 */
	@Getter
	private String companyCode;
	
	/**
	 * Bank code
	 */
	@Getter
	private BankCode bankCode;
	
	/**
	 * Bank name
	 */
	@Getter
	private BankName bankName;
	
	/**
	 * Bank name Katakana
	 */
	@Getter
	private BankNameKana bankNameKana;
	
	/**
	 * Memo
	 */
	@Getter
	private Memo memo;
	
	/**
	 * Check validate data 
	 */
	@Override
	public void validate() {
		super.validate();
		if (this.bankCode == null || StringUtil.isNullOrEmpty(this.bankCode.v(), true)) {
			throw new BusinessException("ER001");
		}
		
		if (this.bankName == null || StringUtil.isNullOrEmpty(this.bankName.v(), true)) {
			throw new BusinessException("ER001");
		}
	}
	
	/**
	 * 
	 * @param companyCode
	 * @param bankCode
	 * @param bankName
	 * @param bankNameKana
	 * @param memo
	 */
	public Bank(String companyCode, BankCode bankCode, BankName bankName, BankNameKana bankNameKana, Memo memo) {
		super();
		this.companyCode = companyCode;
		this.bankCode = bankCode;
		this.bankName = bankName;
		this.bankNameKana = bankNameKana;
		this.memo = memo;
	}


	/**
	 * Convert java type to domain
	 * @param companyCode company code
	 * @param bankCode bank code
	 * @param bankName bank name
	 * @param bankNamKana bank name katakana
	 * @param memo memo
	 * @return Bank
	 */
	public static Bank createFromJavaType(String companyCode, String bankCode, String bankName, String bankNameKana, String memo) {
		return new Bank(
				companyCode, 
				new BankCode(bankCode), 
				new BankName(bankName), 
				new BankNameKana(bankNameKana), 
				new Memo(memo));
	}
}
