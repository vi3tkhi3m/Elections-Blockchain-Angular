import { Injectable } from '@angular/core';
import { HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { environment } from '../../../environments/environment';
import { ElectionListResponse } from '../../dtos/ElectionListResponse';
import { ElectionRequest } from '../../dtos/ElectionRequest';
import { ElectionResultResponse } from '../../dtos/ElectionResultResponse';
import { PartyRequest } from '../../dtos/party-request';
import { ElectionCreateResponse } from '../../dtos/ElectionCreateResponse';
import { ElectionPublishRequest } from '../../dtos/election-publish-request';

@Injectable()
export class ElectionService {

  constructor(public httpClient: HttpClient) { }

  getRunningElections() : Observable<ElectionListResponse> {
    return this.httpClient.get<ElectionListResponse>(environment.appUrl + '/admin/elections/open/');
  }

  closeElection(request: ElectionRequest) : Observable<any> {
    return this.httpClient.post<any>(environment.appUrl + '/admin/elections/close/' + request.id, request);
  }

  getClosedElections() : Observable<ElectionListResponse> {
    return this.httpClient.get<ElectionListResponse>(environment.appUrl + '/elections/result/');
  }

  getElectionResults(request: ElectionRequest) : Observable<ElectionResultResponse> {
    return this.httpClient.post<ElectionResultResponse>(environment.appUrl + '/elections/result/' + request.id, request);
  }

  getPartiesToCreateElection(): Observable<PartyRequest> {
    return this.httpClient.get<PartyRequest>(environment.appUrl + '/admin/parties/');
  }
  
  createElection(request: ElectionCreateResponse) : Observable<any> {
    return this.httpClient.post<ElectionCreateResponse>(environment.appUrl + '/admin/election/' , request);
  }

  getPreRunningElections()  : Observable<ElectionListResponse> {
    return this.httpClient.get<ElectionListResponse>(environment.appUrl + '/admin/election/planned/');
  }

  publishElection(request: ElectionPublishRequest) {
    return this.httpClient.post(environment.appUrl + '/admin/election/publish/' + request.id, request);
  }

}
