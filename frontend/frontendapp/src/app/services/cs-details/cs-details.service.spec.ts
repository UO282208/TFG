import { TestBed } from '@angular/core/testing';
import { CsDetailsService } from './cs-details.service';


describe('CsDetailsService', () => {
  let service: CsDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CsDetailsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
