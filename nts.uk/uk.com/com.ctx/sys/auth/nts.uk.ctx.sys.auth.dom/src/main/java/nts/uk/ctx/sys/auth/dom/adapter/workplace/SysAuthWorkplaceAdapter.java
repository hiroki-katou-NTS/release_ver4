//package nts.uk.ctx.sys.auth.dom.adapter.workplace;
//
//import nts.arc.time.GeneralDate;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface SysAuthWorkplaceAdapter {
//
//    /**
//     * [No.559]運用している職場の情報をすべて取得する
//     *
//     * @param companyId
//     * @param baseDate
//     * @return
//     */
//    List<WorkplaceInfoImport> getAllActiveWorkplaceInfo(String companyId, GeneralDate baseDate);
//
//    /**
//     * [No.560]職場IDから職場の情報をすべて取得する
//     *
//     * @param companyId
//     * @param listWorkplaceId
//     * @param baseDate
//     * @return
//     */
//    List<WorkplaceInfoImport> getWorkplaceInfoByWkpIds(String companyId, List<String> listWorkplaceId, GeneralDate baseDate);
//
//    /**
//     * [No.561]過去の職場の情報を取得する
//     *
//     * @param companyId
//     * @param historyId
//     * @param listWorkplaceId
//     * @return
//     */
//    List<WorkplaceInfoImport> getPastWorkplaceInfo(String companyId, String historyId, List<String> listWorkplaceId);
//
//    /**
//     * [No.567]職場の下位職場を取得する
//     *
//     * @param companyId
//     * @param baseDate
//     * @param parentWorkplaceId
//     * @return
//     */
//    List<String> getAllChildrenOfWorkplaceId(String companyId, GeneralDate baseDate, String parentWorkplaceId);
//
//    /**
//     * [No.573]職場の下位職場を基準職場を含めて取得する
//     *
//     * @param companyId
//     * @param baseDate
//     * @param workplaceId
//     * @return
//     */
//    List<String> getWorkplaceIdAndChildren(String companyId, GeneralDate baseDate, String workplaceId);
//
//    /**
//     *
//     * @param baseDate
//     * @param employeeId
//     * @return
//     */
//    Optional<AffWorkplaceHistImport> findWkpByBaseDateAndEmpId(GeneralDate baseDate, String employeeId);
//
//}
