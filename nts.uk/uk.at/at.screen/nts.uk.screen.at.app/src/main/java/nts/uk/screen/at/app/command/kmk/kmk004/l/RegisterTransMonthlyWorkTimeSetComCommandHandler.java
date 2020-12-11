package nts.uk.screen.at.app.command.kmk.kmk004.l;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.screen.at.app.command.kmk.kmk004.monthlyworktimesetcom.SaveMonthlyWorkTimeSetComCommand;
import nts.uk.screen.at.app.command.kmk.kmk004.monthlyworktimesetcom.SaveMonthlyWorkTimeSetComCommandHandler;

/**
 * UKDesign.UniversalK.就業.KDW_日別実績.KMK_計算マスタ.KMK004_法定労働時間の登録（New）.L：会社別法定労働時間の登録（変形労働）.メニュー別OCD.会社別月単位労働時間（変形労働）を登録する
 * 
 * @author tutt
 *
 */
@Stateless
public class RegisterTransMonthlyWorkTimeSetComCommandHandler extends CommandHandler<SaveMonthlyWorkTimeSetComCommand> {

	@Inject
	private SaveMonthlyWorkTimeSetComCommandHandler saveHandler;

	@Override
	public void handle(CommandHandlerContext<SaveMonthlyWorkTimeSetComCommand> context) {

		// 会社別月単位労働時間（変形労働）を登録する
		this.saveHandler.handle(context.getCommand());
	}

}
