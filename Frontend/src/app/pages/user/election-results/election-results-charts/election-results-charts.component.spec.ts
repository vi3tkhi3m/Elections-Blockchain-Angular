import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ElectionResultsChartsComponent } from './election-results-charts.component';
import { ChartsModule } from 'ng2-charts';
import { ActivatedRoute } from '@angular/router';
import { ElectionResultsResolver } from '../../../../resolvers/election-results-resolver.service';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import {EmptyObservable} from 'rxjs/observable/EmptyObservable';
import { RouterTestingModule } from '@angular/router/testing';
import { ElectionResultResponse } from '../../../../dtos/ElectionResultResponse';
import { Party } from '../../../../domains/election-result/Party';

describe('ElectionResultsChartsComponent', () => {
  let component: ElectionResultsChartsComponent;
  let fixture: ComponentFixture<ElectionResultsChartsComponent>;
  let electionResultResponse: ElectionResultResponse;

let mockSomeService = {
  getData: () => {}
}
const mockResultResolver = {
  initElectionResultsData: () => new EmptyObservable()
};

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        ChartsModule,
        RouterTestingModule
      ],
      declarations: [ ElectionResultsChartsComponent ],
      providers: [
        { provide: ElectionResultsResolver, useValue: mockResultResolver },
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    electionResultResponse = new ElectionResultResponse();
    electionResultResponse.parties = new Array();
    electionResultResponse.parties.push(({id: 0, name: 'test', voteCount: 0}));
    fixture = TestBed.createComponent(ElectionResultsChartsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  Object.defineProperty(component.route, 'data', {
    writable: true,
     value: Observable.of({
      succes: electionResultResponse
     })
  });

  it('should assign org when route is resolved', async(() => {
    let electionResultResponse: ElectionResultResponse = new ElectionResultResponse();
    component.route.data = Observable.of(electionResultResponse)
  
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      expect(component.electionResultResponse).toEqual(electionResultResponse)
    })
  }))
});
