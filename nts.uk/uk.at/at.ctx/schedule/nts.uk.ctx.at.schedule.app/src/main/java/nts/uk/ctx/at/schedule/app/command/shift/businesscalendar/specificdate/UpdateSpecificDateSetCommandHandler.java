package nts.uk.ctx.at.schedule.app.command.shift.businesscalendar.specificdate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.shift.specificdayset.company.CompanySpecificDateItem;
import nts.uk.ctx.at.schedule.dom.shift.specificdayset.company.CompanySpecificDateRepository;
import nts.uk.ctx.at.schedule.dom.shift.specificdayset.workplace.WorkplaceSpecificDateItem;
import nts.uk.ctx.at.schedule.dom.shift.specificdayset.workplace.WorkplaceSpecificDateRepository;
import nts.uk.shr.com.context.AppContexts;
@Stateless
public class UpdateSpecificDateSetCommandHandler extends CommandHandler<UpdateSpecificDateSetCommand>{
	@Inject
	private WorkplaceSpecificDateRepository workplaceRepo;
	@Inject
	private CompanySpecificDateRepository companyRepo;
	
	@Override
	protected void handle(CommandHandlerContext<UpdateSpecificDateSetCommand> context) {
		UpdateSpecificDateSetCommand data = context.getCommand();
		if(data.getStrDate()>data.getEndDate()){
			//strDate > endDate => display msg_136
			throw new BusinessException("Msg_136");
		}
		if(data.getDayofWeek().isEmpty()){
			//date set is emtity => display msg_136
			throw new BusinessException("Msg_137");
		}
		if(data.getLstTimeItemId().isEmpty()){
			//time item seleced is emtity => display msg_136
			throw new BusinessException("Msg_138");
		}
		if(data.getUtil()==1){//company
			UpdatebyDayforCompany(data.getStrDate(),data.getEndDate(), data.getDayofWeek(), data.getLstTimeItemId(),data.getSetUpdate());
		}else{//workplace
			UpdatebyDayforWorkPlace(data.getStrDate(),data.getEndDate(), data.getDayofWeek(), data.getLstTimeItemId(),data.getSetUpdate(),data.getWorkplaceId());
		}
	}
	public static boolean CheckDayofWeek(GeneralDate dateInString, List<Integer> dayofWeek){
		Integer dateInInt = dateInString.dayOfWeek();
		boolean value = false;
		for (Integer integer : dayofWeek) {
			if(integer == dateInInt){
				value = true;
				break;
			}
		}
		return value;
	}
	private void UpdatebyDayforWorkPlace(int strDate, int endDate, List<Integer> dayofWeek, List<Integer> lstTimeItemId ,int setUpdate,String workplaceId){
		GeneralDate sDate = GeneralDate.fromString(String.valueOf(strDate), "yyyyMMdd");
		GeneralDate eDate = GeneralDate.fromString(String.valueOf(endDate), "yyyyMMdd");
		GeneralDate date = sDate;
		while(date == eDate){
			if(!CheckDayofWeek(date,dayofWeek)){//not setting
				date.addDays(1);
				continue;
			}
			//setting
			int a = Integer.valueOf(date.toString());
			BigDecimal b = BigDecimal.valueOf(a);
			if(setUpdate==1){
				//既に設定されている内容は据え置き、追加で設定する - complement
				//list item da co san trong db
				List<WorkplaceSpecificDateItem> lstOld = workplaceRepo.getWorkplaceSpecByDate(workplaceId, a);
				List<WorkplaceSpecificDateItem> lstAdd = lstOld;
				//find item not exist in db
				for (Integer timeItemId : lstTimeItemId) {
					lstAdd = lstAdd.stream().filter(c->
					timeItemId.equals(c.getSpecificDateItemNo().v())).collect(Collectors.toList()); 
				}
				//add item new in db
				workplaceRepo.addWorkplaceSpec(lstAdd);
				
			}else{
				//既に設定されている内容をクリアし、今回選択したものだけを設定する - add new: xoa het caus cu, them moi
				//delete setting old workplace
				
				workplaceRepo.deleteWorkplaceSpec(workplaceId, a);
				//add new
				List<WorkplaceSpecificDateItem> lstWorkplaceSpecificDate = new ArrayList<>();
				
				for (Integer timeItemId : lstTimeItemId) {
					lstWorkplaceSpecificDate.add(WorkplaceSpecificDateItem.createFromJavaType(workplaceId,b,BigDecimal.valueOf(timeItemId)));
				}
				workplaceRepo.addWorkplaceSpec(lstWorkplaceSpecificDate);
				
			}
			date.addDays(1);
		}
	}
	private void UpdatebyDayforCompany(int strDate, int endDate, List<Integer> dayofWeek, List<Integer> lstTimeItemId ,int setUpdate){
		String companyId = AppContexts.user().companyId();
		GeneralDate sDate = GeneralDate.fromString(String.valueOf(strDate), "yyyyMMdd");
		GeneralDate eDate = GeneralDate.fromString(String.valueOf(endDate), "yyyyMMdd");
		GeneralDate date = sDate;
		while(date == eDate){
			if(!CheckDayofWeek(date,dayofWeek)){//not setting
				date.addDays(1);
				continue;
			}
			//setting
			int dateInt = Integer.valueOf(date.toString());
			BigDecimal dateBigDecimal = BigDecimal.valueOf(dateInt);
			if(setUpdate==1){
				//既に設定されている内容は据え置き、追加で設定する - complement
				//list item da co san trong db
				List<CompanySpecificDateItem> lstOld = companyRepo.getComSpecByDate(companyId, dateInt);
				List<CompanySpecificDateItem> lstAdd = lstOld;
				//find item not exist in db
				for (Integer timeItemId : lstTimeItemId) {
					lstAdd = lstAdd.stream().filter(c->
					timeItemId.equals(c.getSpecificDateItemNo().v())).collect(Collectors.toList()); 
				}
				//add item new in db
				companyRepo.addListComSpecDate(lstAdd);
				
			}else{
				//既に設定されている内容をクリアし、今回選択したものだけを設定する - add new
				//delete setting old workplace
				companyRepo.deleteComSpecByDate(companyId, dateInt);
				//add new
				List<CompanySpecificDateItem> lstComSpecificDate = new ArrayList<>();
				
				for (Integer timeItemId : lstTimeItemId) {
					lstComSpecificDate.add(CompanySpecificDateItem.createFromJavaType(companyId, dateBigDecimal, BigDecimal.valueOf(timeItemId),""));
				}
				companyRepo.addListComSpecDate(lstComSpecificDate);
				
			}
			date.addDays(1);
		}
	}

}
