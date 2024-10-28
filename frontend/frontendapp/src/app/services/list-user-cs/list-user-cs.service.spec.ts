import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ListUserCsService } from './list-user-cs.service';


describe('ListUserCsService', () => {
  let service: ListUserCsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]});
    service = TestBed.inject(ListUserCsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
