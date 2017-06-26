package nts.uk.ctx.pr.core.app.find.personalinfo.wage;

import lombok.Value;
import nts.uk.ctx.pr.core.dom.personalinfo.wage.wagename.PersonalWageNameMaster;

@Value
public class PersonalWageNameDto {
	/**会社コード */
	String companyCode;
	/** カテゴリ区分 */
	int categoryAtr;
	/**個人金額コード	 */
	String personalWageCode;
	/**個人金額名称	 */
	String personalWageName;
	public static PersonalWageNameDto fromDomain(PersonalWageNameMaster domain){
		return new PersonalWageNameDto(
				domain.getCompanyCode().v(),
				domain.getCategoryAtr().value,
				domain.getPersonalWageCode().v(),
				domain.getPersonalWageName().v());
	}
}
