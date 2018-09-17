package  nts.uk.ctx.pr.core.app.command.laborinsurance;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.core.dom.laborinsurance.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@Transactional
public class AddOccAccIsPrRateCommandHandler extends CommandHandler<AddOccAccIsPrRateCommand>
{

    @Inject
    private OccAccInsurBusiBurdenRatioService occAccInsurBusiBurdenRatioService;


    @Override
    protected void handle(CommandHandlerContext<AddOccAccIsPrRateCommand> context) {
        AddOccAccIsPrRateCommand command =context.getCommand();
        YearMonth startYearMonth = new YearMonth(command.getStartYearMonth());
        YearMonth endYearMonth = new YearMonth(command.getEndYearMonth());
        List<OccAccInsurBusiBurdenRatio>  occAccInsurBusiBurdenRatios = command.getListAccInsurPreRate().stream().map(item -> {
            return new OccAccInsurBusiBurdenRatio(item.getOccAccInsurBusNo(), EnumAdaptor.valueOf(item.getFracClass(), InsuPremiumFractionClassification.class), new InsuranceRate(new BigDecimal(item.getEmpConRatio())));
        }).collect(Collectors.toList());
          if (command.isNewMode()) {
            occAccInsurBusiBurdenRatioService.addOccAccInsurBusiBurdenRatio(occAccInsurBusiBurdenRatios, startYearMonth, endYearMonth);
        } else {
            occAccInsurBusiBurdenRatioService.updateOccAccInsurBusiBurdenRatio(occAccInsurBusiBurdenRatios,command.getHisId());
        }
    }
}
