package nts.uk.ctx.pr.core.app.command.rule.employment.averagepay;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.itemmaster.AvePayAtr;
import nts.uk.ctx.pr.core.dom.itemmaster.itemattend.ItemAttend;
import nts.uk.ctx.pr.core.dom.itemmaster.itemattend.ItemAttendRespository;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalary.ItemSalary;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalary.ItemSalaryRespository;
import nts.uk.ctx.pr.core.dom.rule.employment.averagepay.AttendDayGettingSet;
import nts.uk.ctx.pr.core.dom.rule.employment.averagepay.AveragePay;
import nts.uk.ctx.pr.core.dom.rule.employment.averagepay.AveragePayRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.averagepay.ExceptionPayRate;
import nts.uk.ctx.pr.core.dom.rule.employment.averagepay.RoundDigitSet;
import nts.uk.ctx.pr.core.dom.rule.employment.averagepay.RoundTimingSet;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
@Transactional
public class UpdateAveragePayCommandHandler extends CommandHandler<UpdateAveragePayCommand> {
	@Inject
	private AveragePayRepository averagePayRepository;

	@Inject
	private ItemSalaryRespository itemSalaryRespository;

	@Inject
	private ItemAttendRespository itemAttendRespository;

	@Override
	protected void handle(CommandHandlerContext<UpdateAveragePayCommand> context) {
		UpdateAveragePayCommand command = context.getCommand();

		String companyCode = AppContexts.user().companyCode();
		
		// validate exist
		Optional<AveragePay> avepay = this.averagePayRepository.findByCompanyCode(companyCode);
		if (!avepay.isPresent()) {
			throw new RuntimeException("Average payment not found");
		}
		
		// QAPMT_AVE_PAY.UPD_1
		AveragePay averagePay = new AveragePay(companyCode,
				EnumAdaptor.valueOf(command.getRoundTimingSet(), RoundTimingSet.class),
				EnumAdaptor.valueOf(command.getAttendDayGettingSet(), AttendDayGettingSet.class),
				EnumAdaptor.valueOf(command.getRoundDigitSet(), RoundDigitSet.class),
				new ExceptionPayRate(command.getExceptionPayRate()));
		averagePay.validate();
		averagePayRepository.update(averagePay);
		
		if(command.getSelectedSalaryItems().isEmpty()){
			throw new BusinessException("ER007");
		}
		
		// QCAMT_ITEM_SALARY.SEL_2
		List<ItemSalary> itemSalarys = this.itemSalaryRespository.findAll(companyCode);

		// QCAMT_ITEM_SALARY.UPD_2: item salary selected
		List<String> itemSelectedSalarys = itemSalarys.stream()
				.filter(x -> command.getSelectedSalaryItems().contains(x.getItemCode().v()))
				.map(x -> x.getItemCode().v())
				.collect(Collectors.toList());
		if(!itemSelectedSalarys.isEmpty()){
			this.itemSalaryRespository.updateItems(companyCode, itemSelectedSalarys, AvePayAtr.Object);
		}
		
		// QCAMT_ITEM_SALARY.UPD_2: item salary unselected
		List<String> itemUnselectedSalarys = itemSalarys.stream()
				.filter(x -> !command.getSelectedSalaryItems().contains(x.getItemCode().v()))
				.map(x -> x.getItemCode().v())
				.collect(Collectors.toList());
		if(!itemUnselectedSalarys.isEmpty()) {
			this.itemSalaryRespository.updateItems(companyCode, itemUnselectedSalarys, AvePayAtr.NotApplicable);
		}
		
		// if 明細書項目から選択 is selected
		if (averagePay.isAttenDayStatementItem()) {
			
			if(command.getSelectedAttendItems().isEmpty()){
				throw new BusinessException("ER007");
			}
			
			// QCAMT_ITEM_ATTEND.SEL_3
			List<ItemAttend> itemAttends = this.itemAttendRespository.findAll(companyCode);
			
			// QCAMT_ITEM_ATTEND.UPD_2: item attend selected
			List<String> itemSelectedAttends = itemAttends.stream()
					.filter(x -> command.getSelectedAttendItems().contains(x.getItemCode().v()))
					.map(x -> x.getItemCode().v())
					.collect(Collectors.toList());
			if(!itemSelectedAttends.isEmpty()) {
				this.itemAttendRespository.updateItems(companyCode, itemSelectedAttends, AvePayAtr.Object);
			}
			
			// QCAMT_ITEM_ATTEND.UPD_2: item attend unselected
			List<String> itemUnselectedAttends = itemAttends.stream()
					.filter(x -> !command.getSelectedAttendItems().contains(x.getItemCode().v()))
					.map(x -> x.getItemCode().v())
					.collect(Collectors.toList());
			if(!itemUnselectedAttends.isEmpty()) {
				this.itemAttendRespository.updateItems(companyCode, itemUnselectedAttends, AvePayAtr.NotApplicable);
			}
		}
	}
}
