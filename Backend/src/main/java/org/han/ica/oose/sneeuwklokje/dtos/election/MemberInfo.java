package org.han.ica.oose.sneeuwklokje.dtos.election;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MemberInfo {
    private int position;
    private String lastName;
    private String initials;
    private String firstName;
    private String gender;
    private String location;
    private int party;

    public MemberInfo(){
        // Used in AdminDao
    }
}
