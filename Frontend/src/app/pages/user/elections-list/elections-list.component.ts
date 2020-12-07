import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ElectionService } from '../../../services/election/election.service';
import { ElectionListResponse } from '../../../dtos/electionListResponse';
import { Election } from '../../../domains/election-list/Election';

@Component({
  selector: 'app-elections-list',
  templateUrl: './elections-list.component.html',
  styleUrls: ['./elections-list.component.css']
})
export class ElectionsListComponent implements OnInit, OnDestroy {

  private subData: any;

  closedElections: Election[] = [];

  constructor(private route: ActivatedRoute, private electionService: ElectionService, private router: Router) { }

  ngOnInit() {
    this.initRuningElections();
  }

  ngOnDestroy() {
    this.subData.unsubscribe();
  }

  initRuningElections() {
    this.subData = this.route.data.subscribe(({ data }) => {
      let electionListResponse: ElectionListResponse = data;
      for(let el of electionListResponse.elections) {
        this.closedElections.push(el);
      }
    });   
  }

  goToElectionResults(id) {
    this.router.navigate(['/election-result', id]);
  }

}
