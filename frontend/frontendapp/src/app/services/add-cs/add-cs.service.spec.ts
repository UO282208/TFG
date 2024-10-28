import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AddCsService } from './add-cs.service';

describe('AddCsService', () => {
  let service: AddCsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(AddCsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
