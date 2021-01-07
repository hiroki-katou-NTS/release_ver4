package nts.uk.ctx.office.app.command.comment;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.office.dom.comment.EmployeeCommentInformation;
import nts.uk.ctx.office.dom.comment.EmployeeCommentInformationRepository;

/*
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.オフィス支援.在席照会.コメント.App.コメントを削除する.コメントを削除する
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class EmployeeCommentInformationDeleteCommandHandler extends CommandHandler<EmployeeCommentInformationCommand> {

	@Inject
	EmployeeCommentInformationRepository employeeCommentInformationRepository;

	@Override
	protected void handle(CommandHandlerContext<EmployeeCommentInformationCommand> context) {
		EmployeeCommentInformationCommand command = context.getCommand();
		EmployeeCommentInformation domain = EmployeeCommentInformation.createFromMemento(command);
		employeeCommentInformationRepository.delete(domain);
	}

}
