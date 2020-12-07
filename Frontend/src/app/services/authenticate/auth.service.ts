import { Injectable} from '@angular/core';
import { Router } from '@angular/router';
import { DataService } from '../data.service';
import { Observable } from 'rxjs/Observable';
import { HttpClient} from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Token } from '../../domains/authenticate/token';
import { Subject } from 'rxjs/Subject';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

@Injectable()
export class AuthService {

  constructor(public httpClient: HttpClient) { 
  }

  postToken(token: Token) : Observable<any> {
    return this.httpClient.post<Token>(environment.appUrl + '/authentication', token)
  }


}
