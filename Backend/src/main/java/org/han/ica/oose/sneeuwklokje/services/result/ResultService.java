package org.han.ica.oose.sneeuwklokje.services.result;

import org.han.ica.oose.sneeuwklokje.dtos.admin.currentrunningelection.ElectionListResponse;
import org.han.ica.oose.sneeuwklokje.dtos.result.ElectionResultResponse;

public interface ResultService {

    ElectionListResponse closedElectionResponse();

    ElectionResultResponse resultElectionResponse(int electionId);

    boolean isElectionClosed(int electionId);
}
