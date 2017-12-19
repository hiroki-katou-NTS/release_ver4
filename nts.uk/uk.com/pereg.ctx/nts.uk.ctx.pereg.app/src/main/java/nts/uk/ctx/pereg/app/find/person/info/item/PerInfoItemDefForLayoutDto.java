package nts.uk.ctx.pereg.app.find.person.info.item;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.pereg.app.find.layoutdef.classification.ActionRole;
import nts.uk.shr.pereg.app.ComboBoxObject;

/**
 * item def dto which declare fields for displaying layout
 * @author xuan vinh
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PerInfoItemDefForLayoutDto extends PerInfoItemDefDto{
	
	private String perInfoCtgCd;
	private int ctgType;
	
	private int itemDefType;
	
	private String recordId;
	
	/**
	 * combo box value list when item type selection
	 */
	private List<ComboBoxObject> lstComboxBoxValue;
	
	private List<PerInfoItemDefForLayoutDto> lstChildItemDef;

	private int row;
	
	private ActionRole actionRole;
	
}
