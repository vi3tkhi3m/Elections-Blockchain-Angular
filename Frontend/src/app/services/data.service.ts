import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { AuthResponse } from '../dtos/auth-response';

@Injectable()
export class DataService {

  private electionData = new BehaviorSubject<any>(null);
  public electionData$ = this.electionData.asObservable();

  public notifyVotePageWithElection(authResponse: AuthResponse) {
    this.electionData.next(authResponse)
  }

  constructor() { 
   
  }

}
