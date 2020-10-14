package nts.uk.query.app.user.information;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHist;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHistRepository;
import nts.uk.ctx.bs.employee.dom.employee.mgndata.EmployeeDataMngInfo;
import nts.uk.ctx.bs.employee.dom.employee.mgndata.EmployeeDataMngInfoRepository;
import nts.uk.ctx.bs.person.dom.person.info.Person;
import nts.uk.ctx.bs.person.dom.person.info.PersonRepository;
import nts.uk.ctx.sys.auth.dom.adapter.employee.employeeinfo.EmployeeInformationImport;
import nts.uk.ctx.sys.auth.dom.adapter.employee.service.EmployeeService;
import nts.uk.ctx.sys.auth.dom.anniversary.AnniversaryNotice;
import nts.uk.ctx.sys.auth.dom.anniversary.AnniversaryRepository;
import nts.uk.ctx.sys.auth.dom.avatar.AvatarRepository;
import nts.uk.ctx.sys.auth.dom.avatar.UserAvatar;
import nts.uk.ctx.sys.auth.dom.employee.contact.EmployeeContact;
import nts.uk.ctx.sys.auth.dom.employee.contact.EmployeeContactRepository;
import nts.uk.ctx.sys.auth.dom.password.changelog.PasswordChangeLog;
import nts.uk.ctx.sys.auth.dom.password.changelog.PasswordChangeLogRepository;
import nts.uk.ctx.sys.auth.dom.personal.contact.PersonalContact;
import nts.uk.ctx.sys.auth.dom.personal.contact.PersonalContactRepository;
import nts.uk.ctx.sys.auth.dom.user.User;
import nts.uk.ctx.sys.auth.dom.user.UserRepository;
import nts.uk.ctx.sys.gateway.dom.login.ContractCode;
import nts.uk.ctx.sys.gateway.dom.securitypolicy.PasswordPolicy;
import nts.uk.ctx.sys.gateway.dom.securitypolicy.PasswordPolicyRepository;
import nts.uk.query.app.user.information.anniversary.AnniversaryNoticeDto;
import nts.uk.query.app.user.information.avatar.UserAvatarDto;
import nts.uk.query.app.user.information.employee.contact.EmployeeContactDto;
import nts.uk.query.app.user.information.employee.data.management.information.EmployeeDataMngInfoDto;
import nts.uk.query.app.user.information.password.changelog.PasswordChangeLogDto;
import nts.uk.query.app.user.information.password.policy.PasswordPolicyDto;
import nts.uk.query.app.user.information.personal.contact.PersonalContactDto;
import nts.uk.query.app.user.information.personal.infomation.PersonDto;
import nts.uk.query.app.user.information.setting.SettingInformationDto;
import nts.uk.query.app.user.information.user.UserDto;
import nts.uk.shr.com.context.AppContexts;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Stateless
public class UserInformationScreenQuery {

    @Inject
    private EmployeeService employeeService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PersonRepository personRepository;

    @Inject
    private EmployeeDataMngInfoRepository employeeDataMngInfoRepository;

    @Inject
    private PersonalContactRepository personalContactRepository;

    @Inject
    private AffCompanyHistRepository affCompanyHistRepository;

    @Inject
    private AnniversaryRepository anniversaryRepository;

    @Inject
    private PasswordChangeLogRepository passwordChangeLogRepository;

    @Inject
    private PasswordPolicyRepository passwordPolicyRepository;

    @Inject
    private EmployeeContactRepository employeeContactRepository;

    @Inject
    private AvatarRepository avatarRepository;

