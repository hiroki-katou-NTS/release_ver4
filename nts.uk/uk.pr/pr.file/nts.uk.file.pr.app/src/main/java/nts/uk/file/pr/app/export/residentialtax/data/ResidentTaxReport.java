package nts.uk.file.pr.app.export.residentialtax.data;


import java.util.List;

import lombok.Data;

@Data
public class ResidentTaxReport {
    private ResidentTaxTextCommonData common; 
    private List<ResidentTaxTextData> data;
}
