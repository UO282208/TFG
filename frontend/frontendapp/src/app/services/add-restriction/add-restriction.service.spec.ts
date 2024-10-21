import { TestBed } from '@angular/core/testing';

import { AddRestrictionService } from './add-restriction.service';

describe('AddRestrictionService', () => {
  let service: AddRestrictionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AddRestrictionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
