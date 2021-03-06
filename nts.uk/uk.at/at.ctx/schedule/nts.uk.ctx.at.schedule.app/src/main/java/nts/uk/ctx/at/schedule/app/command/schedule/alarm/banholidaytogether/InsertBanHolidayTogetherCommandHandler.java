package nts.uk.ctx.at.schedule.app.command.schedule.alarm.banholidaytogether;

import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.schedule.dom.schedule.alarm.banholidaytogether.*;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.*;
import nts.uk.ctx.at.schedule.dom.shift.basicworkregister.ClassificationCode;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.workplace.TargetOrgIdenInfor;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.workplace.TargetOrganizationUnit;
import nts.uk.shr.com.context.AppContexts;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Optional;

/**
 * 同日休日禁止を新規する
 */
@Stateless
public class InsertBanHolidayTogetherCommandHandler extends CommandHandler<InsertBanHolidayTogetherDto> {
    @Inject
    private BanHolidayTogetherRepository banHolidayTogetherRepo;

    @Override
    protected void handle(CommandHandlerContext<InsertBanHolidayTogetherDto> context) {
        InsertBanHolidayTogetherDto command = context.getCommand();

        String companyId = AppContexts.user().companyId();

        TargetOrgIdenInfor targeOrg = new TargetOrgIdenInfor(
                EnumAdaptor.valueOf(command.getUnit(), TargetOrganizationUnit.class),
                Optional.ofNullable(command.getWorkplaceId()),
                Optional.ofNullable(command.getWorkplaceGroupId())
        );

        BanHolidayTogetherCode banHolidayCode = new BanHolidayTogetherCode(command.getBanHolidayTogetherCode());

        boolean isExist = banHolidayTogetherRepo.exists(companyId, targeOrg, banHolidayCode);
        if (isExist) {
            throw new BusinessException("Msg_3");
        }

        BanHolidayTogetherName banHolidayName = new BanHolidayTogetherName(command.getBanHolidayTogetherName());

        Optional<ReferenceCalendar> workDayReference = Optional.empty();
        if (command.getCheckDayReference()) {
            BusinessDaysCalendarType selectedWorkDayReference = EnumAdaptor.valueOf(command.getSelectedWorkDayReference(), BusinessDaysCalendarType.class);

            switch (selectedWorkDayReference){
                case COMPANY: {
                    ReferenceCalendarCompany referenceCalendarCompany = new ReferenceCalendarCompany();
                    workDayReference = Optional.ofNullable((ReferenceCalendar) referenceCalendarCompany);
                    break;
                }
                case WORKPLACE: {
                    ReferenceCalendarWorkplace referenceCalendarWorkplace = new ReferenceCalendarWorkplace(command.getWorkplaceInfoId());
                    workDayReference = Optional.ofNullable((ReferenceCalendar) referenceCalendarWorkplace);
                    break;
                }
                case CLASSSICATION: {
                    ReferenceCalendarClass referenceCalendarClass = new ReferenceCalendarClass(new ClassificationCode(command.getClassificationOrWorkplaceCode()));
                    workDayReference = Optional.ofNullable((ReferenceCalendar) referenceCalendarClass);
                    break;
                }
            }
        }

        BanHolidayTogether banHdTogether = BanHolidayTogether.create(
                targeOrg,
                banHolidayCode,
                banHolidayName,
                workDayReference,
                command.getMinNumberOfEmployeeToWork(),
                command.getEmpsCanNotSameHolidays()
        );

        banHolidayTogetherRepo.insert(companyId, banHdTogether);
    }
}
