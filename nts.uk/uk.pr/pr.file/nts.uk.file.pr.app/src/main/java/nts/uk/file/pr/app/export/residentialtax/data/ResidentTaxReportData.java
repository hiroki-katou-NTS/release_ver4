package nts.uk.file.pr.app.export.residentialtax.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResidentTaxReportData {
	
	/**
	 * DBD_001(住民税納付先コード)
	 */
	private String residenceTaxCode;
	
	/**
	 * DBD_002(住民税納付先名称)
	 */
	private String resiTaxAutonomy;
	
	/**
	 * DBD_003(口座番号)
	 */
	private String companyAccountNo;
	
	/**
	 * DBD_004(加入者名)
	 */
	private String registeredName;
	
	/**
	 * DBD_005(指定番号)
	 */
	private String companySpecifiedNo;
	
	/**
	 * DBD_006(給与分納入金額)
	 * value + taxPayrollMny
	 */
	private Double salaryPaymentAmount;

	/**
	 * DBD_007(退職所得分納入金額)
	 * cityTaxMny1 + prefectureTaxMny1 + taxRetirementMny
	 */
	private Double deliveryAmountRetirement;
	
	/**
	 * DBD_008(特別徴収義務者郵便番号)
	 */
	private String postal;
	
	/**
	 * DBD_009(特別徴収義務者住所１)
	 */
	private String address1;
	
	/**
	 * DBD_010(特別徴収義務者住所2)
	 */
	private String address2;
	
	/**
	 * DBD_011(特別徴収義務者名称)
	 */
	private String companyName;
	
	/**
	 * DBD_012(取りまとめ局名称)
	 */
	private String cordinatePostOffice;
	
	/**
	 * DBD_013(取りまとめ局郵便番号)
	 */
	private String cordinatePostalCode;
	
	/**
	 * DBD_014(納入人数)
	 * residenceCode + headCount
	 */ 
    private String deliveryNumber;
    
    /**
     * DBD_015(退職手当等支払金額)
     */
	private Double actualRecieveMny;
	
	/**
	 * DBD_016(市町村民税)
	 */
	private Double cityTaxMny;
	
	/**
	 * DBD_017(道府県民税)
	 */
	private Double prefectureTaxMny;
	
	/**
	 * DBD_018(延滞金)
	 */
	private Double taxOverdueMny;
	
	/**
	 * DBD_019(督促手数料)
	 */
	private Double taxDemandChargeMny;
	
	/**
	 * DBD_020(提出日)
	 */
	private String filingDate;
	
	/**
	 * CTR_001(指定年月)
	 */
	private String designatedYM;
	
	/**
	 * CTR_002(納入金額合計額 = 給与分納入金額 + 退職所得分納入金額)
	 */
    private Double totalAmountTobePaid;
    
    /**
     * CTR_003(納期限)
     */
    private String dueDate;
}
