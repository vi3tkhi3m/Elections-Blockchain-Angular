import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ElectionResultsMembersComponent } from './election-results-members.component';

describe('ElectionResultsMembersComponent', () => {
  let component: ElectionResultsMembersComponent;
  let fixture: ComponentFixture<ElectionResultsMembersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ElectionResultsMembersComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ElectionResultsMembersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
