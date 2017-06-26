package nts.uk.ctx.basic.app.command.organization.payclassification;


import javax.ejb.Stateless;
import javax.inject.Inject;
import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.basic.dom.organization.payclassification.PayClassification;
import nts.uk.ctx.basic.dom.organization.payclassification.PayClassificationCode;
import nts.uk.ctx.basic.dom.organization.payclassification.PayClassificationName;
import nts.uk.ctx.basic.dom.organization.payclassification.PayClassificationRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.primitive.Memo;

@Stateless
public class AddPayClassificationCommandHandler extends CommandHandler<AddPayClassificationCommand> {

	@Inject
	private PayClassificationRepository payClassificationRepository;

	@Override
	protected void handle(CommandHandlerContext<AddPayClassificationCommand> context) {
		

		String companyCode = AppContexts.user().companyCode();
		

		if(payClassificationRepository.isExisted(companyCode, 
				context.getCommand().getPayClassificationCode())){
			//throw err[ER026]
			throw new BusinessException(new RawErrorMessage("入力した コード は既に存在しています。 \r\n コードを確認してください。"));}
		
		PayClassification payClassification = new PayClassification(new Memo(context.getCommand().getMemo()),new PayClassificationName(context.getCommand().getPayClassificationName()),
				new PayClassificationCode(context.getCommand().getPayClassificationCode()),companyCode);
		payClassificationRepository.add(payClassification);
	}

}
	
