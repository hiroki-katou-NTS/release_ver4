package nts.uk.ctx.sys.assist.infra.repository.deletedata;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.enterprise.inject.New;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Strings;

import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.security.crypt.commonkey.CommonKeyCrypt;
import nts.uk.ctx.sys.assist.dom.deletedata.ManualSetDeletion;
import nts.uk.ctx.sys.assist.dom.deletedata.PasswordCompressFileEncrypt;
import nts.uk.ctx.sys.assist.dom.deletedata.ResultDeletion;
import nts.uk.ctx.sys.assist.dom.deletedata.ResultDeletionRepository;
import nts.uk.ctx.sys.assist.infra.entity.deletedata.SspdtResultDeletion;
import nts.uk.ctx.sys.assist.infra.entity.deletedata.SspdtResultDeletionPK;
import nts.uk.ctx.sys.assist.infra.entity.storage.SspmtResultOfSaving;

@Stateless
public class JpaResultDeletionRepository extends JpaRepository implements ResultDeletionRepository {

	private static final String SELECT_ALL_QUERY_STRING = "SELECT f FROM SspdtResultDeletion f";
	private static final String SELECT_BY_KEY_STRING = SELECT_ALL_QUERY_STRING
			+ " WHERE  f.sspdtResultDeletionPK.delId = :delId ";

	@Override
	public List<ResultDeletion> getAllResultDeletion() {
		return this.queryProxy().query(SELECT_ALL_QUERY_STRING, SspdtResultDeletion.class)
				.getList(item -> item.toDomain());
	}

	@Override
	public Optional<ResultDeletion> getResultDeletionById(String delId) {
		return this.queryProxy().query(SELECT_BY_KEY_STRING, SspdtResultDeletion.class)
				.setParameter("delId", delId).getSingle(c -> c.toDomain());
	}

	@Override
	public void add(ResultDeletion data) {
		this.commandProxy().insert(SspdtResultDeletion.toEntity(data));
	}
	
	@Override
	public void update(ResultDeletion data) {
		 this.commandProxy().update(SspdtResultDeletion.toEntity(data));
	}

	@Override
	public void update(ResultDeletion resultDel, ManualSetDeletion manualSetDel) {
		SspdtResultDeletionPK key  = new SspdtResultDeletionPK(resultDel.getDelId());
		Optional<SspdtResultDeletion> resultOfDeleteOpt = this.queryProxy().find(key, SspdtResultDeletion.class);
		resultOfDeleteOpt.ifPresent(data -> {
			data.status = resultDel.getStatus().value;
			data.endDateTimeDel = resultDel.getEndDateTimeDel();
			data.fileSize = resultDel.getFileSize();
			data.fileId = resultDel.getFileId();
			data.isDeletedFilesFlg = resultDel.isDeletedFilesFlg() == true ? 1: 0;
			data.numberEmployees = resultDel.getNumberEmployees();
			
			// redmine #108204
			String password = "";
			if (manualSetDel.isExistCompressPassFlg() && !Strings.isNullOrEmpty(resultDel.getFileId())) {
				String passwordOpt = manualSetDel.getPasswordCompressFileEncrypt().map(i -> i.v()).orElse("");
				if (StringUtils.isNotEmpty(passwordOpt)) {
					password = CommonKeyCrypt.encrypt(passwordOpt);
				} 
			} 
			
			if(!Strings.isNullOrEmpty(resultDel.getFileId())){
				data.fileName  = resultDel.getFileName().toString();
			}else{
				data.fileName = "";
			}
			
			data.passwordCompressFileEncrypt = password;
			this.commandProxy().update(data);
		});
	}
}
