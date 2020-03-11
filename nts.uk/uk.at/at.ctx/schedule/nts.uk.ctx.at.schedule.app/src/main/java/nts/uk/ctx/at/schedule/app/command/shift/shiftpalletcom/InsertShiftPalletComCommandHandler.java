package nts.uk.ctx.at.schedule.app.command.shift.shiftpalletcom;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import lombok.val;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.schedule.dom.shift.management.ShiftPalletsCom;
import nts.uk.ctx.at.schedule.dom.shift.management.ShiftPalletsComRepository;
import nts.uk.ctx.at.schedule.dom.shift.management.ShiftPalletsOrg;
import nts.uk.ctx.at.schedule.dom.shift.management.ShiftPalletsOrgRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author hieult
 *
 */
@Stateless
public class InsertShiftPalletComCommandHandler extends CommandHandler<InsertShiftPalletComCommand> {

	@Inject
	private ShiftPalletsComRepository repo;
	
	@Inject
	private ShiftPalletsOrgRepository orgRepository;
	@Override
	protected void handle(CommandHandlerContext<InsertShiftPalletComCommand> context) {
		InsertShiftPalletComCommand command = context.getCommand();
		
		if (StringUtils.isEmpty(command.workplaceId)) {
			val existed = repo.findShiftPallet(AppContexts.user().companyId(), command.groupNo);
			ShiftPalletsCom newShiftPalletsCom = command.toDomain();

			if (!existed.isPresent()) {
				//<<Command>> 会社別シフトパレットを登録する
				repo.add(newShiftPalletsCom);
			} else {
				//<<Command>> 会社別シフトパレットを更新する
				repo.update(newShiftPalletsCom);
			}
		} else {
			val existedOrg = orgRepository.findShiftPalletOrg(0, command.getWorkplaceId(), command.groupNo);
			ShiftPalletsOrg newShiftPalletsOrg = command.toDom();
			
			if (!existedOrg.isPresent())
				//<<Command>> 会社別シフトパレットを更新する
				orgRepository.add(newShiftPalletsOrg);
			else
				//<<Command>> 職場で使うシフトパレットを更新する
				orgRepository.update(newShiftPalletsOrg);
		}
	}

}
