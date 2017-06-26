package nts.uk.file.pr.app.export.residentialtax.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegalDocCompanyDto {
	private String regalDocCompanyName;
	private String payerPostal;
	private String payerAddress1;
	private String payerAddress2;

}
