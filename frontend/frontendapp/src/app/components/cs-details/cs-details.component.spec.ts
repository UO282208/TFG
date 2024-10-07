import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CsDetailsComponent } from './cs-details.component';

describe('CsDetailsComponent', () => {
  let component: CsDetailsComponent;
  let fixture: ComponentFixture<CsDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CsDetailsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CsDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
