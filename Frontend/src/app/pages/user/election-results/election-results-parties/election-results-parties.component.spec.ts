import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ElectionResultsPartiesComponent } from './election-results-parties.component';

describe('ElectionResultsPartiesComponent', () => {
  let component: ElectionResultsPartiesComponent;
  let fixture: ComponentFixture<ElectionResultsPartiesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ElectionResultsPartiesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ElectionResultsPartiesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
