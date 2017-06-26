package nts.uk.file.pr.app.export.residentialtax.data;

import java.util.List;

import lombok.Data;

@Data
public class InhabitantTaxChecklistBReport {
	private InhabitantTaxChecklistBRpHeader header;
	private List<InhabitantTaxChecklistBRpData> data;
}
