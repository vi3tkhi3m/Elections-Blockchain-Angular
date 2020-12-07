import { Component, OnInit, NgModule, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';

// Interfaces
import { AuthResponse } from '../../../dtos/auth-response';
import { VoteRequest } from '../../../dtos/vote-request';
import { Election } from '../../../domains/votepage/Election';
import { Token } from '../../../domains/authenticate/token';

// Services
import { VotingService } from '../../../services/vote/voting.service';
import { ModalService } from '../../../services/modal/modal.service';
import { AuthService } from '../../../services/authenticate/auth.service';
import { DataService } from '../../../services/data.service';

// Enums
import { ModalType } from '../../../enums/modaltype.enum';

@Component({
  selector: './votepage.component',
  templateUrl: './votepage.component.html',
  styleUrls: ['./votepage.component.css']
})


export class VotepageComponent implements OnDestroy {

  private subData: any;
  private subPostSelection: any;

  election: Election;
  token: Token;

  isCollapsed: Array<boolean> = [];

  selectedEntry: { [key: string]: any } = {
    value: null
  };

  constructor(private modalService:ModalService,private votingService:VotingService, private router: Router, private dataService: DataService, private spinner:NgxSpinnerService) {
    this.subData = dataService.electionData$.subscribe(
      (data: AuthResponse) => {
        this.election = data.election;
        this.token = data.token;
      }
    )

    this.initCollapsedArray();

    modalService.userPressedConfirm.subscribe((newBool: boolean) => { 
      modalService.closeModal();
      this.postSelectionChoice();
    });
  }

  initCollapsedArray() {
    for(let i = 0; i < this.election.parties.length; i++) {
      this.isCollapsed[i] = true;
    }
  }

  ngOnDestroy() {
    this.subData.unsubscribe();
    this.subPostSelection.unsubscribe();
  }

  onSelectionChange(candidate) {
    this.selectedEntry = Object.assign({}, this.selectedEntry, candidate);
  }

  returnButton(): void {
    this.router.navigateByUrl('/home');
  }

  private showWarning() {
    let name: string= this.selectedEntry.firstName + " " + this.selectedEntry.lastName;
    this.modalService.openModal(ModalType.WARNING,"Weet u zeker dat u op " + name + " wilt stemmen?");
  }

  private postSelectionChoice() {
    this.spinner.show();
    let voteRequest: VoteRequest = {id: this.selectedEntry.id, token: this.token.token}
    this.subPostSelection = this.votingService.postSelectionChoice(voteRequest).subscribe((response) => {
      this.modalService.openModal(ModalType.SUCCES,"Uw stem is succesvol geplaatst!");
      this.spinner.hide();
      this.router.navigateByUrl('/home');
    }, (error) => {
      this.modalService.openModal(ModalType.ALERT, "De servers zijn momenteel buiten gebruik. Probeer het later nog eens.")
      this.spinner.hide();
    });
  }

}
