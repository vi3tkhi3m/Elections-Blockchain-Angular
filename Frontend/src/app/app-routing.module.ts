import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AuthenticatetokenComponent } from './pages/user/authenticatetoken/authenticatetoken.component';
import { VotepageComponent } from './pages/user/votepage/votepage.component';
import { ElectionCreateComponent } from './pages/admin/election-create/election-create.component';
import { ElectionResultsComponent } from './pages/user/election-results/election-results-home/election-results.component';
import { ElectionListComponent } from './pages/admin/election-list/election-list.component';
import { ElectionsListComponent } from './pages/user/elections-list/elections-list.component';

import { RunningElectionResolver } from './resolvers/running-election-resolver.service';
import { ClosedElectionResolverService } from './resolvers/closed-election-resolver.service';
import { ElectionResultsResolver } from './resolvers/election-results-resolver.service';
import { ElectionCreateResolver } from './resolvers/election-create-resolver.service';
import { AdminElectionPublishResolver } from './resolvers/admin-election-publish-resolver.service';

const routes: Routes = [
  {path: 'home', component: AuthenticatetokenComponent},
  {path: 'voting-page', component: VotepageComponent},
  {path: 'elections', component: ElectionsListComponent, resolve: { data: ClosedElectionResolverService }},
  {path: 'election-result/:id', component: ElectionResultsComponent, resolve: { data: ElectionResultsResolver }},
  {path: 'admin/election-list', component: ElectionListComponent, resolve: { data: RunningElectionResolver }},
  {path: 'admin/election-publish', component: ElectionListComponent, resolve: { data: AdminElectionPublishResolver }},
  {path: 'admin/create', component: ElectionCreateComponent, resolve: {data: ElectionCreateResolver}},
  {path: '', redirectTo: 'home', pathMatch: 'full'},
  {path: '**', redirectTo: 'home' , pathMatch: 'full'}
];

@NgModule({
  exports: [ RouterModule ],
  imports: [RouterModule.forRoot(routes)],
  providers: [
    RunningElectionResolver, ClosedElectionResolverService, ElectionResultsResolver, ElectionCreateResolver
  ]
})
export class AppRoutingModule {}