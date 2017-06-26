package nts.uk.ctx.basic.dom.system.bank.linebank;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.AggregateRoot;
import nts.gul.text.StringUtil;
import nts.uk.shr.com.primitive.Memo;

/**
 * 
 * @author sonnh1
 *
 */
public class LineBank extends AggregateRoot {
	@Getter
	private String companyCode;
	@Getter
	private AccountAtr accountAtr;
	@Getter
	private AccountNo accountNo;
	@Getter
	private UUID branchId;
	@Getter
	private List<Consignor> consignor;
	@Getter
	private LineBankCode lineBankCode;
	@Getter
	private LineBankName lineBankName;
	@Getter
	private Memo memo;
	@Getter
	private RequesterName requesterName;

	@Override
	public void validate() {
		super.validate();

		if (StringUtil.isNullOrEmpty(lineBankCode.v(), true) || StringUtil.isNullOrEmpty(lineBankName.v(), true)
				|| StringUtil.isNullOrEmpty(accountNo.v(), true)) {
			throw new BusinessException("ER001");
		}

		if (branchId == null) {
			throw new BusinessException("ER007");
		}
	}

	public LineBank(String companyCode, AccountAtr accountAtr, AccountNo accountNo, UUID branchId,
			LineBankCode lineBankCode, LineBankName lineBankName, Memo memo, RequesterName requesterName) {
		super();
		this.companyCode = companyCode;
		this.accountAtr = accountAtr;
		this.accountNo = accountNo;
		this.branchId = branchId;
		this.lineBankCode = lineBankCode;
		this.lineBankName = lineBankName;
		this.memo = memo;
		this.requesterName = requesterName;
	}

	/**
	 * convert to type of LineBank
	 * 
	 * @param companyCode
	 * @param accountAtr
	 * @param accountNo
	 * @param bankCode
	 * @param branchCode
	 * @param lineBankCode
	 * @param lineBankName
	 * @param memo
	 * @param requesterName
	 * @return
	 */
	public static LineBank createFromJavaType(String companyCode, int accountAtr, String accountNo, String branchId,
			String lineBankCode, String lineBankName, String memo, String requesterName) {
		// if branchId = null, set branchId = (UUID) branchIdUuid
		UUID branchIdUuid = null;
		if (!StringUtil.isNullOrEmpty(branchId, true)) {
			branchIdUuid = UUID.fromString(branchId);
		}

		return new LineBank(companyCode, EnumAdaptor.valueOf(accountAtr, AccountAtr.class), new AccountNo(accountNo),
				branchIdUuid, new LineBankCode(lineBankCode), new LineBankName(lineBankName), new Memo(memo),
				new RequesterName(requesterName));
	}

	/**
	 * convert Consignor to type of LineBank
	 * 
	 * @param code1
	 * @param memo1
	 * @param code2
	 * @param memo2
	 * @param code3
	 * @param memo3
	 * @param code4
	 * @param memo4
	 * @param code5
	 * @param memo5
	 */
	public void createConsignorFromJavaType(String code1, String memo1, String code2, String memo2, String code3,
			String memo3, String code4, String memo4, String code5, String memo5) {
		this.consignor = new ArrayList<>();
		consignor.add(new Consignor(new ConsignorCode(code1), new ConsignorMemo(memo1)));
		consignor.add(new Consignor(new ConsignorCode(code2), new ConsignorMemo(memo2)));
		consignor.add(new Consignor(new ConsignorCode(code3), new ConsignorMemo(memo3)));
		consignor.add(new Consignor(new ConsignorCode(code4), new ConsignorMemo(memo4)));
		consignor.add(new Consignor(new ConsignorCode(code5), new ConsignorMemo(memo5)));
	}

}
