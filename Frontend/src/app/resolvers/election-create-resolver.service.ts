import { Injectable } from '@angular/core';
import { PartyRequest } from '../dtos/party-request';
import { Resolve } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { ElectionService } from '../services/election/election.service';

@Injectable()
export class ElectionCreateResolver implements Resolve<PartyRequest> {

  constructor(private electionService: ElectionService) { }

  resolve(): Observable<PartyRequest> {
    return this.electionService.getPartiesToCreateElection();
  }
  
}
