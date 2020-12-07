import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AuthenticatetokenComponent } from './authenticatetoken.component';
import { NgxSpinnerModule } from 'ngx-spinner';
import { FormsModule } from "@angular/forms";
import { ParticlesModule } from 'angular-particle';
import { Router } from '@angular/router';
import { ModalService } from '../../../services/modal/modal.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgbModalStack } from '@ng-bootstrap/ng-bootstrap/modal/modal-stack';
import { DataService } from '../../../services/data.service';
import { AuthService } from '../../../services/authenticate/auth.service';
import { HttpClientModule } from '@angular/common/http';

describe('AuthenticatetokenComponent', () => {
  let component: AuthenticatetokenComponent;
  let fixture: ComponentFixture<AuthenticatetokenComponent>;

  class RouterStub {
    navigateByUrl(url: string) {
      return url;
    }
  }

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [NgxSpinnerModule,
        FormsModule,
        ParticlesModule,
        HttpClientModule
      ],
      declarations: [ AuthenticatetokenComponent ],
      providers: [
        {provide: Router, useClass: RouterStub},
        ModalService,
        NgbModal,
        NgbModalStack,
        DataService,
        AuthService
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AuthenticatetokenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
