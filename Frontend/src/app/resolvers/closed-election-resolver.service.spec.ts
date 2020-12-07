import { TestBed, inject } from '@angular/core/testing';

import { ClosedElectionResolverService } from './closed-election-resolver.service';

describe('ClosedElectionResolverService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ClosedElectionResolverService]
    });
  });

  it('should be created', inject([ClosedElectionResolverService], (service: ClosedElectionResolverService) => {
    expect(service).toBeTruthy();
  }));
});
