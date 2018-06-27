package nts.uk.ctx.at.function.ws.monthlyworkschedule;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nts.arc.enums.EnumAdaptor;
import nts.arc.enums.EnumConstant;
import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.function.app.command.monthlyworkschedule.OutputItemMonthlyWorkScheduleCommand;
import nts.uk.ctx.at.function.app.command.monthlyworkschedule.OutputItemMonthlyWorkScheduleCopyCommand;
import nts.uk.ctx.at.function.app.command.monthlyworkschedule.OutputItemMonthlyWorkScheduleDeleteHandler;
import nts.uk.ctx.at.function.app.command.monthlyworkschedule.OutputItemMonthlyWorkScheduleSaveHandler;
import nts.uk.ctx.at.function.app.find.monthlyworkschedule.MonthlyDataInforReturnDto;
import nts.uk.ctx.at.function.app.find.monthlyworkschedule.OutputItemMonthlyWorkScheduleFinder;
import nts.uk.ctx.at.function.dom.monthlyworkschedule.PrintSettingRemarksColumn;

// TODO: Auto-generated Javadoc
/**
 * The Class OutputItemMonthlyWorkScheduleWS.
 */
@Path("at/function/monthlyworkschedule")
@Produces(MediaType.APPLICATION_JSON)
public class OutputItemMonthlyWorkScheduleWS extends WebService {

	/** The output item monthly work schedule finder. */
	@Inject
	private OutputItemMonthlyWorkScheduleFinder outputItemMonthlyWorkScheduleFinder;

	/** The output item monthly work schedule save handler. */
	@Inject
	private OutputItemMonthlyWorkScheduleSaveHandler outputItemMonthlyWorkScheduleSaveHandler;

	/** The output item monthly work schedule delete handler. */
	@Inject
	private OutputItemMonthlyWorkScheduleDeleteHandler outputItemMonthlyWorkScheduleDeleteHandler;

	/**
	 * Find employment authority.
	 *
	 * @return the boolean
	 */
	@Path("find/employment/authority")
	@POST
	public Boolean findEmploymentAuthority() {
		return this.outputItemMonthlyWorkScheduleFinder.findEmploymentAuthority();
	}

	/**
	 * Find.
	 *
	 * @return the map
	 */
	@Path("find")
	@POST
	public Map<String, Object> find() {
		return this.outputItemMonthlyWorkScheduleFinder.findByCid();
	}

	/**
	 * Save.
	 *
	 * @param command
	 *            the command
	 */
	@Path("save")
	@POST
	public void save(OutputItemMonthlyWorkScheduleCommand command) {
		this.outputItemMonthlyWorkScheduleSaveHandler.handle(command);
	}

	/**
	 * Delete.
	 *
	 * @param code
	 *            the code
	 */
	@Path("delete/{code}")
	@POST
	public void delete(@PathParam("code") String code) {
		this.outputItemMonthlyWorkScheduleDeleteHandler.delete(code);
	}

	/**
	 * Gets the enum setting print.
	 *
	 * @return the enum setting print
	 */
	@Path("enumSettingPrint")
	@POST
	public List<EnumConstant> getEnumSettingPrint() {
		return EnumAdaptor.convertToValueNameList(PrintSettingRemarksColumn.class);
	}

	/**
	 * Find copy.
	 *
	 * @return the list
	 */
	@Path("findCopy")
	@POST
	public List<MonthlyDataInforReturnDto> findCopy() {
		return this.outputItemMonthlyWorkScheduleFinder.getFormatMonthlyPerformance();
	}

	/**
	 * Execute copy.
	 *
	 * @param codeCopy
	 *            the code copy
	 * @param codeSourceSerivce
	 *            the code source serivce
	 * @param lstCommandCopy
	 *            the lst command copy
	 * @return the list
	 */
	@Path("executeCopy/{codeCopy}/{codeSourceSerivce}")
	@POST
	public List<MonthlyDataInforReturnDto> executeCopy(@PathParam("codeCopy") String codeCopy,
			@PathParam("codeSourceSerivce") String codeSourceSerivce,
			List<OutputItemMonthlyWorkScheduleCopyCommand> lstCommandCopy) {
		return this.outputItemMonthlyWorkScheduleFinder.executeCopy(codeCopy, codeSourceSerivce, lstCommandCopy);
	}

}
