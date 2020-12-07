import { Component, Input } from '@angular/core';

import {NgbModal, NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-succes-modal',
  templateUrl: './succes-modal.component.html',
  styleUrls: ['./succes-modal.component.css']
})
export class SuccesModalComponent {

  @Input() message;

  constructor(public activeModal: NgbActiveModal) {}

}
