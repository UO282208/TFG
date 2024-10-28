import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ModifyCsService } from './modify-cs.service';

describe('ModifyCsService', () => {
  let service: ModifyCsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(ModifyCsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
