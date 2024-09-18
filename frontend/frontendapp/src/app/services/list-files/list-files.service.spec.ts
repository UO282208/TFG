import { TestBed } from '@angular/core/testing';

import { ListFilesService } from './list-files.service';

describe('ListFilesService', () => {
  let service: ListFilesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ListFilesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
