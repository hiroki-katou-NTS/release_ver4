package nts.uk.ctx.basic.app.find.organization.employment;

import lombok.Value;
import nts.uk.ctx.basic.dom.organization.employment.Employment;

@Value
public class EmploymentDto {
	/*会社コード	 */
	String companyCode;
	/*雇用コード	 */
	String employmentCode;
	/*雇用名称	 */
	String employmentName;
	/*締め日区分	 */
	int closeDateNo;
	/*処理日番号	 */
	int processingNo;
	/*公休管理区分	 */
	int statutoryHolidayAtr;
	/*外部コード	 */
	String employementOutCd;
	/*初期表示	 */
	int displayFlg;
	/*メモ	 */
	String memo;
	public static EmploymentDto fromDomain(Employment domain){
		return new EmploymentDto(domain.getCompanyCode(),
				domain.getEmploymentCode().v(),
				domain.getEmploymentName().v(),
				domain.getCloseDateNo().v(),
				domain.getProcessingNo().v(),
				domain.getStatutoryHolidayAtr().value,
				domain.getEmployementOutCd().v(),
				domain.getDisplayFlg().value,
				domain.getMemo().v());
	}
}
