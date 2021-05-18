package nts.uk.screen.com.app.find.user.information.employee.contact;

import lombok.Data;
import lombok.Builder;
import nts.uk.ctx.bs.employee.dom.employee.data.management.contact.EmployeeContact;


/**
 * Dto 社員連絡先
 */
@Data
@Builder
public class EmployeeContactDto implements EmployeeContact.MementoSetter {
    /**
     * 社員ID
     */
    private String employeeId;

    /**
     * メールアドレス
     */
    private String mailAddress;

    /**
     * 座席ダイヤルイン
     */
    private String seatDialIn;

    /**
     * 座席内線番号
     */
    private String seatExtensionNumber;

    /**
     * 携帯メールアドレス
     */
    private String mobileMailAddress;

    /**
     * 携帯電話番号
     */
    private String cellPhoneNumber;
}
