package nts.uk.screen.at.app.command.kmk.kmk004.n;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.monunit.MonthlyWorkTimeSet.LaborWorkTypeAttr;
import nts.uk.screen.at.app.command.kmk.kmk004.monthlyworktimesetemp.SaveMonthlyWorkTimeSetEmpCommand;
import nts.uk.screen.at.app.command.kmk.kmk004.monthlyworktimesetemp.SaveMonthlyWorkTimeSetEmpCommandHandler;
import nts.uk.screen.at.app.query.kmk004.common.EmploymentCodeDto;
import nts.uk.screen.at.app.query.kmk004.common.EmploymentList;

/**
 * UKDesign.UniversalK.就業.KDW_日別実績.KMK_計算マスタ.KMK004_法定労働時間の登録（New）.N：雇用別法定労働時間の登録（変形労働）.メニュー別OCD.雇用別月単位労働時間（変形労働）を登録する
 * 
 * @author tutt
 *
 */
@Stateless
public class RegisterTransMonthlyWorkTimeSetEmpCommandHandler
		extends CommandHandlerWithResult<SaveMonthlyWorkTimeSetEmpCommand, List<EmploymentCodeDto>> {

	@Inject
	private SaveMonthlyWorkTimeSetEmpCommandHandler saveHandler;

	@Inject
	private EmploymentList employmentList;

	@Override
	protected List<EmploymentCodeDto> handle(CommandHandlerContext<SaveMonthlyWorkTimeSetEmpCommand> context) {

		// 1: <call> 雇用別月単位労働時間を登録・更新する
		this.saveHandler.handle(context.getCommand());

		// 2: <call> 雇用リストを表示する
		return this.employmentList.get(LaborWorkTypeAttr.DEFOR_LABOR);
	}

}
