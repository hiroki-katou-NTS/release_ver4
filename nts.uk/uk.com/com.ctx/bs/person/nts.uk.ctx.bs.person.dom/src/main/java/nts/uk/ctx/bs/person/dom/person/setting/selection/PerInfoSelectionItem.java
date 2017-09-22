package nts.uk.ctx.bs.person.dom.person.setting.selection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.enums.EnumAdaptor;

@AllArgsConstructor
@Getter
public class PerInfoSelectionItem {
	private String selectionItemId;
	private SelectionItemName selectionItemName;
	private Memo Memo;
	private SelectionItemClassification selectionItemClassification;
	private String contractCode;
	private IntegrationCode integrationCode;
	private FormatSelection formatSelection;
	
	public static PerInfoSelectionItem createFromJavaType(String selectionItemId, String selectionItemName, String memo,
			int selectionItemClsAtr, String contractCd, String integrationCd, int selectionCd, int characterTypeAtr,
			int selectionName, int selectionExtCd) {
		
		return new PerInfoSelectionItem(selectionItemId, 
				new SelectionItemName(selectionItemName), 
				new Memo(memo),
				EnumAdaptor.valueOf(selectionItemClsAtr, SelectionItemClassification.class), 
				contractCd,
				new IntegrationCode(integrationCd),
				FormatSelection.createFromJavaType(selectionCd, characterTypeAtr, selectionName, selectionExtCd));
		
	}
}
