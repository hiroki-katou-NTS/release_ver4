package nts.uk.screen.at.app.ksm005.find;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class RequestPrams {

    public List<String> listWorkTypeCd;

    public String worktimeCode;

}
