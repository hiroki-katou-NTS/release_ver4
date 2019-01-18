package nts.uk.ctx.pereg.ws.layout;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pereg.app.command.person.setting.matrix.GridSettingCommand;
import nts.uk.ctx.pereg.app.command.person.setting.matrix.matrixdisplayset.CreateMatrixDisplaySetCommandHandler;
import nts.uk.ctx.pereg.app.command.person.setting.matrix.personinfomatrixitem.CreatePersonInfoMatrixItemCommandHandler;
import nts.uk.ctx.pereg.app.find.layoutdef.classification.GridEmployeeDto;
import nts.uk.ctx.pereg.app.find.person.info.item.PerInfoItemDefFinder;
import nts.uk.ctx.pereg.app.find.person.setting.matrix.matrixdisplayset.MatrixDisplaySetFinder;
import nts.uk.ctx.pereg.app.find.person.setting.matrix.personinfomatrixitem.DisplayItemColumnSetFinder;
import nts.uk.ctx.pereg.app.find.processor.GridPeregProcessor;
import nts.uk.ctx.pereg.dom.person.setting.matrix.matrixdisplayset.MatrixDisplaySetting;
import nts.uk.ctx.pereg.dom.person.setting.matrix.personinfomatrixitem.PersonInfoMatrixData;
import nts.uk.shr.pereg.app.find.GridComboBoxSettingQuery;
import nts.uk.shr.pereg.app.find.ItemNameQuery;
import nts.uk.shr.pereg.app.find.PeregGridQuery;

@Path("ctx/pereg/grid-layout")
@Produces(MediaType.APPLICATION_JSON)
public class GridLayoutWebService extends WebService {

	@Inject
	GridPeregProcessor gridProcessor;

	@Inject
	MatrixDisplaySetFinder mdsFinder;

	@Inject
	DisplayItemColumnSetFinder dicFinder;

	@Inject
	CreateMatrixDisplaySetCommandHandler cmdsHandler;

	@Inject
	CreatePersonInfoMatrixItemCommandHandler cpimHandler;
	
	@Inject 
	PerInfoItemDefFinder itemFinder;

	@POST
	@Path("get-data")
	public GridEmployeeDto get(PeregGridQuery query) {
		return gridProcessor.getGridLayout(query);
	}

	@POST
	@Path("save-data")
	public void save(Object command) {

	}

	@POST
	@Path("get-setting/{categoryId}")
	public Object getFixedSetting(@PathParam("categoryId") String categoryId) {
		return new Object() {
			@SuppressWarnings("unused")
			public final MatrixDisplaySetting matrixDisplay = mdsFinder.findByKey().orElse(null);
			@SuppressWarnings("unused")
			public final List<PersonInfoMatrixData> perInfoData = dicFinder.getData(categoryId);
		};
	}

	@POST
	@Path("save-setting")
	public void saveFixedSetting(GridSettingCommand command) {
		if (command.getPersonInfoItems() != null) {
			cpimHandler.handle(command.getPersonInfoItems());
		}

		if (command.getMaxtrixDisplays() != null) {
			cmdsHandler.handle(command.getMaxtrixDisplays());
		}
	}
	
	@POST
	@Path("get-combobox/data")
	public Object getComboboxData(GridComboBoxSettingQuery query) {
		return gridProcessor.getComboBox(query);
	}
	
	@POST
	@Path("get-item/name")
	public Map<String, String> getItemName(ItemNameQuery query) {
		return itemFinder.getNamesByCodes(query.getItemCodes());
	}
}
