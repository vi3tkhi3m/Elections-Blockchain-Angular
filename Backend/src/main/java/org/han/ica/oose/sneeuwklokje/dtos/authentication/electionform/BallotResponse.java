package org.han.ica.oose.sneeuwklokje.dtos.authentication.electionform;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BallotResponse {
    private int id;
    private String name;
    private List<Party> parties;
    private String startDate;
    private String endDate;
}
