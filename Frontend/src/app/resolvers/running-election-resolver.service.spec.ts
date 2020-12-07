import { TestBed, inject } from '@angular/core/testing';

import { RunningElectionResolver } from './running-election-resolver.service';

describe('ElectionResolverService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [RunningElectionResolver]
    });
  });

  it('should be created', inject([RunningElectionResolver], (service: RunningElectionResolver) => {
    expect(service).toBeTruthy();
  }));
});
