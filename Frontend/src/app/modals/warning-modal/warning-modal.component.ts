import { Component, Input, NgModule, Injector } from '@angular/core';

import {NgbModal, NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import { ModalService } from '../../services/modal/modal.service';
import { Warningstatus } from '../../enums/warningstatus.enum';
import { DataService } from '../../services/data.service';

@Component({
  selector: 'app-warning-modal',
  templateUrl: './warning-modal.component.html',
  styleUrls: ['./warning-modal.component.css']
})

export class WarningModalComponent {

  @Input() message;

  modalService:ModalService;
  constructor(public activeModal: NgbActiveModal, private injector: Injector) {
    this.modalService = this.injector.get(ModalService);
  }

  public confirmButton() {
    this.modalService.setUserPressedConfirm(true);
  }

}