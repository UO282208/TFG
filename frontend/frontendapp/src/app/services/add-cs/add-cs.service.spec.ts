import { TestBed } from '@angular/core/testing';

import { AddCsService } from './add-cs.service';

describe('AddCsService', () => {
  let service: AddCsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AddCsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
