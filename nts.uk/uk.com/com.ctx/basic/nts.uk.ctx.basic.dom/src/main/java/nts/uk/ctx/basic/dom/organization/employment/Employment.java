package nts.uk.ctx.basic.dom.organization.employment;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.com.primitive.Memo;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
@Getter
@Setter
public class Employment extends AggregateRoot {
	/*会社コード	 */
	private final String companyCode;
	/*雇用コード	 */
	private EmploymentCode employmentCode;
	/*雇用名称	 */
	private EmploymentName employmentName;
	/*締め日区分	 */
	private CloseDateNo closeDateNo;
	/*メモ	 */
	private Memo memo;
	/*処理日番号	 */
	private ProcessingNo processingNo;
	/*公休管理区分	 */
	private ManageOrNot statutoryHolidayAtr;
	/*外部コード	 */
	private EmploymentCode employementOutCd;
	/*初期表示	 */
	@Setter
	private ManageOrNot displayFlg;
	
	public Employment(String companyCode, EmploymentCode employmentCode, EmploymentName employmentName,
			CloseDateNo closeDateNo, Memo memo, ProcessingNo processingNo, ManageOrNot statutoryHolidayAtr,
			EmploymentCode employementOutCd, ManageOrNot displayFlg) {
		super();
		this.companyCode = companyCode;
		this.employmentCode = employmentCode;
		this.employmentName = employmentName;
		this.closeDateNo = closeDateNo;
		this.memo = memo;
		this.processingNo = processingNo;
		this.statutoryHolidayAtr = statutoryHolidayAtr;
		this.employementOutCd = employementOutCd;
		this.displayFlg = displayFlg;
	}
	
	public static Employment createFromJavaType(String companyCode, String employmentCode, String employmentName,
			int closeDateNo, String memo, int processingNo, int statutoryHolidayAtr,String employementOutCd, int displayFlg){
		return new Employment(
					companyCode,
					new EmploymentCode(employmentCode),
					new EmploymentName(employmentName),
					new CloseDateNo(closeDateNo),
					new Memo(memo),
					new ProcessingNo(processingNo),
					EnumAdaptor.valueOf(statutoryHolidayAtr, ManageOrNot.class),
					new EmploymentCode(employementOutCd),
					EnumAdaptor.valueOf(displayFlg, ManageOrNot.class)
				);
		
	}
}
