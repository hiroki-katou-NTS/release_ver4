//package nts.uk.query.model.department;
//
//import nts.arc.time.GeneralDate;
//
//import java.util.List;
//
//public interface DepartmentAdapter {
//
//    /**
//     * [No.562]運用している部門の情報をすべて取得する
//     *
//     * @param companyId
//     * @param baseDate
//     * @return
//     */
//    List<DepartmentInfoImport> getAllActiveDepartment(String companyId, GeneralDate baseDate);
//
//    /**
//     * [No.563]部門IDから部門の情報をすべて取得する
//     *
//     * @param companyId
//     * @param listDepartmentId
//     * @param baseDate
//     * @return
//     */
//    List<DepartmentInfoImport> getDepartmentInfoByDepIds(String companyId, List<String> listDepartmentId, GeneralDate baseDate);
//
//    /**
//     * [No.564]過去の部門の情報を取得する
//     *
//     * @param companyId
//     * @param depHistId
//     * @param listDepartmentId
//     * @return
//     */
//    List<DepartmentInfoImport> getPastDepartmentInfo(String companyId, String depHistId, List<String> listDepartmentId);
//}
