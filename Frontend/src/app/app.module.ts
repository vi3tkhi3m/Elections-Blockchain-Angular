import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ParticlesModule } from 'angular-particle';
import { HttpClientModule } from '@angular/common/http';
import { ChartsModule } from 'ng2-charts';
import { TooltipModule } from 'ngx-bootstrap/tooltip';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { Ng2TableModule } from 'ng2-table/ng2-table';
import { NgTableComponent, NgTableFilteringDirective, NgTablePagingDirective, NgTableSortingDirective } from 'ng2-table/ng2-table';
import { FormsModule } from "@angular/forms";
import { AppRoutingModule } from './app-routing.module';
import { NgxSpinnerModule } from 'ngx-spinner';
import { CountdownTimerModule } from 'ngx-countdown-timer';
import { PaginationModule, PaginationConfig } from 'ng2-bootstrap';

import 'hammerjs';

import { AlertModalComponent } from './modals/alert-modal/alert-modal.component';
import { AppComponent } from './app.component';
import { AuthenticatetokenComponent } from './pages/user/authenticatetoken/authenticatetoken.component';
import { ElectionCreateComponent } from './pages/admin/election-create/election-create.component';
import { ElectionListComponent } from './pages/admin/election-list/election-list.component';
import { ElectionResultsComponent } from './pages/user/election-results/election-results-home/election-results.component';
import { ElectionResultsChartsComponent } from './pages/user/election-results/election-results-charts/election-results-charts.component';
import { ElectionResultsPartiesComponent } from './pages/user/election-results/election-results-parties/election-results-parties.component';
import { ElectionResultsMembersComponent } from './pages/user/election-results/election-results-members/election-results-members.component';
import { ElectionsListComponent } from './pages/user/elections-list/elections-list.component';
import { SuccesModalComponent } from './modals/succes-modal/succes-modal.component';
import { VotepageComponent } from './pages/user/votepage/votepage.component';
import { WarningModalComponent } from './modals/warning-modal/warning-modal.component';

import { AuthService } from './services/authenticate/auth.service';
import { DataService } from './services/data.service';
import { ElectionService } from './services/election/election.service';
import { ModalService } from './services/modal/modal.service';
import { VotingService } from './services/vote/voting.service';
import { AdminElectionPublishResolver } from './resolvers/admin-election-publish-resolver.service';
import { NavbarComponent } from './navbar/navbar/navbar.component';
import { RouterModule } from '@angular/router';


@NgModule({
  declarations: [
    AppComponent,
    VotepageComponent,
    AuthenticatetokenComponent,
    ElectionsListComponent,
    ElectionResultsComponent,
    ElectionResultsChartsComponent,
    ElectionResultsPartiesComponent,
    ElectionResultsMembersComponent,
    AlertModalComponent,
    SuccesModalComponent,
    WarningModalComponent,
    ElectionListComponent,
    ElectionCreateComponent,
    NavbarComponent
  ],
  imports: [
    BrowserModule, 
    NgbModule.forRoot(), 
    ParticlesModule, 
    HttpClientModule, 
    ChartsModule, 
    TabsModule.forRoot(), 
    Ng2TableModule, 
    FormsModule, 
    TooltipModule.forRoot(),
    AppRoutingModule,
    NgxSpinnerModule,
    CountdownTimerModule.forRoot(),
    PaginationModule,
    RouterModule
  ],
  providers: [DataService, AuthService, VotingService, ModalService, ElectionService, AdminElectionPublishResolver, PaginationConfig],
  bootstrap: [AppComponent],
  entryComponents: [AlertModalComponent, SuccesModalComponent, WarningModalComponent]
})
export class AppModule { }