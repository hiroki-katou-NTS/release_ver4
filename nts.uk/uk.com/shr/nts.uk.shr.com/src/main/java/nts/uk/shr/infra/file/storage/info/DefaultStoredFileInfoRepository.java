package nts.uk.shr.infra.file.storage.info;

import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.app.file.storage.StoredFileInfo;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.file.storage.StoredFileInfoRepository;
import nts.uk.shr.infra.file.storage.info.entity.CisdtStoredFile;

/**
 * DefaultStoredFileInfoRepository
 * @author kitahira
 */
@Stateless
public class DefaultStoredFileInfoRepository extends JpaRepository implements StoredFileInfoRepository {

	@Override
	public Optional<StoredFileInfo> find(String fileId) {
		return this.queryProxy().find(fileId, CisdtStoredFile.class)
				.map(e -> toDomain(e));
	}

	@Override
	public void add(StoredFileInfo fileInfo) {
		this.commandProxy().insert(toEntity(fileInfo));
	}

	@Override
	public void delete(String fileId) {
		this.commandProxy().remove(CisdtStoredFile.class, fileId);
	}

	private static StoredFileInfo toDomain(CisdtStoredFile entity) {
		return new StoredFileInfo(
				entity.fileId,
				entity.originalName,
				entity.fileType,
				entity.mimeType,
				entity.originalSizeBytes,
				entity.storedAt);
	}
	
	private static CisdtStoredFile toEntity(StoredFileInfo domain) {
		val entity = new CisdtStoredFile();
		entity.originalName = domain.getOriginalName();
		entity.fileId = domain.getId();
		entity.fileType = domain.getFileType();
		entity.mimeType = domain.getMimeType();
		entity.originalSizeBytes = domain.getOriginalSize();
		entity.storedAt = domain.getStoredAt();
		return entity;
	}
}
