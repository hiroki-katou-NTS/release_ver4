package nts.uk.file.pr.app.export.residentialtax.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResidentTaxTextData {
	
	/**
	 * 12.市町村コード
	 */
	private String municipalCode;
	
	/**
	 * 13.市町村名
	 */
	private String cityName; 
	
	/**
	 * 14.指定番号
	 */
	private String designatedNumber;
		
	/**
	 * 16.給与分件数
	 */
	private String numberSalaries;
	
	/**
	 * 17.給与分金額
	 */
	private Double salaryAmount;
	
	/**
	 * 18.退職分件数
	 */
	private String numberRetirees;
	
	/**
	 * 19.退職分金額
	 */
	private Double retirementAmount;
	
	/**
	 * 20.給与・退職合計件数
	 */
	private String totalNumberSalaReti;
	
	/**
	 * 21.給与・退職合計金額
	 */
	private Double totalSalaryRetiAmount;
	
	/**
	 * 22.退職明細（人員）
	 */
	private String personnel;
	
	/**
	 * 23.退職明細（支払金額）
	 */
	private Double payment;
	
	/**
	 * 24.退職明細（市区町村民税）
	 */
	private Double cityTownTax;
	
	/**
	 * 25.退職明細（都道府県民税）
	 */
	private Double prefecturalTax;

}
