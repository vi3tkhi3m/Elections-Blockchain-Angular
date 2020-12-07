import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ElectionListComponent } from './election-list.component';
import { FormsModule } from "@angular/forms";
import { CountdownTimerModule } from 'ngx-countdown-timer';
import { ModalService } from '../../../services/modal/modal.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgbModalStack } from '@ng-bootstrap/ng-bootstrap/modal/modal-stack';
import { ActivatedRoute, Router } from '@angular/router';
import { ElectionService } from '../../../services/election/election.service';
import { HttpClientModule } from '@angular/common/http';
import { NgxSpinnerService } from 'ngx-spinner';

describe('ElectionListComponent', () => {
  let component: ElectionListComponent;
  let fixture: ComponentFixture<ElectionListComponent>;

  const fakeActivatedRoute = {
    snapshot: { data: {} }
}

class RouterStub {
  navigateByUrl(url: string) {
    return url;
  }
}

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        FormsModule,
        CountdownTimerModule.forRoot(),
        HttpClientModule
      ],
      declarations: [ ElectionListComponent ],
      providers: [
        ModalService, NgbModal, 
        NgbModalStack, 
        { provide: ActivatedRoute, useValue: fakeActivatedRoute }, 
        ElectionService,
        NgxSpinnerService,
        {provide: Router, useClass: RouterStub},

      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ElectionListComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
