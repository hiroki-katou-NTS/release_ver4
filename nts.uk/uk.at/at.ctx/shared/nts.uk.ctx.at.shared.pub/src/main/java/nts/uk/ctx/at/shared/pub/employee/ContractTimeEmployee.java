/**
 * 
 */
package nts.uk.ctx.at.shared.pub.employee;

import java.util.List;

import nts.arc.time.GeneralDate;

/**
 * @author hieult
 *
 */
public interface ContractTimeEmployee {
	
	List<ContractTimeEmployeeExport> getData(List<String> listEmpID, GeneralDate baseDate);
}
