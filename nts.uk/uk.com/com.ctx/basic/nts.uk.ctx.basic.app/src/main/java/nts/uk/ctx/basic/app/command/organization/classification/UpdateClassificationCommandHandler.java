package nts.uk.ctx.basic.app.command.organization.classification;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.basic.dom.organization.classification.Classification;
import nts.uk.ctx.basic.dom.organization.classification.ClassificationCode;
import nts.uk.ctx.basic.dom.organization.classification.ClassificationName;
import nts.uk.ctx.basic.dom.organization.classification.ClassificationRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.primitive.Memo;

@Stateless
public class UpdateClassificationCommandHandler extends CommandHandler<UpdateClassificationCommand> {

	@Inject
	private ClassificationRepository classificationRepository;

	@Override
	protected void handle(CommandHandlerContext<UpdateClassificationCommand> context) {
		String companyCode = AppContexts.user().companyCode();
		// Check xem classification còn tồn tại ko?
		if (!classificationRepository.isExisted(companyCode,
				new ClassificationCode(context.getCommand().getClassificationCode()))) {
			throw new BusinessException("ER026");
		}
		Classification classification = new Classification(companyCode,
				new ClassificationCode(context.getCommand().getClassificationCode()),
				new ClassificationName(context.getCommand().getClassificationName()), null,
				new Memo(context.getCommand().getMemo()));
		classificationRepository.update(classification);
	}

}
