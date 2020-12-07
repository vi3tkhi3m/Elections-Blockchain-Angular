import { Injectable} from '@angular/core';
import { Router } from '@angular/router';
import { DataService } from '../data.service';
import { Observable } from 'rxjs/Observable';
import { HttpClient} from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { VoteRequest } from '../../dtos/vote-request';

@Injectable()
export class VotingService {

  constructor(public httpClient: HttpClient) { }

  postSelectionChoice(voteRequest: VoteRequest) : Observable<any> {
    return this.httpClient.post<any>(environment.appUrl + '/vote', voteRequest)
  }

}
