/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
/**
 * 
 */
package nts.uk.file.pr.app.export.insurance.data;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MLayoutInsuOfficeDto {
    
    /** The number of employee. */
    private int numberOfEmployee;
    
    /** The office code. */
    private String code;
    
    /** The office name. */
    private String name;
    
    /** The employees. */
    private List<MLayoutRowItem> employees;
    
    /** The total each office. */
    private MLayoutRowItem totalEachOffice;
}
