package org.han.ica.oose.sneeuwklokje.dtos.admin.currentrunningelection;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ElectionListResponse {
    private List<Election> elections = new ArrayList<>();

    /**
     * Adds an election to the elections
     * @param election
     */
    public void addElection(Election election){
        elections.add(election);
    }
}