import { Injectable } from '@angular/core';
import { ElectionResultResponse } from '../dtos/ElectionResultResponse';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { ElectionService } from '../services/election/election.service';
import { Observable } from 'rxjs/Observable';
import { ElectionRequest } from '../dtos/ElectionRequest';

@Injectable()
export class ElectionResultsResolver implements Resolve<any> {

  constructor(private adminService: ElectionService) { }

  resolve(route: ActivatedRouteSnapshot): Observable<any> {
    let electionRequest: ElectionRequest = {id: +route.paramMap.get('id')}
    return this.adminService.getElectionResults(electionRequest);
  }
}
