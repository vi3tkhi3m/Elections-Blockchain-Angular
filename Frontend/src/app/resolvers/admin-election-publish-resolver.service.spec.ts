import { TestBed, inject } from '@angular/core/testing';

import { AdminElectionPublishResolver } from './admin-election-publish-resolver.service';

describe('AdminElectionPublishResolver', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AdminElectionPublishResolver]
    });
  });

  it('should be created', inject([AdminElectionPublishResolver], (service: AdminElectionPublishResolver) => {
    expect(service).toBeTruthy();
  }));
});
