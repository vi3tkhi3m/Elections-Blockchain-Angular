import { Injectable } from '@angular/core';
import { ModalType } from '../../enums/modaltype.enum';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertModalComponent } from '../../modals/alert-modal/alert-modal.component';
import { WarningModalComponent } from '../../modals/warning-modal/warning-modal.component';
import { SuccesModalComponent } from '../../modals/succes-modal/succes-modal.component';
import { Warningstatus } from '../../enums/warningstatus.enum';
import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';

@Injectable()
export class ModalService {

  userPressedConfirm: Observable<boolean>;
  private userPressedConfirmSubject: Subject<boolean>;

  private modalRef : NgbModalRef;

  constructor(private modalService: NgbModal) {
    this.userPressedConfirmSubject = new Subject<boolean>();
        this.userPressedConfirm = this.userPressedConfirmSubject.asObservable();
   }

  public openModal(modal: ModalType, message: string) {
    if(modal == ModalType.ALERT) {
      this.modalRef = this.modalService.open(AlertModalComponent);
    } else if (modal == ModalType.WARNING) {
      this.modalRef = this.modalService.open(WarningModalComponent);
    } else if (modal == ModalType.SUCCES) {
      this.modalRef = this.modalService.open(SuccesModalComponent);
    }
    this.modalRef.componentInstance.message = message;
  }

  public closeModal() {
    this.modalRef.close();
  }

  public setUserPressedConfirm(status: boolean) {
    this.closeModal();
    this.userPressedConfirmSubject.next(status);
  }
}
