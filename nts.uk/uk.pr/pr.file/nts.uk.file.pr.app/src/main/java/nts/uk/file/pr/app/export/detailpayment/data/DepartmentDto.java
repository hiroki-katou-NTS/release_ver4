/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.detailpayment.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Class DepartmentDto.
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DepartmentDto {

    /** The year month. */
    private Integer yearMonth;
    
    /** The code. */
    private String code;
    
    /** The name. */
    private String name;
    
    /** The dep path. */
    private String depPath;
    
    /** The dep level. */
    private Integer depLevel;
    
    public static DepartmentDto newDepartment(DepartmentDto otherDep, Integer yearMonth) {
        DepartmentDto dep = new DepartmentDto();
        dep.yearMonth = yearMonth;
        dep.code = otherDep.code;
        dep.name = otherDep.name;
        dep.depPath = otherDep.depPath;
        dep.depLevel = otherDep.depLevel;
        return dep;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + depLevel;
        result = prime * result + ((depPath == null) ? 0 : depPath.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((yearMonth == null) ? 0 : yearMonth.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DepartmentDto other = (DepartmentDto) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        if (depLevel != other.depLevel)
            return false;
        if (depPath == null) {
            if (other.depPath != null)
                return false;
        } else if (!depPath.equals(other.depPath))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (yearMonth == null) {
            if (other.yearMonth != null)
                return false;
        } else if (!yearMonth.equals(other.yearMonth))
            return false;
        return true;
    }
}
