import { TestBed, inject } from '@angular/core/testing';

import { ElectionResultsResolver } from './election-results-resolver.service';

describe('ElectionResultsResolverService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ElectionResultsResolver]
    });
  });

  it('should be created', inject([ElectionResultsResolver], (service: ElectionResultsResolver) => {
    expect(service).toBeTruthy();
  }));
});
