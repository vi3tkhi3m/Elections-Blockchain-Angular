package org.han.ica.oose.sneeuwklokje.dtos.result;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class ElectionResultResponse {
    @Getter @Setter private String name;

    private List<Party> parties = new ArrayList<>();
    private List<Member> members = new ArrayList<>();

    /**
     * Adds an party to parties
     * @param party A party with an id, name and voteCount
     */
    public void addParty(Party party) {
        parties.add(party);
    }

    /**
     * Adds member to members
     * @param member A member with an id, firstname, initials, lastname, partyId and voteCount
     */
    public void addMember(Member member) {
        members.add(member);
    }
}
