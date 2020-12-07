import { Component, OnInit, OnDestroy } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { Party } from '../../../domains/election-create/Party';
import { Router, ActivatedRoute } from '@angular/router';
import { ModalService } from '../../../services/modal/modal.service';
import { ElectionService } from '../../../services/election/election.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { PartyRequest } from '../../../dtos/party-request';
import { ModalType } from '../../../enums/modaltype.enum';
import { ElectionCreateResponse } from '../../../dtos/ElectionCreateResponse';

@Component({
  selector: 'app-election-create',
  templateUrl: './election-create.component.html',
  styleUrls: ['./election-create.component.css']
})
export class ElectionCreateComponent implements OnInit, OnDestroy {

  subData: any;
  subModal: any;
  subCreateElection: any;

  startTime = { hour: 13, minute: 30 };
  endTime = { hour: 13, minute: 30 };

  electionName: string = "";
  electionEndDate: string = "";
  electionStartDate: string = "";
  electedPartiesId: number[] = [];

  partyOptions: any[] = [];

  constructor(private modalService: ModalService, private route: ActivatedRoute, private electionService: ElectionService, private spinner: NgxSpinnerService, private router: Router) {
    this.subModal = modalService.userPressedConfirm.subscribe((newBool: boolean) => {
      modalService.closeModal();
      this.createElection(this.electionName, this.electionStartDate, this.electionEndDate, this.electedPartiesId)
    });
  }

  ngOnInit() {
    this.initParties();
  }

  ngOnDestroy() {
    this.subData.unsubscribe();
    this.subModal.unsubscribe();
    this.subCreateElection.unsubscribe();
  }

  initParties() {
    this.subData = this.route.data.subscribe(({ data }) => {
      let toBeElectedParties: PartyRequest = data;
      for (let party of toBeElectedParties.parties) {
        this.partyOptions.push({ name: party.name, id: party.id, checked: false });
      }
    });
  }

  showWarning(formData) {
    if (formData) {
      if (formData.verkiezingnaam === undefined) {
        this.modalService.openModal(ModalType.ALERT, "De verkiezings naam is niet ingevuld.");
      } else if (formData.startDate === undefined) {
        this.modalService.openModal(ModalType.ALERT, "De verkiezings startdatum is niet ingevuld.");
      } else if (formData.endDate === undefined) {
        this.modalService.openModal(ModalType.ALERT, "De verkiezings einddatum is niet ingevuld.");
      } else if (this.selectedOptions().length === 0) {
        this.modalService.openModal(ModalType.ALERT, "Er zijn geen partijen gekozen.");
      } else {
        this.electionName = formData.verkiezingnaam;
        this.electionStartDate = formData.startDate.year + '-' + formData.startDate.month + '-' + formData.startDate.day + ' ' + formData.startTime.hour + ':' + formData.startTime.minute;
        this.electionEndDate = formData.endDate.year + '-' + formData.endDate.month + '-' + formData.endDate.day + ' ' + formData.endTime.hour + ':' + formData.endTime.minute;
        this.electedPartiesId = this.selectedOptions();
        this.modalService.openModal(ModalType.WARNING, "Weet u zeker dat u deze verkiezing wilt aanmaken?");
      }
    }
  }

  selectedOptions(): number[] {
    return this.partyOptions
      .filter(opt => opt.checked)
      .map(opt => opt.id)
  }

  createElection(name: string, startdate: string, enddate: string, ids: number[]) {
    let electionInput: ElectionCreateResponse = { name: name, startDate: startdate, endDate: enddate, partyIds: ids }
    this.subCreateElection = this.electionService.createElection(electionInput).subscribe(res => {
      this.modalService.openModal(ModalType.SUCCES, "Verkiezing is succesvol aangemaakt")
      this.router.navigateByUrl('/admin/election-publish');
    }, err => {
      this.modalService.openModal(ModalType.WARNING, "Verkiezing is niet aangemaakt, omdat: " + err)
    })
  }
}
