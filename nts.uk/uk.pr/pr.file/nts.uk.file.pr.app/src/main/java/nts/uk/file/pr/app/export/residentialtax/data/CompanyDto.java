package nts.uk.file.pr.app.export.residentialtax.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanyDto {
  private String companyCode;
  private String companyName;
  private String postal;
  private String address1;
  private String address2;
}
