import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ElectionResultResponse } from '../../../../dtos/ElectionResultResponse';

@Component({
  selector: 'app-election-results-members',
  templateUrl: './election-results-members.component.html',
  styleUrls: ['./election-results-members.component.css']
})
export class ElectionResultsMembersComponent implements OnInit, OnDestroy {

  private subData: any;

  public rows: Array<any> = [];
  public columns: Array<any> = [
    { title: 'Naam', name: 'name', filtering: { filterString: '', placeholder: 'Filter op naam' } },
    { title: 'Partij', name: 'party', filtering: { filterString: '', placeholder: 'Filter op partij' } },
    { title: 'Aantal stemmen', name: 'voteCount' }

  ];

  public page: number = 1;
  public itemsPerPage: number = 10;
  public maxSize: number = 5;
  public numPages: number = 1;
  public length: number = 0;

  public config: any = {
    paging: true,
    sorting: { columns: this.columns },
    filtering: { filterString: '' },
    className: ['table-striped', 'table-bordered'],
  };

  public constructor(private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.initElectionResultsData();
    this.onChangeTable(this.config);
    this.length = this.data.length;
  }

  ngOnDestroy() {
    this.subData.unsubscribe();
  }

  initElectionResultsData() {
    this.subData = this.route.data.subscribe(({ data }) => {
      let electionResultResponse: ElectionResultResponse = data;
      for (let member of electionResultResponse.members) {
        for (let party of electionResultResponse.parties) {
          if (party.id == member.partyId) {
            this.data.push({ 'name': member.firstname + ' ' + member.lastname, 'party': party.name, 'voteCount': member.voteCount })
          }
        }
      }
    });
  }

  private data: Array<any> = [];

  public changePage(page: any, data: Array<any> = this.data): Array<any> {
    let start = (page.page - 1) * page.itemsPerPage;
    let end = page.itemsPerPage > -1 ? (start + page.itemsPerPage) : data.length;
    return data.slice(start, end);
  }

  public changeSort(data: any, config: any): any {
    if (!config.sorting) {
      return data;
    }

    let columns = this.config.sorting.columns || [];
    let columnName: string = void 0;
    let sort: string = void 0;

    for (let i = 0; i < columns.length; i++) {
      if (columns[i].sort !== '' && columns[i].sort !== false) {
        columnName = columns[i].name;
        sort = columns[i].sort;
      }
    }

    if (!columnName) {
      return data;
    }

    // simple sorting
    return data.sort((previous: any, current: any) => {
      if (previous[columnName] > current[columnName]) {
        return sort === 'desc' ? -1 : 1;
      } else if (previous[columnName] < current[columnName]) {
        return sort === 'asc' ? -1 : 1;
      }
      return 0;
    });
  }

  public changeFilter(data: any, config: any): any {
    let filteredData: Array<any> = data;
    this.columns.forEach((column: any) => {
      if (column.filtering) {
        filteredData = filteredData.filter((item: any) => {
          return item[column.name].match(column.filtering.filterString);
        });
      }
    });

    if (!config.filtering) {
      return filteredData;
    }

    if (config.filtering.columnName) {
      return filteredData.filter((item: any) =>
        item[config.filtering.columnName].match(this.config.filtering.filterString));
    }

    let tempArray: Array<any> = [];
    filteredData.forEach((item: any) => {
      let flag = false;
      this.columns.forEach((column: any) => {
        if (item[column.name].toString().match(this.config.filtering.filterString)) {
          flag = true;
        }
      });
      if (flag) {
        tempArray.push(item);
      }
    });
    filteredData = tempArray;

    return filteredData;
  }

  public onChangeTable(config: any, page: any = { page: this.page, itemsPerPage: this.itemsPerPage }): any {
    if (config.filtering) {
      Object.assign(this.config.filtering, config.filtering);
    }

    if (config.sorting) {
      Object.assign(this.config.sorting, config.sorting);
    }

    let filteredData = this.changeFilter(this.data, this.config);
    let sortedData = this.changeSort(filteredData, this.config);
    this.rows = page && config.paging ? this.changePage(page, sortedData) : sortedData;
    this.length = sortedData.length;
  }

}
