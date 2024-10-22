import { TestBed } from '@angular/core/testing';

import { ModifyCsService } from './modify-cs.service';

describe('ModifyCsService', () => {
  let service: ModifyCsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ModifyCsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
