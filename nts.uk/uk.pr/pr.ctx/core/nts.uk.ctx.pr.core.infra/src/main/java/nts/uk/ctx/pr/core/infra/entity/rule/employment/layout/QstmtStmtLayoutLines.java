package nts.uk.ctx.pr.core.infra.entity.rule.employment.layout;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="QSTMT_STMT_LAYOUT_LINES")
public class QstmtStmtLayoutLines implements Serializable {

	public static final long serialVersionUID = 1L;

	@EmbeddedId
	public QstmtStmtLayoutLinesPK qstmtStmtLayoutLinesPk;

// Lanlt remove	
//	@Basic(optional = false)
//	@Column(name ="STR_YM")
//	public int strYm;
//
//	@Basic(optional = false)
//	@Column(name ="END_YM")
//	public int endYm;

	@Basic(optional = false)
	@Column(name ="LINE_POS")
	public int linePos;

	@Basic(optional = false)
	@Column(name ="LINE_DISP_SET")
	public int lineDispAtr;
	
//	@ManyToOne
//	@JoinColumns({
//        @JoinColumn(name="CCD", referencedColumnName="CCD", insertable = false, updatable = false),
//        @JoinColumn(name="STMT_CD", referencedColumnName="STMT_CD", insertable = false, updatable = false),
//        @JoinColumn(name="STR_YM", referencedColumnName="STR_YM", insertable = false, updatable = false),
//        @JoinColumn(name="CTG_ATR", referencedColumnName="CTG_ATR", insertable = false, updatable = false)
//    })
//	public QstmtStmtLayoutCtg layoutCategory;
//	
//	@OneToMany(cascade=CascadeType.ALL, mappedBy="layoutLine")
//	public List<QstmtStmtLayoutDetail> layoutDetails;
}
