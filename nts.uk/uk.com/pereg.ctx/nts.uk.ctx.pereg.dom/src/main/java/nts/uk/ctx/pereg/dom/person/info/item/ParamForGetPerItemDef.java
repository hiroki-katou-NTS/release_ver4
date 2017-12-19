package nts.uk.ctx.pereg.dom.person.info.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.uk.ctx.pereg.dom.person.info.category.PersonInfoCategory;

@Getter
@AllArgsConstructor
public class ParamForGetPerItemDef {
	private PersonInfoCategory personInfoCategory;
	private String parentInfoId;
	private String roleId;
	private String companyId;
	private String contractCode;
	/**
	 * check whether sId is seen that equals login sId or not
	 * */
	private boolean selfAuth;
}
