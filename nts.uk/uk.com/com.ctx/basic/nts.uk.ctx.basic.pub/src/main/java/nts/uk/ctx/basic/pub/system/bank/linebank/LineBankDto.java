package nts.uk.ctx.basic.pub.system.bank.linebank;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class LineBankDto {
	@Getter
	private String companyCode;
	@Getter
	private int accountAtr;
	@Getter
	private String accountNo;
	@Getter
	private String branchId;
	@Getter
	private List<ConsignorDto> consignor;
	@Getter
	private String lineBankCode;
	@Getter
	private String lineBankName;
	@Getter
	private String memo;
	@Getter
	private String requesterName;

	// public LineBankDto(String companyCode, int accountAtr, String accountNo,
	// String branchId,
	// String lineBankCode, String lineBankName, String memo, String
	// requesterName) {
	// super();
	// this.companyCode = companyCode;
	// this.accountAtr = accountAtr;
	// this.accountNo = accountNo;
	// this.branchId = branchId;
	// this.lineBankCode = lineBankCode;
	// this.lineBankName = lineBankName;
	// this.memo = memo;
	// this.requesterName = requesterName;
	// }

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
	// public static LineBankDto createFromJavaType(String companyCode, int
	// accountAtr, String accountNo, String branchId,
	// String lineBankCode, String lineBankName, String memo, String
	// requesterName) {
	// // if branchId = null, set branchId = (UUID) branchIdUuid
	// UUID branchIdUuid = null;
	// if (!StringUtil.isNullOrEmpty(branchId, true)) {
	// branchIdUuid = UUID.fromString(branchId);
	// }
	//
	// return new LineBankDto(companyCode, accountAtr, accountNo,
	// branchIdUuid, lineBankCode, lineBankName, memo,
	// requesterName);
	// }
	//
	// /**
	// * convert Consignor to type of LineBank
	// *
	// * @param code1
	// * @param memo1
	// * @param code2
	// * @param memo2
	// * @param code3
	// * @param memo3
	// * @param code4
	// * @param memo4
	// * @param code5
	// * @param memo5
	// */
	// public void createConsignorFromJavaType(String code1, String memo1,
	// String code2, String memo2, String code3,
	// String memo3, String code4, String memo4, String code5, String memo5) {
	// this.consignor = new ArrayList<>();
	// consignor.add(new ConsignorDto(code1, memo1));
	// consignor.add(new ConsignorDto(code2, memo2));
	// consignor.add(new ConsignorDto(code3, memo3));
	// consignor.add(new ConsignorDto(code4, memo4));
	// consignor.add(new ConsignorDto(code5, memo5));
	// }
}
