import { Injectable } from '@angular/core';
import { Resolve } from '@angular/router';
import { ElectionResultResponse } from '../dtos/ElectionResultResponse';
import { ElectionService } from '../services/election/election.service';
import { Observable } from 'rxjs/Observable';
import { ElectionListResponse } from '../dtos/ElectionListResponse';

@Injectable()
export class ClosedElectionResolverService implements Resolve<ElectionListResponse> {

  constructor(private electionService: ElectionService) { }

  resolve(): Observable<ElectionListResponse> {
    return this.electionService.getClosedElections();
  }

}