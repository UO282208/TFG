import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { AddRestrictionComponent } from './add-restriction.component';
import { of } from 'rxjs';
import { TokenService } from '../../services/token/token.service';

describe('AddRestrictionComponent', () => {
  let component: AddRestrictionComponent;
  let fixture: ComponentFixture<AddRestrictionComponent>;
  let mockTokenService: any;

  beforeEach(async () => {
    mockTokenService = { isLoggedIn: true };
    
    await TestBed.configureTestingModule({
      imports: [AddRestrictionComponent, HttpClientTestingModule],
      providers: [
        { provide: TokenService, useValue: mockTokenService },
        {
          provide: ActivatedRoute,
          useValue: {
            paramMap: of({ get: (key: string) => '123' }),
          },
        },
      ]
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
