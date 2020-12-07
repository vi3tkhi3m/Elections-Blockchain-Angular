import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WarningModalComponent } from './warning-modal.component';
import {NgbModal, NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import { ModalService } from '../../services/modal/modal.service';
import { NgbModalStack } from '@ng-bootstrap/ng-bootstrap/modal/modal-stack';


describe('WarningModalComponent', () => {
  let component: WarningModalComponent;
  let fixture: ComponentFixture<WarningModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WarningModalComponent ],
      providers: [NgbActiveModal, ModalService, NgbModal, NgbModalStack],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WarningModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
