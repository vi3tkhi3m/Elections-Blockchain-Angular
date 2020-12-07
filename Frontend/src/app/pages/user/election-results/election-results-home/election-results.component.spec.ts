import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ElectionResultsComponent } from './election-results.component';
import { NgxSpinnerService } from 'ngx-spinner';
import { ElectionResultsChartsComponent } from '../election-results-charts/election-results-charts.component';
import { ElectionResultsMembersComponent } from '../election-results-members/election-results-members.component';
import { ElectionResultsPartiesComponent } from '../election-results-parties/election-results-parties.component';



describe('ElectionResultsComponent', () => {
  let component: ElectionResultsComponent;
  let fixture: ComponentFixture<ElectionResultsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [],
      declarations: [ 
        ElectionResultsComponent,
        ElectionResultsChartsComponent,
        ElectionResultsMembersComponent,
        ElectionResultsPartiesComponent
       ],
      providers: [
        NgxSpinnerService
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ElectionResultsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
