package nts.uk.ctx.at.function.app.command.processexecution.createlogfileexecution;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.function.dom.processexecution.ProcessExecution;
import nts.uk.ctx.at.function.dom.processexecution.createlogfileexecution.CalTimeRangeDateTimeToString;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.EndStatus;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLogManage;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionLogManageRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionRepository;

/**
 * 実行状態ログファイル作成処理
 * 
 * @author tutk
 *
 */
@Stateless
public class CreateLogFileExecution {

	@Inject
	private ProcessExecutionLogManageRepository processExeLogManageRepo;

	@Inject
	private ProcessExecutionRepository processExecutionRepo;
	

	public void createLogFile(String companyId, String execItemCd) {
		// ドメインモデル「更新処理自動実行管理」取得
		Optional<ProcessExecutionLogManage> optProcessExeLogManage = processExeLogManageRepo
				.getLogByCIdAndExecCd(companyId, execItemCd);	
		if (!optProcessExeLogManage.isPresent()) {
			return;
		}
		// ドメインモデル「更新処理自動実行」取得
		Optional<ProcessExecution> optProcessExecution = processExecutionRepo
				.getProcessExecutionByCidAndExecCd(companyId, execItemCd);
		String execItemName = "";
		if (optProcessExecution.isPresent()) {
			execItemName = optProcessExecution.get().getExecItemName().v();
		}
		String errorSystem = optProcessExeLogManage.get().getErrorSystem() == null ? "正常"
				: (optProcessExeLogManage.get().getErrorSystem() ? "異常" : "正常");
		String errorBusiness = optProcessExeLogManage.get().getErrorBusiness() == null ? "正常"
				: (optProcessExeLogManage.get().getErrorBusiness() ? "異常" : "正常");

		String timeRun = CalTimeRangeDateTimeToString.calTimeExec(optProcessExeLogManage.get().getLastExecDateTime(),
				optProcessExeLogManage.get().getLastEndExecDateTime());

		String lastEndExecDateTime = "";
		if(optProcessExeLogManage.get().getLastEndExecDateTime() != null) {
			lastEndExecDateTime = optProcessExeLogManage.get().getLastEndExecDateTime().toString();
		}
				
		String content = companyId + "," + execItemCd + "," + execItemName + "," + errorSystem + "," + errorBusiness
				+ "," + optProcessExeLogManage.get().getLastExecDateTime().toString() + ","
				+ lastEndExecDateTime + "," + timeRun;

		createFile(content,companyId,execItemCd);

	}

	public void createFile(String content,String companyId, String execItemCd) {
		Path myPath = Paths.get(System.getProperty("jboss.server.log.dir"), "/", "KBT002");

		if (!Files.exists(myPath)) {
			try {
				Files.createDirectories(myPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try (Writer writer = new BufferedWriter(new FileWriter(myPath.toAbsolutePath() + "\\KBT002_実行状態_"+companyId+"_"+execItemCd+".log", false))) {
			writer.write("会社ID,コード,名称,システムエラー区分,業務エラー区分,開始日時,終了日時,処理時間"+ "\n");
			writer.write(content+ "\n");
//			writer.append("会社ID,コード,名称,システムエラー区分,業務エラー区分,開始日時,終了日時,処理時間"+ "\n");
//			writer.append(content+ "\n");

		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
	}

	
}
