package nts.uk.file.at.app.schedule.export;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ItemRowDto {
    
    private String employeeID;
    
    private int itemOrder;
    
    private String value;
    
    private String color;
    
    private String backgroundColor;
}
