package org.han.ica.oose.sneeuwklokje.dtos.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {
    private int id;
    private String firstname;
    private String initials;
    private String lastname;
    private int partyId;
    private int voteCount;
}
