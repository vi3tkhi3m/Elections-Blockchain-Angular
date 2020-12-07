import { Injectable } from '@angular/core';

import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

import { Observable } from 'rxjs/Observable';
import { ElectionListResponse } from '../dtos/ElectionListResponse';
import { ElectionService } from '../services/election/election.service';

@Injectable()
export class AdminElectionPublishResolver implements Resolve<ElectionListResponse> {

  constructor(private adminService: ElectionService) { }

  resolve(): Observable<ElectionListResponse> {
    return this.adminService.getPreRunningElections();
  }

}
