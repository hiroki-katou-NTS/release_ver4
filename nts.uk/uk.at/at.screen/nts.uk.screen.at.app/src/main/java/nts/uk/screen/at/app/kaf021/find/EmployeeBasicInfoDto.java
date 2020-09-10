package nts.uk.screen.at.app.kaf021.find;

import lombok.Value;

@Value
public class EmployeeBasicInfoDto {
    /**
     * 社員ID
     */
    private String employeeId;
    /**
     * 社員コード
     */
    private String employeeCode;
    /**
     * 社員名
     */
    private String employeeName;
    /**
     * 所属ID
     */
    private String affId;
    /**
     * 所属名称
     */
    private String affName;
}
