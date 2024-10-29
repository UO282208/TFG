import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { TokenService } from './services/token/token.service';

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let tokenService: jasmine.SpyObj<TokenService>;

  beforeEach(() => {
    tokenService = jasmine.createSpyObj('TokenService', ['logout']);
    
    TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [
        { provide: TokenService, useValue: tokenService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should call logout on TokenService when logout is called', () => {
    component.logout();

    expect(tokenService.logout).toHaveBeenCalled();
  });
});
