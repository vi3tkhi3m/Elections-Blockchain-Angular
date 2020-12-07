package org.han.ica.oose.sneeuwklokje.dtos.election;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PartyInfo {
    private String name;
    private int databaseId;
    private int smartContractId;
}
