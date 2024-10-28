import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AddCsComponent } from './add-cs.component';

describe('AddCsComponent', () => {
  let component: AddCsComponent;
  let fixture: ComponentFixture<AddCsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddCsComponent, HttpClientTestingModule]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddCsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
