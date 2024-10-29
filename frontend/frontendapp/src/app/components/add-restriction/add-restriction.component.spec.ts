import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { AddRestrictionComponent } from './add-restriction.component';
import { of } from 'rxjs';
import { TokenService } from '../../services/token/token.service';
import { HttpClientModule } from '@angular/common/http';

describe('AddRestrictionComponent', () => {
  let component: AddRestrictionComponent;
  let fixture: ComponentFixture<AddRestrictionComponent>;
  let router: jasmine.SpyObj<Router>;
  let tokenService: jasmine.SpyObj<TokenService>;

  beforeEach(() => {
    router = jasmine.createSpyObj('Router', ['navigate']);
    tokenService = jasmine.createSpyObj('TokenService', ['isLoggedIn'], { isLoggedIn: true });

    TestBed.configureTestingModule({
      imports: [AddRestrictionComponent, HttpClientModule],
      providers: [
        { provide: Router, useValue: router },
        { provide: TokenService, useValue: tokenService },
        { provide: ActivatedRoute, useValue: { paramMap: of({ get: () => '1' }) } }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AddRestrictionComponent);
    component = fixture.componentInstance;
  });

  it('should alert when end date is before or equal to start date', () => {
    spyOn(window, 'alert');

    component.NewRestrictionRequest.startDate = new Date('2024-10-29T01:00:00');
    component.NewRestrictionRequest.endDate = new Date('2024-10-28T01:00:00');

    component.onSubmit();

    expect(window.alert).toHaveBeenCalledWith("La fecha de fin debe ser después de la fecha de inicio.");
  });

  it('should not alert when end date is after start date', () => {
    spyOn(window, 'alert');

    component.NewRestrictionRequest.startDate = new Date('2024-10-28T01:00:00');
    component.NewRestrictionRequest.endDate = new Date('2024-10-29T01:00:00');

    component.onSubmit();

    expect(window.alert).not.toHaveBeenCalled();
  });
});