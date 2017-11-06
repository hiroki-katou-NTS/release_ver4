package find.person.setting.selectionitem.selection;

import lombok.Value;
import nts.uk.ctx.bs.person.dom.person.setting.selectionitem.selection.Selection;
import nts.uk.ctx.bs.person.dom.person.setting.selectionitem.selection.SelectionItemOrder;

/**
 * 
 * @author tuannv
 *
 */

@Value
public class SelectionItemOrderDto {

	private String selectionID;
	private String histId;
	private int disporder;
	private int initSelection;
	private String selectionName;
	private String selectionCD;
	private String externalCD;
	private String memoSelection;

	public static SelectionItemOrderDto fromSelectionOrder(SelectionItemOrder domain, Selection selectionDomain) {
		return new SelectionItemOrderDto(domain.getSelectionID(), domain.getHistId(), domain.getDisporder().v(),
				domain.getInitSelection().value,
				selectionDomain != null ? selectionDomain.getSelectionName().v() : null,
				selectionDomain != null ? selectionDomain.getSelectionCD().v() : null,
				selectionDomain != null ? selectionDomain.getExternalCD().v() : null,
				selectionDomain != null ? selectionDomain.getMemoSelection().v() : null);
	}
}
