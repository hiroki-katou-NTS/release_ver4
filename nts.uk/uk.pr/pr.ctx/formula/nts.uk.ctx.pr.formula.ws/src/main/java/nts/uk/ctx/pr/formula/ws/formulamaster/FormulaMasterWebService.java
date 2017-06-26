package nts.uk.ctx.pr.formula.ws.formulamaster;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.formula.app.command.formulamaster.AddFormulaMasterCommand;
import nts.uk.ctx.pr.formula.app.command.formulamaster.AddFormulaMasterCommandHandler;
import nts.uk.ctx.pr.formula.app.command.formulamaster.UpdateFormulaMasterCommand;
import nts.uk.ctx.pr.formula.app.command.formulamaster.UpdateFormulaMasterCommandHandler;
import nts.uk.ctx.pr.formula.app.find.formula.CalculationTrial;
import nts.uk.ctx.pr.formula.app.find.formula.CalculatorDto;
import nts.uk.ctx.pr.formula.app.find.formula.FormulaBasicInformationDto;
import nts.uk.ctx.pr.formula.app.find.formula.FormulaBasicInformationFinder;
import nts.uk.ctx.pr.formula.app.find.formula.FormulaEasyFinder;
import nts.uk.ctx.pr.formula.app.find.formula.FormulaEasyFinderDto;
import nts.uk.ctx.pr.formula.app.find.formula.FormulaFinder;
import nts.uk.ctx.pr.formula.app.find.formula.FormulaFinderDto;
import nts.uk.ctx.pr.formula.app.find.formula.FormulaItemSelectDto;
import nts.uk.ctx.pr.formula.app.find.formula.FormulaItemSelectFinder;
import nts.uk.ctx.pr.formula.app.find.formula.FormulaManualDto;
import nts.uk.ctx.pr.formula.app.find.formula.FormulaManualLastestHistoryFinder;
import nts.uk.ctx.pr.formula.app.find.formula.FormulaSettingDto;
import nts.uk.ctx.pr.formula.app.find.formula.FormulaSettingFinder;
import nts.uk.ctx.pr.formula.app.find.formulahistory.FormulaHistoryDto;
import nts.uk.ctx.pr.formula.app.find.formulahistory.FormulaHistoryFinder;

@Path("pr/formula/formulaMaster")
@Produces("application/json")
public class FormulaMasterWebService extends WebService {

	@Inject
	private FormulaFinder formulaMasterFinder;
	@Inject
	private FormulaBasicInformationFinder formulaBasicInformationFinder;
	@Inject
	private FormulaSettingFinder formulaSettingFinder;
	@Inject
	private FormulaHistoryFinder formulaHistoryFinder;
	@Inject
	private FormulaItemSelectFinder formulaItemSelectFinder;
	@Inject
	private AddFormulaMasterCommandHandler addFormulaMasterCommandHandler;
	@Inject
	private UpdateFormulaMasterCommandHandler updateFormulaMasterCommandHandler;
	@Inject
	private FormulaEasyFinder formulaEasyFinder;
	@Inject
	private FormulaManualLastestHistoryFinder formulaManualLastestHistoryFinder;
	@Inject 
	private CalculationTrial calculationTrial;
	

	@POST
	@Path("getAllFormula")
	public List<FormulaFinderDto> getAllFormula() {
		return this.formulaMasterFinder.init();
	}

	@POST
	@Path("findFormula/{formulaCode}/{historyId}")
	public FormulaBasicInformationDto findFormula(@PathParam("formulaCode") String formulaCode,
			@PathParam("historyId") String historyId) {
		return this.formulaBasicInformationFinder.init(formulaCode, historyId);
	}
	
	@POST
	@Path("findLastestFormulaManual/{formulaCode}")
	public FormulaManualDto findLastestFormulaManual(@PathParam("formulaCode") String formulaCode) {
		return this.formulaManualLastestHistoryFinder.find(formulaCode);
	}

	@POST
	@Path("getFormulaDetail/{formulaCode}/{historyId}/{difficultyAtr}")
	public FormulaSettingDto getFormulaDetail(@PathParam("formulaCode") String formulaCode,
			@PathParam("historyId") String historyId, @PathParam("difficultyAtr") int difficultyAtr) {
		return this.formulaSettingFinder.init(formulaCode, historyId, difficultyAtr);
	}

	@POST
	@Path("findFormulaHistory/{formulaCode}/{historyId}")
	public FormulaHistoryDto findFormulaHistory(@PathParam("formulaCode") String formulaCode,
			@PathParam("historyId") String historyId) {
		return this.formulaHistoryFinder.findByPriKey(formulaCode, historyId).get();
	}
	
	@POST
	@Path("getFormulaEasyDetail/{formulaCode}/{historyId}/{easyFormulaCode}")
	public FormulaEasyFinderDto getFormulaEasyDetail(@PathParam("formulaCode") String formulaCode,
			@PathParam("historyId") String historyId, @PathParam("easyFormulaCode") String easyFormulaCode) {
		return this.formulaEasyFinder.init(formulaCode, historyId, easyFormulaCode);
	}

	@POST
	@Path("findOtherFormulas/{formulaCode}/{baseDate}")
	public List<FormulaItemSelectDto> getSelectedItems(@PathParam("formulaCode") String formulaCode,
			@PathParam("baseDate") int baseDate) {
		return this.formulaItemSelectFinder.init(formulaCode, baseDate);
	}
	
	@POST
	@Path("addFormulaMaster")
	public void addFormulaMaster (AddFormulaMasterCommand command){
		this.addFormulaMasterCommandHandler.handle(command);
	}
	
	@POST
	@Path("updateFormulaMaster")
	public void updateFormulaMaster (UpdateFormulaMasterCommand command){
		this.updateFormulaMasterCommandHandler.handle(command);
	}
	
	@POST
	@Path("trialCalculate")
	public CalculatorDto trialCalculate (String formulaContent){
		return this.calculationTrial.init(formulaContent);
	}
}
