import { Component, OnInit, OnDestroy } from '@angular/core';
import { forEach } from '@angular/router/src/utils/collection';
import { ElectionResultResponse } from '../../../../dtos/ElectionResultResponse';
import { Party } from '../../../../domains/election-result/Party';
import { Member } from '../../../../domains/election-result/Member';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-election-results-charts',
  templateUrl: './election-results-charts.component.html',
  styleUrls: ['./election-results-charts.component.css']
})
export class ElectionResultsChartsComponent implements OnDestroy {

  private pieChartLabels:string[] = [];
  private pieChartData:number[] = [];
  private pieChartType:string = 'pie';

  private parties: Party[] = [];

  private subData: any;

  public electionResultResponse: ElectionResultResponse;

  constructor(public route: ActivatedRoute) { 
    this.initElectionResultsData();
  }

  initElectionResultsData() {
    this.subData = this.route.data.subscribe(({ data }) => {
      let electionResultResponse: ElectionResultResponse = data;
      for(let party of electionResultResponse.parties) {
        this.pieChartLabels.push(party.name);
        this.pieChartData.push(party.voteCount)
      }
    });   
  }

  ngOnDestroy() {
    this.subData.unsubscribe();
  }

}
