import { TestBed } from '@angular/core/testing';
import { ListUserCsService } from './list-user-cs.service';


describe('ListUserCsService', () => {
  let service: ListUserCsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ListUserCsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
