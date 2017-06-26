package nts.uk.shr.infra.file.storage.info.entity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import nts.arc.layer.infra.data.entity.type.GeneralDateTimeToDBConverter;
import nts.arc.time.GeneralDateTime;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@Table(name="CISDT_STORED_FILE")
public class CisdtStoredFile extends UkJpaEntity {

	@Id
	@Column(name = "FILE_ID")
	public String fileId;
	
	@Column(name = "ORIGINAL_NAME")
	public String originalName;
	
	@Column(name = "FILE_TYPE")
	public String fileType;
	
	@Column(name = "MIME_TYPE")
	public String mimeType;
	
	@Column(name = "ORIGINAL_SIZE_BYTES")
	public long originalSizeBytes;
	
	@Column(name = "STORED_AT")
	@Convert(converter = GeneralDateTimeToDBConverter.class)
    public GeneralDateTime storedAt;

	@Override
	protected Object getKey() {
		return this.fileId;
	}
	
}
