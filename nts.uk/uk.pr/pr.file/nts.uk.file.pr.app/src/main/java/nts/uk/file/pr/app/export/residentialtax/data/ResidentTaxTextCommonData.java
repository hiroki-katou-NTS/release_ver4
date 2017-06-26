package nts.uk.file.pr.app.export.residentialtax.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResidentTaxTextCommonData {
	
	/**
	 * 2.種別コード
	 */
	private String typeCode;

	/**
	 * 4.依頼人コード
	 */
	private String clientCode;
	/**
	 * 5.仕向支店番号
	 */
	private String desBranchNumber;
	/**
	 * 6.納付期限日
	 */
	private String paymentDueDate;
	/**
	 * 7.納付月
	 */
	private String paymentMonth;	
	/**
	 * 8.依頼人名
	 */
	private String clientName;
	/**
	 * 9.依頼人住所
	 */
	private String clientAddress;
	/**
	 * 28.給与分合計件数
	 */
	private String totalNumSalaryMi;
	/**
	 * 29.給与分合計金額
	 */
	private Double totalSalaryAmount;
	
	/**
	 * 30.退職分合計件数
	 */
	private String totalNumberRetirees;
	
	/**
	 * 31.退職分合計金額
	 */
	private Double totalRetirementAmount;
	
	/**
	 * 32.給与・退職合計件数
	 */
	private String totalNumberSala;
	
	/**
	 * 33.給与・退職合計金額
	 */
    private Double totalSalaRetiAmount;
}
