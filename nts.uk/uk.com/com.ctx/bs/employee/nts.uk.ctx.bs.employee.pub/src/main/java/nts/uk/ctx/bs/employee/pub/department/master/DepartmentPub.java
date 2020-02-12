package nts.uk.ctx.bs.employee.pub.department.master;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

public interface DepartmentPub {

	/**
	 * [No.562]運用している部門の情報をすべて取得する
	 * 
	 * @param companyId
	 * @param baseDate
	 * @return
	 */
	public List<DepartmentInforExport> getAllActiveDepartment(String companyId, GeneralDate baseDate);

	/**
	 * [No.563]部門IDから部門の情報をすべて取得する
	 * 
	 * @param companyId
	 * @param listDepartmentId
	 * @param baseDate
	 * @return
	 */
	public List<DepartmentInforExport> getDepartmentInforByDepIds(String companyId, List<String> listDepartmentId,
			GeneralDate baseDate);

	/**
	 * [No.564]過去の部門の情報を取得する
	 * 
	 * @param companyId
	 * @param depHistId
	 * @param listDepartmentId
	 * @return
	 */
	public List<DepartmentInforExport> getPastDepartmentInfor(String companyId, String depHistId,
			List<String> listDepartmentId);

	// for salary qmm016, 017
    List<DepartmentExport> getDepartmentByCompanyIdAndBaseDate(String companyId, GeneralDate baseDate);
    

    /**
     * [No.568]部門の下位部門を取得する
     *
     * @param companyId
     * @param baseDate
     * @param parentDepartmentId
     * @return
     */
    public List<String> getAllChildDepartmentId(String companyId, GeneralDate baseDate, String parentDepartmentId);

    /**
     * [No.574]部門の下位部門を基準部門を含めて取得する
     *
     * @param companyId
     * @param baseDate
     * @param departmentId
     * @return
     */
    public List<String> getDepartmentIdAndChildren(String companyId, GeneralDate baseDate, String departmentId);
    
    public Optional<DepartmentExport> getInfoDep(String companyId, String depId, GeneralDate baseDate);
    
    /**
     * 社員と基準日から所属部門履歴項目を取得する
     * @param employeeID
     * @param date
     * @return
     */
    public String getDepartmentIDByEmpDate(String employeeID, GeneralDate date);
    
    /**
     * 部門の上位部門を取得する
     * @param companyID
     * @param departmentID
     * @param date
     * @return
     */
    public List<String> getUpperDepartment(String companyID, String departmentID, GeneralDate date);
}
