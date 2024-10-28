import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CsDetailsService } from './cs-details.service';


describe('CsDetailsService', () => {
  let service: CsDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(CsDetailsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
