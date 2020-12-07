import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VotepageComponent } from './votepage.component';

describe('VotepageComponent', () => {
  let component: VotepageComponent;
  let fixture: ComponentFixture<VotepageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VotepageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VotepageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
