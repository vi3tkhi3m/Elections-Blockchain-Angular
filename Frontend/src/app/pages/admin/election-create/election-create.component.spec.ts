import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ElectionCreateComponent } from './election-create.component';

import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from "@angular/forms";
import { CountdownTimerModule } from 'ngx-countdown-timer';

describe('ElectionCreateComponent', () => {
  let component: ElectionCreateComponent;
  let fixture: ComponentFixture<ElectionCreateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        FormsModule,
        NgbModule.forRoot(),
        CountdownTimerModule.forRoot()
      ],
      declarations: [ ElectionCreateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ElectionCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
