import { Injectable } from '@angular/core';
import { ElectionListResponse } from '../dtos/ElectionListResponse';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { ElectionService } from '../services/election/election.service';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class RunningElectionResolver implements Resolve<ElectionListResponse> {

  constructor(private electionService: ElectionService) { }

  resolve(): Observable<ElectionListResponse> {
    return this.electionService.getRunningElections();
  }

}
