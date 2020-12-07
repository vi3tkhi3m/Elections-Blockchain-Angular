package org.han.ica.oose.sneeuwklokje.dtos.admin.currentrunningelection;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Election {
    private int electionID;
    private String electionName;
    private String startDate;
    private String endDate;
}
