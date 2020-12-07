import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ElectionService } from '../../../../services/election/election.service';
import { ElectionRequest } from '../../../../dtos/ElectionRequest';
import { ElectionResultResponse } from '../../../../dtos/ElectionResultResponse';

@Component({
  selector: 'app-election-results',
  templateUrl: './election-results.component.html',
  styleUrls: ['./election-results.component.css'],
})
export class ElectionResultsComponent implements OnInit, OnDestroy {

  name: string;
  private subRoute: any;
  private subData: any;

  constructor(private route: ActivatedRoute, private ElectionService: ElectionService) {
    this.subRoute = this.route.params.subscribe(params => {
      let request: ElectionRequest = {id: +params['id']};
      this.ElectionService.getElectionResults(request);
      this.initElectionResultsData()
    });
    
  }
  initElectionResultsData() {
    this.subData = this.route.data.subscribe(({ data }) => {
      let electionResultResponse: ElectionResultResponse = data;
      this.name = electionResultResponse.name;
    });   
  }
  
  ngOnInit() {
  }

  ngOnDestroy() {
    this.subRoute.unsubscribe();
    this.subData.unsubscribe();
  }

  get electionName() {
    return this.name;
  }
  
}
