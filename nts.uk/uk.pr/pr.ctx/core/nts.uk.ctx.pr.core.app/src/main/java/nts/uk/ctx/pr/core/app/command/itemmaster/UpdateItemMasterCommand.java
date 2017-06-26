package nts.uk.ctx.pr.core.app.command.itemmaster;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.app.command.itemmaster.itemattend.UpdateItemAttendCommand;
import nts.uk.ctx.pr.core.app.command.itemmaster.itemdeduct.UpdateItemDeductCommand;
import nts.uk.ctx.pr.core.app.command.itemmaster.itemsalary.UpdateItemSalaryCommand;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemMaster;
import nts.uk.shr.com.context.AppContexts;

@Getter
@Setter
public class UpdateItemMasterCommand {

	private UpdateItemSalaryCommand itemSalary;
	private UpdateItemDeductCommand itemDeduct;
	private UpdateItemAttendCommand itemAttend;
	private String itemCode;
	private String itemName;
	private String itemAbName;
	private String itemAbNameE;
	private String itemAbNameO;
	private int categoryAtr;
	private int fixAtr;
	private int displaySet;
	private String uniteCode;
	private int zeroDisplaySet;
	private int itemDisplayAtr;

	public ItemMaster toDomain() {
		return ItemMaster.createFromJavaType(AppContexts.user().companyCode(), this.itemCode, this.itemName,
				this.itemAbName, this.itemAbNameE, this.itemAbNameO, this.categoryAtr, this.fixAtr, this.displaySet,
				this.uniteCode, this.zeroDisplaySet, this.itemDisplayAtr);
	}
}
