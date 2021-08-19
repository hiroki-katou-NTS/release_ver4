package nts.uk.ctx.at.function.dom.adapter.employment;

import java.util.List;

/**
 * 社員所属チーム情報Adapter
 *
 * @author rafiqul.islam
 */
public interface EmpAffTeamInfoAdapter {
    /**
     * @param List<String> listEmpId List<社員ID>
     * @return List<社員所属チーム情報Imported>
     */
    List<EmpTeamInfoImport> get(List<String> listEmpId);
}
