package nts.uk.screen.at.app.command.kmk.kmk004.common.monthlyworktimesetcom;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.monunit.MonthlyWorkTimeSetCom;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.monunit.MonthlyWorkTimeSetRepo;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author sonnlb
 *
 *         UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.shared(勤務予定、勤務実績).法定労働時間.法定労働時間（New）.月単位の法定労働時間.APP.会社別月単位労働時間を登録・更新する.会社別月単位労働時間を登録・更新する
 */
@Stateless
public class SaveMonthlyWorkTimeSetComCommandHandler extends CommandHandler<SaveMonthlyWorkTimeSetComCommand> {

	@Inject
	private MonthlyWorkTimeSetRepo monthlyWorkTimeSetRepo;

	@Override
	protected void handle(CommandHandlerContext<SaveMonthlyWorkTimeSetComCommand> context) {
		SaveMonthlyWorkTimeSetComCommand cmd = context.getCommand();

		// Loop：Listの年月ごと（12ヵ月分）
		// 1.チェックする
		// 2.月間労働時間.inv-1
		// 3.BusinessException=0件
		List<MonthlyWorkTimeSetCom> setComs = cmd.getWorkTimeSetComs().stream().map(wkTimeset -> wkTimeset.toDomain())
				.collect(Collectors.toList());
		
		for (MonthlyWorkTimeSetCom setcom : setComs) {
			int legalLaborTime = setcom.getLaborTime().getLegalLaborTime().v();
			
			int weekAvgTime = setcom.getLaborTime().getWithinLaborTime().map(x -> x.v()).orElse(0);
			
			if (weekAvgTime > legalLaborTime) {

				throw new BusinessException("Msg_1906");
			}
		}

		setComs.forEach(setCom -> {
			// 1.get(ログイン会社ID,勤務区分,年月)
			Optional<MonthlyWorkTimeSetCom> setComOpt = this.monthlyWorkTimeSetRepo
					.findCompany(AppContexts.user().companyId(), setCom.getLaborAttr(), setCom.getYm());

			if (setComOpt.isPresent()) {
				// 会社別月単位労働時間 not Empty : update
				this.monthlyWorkTimeSetRepo.update(setCom);
			} else {
				// 会社別月単位労働時間 isEmpty :add
				this.monthlyWorkTimeSetRepo.add(setCom);
			}
		});
	}

}