    public UserInformationDto getUserInformation() {

        String loginUserId = AppContexts.user().userId();
        String loginPersonalId = AppContexts.user().personId();
        String loginEmployeeId = AppContexts.user().employeeId();
        String loginContractCode = AppContexts.user().contractCode();

        //TODO SQ1 - get ユーザー情報の使用方法 from CMM049
        SettingInformationDto settingInformationDto = SettingInformationDto.builder().build();
        //SQ2 - call DomainService 社員情報を取得する
        EmployeeInformationImport employeeInformation = employeeService.getEmployeeInformation(loginEmployeeId, GeneralDate.today());
        String positionName = employeeInformation.getPositionName();
        String wkpDisplayName = employeeInformation.getWkpDisplayName();
        Boolean isInCharge = employeeInformation.isEmployeeCharge();

        //TODO SQ3 - Handle ユーザ情報の使用方法.Empty (pending because of CMM049)

        //SQ4 - get ユーザ
        Optional<User> loginUser = userRepository.getByUserID(loginUserId);
        UserDto userDto = loginUser.map(UserDto::toDto).orElse(new UserDto());

        //SQ5 - get 個人
        Optional<Person> personalInfo = personRepository.getByPersonId(loginPersonalId);
        PersonDto personDto = personalInfo.map(PersonDto::toDto).orElse(new PersonDto());

        //SQ6 - get 社員データ管理情報
        Optional<EmployeeDataMngInfo> employeeInfo = employeeDataMngInfoRepository.findByEmpId(loginEmployeeId);
        EmployeeDataMngInfoDto employeeDataMngInfoDto = employeeInfo.map(EmployeeDataMngInfoDto::toDto).orElse(new EmployeeDataMngInfoDto());

        //SQ7 - get 個人連絡先 dang chet o day
        Optional<PersonalContact> personalContact = personalContactRepository.getByPersonalId(loginPersonalId);
        PersonalContactDto personalContactDto = PersonalContactDto.builder().build();
        personalContact.ifPresent(contact -> contact.setMemento(personalContactDto));

        //TODO SQ8 - call 所属会社履歴
        AffCompanyHist affCompanyHist = affCompanyHistRepository.getAffCompanyHistoryOfEmployee(loginEmployeeId);
        GeneralDate hireDate = GeneralDate.ymd(1999,10,7);

        //SQ9 - get 個人の記念日情報
        List<AnniversaryNotice> anniversaryNoticeList = anniversaryRepository.getByPersonalId(loginPersonalId);
        List<AnniversaryNoticeDto> anniversaryNoticeDtos = new ArrayList<>();
        for (AnniversaryNotice anniversaryNotice : anniversaryNoticeList) {
            AnniversaryNoticeDto anniversaryNoticeDto = AnniversaryNoticeDto.builder().build();
            anniversaryNotice.setMemento(anniversaryNoticeDto);
            anniversaryNoticeDtos.add(anniversaryNoticeDto);
        }

        //SQ10 - get パスワード変更ログ
        List<PasswordChangeLog> passwordChangeLogs = passwordChangeLogRepository.findByUserId(loginUserId, 1);
        PasswordChangeLog passwordChangeLog = passwordChangeLogs.isEmpty() ? null : passwordChangeLogs.get(0);
        PasswordChangeLogDto passwordChangeLogDto = PasswordChangeLogDto.toDto(passwordChangeLog);

        //SQ11 - get パスワードポリシー
        Optional<PasswordPolicy> passwordPolicy = passwordPolicyRepository.getPasswordPolicy(new ContractCode(loginContractCode));
        PasswordPolicyDto passwordPolicyDto = passwordPolicy.map(PasswordPolicyDto::toDto).orElse(new PasswordPolicyDto());

        //SQ12 - get 社員連絡先
        Optional<EmployeeContact> employeeContact = employeeContactRepository.getByEmployeeId(loginEmployeeId);
        EmployeeContactDto employeeContactDto = EmployeeContactDto.builder().build();
        employeeContact.ifPresent(contact -> contact.setMemento(employeeContactDto));

        //SQ13 - get 個人の顔写真
        Optional<UserAvatar> userAvatar = avatarRepository.getAvatarByPersonalId(loginPersonalId);
        UserAvatarDto userAvatarDto = UserAvatarDto.builder().build();
        userAvatar.ifPresent(avatar -> avatar.setMemento(userAvatarDto));

        return UserInformationDto.builder()
                .passwordPolicy(passwordPolicyDto)
                .isInCharge(isInCharge)
                .settingInformation(settingInformationDto)
                .hireDate(hireDate)
                .user(userDto)
                .person(personDto)
                .personalContact(personalContactDto)
                .employeeDataMngInfo(employeeDataMngInfoDto)
                .employeeContact(employeeContactDto)
                .passwordChangeLog(passwordChangeLogDto)
                .anniversaryNotices(anniversaryNoticeDtos)
                .userAvatar(userAvatarDto)
                .positionName(positionName)
                .wkpDisplayName(wkpDisplayName)
                .build();
    }
}
