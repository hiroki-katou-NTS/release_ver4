package nts.uk.ctx.at.shared.app.command.remainingnumber;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.PayoutManagementData;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.PayoutManagementDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.PayoutSubofHDManaRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.SubstitutionOfHDManaDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.SubstitutionOfHDManagementData;

/*
 * 振休振出管理データを削除 
 * Delete management data of taking holidays
 */
@Stateless
public class DeletePaymentManagementDataCmdHandler extends CommandHandler<DeletePaymentManagementDataCommand>{
	
	@Inject 
	private PayoutManagementDataRepository payoutManagementDataRepo;

	@Inject
	private SubstitutionOfHDManaDataRepository substitutionOfHDManaDataRepo;
	
	@Inject
	private PayoutSubofHDManaRepository payoutSubofHDManaRepository;
	
	@Override
	protected void handle(CommandHandlerContext<DeletePaymentManagementDataCommand> context) {
		DeletePaymentManagementDataCommand command = context.getCommand();
		//	ドメインモデル「振出管理データ」を取得
		List<PayoutManagementData> dataPayout = payoutManagementDataRepo.getByListId(command.getPayoutId());
		//	ドメインモデル「振出管理データ」を削除 (Delete domain model「振出管理データ」)
		if (!dataPayout.isEmpty()) {
			payoutManagementDataRepo.delete(dataPayout);
		}
		//	ドメインモデル「振休管理データ」を取得
		List<SubstitutionOfHDManagementData> dataSub = substitutionOfHDManaDataRepo
				.getByListId(command.getSubOfHDID());
		// ドメインモデル「振休管理データ」を削除 (Delete domain model 「振休管理データ」)
		if (!dataSub.isEmpty()) {
			substitutionOfHDManaDataRepo.delete(dataSub);
		}
		//	ドメインモデル「振出振休紐付け管理」を削除 (Delete domain model 「振出振休紐付け管理」)
		if (!dataPayout.isEmpty() && !dataSub.isEmpty()) {
			this.payoutSubofHDManaRepository.delete(dataPayout.size() > 0 ? dataPayout.get(0).getSID() : null,
				dataSub.size() > 0 ? dataSub.get(0).getSID() : null,
				dataPayout.stream().map(x -> x.getPayoutDate().getDayoffDate().orElse(null)).collect(Collectors.toList()),
				dataSub.stream().map(x -> x.getHolidayDate().getDayoffDate().orElse(null)).collect(Collectors.toList()));
		}
	}

}
