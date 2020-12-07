package org.han.ica.oose.sneeuwklokje.dtos.admin.newelection;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PartyListResponse {
    private List<Party> parties = new ArrayList<>();

    /**
     * Adds an party to parties
     * @param party A party with a name and an id
     */
    public void addParty(Party party){
        parties.add(party);
    }
}
