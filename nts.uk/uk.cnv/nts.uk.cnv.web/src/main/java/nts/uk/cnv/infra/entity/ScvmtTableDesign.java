package nts.uk.cnv.infra.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.layer.infra.data.entity.JpaEntity;
import nts.arc.time.GeneralDateTime;
import nts.uk.cnv.dom.tabledesign.ColumnDesign;
import nts.uk.cnv.dom.tabledesign.Indexes;
import nts.uk.cnv.dom.tabledesign.TableDesign;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SCVMT_TABLE_DESIGN")
public class ScvmtTableDesign extends JpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TABLE_ID")
	private String tableId;

	@Column(name = "NAME")
	private String name;

	@Column(name = "COMMENT")
	private String comment;

	@Column(name = "CREATE_DATE")
	private GeneralDateTime createDate;

	@Column(name = "UPDATE_DATE")
	public GeneralDateTime updateDate;

	@OrderBy(value = "scvmtColumnDesignPk.id asc")
	@OneToMany(targetEntity = ScvmtColumnDesign.class, mappedBy = "tabledesign", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "SCVMT_COLUMN_DESIGN")
	private List<ScvmtColumnDesign> columns;

	@OneToMany(targetEntity = ScvmtIndexDesign.class, mappedBy = "tabledesign", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "SCVMT_INDEX_DESIGN")
	private List<ScvmtIndexDesign> indexes;

	@Override
	protected Object getKey() {
		return name;
	}

	public TableDesign toDomain() {
		List<ColumnDesign> cols = columns.stream()
				.map(col -> col.toDomain())
				.collect(Collectors.toList());

		List<Indexes> idxs = new ArrayList<>();
		for (ScvmtIndexDesign index :indexes) {
			 List<String> colmns = index.columns.stream()
				.map(col -> col.pk.getColumnName())
				.collect(Collectors.toList());
			 List<String> params = new ArrayList<>();
			 for (String p : index.params.split(",")) {
				 params.add(p);
			 }
			 idxs.add(new Indexes(
					 index.pk.getName(),
					 index.type,
					 index.clustered,
					 colmns,
					 params
			));
		}

		return new TableDesign(tableId, name, comment, createDate, updateDate, cols, idxs);
	}
}
