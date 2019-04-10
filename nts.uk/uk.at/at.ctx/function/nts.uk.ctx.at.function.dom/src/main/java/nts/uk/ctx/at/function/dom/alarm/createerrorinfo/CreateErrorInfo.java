package nts.uk.ctx.at.function.dom.alarm.createerrorinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.dom.adapter.alarm.EmployeeSprPubAlarmAdapter;
import nts.uk.ctx.at.function.dom.adapter.employeebasic.EmployeeInfoImport;
import nts.uk.ctx.at.function.dom.adapter.employeebasic.SyEmployeeFnAdapter;
import nts.uk.ctx.at.function.dom.adapter.workplace.WorkplaceAdapter;
import nts.uk.ctx.at.function.dom.adapter.workplace.WorkplaceIdName;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;

/**
 * メール送信のエラー情報を作成する
 * @author tutk
 *
 */
@Stateless
public class CreateErrorInfo {
	
	@Inject 
	private SyEmployeeFnAdapter syEmployeeFnAdapter;
	
	@Inject
	private WorkplaceAdapter workplaceAdapter;
	
	public OutputErrorInfo getErrorInfo(GeneralDate baseDate,List<String> listEmp,Map<String, List<String>> mapWorkplaceAndListSid,List<String> listWkp) {
		
		String companyId = AppContexts.user().companyId();
		String error = "";
		String errorWkp = "";
		//送信エラー有無をチェックする
		if(!listEmp.isEmpty() || !mapWorkplaceAndListSid.isEmpty() ) {
			error = TextResource.localize("Msg_965")+"<br/>";
			//INPUT.送信先エラーリスト(本人宛).件数＞0件
			if(!listEmp.isEmpty()) {
				//RQ228
				List<EmployeeInfoImport> listEmployeeInfoImport =  syEmployeeFnAdapter.getByListSid(listEmp);
				for (EmployeeInfoImport employee : listEmployeeInfoImport) {
					if (!StringUtils.isEmpty(employee.getBussinessName())) {
						error += TextResource.localize("KAL010_303",employee.getBussinessName())+"<br/>";
					}   
				}
			}
			//INPUT.送信先エラーリスト(管理者宛).件数＞0件
			if(!mapWorkplaceAndListSid.isEmpty()) {
				
				List<String> wpkIds = new ArrayList<>();
				for(String wpkId :mapWorkplaceAndListSid.keySet() ) {
					wpkIds.add(wpkId);
				}
				//RQ324	
				List<WorkplaceIdName> listWorkplaceIdName =  workplaceAdapter.findWkpByWkpId(companyId,baseDate,wpkIds);
				List<String> errorAdm = new ArrayList<>(); 
				mapWorkplaceAndListSid.forEach((key, value) -> {
					Optional<WorkplaceIdName> workplaceIdName = listWorkplaceIdName.stream().filter(c->c.getWorkplaceId().equals(key)).findFirst();
					String wkpName = "";
					if(workplaceIdName.isPresent()) {
						wkpName = workplaceIdName.get().getWorkplaceName();
					}
					List<EmployeeInfoImport> listEmployeeInfoImport =  syEmployeeFnAdapter.getByListSid(value);
					for(EmployeeInfoImport empAdmId :listEmployeeInfoImport) {
						errorAdm.add(TextResource.localize("KAL010_304",empAdmId.getBussinessName(),wkpName)+"<br/>");
					}
				});
				for(String errorAdmItem : errorAdm) {
					error += errorAdmItem;
				}
				if(listEmp.isEmpty()&&errorAdm.isEmpty()) {
					error = "";
				}
			}
			
		}
		if(!listWkp.isEmpty()) {
			//INPUT.管理者未設定職場リスト.件数＞0件
			errorWkp = TextResource.localize("Msg_1514")+"<br/>";
			//RQ324
			List<WorkplaceIdName> listWorkplaceIdName =  workplaceAdapter.findWkpByWkpId(companyId,baseDate,listWkp);
			for (WorkplaceIdName workplaceIdName : listWorkplaceIdName) {
				if (!StringUtils.isEmpty(workplaceIdName.getWorkplaceName())) {
					errorWkp += TextResource.localize("KAL010_303",workplaceIdName.getWorkplaceName())+"<br/>";
				}   
			}
		}
		
		return new OutputErrorInfo(error,errorWkp);
	}

}
