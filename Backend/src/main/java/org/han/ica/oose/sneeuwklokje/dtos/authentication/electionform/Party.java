package org.han.ica.oose.sneeuwklokje.dtos.authentication.electionform;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Party {
    private int id;
    private String name;
    private List<Member> members = new ArrayList<>();
    private String startDate;
    private String endDate;

    /**
     * Adds m to members
     * @param m A members with an id, position, lastName, initials, firstname, gender and location
     */
    public void addMember(Member m){
        members.add(m);
    }
}
