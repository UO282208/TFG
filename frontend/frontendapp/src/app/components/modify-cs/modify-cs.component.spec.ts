import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ModifyCsComponent } from './modify-cs.component';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { TokenService } from '../../services/token/token.service';

describe('ModifyCsComponent', () => {
  let component: ModifyCsComponent;
  let fixture: ComponentFixture<ModifyCsComponent>;
  let mockTokenService: any;

  beforeEach(async () => {
    mockTokenService = { isLoggedIn: true };

    await TestBed.configureTestingModule({
      imports: [ModifyCsComponent, HttpClientTestingModule],
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
    
    fixture = TestBed.createComponent(ModifyCsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModifyCsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
