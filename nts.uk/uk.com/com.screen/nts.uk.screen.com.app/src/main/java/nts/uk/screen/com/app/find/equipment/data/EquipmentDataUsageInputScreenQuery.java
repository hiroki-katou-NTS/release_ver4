package nts.uk.screen.com.app.find.equipment.data;

import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.dom.adapter.annualworkschedule.EmployeeInformationAdapter;
import nts.uk.ctx.at.function.dom.adapter.annualworkschedule.EmployeeInformationImport;
import nts.uk.ctx.at.function.dom.adapter.annualworkschedule.EmployeeInformationQueryDtoImport;
import nts.uk.shr.com.context.AppContexts;

import javax.ejb.TransactionAttribute;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class EquipmentDataUsageInputScreenQuery {

	@Inject
	private EmployeeInformationAdapter employeeInformationAdapter;

	/**
	 * UKDesign.UniversalK.オフィス.OEW_設備管理.OEW001_設備利用の入力.Ｂ：設備利用の入力.メニュー別OCD.Ｂ：起動時社員取得
	 * 
	 * @return
	 */
	public List<EmployeeInformationImport> getEmployeeList() {
		List<String> sids = Arrays.asList(AppContexts.user().employeeId());
		// <<Public>> 社員の情報を取得する
		EmployeeInformationQueryDtoImport param = new EmployeeInformationQueryDtoImport(sids, GeneralDate.today(),
				false, false, false, false, false, false);
		return this.employeeInformationAdapter.getEmployeeInfo(param);
	}
}
