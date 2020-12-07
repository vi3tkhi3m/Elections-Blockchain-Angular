package org.han.ica.oose.sneeuwklokje.dtos.admin.newelection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewElectionRequest {
    private String name;
    private String startDate;
    private String endDate;
    private int[] partyIds;
}
