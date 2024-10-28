import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AddRestrictionService } from './add-restriction.service';

describe('AddRestrictionService', () => {
  let service: AddRestrictionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(AddRestrictionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
