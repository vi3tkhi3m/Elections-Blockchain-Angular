import { Component, OnInit, OnDestroy } from '@angular/core';
import { ElectionService } from '../../../services/election/election.service';
import { Election } from '../../../domains/election-list/Election';
import { ElectionListResponse } from '../../../dtos/ElectionListResponse';
import { NgxSpinnerService } from 'ngx-spinner';
import { ActivatedRoute, Router } from '@angular/router';

import { ElectionRequest } from '../../../dtos/ElectionRequest';
import { ModalService } from '../../../services/modal/modal.service';
import { ModalType } from '../../../enums/modaltype.enum';
import { ElectionPublishRequest } from '../../../dtos/election-publish-request';

@Component({
  selector: 'app-election-list',
  templateUrl: './election-list.component.html',
  styleUrls: ['./election-list.component.css']
})
export class ElectionListComponent implements OnInit, OnDestroy {

  subModal: any;
  subData: any;

  elections: Election[] = [];

  electionIndex: number;
  electionID: number;
  routeName: string;

  loadingText: string;

  constructor(private modalService:ModalService,private route: ActivatedRoute, private electionService: ElectionService, private spinner:NgxSpinnerService, private router: Router) { 
    this.subModal = modalService.userPressedConfirm.subscribe((newBool: boolean) => { 
      modalService.closeModal();
      if(this.routeName === 'election-publish'){
        this.publishElection(this.electionID, this.electionIndex);
      }else if (this.routeName === 'election-list'){
        this.closeElection(this.electionID, this.electionIndex);      
      }
    });
  }

  ngOnInit() {
    this.routeName = this.route.snapshot.url[1].path;
    this.initRuningElections(); 
  }

  ngOnDestroy() {
    this.subData.unsubscribe();
    this.subModal.unsubscribe();
  }

  initRuningElections() {
    this.subData = this.route.data.subscribe(({ data }) => {
      let electionListResponse: ElectionListResponse = data;
      for(let el of electionListResponse.elections) {
        this.elections.push(el);
      }
    });  
  }

  showWarning(id: number, index:number, name: string) {
    this.electionID = id;
    this.electionIndex = index;
    if(this.routeName === 'election-publish'){
      this.modalService.openModal(ModalType.WARNING,"Weet u zeker dat u " + name + " wilt publiceren?");
    }else if(this.routeName === 'election-list'){
      this.modalService.openModal(ModalType.WARNING,"Weet u zeker dat u " + name + " wilt verwijderen?");
    }
  }

  closeElection(id: number, index:number) {
    this.loadingText = "Uw verzoek om deze verkiezing te sluiten wordt verwerkt! Een ogenblik geduld a.u.b.";
    this.spinner.show();
    let closeElectionRequest: ElectionRequest = {id: id};
   
    this.electionService.closeElection(closeElectionRequest).subscribe((Response) => {
      this.elections.splice(index, 1);
      this.modalService.openModal(ModalType.SUCCES,"Verkiezing is succesvol gesloten")
      this.spinner.hide();
    }, (error) => {
      this.modalService.openModal(ModalType.ALERT,"Kan de verkiezing niet sluiten.")
      this.spinner.hide();
    }  
  )
  }

  publishElection(id: number, index:number){
    this.loadingText = "Uw verzoek om deze verkiezing te publiceren wordt verwerkt! Een ogenblik geduld a.u.b.";
    this.spinner.show();
    let PublishElectionRequest: ElectionPublishRequest = {id: id}

    this.electionService.publishElection(PublishElectionRequest).subscribe((Response) => {
      this.elections.splice(index, 1);
      this.modalService.openModal(ModalType.SUCCES, "De verkiezing is gepubliceerd")
      this.spinner.hide();
    }, (error) => {
      this.modalService.openModal(ModalType.ALERT,"Kan de verkiezing niet publiceren.")
      this.spinner.hide();
    })
  }
}
