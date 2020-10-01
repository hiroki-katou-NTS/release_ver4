package nts.uk.cnv.dom.service;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.uk.cnv.dom.databasetype.DatabaseType;
import nts.uk.cnv.dom.tabledesign.TableDesign;

@Stateless
public class ExportDdlService {

	public String exportDdl(Require require, String tablename, String type) {
		Optional<TableDesign> td = require.find(tablename);
		if(!td.isPresent()) {
			throw new BusinessException(new RawErrorMessage("定義が見つかりません：" + tablename));
		}

		if("uk".equals(type))
			return td.get().createDdl();

		DatabaseType dbtype = DatabaseType.valueOf(type);
		return td.get().createDdl(dbtype.spec());
	}

	public interface Require {
		Optional<TableDesign> find(String tablename);

	}
}
