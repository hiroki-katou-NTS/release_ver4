/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.detailpayment.data;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class EmployeeDto.
 */
@Setter
@Getter
public class EmployeeDto {
    
    /** The code. */
    private String code;
    
    /** The name. */
    private String name;
    
    /** The department. */
    
    private DepartmentDto department;
    
}
