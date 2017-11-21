package nts.uk.ctx.pereg.app.command.optional;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import lombok.val;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.bs.employee.dom.regpersoninfo.personinfoadditemdata.category.EmInfoCtgDataRepository;
import nts.uk.ctx.bs.employee.dom.regpersoninfo.personinfoadditemdata.category.EmpInfoCtgData;
import nts.uk.ctx.bs.employee.dom.regpersoninfo.personinfoadditemdata.item.EmpInfoItemData;
import nts.uk.ctx.bs.employee.dom.regpersoninfo.personinfoadditemdata.item.EmpInfoItemDataRepository;
import nts.uk.ctx.bs.person.dom.person.info.category.PerInfoCategoryRepositoty;
import nts.uk.ctx.bs.person.dom.person.info.category.PersonEmployeeType;
import nts.uk.ctx.bs.person.dom.person.info.category.PersonInfoCategory;
import nts.uk.ctx.bs.person.dom.person.personinfoctgdata.categor.PerInfoCtgData;
import nts.uk.ctx.bs.person.dom.person.personinfoctgdata.categor.PerInfoCtgDataRepository;
import nts.uk.ctx.bs.person.dom.person.personinfoctgdata.item.DataState;
import nts.uk.ctx.bs.person.dom.person.personinfoctgdata.item.PerInfoItemDataRepository;
import nts.uk.ctx.bs.person.dom.person.personinfoctgdata.item.PersonInfoItemData;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.pereg.app.ItemValue;
import nts.uk.shr.pereg.app.command.userdef.PeregUserDefAddCommand;
import nts.uk.shr.pereg.app.command.userdef.PeregUserDefAddCommandHandler;

@Stateless
public class AddOptionalCommandHandler extends CommandHandler<PeregUserDefAddCommand>
		implements PeregUserDefAddCommandHandler {
	
	@Inject 
	private PerInfoCategoryRepositoty perInfoCategoryRepositoty;
	
	@Inject
	private PerInfoCtgDataRepository perInfoCtgDataRepository;
	
	@Inject
	private PerInfoItemDataRepository perInfoItemDataRepository;
	
	@Inject
	private EmInfoCtgDataRepository emInfoCtgDataRepository;
	
	@Inject
	private EmpInfoItemDataRepository empInfoItemDataRepository;
	
	@Override
	protected void handle(CommandHandlerContext<PeregUserDefAddCommand> context) {
		val command = context.getCommand();
		// Get company id
		String companyId = AppContexts.user().companyId();
		// Get Command
		Optional<PersonInfoCategory> perInfoCategory = perInfoCategoryRepositoty.getPerInfoCategoryByCtgCD(command.getCategoryId(),companyId);
		
		if (!perInfoCategory.isPresent()){
			throw new RuntimeException("invalid PersonInfoCategory");
		}
		String recordId = command.getRecordId();
		
		// In case of optional category
		if (StringUtils.isNotEmpty(recordId)){
			recordId = IdentifierUtil.randomUniqueId();
		}
		// In case of person
		if (perInfoCategory.get().getPersonEmployeeType() == PersonEmployeeType.PERSON) {
			
			// Insert category data
			perInfoCtgDataRepository.addCategoryData(new PerInfoCtgData(recordId,perInfoCategory.get().getPersonInfoCategoryId(),command.getPersonId()));
			
			// Insert item data
			PersonInfoItemData itemData = null;
			DataState state = null;
			
			for (ItemValue item : command.getItems()){
				
				OptionalUtil.createDataState(item,state);
				
				itemData = new PersonInfoItemData(item.definitionId(), recordId, state);
				perInfoItemDataRepository.addItemData(itemData);
			}
			
		} else if (perInfoCategory.get().getPersonEmployeeType() == PersonEmployeeType.EMPLOYEE){
			// Add emp category data
			emInfoCtgDataRepository.addCategoryData(new EmpInfoCtgData(recordId, perInfoCategory.get().getPersonInfoCategoryId(),command.getEmployeeId()));
			
			// Add item data
			EmpInfoItemData itemData = null;
			DataState state = null;
			for (ItemValue item : command.getItems()){
				OptionalUtil.createDataState(item,state);
				itemData = new EmpInfoItemData(item.definitionId(), recordId, state);
				empInfoItemDataRepository.addItemData(itemData);
			}
			
		}
	}

}
