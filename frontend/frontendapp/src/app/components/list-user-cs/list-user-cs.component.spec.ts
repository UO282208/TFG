import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListUserCsComponent } from './list-user-cs.component';

describe('ListUserCsComponent', () => {
  let component: ListUserCsComponent;
  let fixture: ComponentFixture<ListUserCsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListUserCsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ListUserCsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
