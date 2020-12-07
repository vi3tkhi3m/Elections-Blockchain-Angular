package org.han.ica.oose.sneeuwklokje.dtos.election;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
@AllArgsConstructor
public class ElectionInfo {
    private String name;
    private Calendar startDate;
    private Calendar endDate;

    public ElectionInfo(){
        // Used in AdminDao
    }
}
