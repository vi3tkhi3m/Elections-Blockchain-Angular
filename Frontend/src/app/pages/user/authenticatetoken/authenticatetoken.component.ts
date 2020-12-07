import { Component, OnInit, ElementRef, OnDestroy } from '@angular/core';
import { DataService } from '../../../services/data.service';
import { ParticlesModule } from 'angular-particle';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { Router } from '@angular/router';

// Services
import { AuthService } from '../../../services/authenticate/auth.service';
import { ModalService } from '../../../services/modal/modal.service';

// Interfaces
import { AuthResponse } from '../../../dtos/auth-response';
import { Token } from '../../../domains/authenticate/token';

// Enums
import { ModalType } from '../../../enums/modaltype.enum';
import { BackgroundColors } from '../../../enums/background-colors.enum';

@Component({
  templateUrl: './authenticatetoken.component.html',
  styleUrls: ['./authenticatetoken.component.css']
})
export class AuthenticatetokenComponent implements OnInit, OnDestroy {
  private subPostToken: any;

  tokenField: string = '';

  // Particles Variables
  myStyle: object = {};
  myParams: object = {};
  width: number = 100;
  height: number = 100;

  constructor(private _router: Router, private modalService: ModalService, private _dataservice: DataService, private authService: AuthService, private spinner: NgxSpinnerService, public elementRef: ElementRef) { }

  onNameKeyUp(event: any) {
    this.tokenField = event.target.value;
  }

  ngAfterViewInit() {
    this.setBodyBackgroundColor(BackgroundColors.BLACK);
  }

  ngOnInit() {
    this.initParticlesSettings();
  }

  ngOnDestroy() {
    this.subPostToken.unsubscribe();
  }

  private initParticlesSettings() {
    this.myStyle = {
      'position': 'fixed',
      'width': '100%',
      'height': '100%',
      'z-index': -1,
      'top': 0,
      'left': 0,
      'right': 0,
      'bottom': 0,
    };

    this.myParams = {
      particles: {
        number: {
          value: 80,
        },
        color: {
          value: '#ffffff'
        },
        shape: {
          type: 'circle',
        },
      }
    };
  }

  private postToken() {
    this.spinner.show();
    let token: Token = { token: this.tokenField }
    this.subPostToken = this.authService.postToken(token).subscribe(response => {
      this.doAfterPostTokenResponses(response, token);
    }, (error) => {
      this.showErrorModal(error)
    });
  }

  private doAfterPostTokenResponses(response, token: Token) {
    this.setBodyBackgroundColor(BackgroundColors.WHITE);
    let authResponse: AuthResponse = { election: response, token: token }
    this._dataservice.notifyVotePageWithElection(authResponse);
    this.spinner.hide();
    this._router.navigateByUrl('/voting-page');
  }

  private showErrorModal(error) {
    if (error.status == 403) {
      this.modalService.openModal(ModalType.ALERT, "De ingevoerde token is niet geldig! Voer aub een geldige token in.")
      this.spinner.hide();
    } else if (error.status == 500) {
      this.modalService.openModal(ModalType.ALERT, "Er kan niet gecommuniceerd worden met het smart contract. Proheer het later nog eens.")
      this.spinner.hide();
    } else {
      this.modalService.openModal(ModalType.ALERT, "De servers zijn momenteel buiten gebruik. Probeer het later nog eens.")
      this.spinner.hide();
    }
  }

  private setBodyBackgroundColor(color: BackgroundColors) {
    this.elementRef.nativeElement.ownerDocument.body.style.backgroundColor = color;
  }

}