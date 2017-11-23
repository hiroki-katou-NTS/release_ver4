package nts.uk.ctx.bs.person.ws.person.setting.selection;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import command.person.setting.selectionitem.AddSelectionItemCommand;
import command.person.setting.selectionitem.AddSelectionItemCommandHandler;
import command.person.setting.selectionitem.RemoveSelectionItemCommand;
import command.person.setting.selectionitem.RemoveSelectionItemCommandHandler;
import command.person.setting.selectionitem.UpdateSelectionItemCommand;
import command.person.setting.selectionitem.UpdateSelectionItemCommandHandler;
import command.person.setting.selectionitem.selection.AddSelectionCommand;
import command.person.setting.selectionitem.selection.AddSelectionCommandHandler;
import command.person.setting.selectionitem.selection.AddSelectionHistoryCommand;
import command.person.setting.selectionitem.selection.AddSelectionHistoryCommandHandler;
import command.person.setting.selectionitem.selection.EditHistoryCommand;
import command.person.setting.selectionitem.selection.EditHistoryCommandHandler;
import command.person.setting.selectionitem.selection.ReflUnrCompCommand;
import command.person.setting.selectionitem.selection.ReflUnrCompCommandHandler;
import command.person.setting.selectionitem.selection.RemoveHistoryCommand;
import command.person.setting.selectionitem.selection.RemoveHistoryCommandHandler;
import command.person.setting.selectionitem.selection.RemoveSelectionCommand;
import command.person.setting.selectionitem.selection.RemoveSelectionCommandHandler;
import command.person.setting.selectionitem.selection.UpdateSelOrderCommand;
import command.person.setting.selectionitem.selection.UpdateSelOrderCommandHandler;
import command.person.setting.selectionitem.selection.UpdateSelectionCommand;
import command.person.setting.selectionitem.selection.UpdateSelectionCommandHandler;
import find.person.setting.init.item.SelectionInitDto;
import find.person.setting.selectionitem.PerInfoHistorySelectionDto;
import find.person.setting.selectionitem.PerInfoHistorySelectionFinder;
import find.person.setting.selectionitem.PerInfoSelectionItemDto;
import find.person.setting.selectionitem.PerInfoSelectionItemFinder;
import find.person.setting.selectionitem.selection.SelectionFinder;
import find.person.setting.selectionitem.selection.SelectionItemOrderDto;
import nts.arc.layer.app.command.JavaTypeResult;
import nts.arc.layer.ws.WebService;

@Path("ctx/bs/person/info/setting/selection")
@Produces("application/json")
public class PerInfoSelectionItemWebservice extends WebService {
	@Inject
	private PerInfoSelectionItemFinder finder;

	@Inject
	private AddSelectionItemCommandHandler addCommandHandler;

	@Inject
	private UpdateSelectionItemCommandHandler updateCommandHandler;

	@Inject
	private RemoveSelectionItemCommandHandler removeCommandHandler;

	// history:
	@Inject
	private PerInfoHistorySelectionFinder hisFinder;

	@Inject
	private SelectionFinder selecFider;

	// Add selection:
	@Inject
	private AddSelectionCommandHandler addSelectionCommandHandler;

	// update Selection:
	@Inject
	private UpdateSelectionCommandHandler updateSelection;

	// Remove Selection
	@Inject
	private RemoveSelectionCommandHandler removeSelection;

	// add history data: screen C:
	@Inject
	AddSelectionHistoryCommandHandler addHistory;

	// Edit History:
	@Inject
	EditHistoryCommandHandler editHistory;

	// Delete history:
	@Inject
	RemoveHistoryCommandHandler removeHistory;

	// Phan anh cong ty:
	@Inject
	ReflUnrCompCommandHandler reflUnrComp;

	// hoatt - update selection order
	@Inject
	private UpdateSelOrderCommandHandler updateSelOrder;

	@POST
	@Path("findAll")
	public List<PerInfoSelectionItemDto> getAllPerInfoSelectionItem() {
		return this.finder.getAllPerInfoSelectionItem();
	}

	@POST
	@Path("findItem/{selectionItemId}")
	public PerInfoSelectionItemDto getPerInfoSelectionItem(@PathParam("selectionItemId") String selectionItemId) {
		return this.finder.getPerInfoSelectionItem(selectionItemId);
	}

	@POST
	@Path("addSelectionItem")
	public JavaTypeResult<String> addSelectionItem(AddSelectionItemCommand command) {
		return new JavaTypeResult<String>(this.addCommandHandler.handle(command));
	}

	@POST
	@Path("updateSelectionItem")
	public void updateSelectionItem(UpdateSelectionItemCommand command) {
		this.updateCommandHandler.handle(command);
	}

	@POST
	@Path("removeSelectionItem")
	public void removeSelectionItem(RemoveSelectionItemCommand command) {
		this.removeCommandHandler.handle(command);
	}

	// history:
	@POST
	@Path("findAllHistSelection/{selectedId}")
	public List<PerInfoHistorySelectionDto> getAllPerInfoHistorySelection(@PathParam("selectedId") String selectedId) {
		return this.hisFinder.historySelection(selectedId);
	}

	@POST
	@Path("findAllSelection/{histId}")
	public List<SelectionItemOrderDto> getAllItemOrderSelection(@PathParam("histId") String histId) {
		return this.selecFider.getHistIdSelection(histId);
	}

	// Addselection:
	@POST
	@Path("addSelection")
	public void AddSelection(AddSelectionCommand command) {
		this.addSelectionCommandHandler.handle(command);

	}

	// Update Selection:
	@POST
	@Path("updateSelection")
	public void UpdateSelection(UpdateSelectionCommand command) {
		this.updateSelection.handle(command);
	}

	// remove Selection
	@POST
	@Path("removeSelection")
	public void removeSelection(RemoveSelectionCommand command) {
		this.removeSelection.handle(command);
	}

	// Order setting
	@POST
	@Path("OrderSetting/{histId}")
	public List<SelectionItemOrderDto> getAllOrderSetting(@PathParam("histId") String histId) {
		return this.selecFider.getHistIdSelection(histId);
	}

	// add history data Screen C:
	@POST
	@Path("addHistoryData")
	public void AddHistoryData(AddSelectionHistoryCommand command) {
		this.addHistory.handle(command);
	}

	// Edit History:
	@POST
	@Path("editHistory")
	public void EditHistory(EditHistoryCommand command) {
		this.editHistory.handle(command);
	}

	// Delete History:
	@POST
	@Path("removeHistory")
	public void RemoveHistory(RemoveHistoryCommand command) {
		this.removeHistory.handle(command);
	}

	// Lanlt
	@POST
	@Path("find/{selectionItemId}/{baseDate}")
	public List<SelectionInitDto> getAllSelectionByHistoryId(@PathParam("selectionItemId") String selectionItemId,
			@PathParam("baseDate") String baseDate) {
		return this.selecFider.getAllSelectionByHistoryId(selectionItemId, baseDate);
	}

	// Phan anh cong ty:
	@POST
	@Path("reflunrcomp")
	public void ReflUnrComp(ReflUnrCompCommand command) {
		this.reflUnrComp.handle(command);
	}

	// Lanlt
	@POST
	@Path("findAllSelectionItem")
	public List<PerInfoSelectionItemDto> getAllelectionItem() {
		return this.finder.getAllSelectionItem();
	}

	// update selection order
	@POST
	@Path("updateSelOrder")
	public void updateSelOrder(List<UpdateSelOrderCommand> lstSelOrder) {
		this.updateSelOrder.handle(lstSelOrder);
	}
}
