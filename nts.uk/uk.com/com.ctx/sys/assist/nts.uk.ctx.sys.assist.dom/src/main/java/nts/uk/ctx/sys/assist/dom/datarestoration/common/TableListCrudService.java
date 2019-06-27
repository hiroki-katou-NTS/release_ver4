package nts.uk.ctx.sys.assist.dom.datarestoration.common;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.ctx.sys.assist.dom.tablelist.TableList;
import nts.uk.ctx.sys.assist.dom.tablelist.TableListRepository;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class TableListCrudService {

	@Inject
	private TableListRepository tableListRepository;
	
	public void crudTableList(TableList tableListData) {
		tableListRepository.remove(tableListData);
		tableListRepository.add(tableListData);
	}
}
