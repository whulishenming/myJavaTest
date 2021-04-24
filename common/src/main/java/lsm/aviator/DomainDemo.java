package lsm.aviator;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lishenming
 * @version 1.0
 * @date 2019/10/10 17:11
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DomainDemo {
    private int i;

    private float f;

    private Date date;

    private List<LocalDate> years;
}
