package nts.uk.ctx.at.shared.app.command.specialholiday;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.uk.ctx.at.shared.dom.specialholiday.SpecialHoliday;
import nts.uk.ctx.at.shared.dom.specialholiday.SpecialHolidayRepository;
import nts.uk.shr.com.context.AppContexts;

@Transactional
@RequestScoped
public class AddSpecialHolidayCommandHandler extends CommandHandlerWithResult<AddSpecialHolidayCommand, List<String>> {

	@Inject
	private SpecialHolidayRepository specialHolidayRepository;

	@Override
	protected List<String> handle(CommandHandlerContext<AddSpecialHolidayCommand> context) {
		List<String> errList = new ArrayList<String>();

		AddSpecialHolidayCommand addSpecialHolidayCommand = context.getCommand();
		String companyId = AppContexts.user().companyId();

		// check Error when Add
		boolean existsBranch = specialHolidayRepository.checkExists(companyId,
				addSpecialHolidayCommand.getSpecialHolidayCode());
		if (existsBranch) {
			addMessage(errList, "Msg_3");
		}

		SpecialHoliday specialHoliday = addSpecialHolidayCommand.toDomain(companyId);
		specialHoliday.validate();

		try {
			specialHoliday.validateInput();
		} catch (BusinessException e) {
			addMessage(errList, e.getMessageId());
		}

		try {
			this.validate(errList, specialHoliday);
		} catch (BusinessException e) {
			addMessage(errList, e.getMessageId());
		}

		if (errList.isEmpty()) {
			// add Special Holiday
			specialHolidayRepository.add(specialHoliday);
		}

		return errList;
	}

	private void validate(List<String> errList, SpecialHoliday specialHoliday) {
		if (specialHoliday.isMethodManageRemainNumber()) {
			try {
				specialHoliday.getGrantSingle().checkFixNumberDay();
			} catch (BusinessException e) {
				addMessage(errList, e.getMessageId());
			}
		} else {
			try {
				specialHoliday.getGrantRegular().checkTime();

			} catch (BusinessException e) {
				addMessage(errList, e.getMessageId());
			}
			
			try {
				specialHoliday.getGrantPeriodic().checkGrantDay();
			} catch (BusinessException e) {
				addMessage(errList, e.getMessageId());
			}
			
			try {
				specialHoliday.getSphdLimit().checkTime();
			} catch (BusinessException e) {
				addMessage(errList, e.getMessageId());
			} 
			
			try {
				specialHoliday.getSubCondition().checkAge();
			} catch (BusinessException e) {
				addMessage(errList, e.getMessageId());
			}
		}
	}

	/**
	 * Add exception message
	 * 
	 * @param exceptions
	 * @param messageId
	 */
	private void addMessage(List<String> errorsList, String messageId) {
		if (!errorsList.contains(messageId)) {
			errorsList.add(messageId);
		}
	}
}
