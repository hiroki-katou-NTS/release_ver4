package nts.uk.ctx.at.schedule.app.command.schedule.setting.modifydeadline;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.schedule.dom.schedule.setting.modify.control.CommonAuthor;
import nts.uk.ctx.at.schedule.dom.schedule.setting.modify.control.CommonAuthorRepository;
import nts.uk.ctx.at.schedule.dom.schedule.setting.modify.control.DateAuthority;
import nts.uk.ctx.at.schedule.dom.schedule.setting.modify.control.DateAuthorityRepository;
import nts.uk.ctx.at.schedule.dom.schedule.setting.modify.control.PerWorkplace;
import nts.uk.ctx.at.schedule.dom.schedule.setting.modify.control.PerWorkplaceRepository;
import nts.uk.ctx.at.schedule.dom.schedule.setting.modify.control.PersAuthority;
import nts.uk.ctx.at.schedule.dom.schedule.setting.modify.control.PersAuthorityRepository;
import nts.uk.ctx.at.schedule.dom.schedule.setting.modify.control.ScheModifyDeadlineRepository;
import nts.uk.ctx.at.schedule.dom.schedule.setting.modify.control.SchemodifyDeadline;
import nts.uk.ctx.at.schedule.dom.schedule.setting.modify.control.ShiftPermisson;
import nts.uk.ctx.at.schedule.dom.schedule.setting.modify.control.ShiftPermissonRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author phongtq
 *
 */
@Transactional
@Stateless
public class AddPermissonCommandHandler extends CommandHandler<AddPermissonCommand> {
	@Inject
	private ScheModifyDeadlineRepository modifyDeadlineRepository;
	@Inject
	private CommonAuthorRepository commonAuthorRepository;
	@Inject
	private DateAuthorityRepository dateAuthorityRepository;
	@Inject
	private PersAuthorityRepository persAuthorityRepository;
	@Inject
	private PerWorkplaceRepository perWorkplaceRepository;
	@Inject
	private ShiftPermissonRepository shiftPermissonRepository;

	@Override
	protected void handle(CommandHandlerContext<AddPermissonCommand> context) {

		String companyId = AppContexts.user().companyId();

		// Add Schemodify Deadline
		SchemodifyDeadlineCommand deadlineCommand = context.getCommand().getSchemodifyDeadline();
		SchemodifyDeadline deadline = deadlineCommand.toDomain(companyId);
		Optional<SchemodifyDeadline> optionalDeadline = this.modifyDeadlineRepository.findByCId(companyId,
				deadlineCommand.getRoleId());

		if (optionalDeadline.isPresent()) {
			// update Schemodify Deadline
			this.modifyDeadlineRepository.update(deadline);
		} else {
			// add Schemodify Deadline
			this.modifyDeadlineRepository.add(deadline);
		}

		// Add Common Author
		List<CommonAuthorCommand> authorCommand = context.getCommand().getCommonAuthor();
		List<CommonAuthor> commonAuthors = commonAuthorRepository.findByCompanyId(companyId,
				deadlineCommand.getRoleId());

		for (CommonAuthorCommand item : authorCommand) {
			CommonAuthor author = item.toDomain(companyId);
			if (commonAuthors.stream().anyMatch(x -> x.getRoleId().equals(item.getRoleId())
					&& x.getFunctionNoCommon() == item.getFunctionNoCommon())) {
				// update Common Author
				this.commonAuthorRepository.update(author);
			} else {
				// add Common Author
				this.commonAuthorRepository.add(author);
			}
		}

		// Add Date Authority
		List<DateAuthorityCommand> dateAuthorityCommand = context.getCommand().getDateAuthority();
		List<DateAuthority> optionalDate = dateAuthorityRepository.findByCompanyId(companyId,
				deadlineCommand.getRoleId());

		for (DateAuthorityCommand item : dateAuthorityCommand) {
			DateAuthority author = item.toDomain(companyId);
			if (optionalDate.stream().anyMatch(
					x -> x.getRoleId().equals(item.getRoleId()) && x.getFunctionNoDate() == item.getFunctionNoDate())) {
				// update Date Authority
				this.dateAuthorityRepository.update(author);
			} else {
				// add Date Authority
				this.dateAuthorityRepository.add(author);
			}
		}

		// Add Pers Authority
		List<PersAuthorityCommand> persAuthorityCommand = context.getCommand().getPersAuthority();
		List<PersAuthority> optionalPers = this.persAuthorityRepository.findByCompanyId(companyId,
				deadlineCommand.getRoleId());

		for (PersAuthorityCommand item : persAuthorityCommand) {
			PersAuthority author = item.toDomain(companyId);
			if (optionalPers.stream().anyMatch(
					x -> x.getRoleId().equals(item.getRoleId()) && x.getFunctionNoPers() == item.getFunctionNoPers())) {
				// update Pers Authority
				this.persAuthorityRepository.update(author);
			} else {
				// add Pers Authority
				this.persAuthorityRepository.add(author);
			}
		}

		// Add Per Workplace
		List<PerWorkplaceCommand> perWorkplaceCommand = context.getCommand().getPerWorkplace();
		List<PerWorkplace> optionalWorkplace = this.perWorkplaceRepository.findByCompanyId(companyId,
				deadlineCommand.getRoleId());

		for (PerWorkplaceCommand item : perWorkplaceCommand) {
			PerWorkplace author = item.toDomain(companyId);
			if (optionalWorkplace.stream().anyMatch(x -> x.getRoleId().equals(item.getRoleId())
					&& x.getFunctionNoWorkplace() == item.getFunctionNoWorkplace())) {
				// update Per Workplace
				this.perWorkplaceRepository.update(author);
			} else {
				// add Per Workplace
				this.perWorkplaceRepository.add(author);
			}
		}

		// Add Shift Permisson
		List<ShiftPermissonCommand> shiftPermissonCommand = context.getCommand().getShiftPermisson();
		List<ShiftPermisson> optionalShift = this.shiftPermissonRepository.findByCompanyId(companyId,
				deadlineCommand.getRoleId());

		for (ShiftPermissonCommand item : shiftPermissonCommand) {
			ShiftPermisson author = item.toDomain(companyId);
			if (optionalShift.stream().anyMatch(x -> x.getRoleId().equals(item.getRoleId())
					&& x.getFunctionNoShift() == item.getFunctionNoShift())) {
				// update Shift Permisson
				this.shiftPermissonRepository.update(author);
			} else {
				// add Shift Permisson
				this.shiftPermissonRepository.add(author);
			}
		}
	}

}
