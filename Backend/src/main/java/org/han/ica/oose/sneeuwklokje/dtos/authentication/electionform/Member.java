package org.han.ica.oose.sneeuwklokje.dtos.authentication.electionform;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Member {
    private int id;
    private int position;
    private String lastName;
    private String initials;
    private String firstName;
    private String gender;
    private String location;
}
