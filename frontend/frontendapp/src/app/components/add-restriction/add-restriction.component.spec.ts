import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddRestrictionComponent } from './add-restriction.component';

describe('AddRestrictionComponent', () => {
  let component: AddRestrictionComponent;
  let fixture: ComponentFixture<AddRestrictionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddRestrictionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddRestrictionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
