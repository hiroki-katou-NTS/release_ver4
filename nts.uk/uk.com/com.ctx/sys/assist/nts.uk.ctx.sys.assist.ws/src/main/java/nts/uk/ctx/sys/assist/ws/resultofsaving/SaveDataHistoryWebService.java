package nts.uk.ctx.sys.assist.ws.resultofsaving;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.uk.ctx.sys.assist.app.command.datarestoration.FindDataHistoryDto;
import nts.uk.ctx.sys.assist.app.command.datarestoration.GetDataHistoryCommand;
import nts.uk.ctx.sys.assist.app.command.datarestoration.GetSaveSetHistoryCommand;
import nts.uk.ctx.sys.assist.app.find.datarestoration.SaveSetDto;
import nts.uk.ctx.sys.assist.app.find.manualsetting.DeleteDataHandler;
import nts.uk.ctx.sys.assist.app.find.resultofsaving.ResultOfSavingDto;
import nts.uk.ctx.sys.assist.app.find.resultofsaving.StorageHistoryFinder;
import nts.uk.ctx.sys.assist.app.find.resultofsaving.StorageSaveSetFinder;

@Path("ctx/sys/assist/datastorage")
@Produces("application/json")
public class SaveDataHistoryWebService {
	
	@Inject
	private StorageSaveSetFinder saveSetFinder;
	
	@Inject
	private StorageHistoryFinder historyFinder;
	
	@Inject
	private DeleteDataHandler deleteDataHandler;
	
	@POST
	@Path("findSaveSet")
	public List<SaveSetDto> findSaveSetHistory(GetSaveSetHistoryCommand command) {
		return saveSetFinder.findSaveSet(command.getFrom(), command.getTo());
	}
	
	@POST
	@Path("findData")
	public List<ResultOfSavingDto> findData(GetDataHistoryCommand command) {
		return historyFinder.findHistory(command.getObjects().stream()
				.map(FindDataHistoryDto::getPatternCode).collect(Collectors.toList()));
	}
	
	@POST
	@Path("deleteData")
	public void deleteData(String fileId) {
		deleteDataHandler.handle(fileId);
	}
}
