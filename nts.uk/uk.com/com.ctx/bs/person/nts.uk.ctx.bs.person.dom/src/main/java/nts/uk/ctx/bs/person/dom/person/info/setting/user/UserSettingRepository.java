package nts.uk.ctx.bs.person.dom.person.info.setting.user;

import java.util.Optional;

public interface UserSettingRepository {

	boolean checkUserSettingExist(String employeeId);

	void createUserSetting(UserSetting userSetting);

	void updateUserSetting(UserSetting userSetting);

	Optional<UserSetting> getUserSetting(String EmployeeId);

}
