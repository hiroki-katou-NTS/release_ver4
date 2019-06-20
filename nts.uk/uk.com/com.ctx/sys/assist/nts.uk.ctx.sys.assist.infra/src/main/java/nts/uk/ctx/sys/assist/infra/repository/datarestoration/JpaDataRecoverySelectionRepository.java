package nts.uk.ctx.sys.assist.infra.repository.datarestoration;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.sys.assist.dom.datarestoration.DataRecoverySelection;
import nts.uk.ctx.sys.assist.dom.datarestoration.DataRecoverySelectionRepository;

@Stateless
public class JpaDataRecoverySelectionRepository extends JpaRepository implements DataRecoverySelectionRepository {

	private final String SELECT_FILE_RECOVERY_SELECTION_SAVE = "SELECT r.saveSetCode, r.saveName, m.suppleExplanation, r.saveStartDatetime, r.saveForm, "
			+ "r.targetNumberPeople, r.saveFileName, r.fileId,  r.storeProcessingId FROM SspmtResultOfSaving  r "
			+ "INNER JOIN SspmtManualSetOfDataSave m on r.storeProcessingId = m.storeProcessingId "
			+ "where r.cid = :companyId and r.saveStartDatetime >= :startDate and r.saveStartDatetime <= :endDate and "
			+ "r.deletedFiles = 0 and r.systemType in :systemType AND r.fileId IS NOT NULL AND r.saveStatus = 0";

	private final String SELECT_FILE_RECOVERY_SELECTION_DELETE = "SELECT r.delCode, r.delName, m.supplementExplanation, r.startDateTimeDel, r.delType, "
			+ "r.numberEmployees, r.fileName, r.fileId, r.sspdtResultDeletionPK.delId FROM SspdtResultDeletion  r "
			+ "INNER JOIN SspdtManualSetDeletion m on r.sspdtResultDeletionPK.delId = m.sspdtManualSetDeletionPK.delId "
			+ "where r.companyID = :companyId and r.startDateTimeDel >= :startDate and r.startDateTimeDel <= :endDate and "
			+ "r.isDeletedFilesFlg = 0 and r.systemType in :systemType AND r.fileId IS NOT NULL AND r.status = 0 ";

	@Override
	public List<DataRecoverySelection> getDataRecoverySelection(String companyId, List<Integer> systemType,
			GeneralDateTime startDate, GeneralDateTime endDate) {

		List<DataRecoverySelection> result = new ArrayList<>();
		if (systemType.isEmpty())
			return result;

		List<Object[]> dataSave = this.queryProxy().query(SELECT_FILE_RECOVERY_SELECTION_SAVE, Object[].class)
				.setParameter("companyId", companyId).setParameter("startDate", startDate)
				.setParameter("endDate", endDate).setParameter("systemType", systemType).getList();
		List<Object[]> dataDelete = this.queryProxy().query(SELECT_FILE_RECOVERY_SELECTION_DELETE, Object[].class)
				.setParameter("companyId", companyId).setParameter("startDate", startDate)
				.setParameter("endDate", endDate).setParameter("systemType", systemType).getList();

		List<Object[]> targetData = new ArrayList<>();
		targetData.addAll(dataSave);
		targetData.addAll(dataDelete);
		if (!targetData.isEmpty()) {
			for (val object : targetData) {
				String code = (String) object[0];
				String name = (String) object[1];
				String suppleExplanation = (String) object[2];
				GeneralDateTime saveStartDatetime = object[3] == null ? null
						: GeneralDateTime.fromString(object[3].toString(), "yyyy/MM/dd HH:mm:ss");
				Integer saveForm = object[4] == null ? null : Integer.parseInt(object[4].toString());
				Integer targetNumberPeople = object[5] == null ? null : Integer.parseInt(object[5].toString());
				String saveFileName = (String) object[6];
				String fileId = (String) object[7];
				String storeProcessingId = (String) object[8];
				result.add(new DataRecoverySelection(code, name, suppleExplanation, saveStartDatetime, saveForm,
						targetNumberPeople, saveFileName, fileId, storeProcessingId));
			}
			return result;
		}
		return result;
	}

}
