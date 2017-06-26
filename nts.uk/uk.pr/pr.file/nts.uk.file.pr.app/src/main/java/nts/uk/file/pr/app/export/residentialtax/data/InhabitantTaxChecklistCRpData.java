package nts.uk.file.pr.app.export.residentialtax.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InhabitantTaxChecklistCRpData {
	
	/**
	 * DBD_001(住民税納付先コード)
	 */
	private String residenceTaxCode;

	/**
	 * DBD_002(住民税納付先名称)
	 */
	private String resiTaxAutonomy;

	/**
	 * DBD_005(社員CD)
	 */
	private String code;
	
	/**
	 * DBD_006(社員名)
	 */
	private String name;

	/**
	 * DBD_004(納付額)
	 */
	private Double value;
	
	/**
	 * unit(円)
	 */
	private String unit;
	
	/**
	 * Check column sum
	 */
	private Boolean checkSum;
}
