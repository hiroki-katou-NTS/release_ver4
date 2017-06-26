package nts.uk.file.pr.app.export.residentialtax;

import java.util.List;

import lombok.Data;
import nts.arc.time.GeneralDate;

@Data
public class ResidentialTaxQuery {
	private List<String> residentTaxCodeList;
	private int companyLogin;
	private String regalDocCompanyCode;
	private String yearMonth;
	private String processingYearMonth;
	private String processingYearMonthJapan;
	private GeneralDate endDate;
	private String endDateJapan;
	private String typeCode;
	private String clientCode;
	private String destinationBranchNumber;
}
