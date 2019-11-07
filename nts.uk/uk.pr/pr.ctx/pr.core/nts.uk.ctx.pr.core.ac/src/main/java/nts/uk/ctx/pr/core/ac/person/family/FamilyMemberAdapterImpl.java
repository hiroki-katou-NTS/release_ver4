package nts.uk.ctx.pr.core.ac.person.family;

import nts.uk.ctx.bs.person.pub.family.FamilyExport623;
import nts.uk.ctx.bs.person.pub.family.FamilyPub;
import nts.uk.ctx.pr.core.dom.adapter.person.family.FamilyMemberAdapter;
import nts.uk.ctx.pr.core.dom.adapter.person.family.FamilyMemberInfoEx;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class FamilyMemberAdapterImpl implements FamilyMemberAdapter {
    @Inject
    private FamilyPub familyPub;
    @Override
    public List<FamilyMemberInfoEx> getRomajiOfFamilySpouseByPid(String pid) {
        return familyPub.getRomajiOfFamilySpouseByPid(pid)
                .stream()
                .map(e -> {
                    return new FamilyMemberInfoEx(
                          e.getFamilyId(),
                          e.getBirthday(),
                          e.getRomajiName(),
                          e.getRomajiNameKana()
                    );
                }).collect(Collectors.toList());
    }
}
