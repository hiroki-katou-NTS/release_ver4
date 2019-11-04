package nts.uk.ctx.pr.core.dom.adapter.person;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FullNameSetExport {
    /** 氏名 - FullName */
    private String fullName;

    /** 氏名カナ - FullNameKana */
    private String fullNameKana;
}
