package nts.uk.ctx.pereg.app.find.layoutdef.classification;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.pereg.app.ComboBoxObject;

@Getter
@Setter
@AllArgsConstructor
public class GridEmpBody {
	private String itemCode;
	private String itemParentCode;
	
	private ActionRole actionRole;

	private Object value;
	private String textValue;

	private String recordId;
	
	private List<ComboBoxObject> lstComboBoxValue;
}
