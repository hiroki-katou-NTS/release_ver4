/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.insurance.salary;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class SocialInsuranceQuery.
 *
 */
@Setter
@Getter
public class SocialInsuQuery {

    /** The year month. */
    private Integer yearMonth;
    
    /** The is equal. */
    private Boolean isEqual;
    
    /** The is deficient. */
    private Boolean isDeficient;
    
    /** The is redundant. */
    private Boolean isRedundant;
    
    /** The office codes. */
    private List<String> officeCodes;
}
