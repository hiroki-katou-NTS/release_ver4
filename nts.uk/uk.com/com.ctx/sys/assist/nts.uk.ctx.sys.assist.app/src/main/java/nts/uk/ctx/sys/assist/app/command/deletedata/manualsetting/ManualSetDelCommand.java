/**
 * 
 */
package nts.uk.ctx.sys.assist.app.command.deletedata.manualsetting;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Value;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.sys.assist.dom.deletedata.BusinessName;
import nts.uk.ctx.sys.assist.dom.deletedata.CategoryDeletion;
import nts.uk.ctx.sys.assist.dom.deletedata.DelName;
import nts.uk.ctx.sys.assist.dom.deletedata.EmployeeDeletion;
import nts.uk.ctx.sys.assist.dom.deletedata.ManualSetDeletion;
import nts.uk.ctx.sys.assist.dom.deletedata.PasswordCompressFileEncrypt;
import nts.uk.ctx.sys.assist.dom.deletedata.SupplementExplanation;

/**
 * @author hiep.th
 *
 */
@Value
public class ManualSetDelCommand {
	
	private String delName;
	private String suppleExplanation;
	private int systemType;
	private GeneralDate referenceDate;
	private GeneralDateTime executionDateAndTime;
	private GeneralDate dayStartDate;
	private GeneralDate dayEndDate;
	
	private GeneralDate monthStartDate;
	private GeneralDate monthEndDate;
	private int startYear;
	private int endYear;
	
	private int isSaveBeforeDeleteFlg;
	private int isExistCompressPasswordFlg;
	private String passwordForCompressFile;
	private int haveEmployeeSpecifiedFlg;
	private List<EmployeesDeletionCommand> employees;
	private List<CategoryDeletionCommand> categories;
	
	public ManualSetDeletion toDomain(String delId, String cid, String sid) {
		boolean isSaveBeforeDeleteFlg = this.isSaveBeforeDeleteFlg == 1;
		boolean isExistCompressPasswordFlg = this.isExistCompressPasswordFlg == 1;
		boolean haveEmployeeSpecifiedFlg = this.haveEmployeeSpecifiedFlg == 1;
		return new ManualSetDeletion(delId, cid, systemType, new DelName(delName), isSaveBeforeDeleteFlg, 
				isExistCompressPasswordFlg, new PasswordCompressFileEncrypt(passwordForCompressFile), haveEmployeeSpecifiedFlg, 
				sid, new SupplementExplanation(suppleExplanation),
				referenceDate, executionDateAndTime, dayStartDate, dayEndDate, monthStartDate, monthEndDate, startYear, endYear);			
	}
	
	public List<EmployeeDeletion> getEmployees(String delId) {
		return employees.stream().map(x -> {
			return new EmployeeDeletion(delId, x.getEmployeeId(), new BusinessName(x.getBusinessName()));
		}).collect(Collectors.toList());
	}
	
	
	public List<CategoryDeletion> getCategories(String delId) {
		return categories.stream().map(x -> {
			return new CategoryDeletion(delId, x.getCategoryId(), x.getPeriodDeletion());
		}).collect(Collectors.toList());
	}
}
