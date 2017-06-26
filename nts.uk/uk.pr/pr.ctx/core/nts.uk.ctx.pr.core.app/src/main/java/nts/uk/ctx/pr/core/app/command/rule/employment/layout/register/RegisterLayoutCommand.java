package nts.uk.ctx.pr.core.app.command.rule.employment.layout.register;

import java.util.List;

import lombok.Getter;

@Getter
public class RegisterLayoutCommand {

	private Layout layoutCommand;
	private List<LayoutCategory> categoryCommand;
	private List<LayoutLine> lineCommand;
	private List<LayoutDetail> detailCommand;
	
	private List<Integer> listCategoryAtrDeleted;
	
	//Chỉ cần trường autoLineId và categoryAtr
	private List<LayoutLine> listAutoLineIdDeleted;
	
	//Chỉ cần trường itemCode và categoryAtr
	private List<LayoutDetail> listItemCodeDeleted;
	
}
