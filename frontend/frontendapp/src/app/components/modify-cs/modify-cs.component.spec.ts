import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModifyCsComponent } from './modify-cs.component';

describe('ModifyCsComponent', () => {
  let component: ModifyCsComponent;
  let fixture: ComponentFixture<ModifyCsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModifyCsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ModifyCsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
