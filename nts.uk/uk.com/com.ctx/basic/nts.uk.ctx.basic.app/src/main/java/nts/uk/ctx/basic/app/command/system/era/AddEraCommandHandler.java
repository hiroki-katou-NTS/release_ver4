package nts.uk.ctx.basic.app.command.system.era;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.arc.time.GeneralDate;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.basic.app.find.system.era.EraDto;
import nts.uk.ctx.basic.dom.system.era.Era;
import nts.uk.ctx.basic.dom.system.era.EraRepository;
import nts.uk.ctx.basic.dom.system.era.FixAttribute;

@RequestScoped
@Transactional
public class AddEraCommandHandler extends CommandHandlerWithResult<AddEraCommand, EraDto> {
	@Inject
	private EraRepository eraRepository;

	@Override
	public EraDto handle(CommandHandlerContext<AddEraCommand> context) {
		Era era = context.getCommand().toDomain();
		era.setEraHist(IdentifierUtil.randomUniqueId());
		era.validate();
		era.setEndDate(GeneralDate.ymd(9999, 12, 31));
		era.setFixAttribute(FixAttribute.EDITABLE);
		Optional<Era> latestEra = this.eraRepository.getLatestEra();
		if (latestEra.isPresent()) {
			if(era.getStartDate().beforeOrEquals(latestEra.get().getStartDate())){
				throw new BusinessException("履歴の期間が正しくありません。");
			};
			latestEra.get().setEndDate(era.getStartDate().addDays(-1));
			this.eraRepository.update(latestEra.get());
		}
		
		this.eraRepository.add(era);
		return EraDto.fromDomain(era);
	}
	
	
	

}
