import { TestBed, inject } from '@angular/core/testing';

import { ElectionCreateResolver} from './election-create-resolver.service';

describe('ElectionCreateResolverService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ElectionCreateResolver]
    });
  });

  it('should be created', inject([ElectionCreateResolver], (service: ElectionCreateResolver) => {
    expect(service).toBeTruthy();
  }));
});
