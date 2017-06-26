package nts.uk.file.pr.app.export.residentialtax.data;

import java.util.List;

import lombok.Data;

@Data
public class InhabitantTaxChecklistCReport {
	private InhabitantTaxChecklistBRpHeader header;
	private List<InhabitantTaxChecklistCRpData> data;
}
