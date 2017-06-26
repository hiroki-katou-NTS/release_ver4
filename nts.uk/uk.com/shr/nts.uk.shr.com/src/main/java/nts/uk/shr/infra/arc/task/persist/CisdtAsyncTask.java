package nts.uk.shr.infra.arc.task.persist;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import nts.arc.layer.infra.data.entity.type.GeneralDateTimeToDBConverter;
import nts.arc.time.GeneralDateTime;

@Entity
@Table(name="CISDT_ASYNC_TASK")
public class CisdtAsyncTask implements Serializable {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TASK_ID")
	public String taskId;
	
	@Column(name = "TASK_STS")
	public int taskSts;

	@Column(name = "CREATED_AT")
	@Convert(converter = GeneralDateTimeToDBConverter.class)
    public GeneralDateTime createdAt;

	@Convert(converter = GeneralDateTimeToDBConverter.class)
	@Column(name = "STARTED_AT")
    public GeneralDateTime startedAt;

	@Convert(converter = GeneralDateTimeToDBConverter.class)
	@Column(name = "FINISHED_AT")
    public GeneralDateTime finishedAt;
	
	@OneToOne(optional = true, cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	public CisdtAsyncTaskAbort abort;
	
}
