import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CsDetailsComponent } from './cs-details.component';
import { CsDetailsService } from '../../services/cs-details/cs-details.service';
import { TokenService } from '../../services/token/token.service';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { of, throwError } from 'rxjs';
import { HttpEvent, HttpResponse } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

describe('CsDetailsComponent', () => {
  let component: CsDetailsComponent;
  let fixture: ComponentFixture<CsDetailsComponent>;
  let csDetailsServiceMock: jasmine.SpyObj<CsDetailsService>;
  let tokenServiceMock: jasmine.SpyObj<TokenService>;
  let routerMock: jasmine.SpyObj<Router>;
  let activatedRouteMock: any;

  beforeEach(() => {
    const csDetailsSpy = jasmine.createSpyObj('CsDetailsService', ['getConstructionSiteDetails', 'uploadFile']);
    const tokenSpy = jasmine.createSpyObj('TokenService', ['isLoggedIn']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    activatedRouteMock = {
      paramMap: of({ get: (key: string) => '1' })
    };

    TestBed.configureTestingModule({
      imports: [
        CommonModule,
        FormsModule,
        CsDetailsComponent
      ],
      providers: [
        { provide: CsDetailsService, useValue: csDetailsSpy },
        { provide: TokenService, useValue: tokenSpy },
        { provide: Router, useValue: routerSpy },
        { provide: ActivatedRoute, useValue: activatedRouteMock }
      ]
    }).compileComponents();

    csDetailsServiceMock = TestBed.inject(CsDetailsService) as jasmine.SpyObj<CsDetailsService>;
    tokenServiceMock = TestBed.inject(TokenService) as jasmine.SpyObj<TokenService>;
    routerMock = TestBed.inject(Router) as jasmine.SpyObj<Router>;
    
    fixture = TestBed.createComponent(CsDetailsComponent);
    component = fixture.componentInstance;

    csDetailsServiceMock.getConstructionSiteDetails.and.returnValue(of({
      numberOfTransformers: 1,
      numberOfExpansionTanks: 1,
      numberOfRadiators: 1,
      numberOfConnectionPoints: 1,
      numberOfFirewalls: 1,
      lastDayUploaded: new Date().toISOString(),
      restrictionsViolated: []
    }));
  });

  describe('hasUploadedToday', () => {
    it('verdadero si ha subido archivo hoy', () => {
      component.csDetails.lastDayUploaded = new Date();
      expect(component.hasUploadedToday()).toBeTrue();
    });

    it('falso si no ha subido archivo hoy', () => {
      component.csDetails.lastDayUploaded = new Date(Date.now() - 86400000);
      expect(component.hasUploadedToday()).toBeFalse();
    });
  });


  describe('getDetails', () => {
    it('devuelve los detalles adicionales correctamente', () => {
      const mockDetails = { 
        numberOfTransformers: 1, 
        numberOfExpansionTanks: 1,
        numberOfRadiators: 1,
        numberOfConnectionPoints: 1,
        numberOfFirewalls: 1,
        lastDayUploaded: new Date(),
        restrictionsViolated: [] };
      csDetailsServiceMock.getConstructionSiteDetails.and.returnValue(of(mockDetails));
      
      component.getDetails();
      expect(component.csDetails).toEqual(mockDetails);
    });
  });

  describe('selectFile', () => {
    it('selecciona el archivo si es válido', () => {
        const file = new File([''], 'test.png', { type: 'image/png' });
        const event = { target: { files: [file] } };
        
        component.selectFile(event);
        expect(component.currentFile).toBe(file);
    });

    it('no selecciona el archivo si es no válido', () => {
        const file = new File([''], 'test.txt', { type: 'text/plain' });
        const event = { target: { files: [file] } };
        
        spyOn(window, 'alert'); 
        component.selectFile(event);
        expect(component.currentFile).toBeNull();
        expect(window.alert).toHaveBeenCalledWith('Formato de archivo incorrecto. Porfavor seleccione una imagen (JPG, PNG, WEBP).');
    });
});

  describe('upload', () => {
    it('procesa el archivo correctamente', () => {
      const mockResponse = { message: 'Upload successful' };
      component.currentFile = new File([''], 'test.png', { type: 'image/png' });

      csDetailsServiceMock.uploadFile.and.returnValue(
          of(new HttpResponse({ body: mockResponse }))
      );

      component.upload();

      expect(component.message).toBe(mockResponse.message);
    });

    it('maneja errores de tamaño máximo', () => {
      const mockError = { error: { sizeLimitError: 'File size limit exceeded' } };
      component.currentFile = new File([''], 'test.png', { type: 'image/png' });
      csDetailsServiceMock.uploadFile.and.returnValue(throwError(mockError));
      
      component.upload();
      expect(component.message).toBe('File size limit exceeded');
    });

    it('resetea el archivo seleccionado y obtiene los detalles adicionales tras la subida', () => {
      const mockResponse = { message: 'Upload successful' };
      component.currentFile = new File([''], 'test.png', { type: 'image/png' });
      csDetailsServiceMock.uploadFile.and.returnValue(of(new HttpResponse({ body: mockResponse })));
      spyOn(component, 'getDetails');

      component.upload();
      expect(component.currentFile).toBeNull();
      expect(component.getDetails).toHaveBeenCalled();
    });
  });

  it('deshabilita el botón de subida si hasUploadedToday() devuelve verdadero', () => {
    spyOn(component, 'hasUploadedToday').and.returnValue(true);
    component.currentFile = new File([''], 'test.png', { type: 'image/png' });
    fixture.detectChanges();

    const button = fixture.nativeElement.querySelector('[data-testid="upload-button"]') as HTMLButtonElement;
    expect(button.disabled).toBeTrue();
  });

  it('deshabilita el botón de subida si currentFile es undefined', () => {
    spyOn(component, 'hasUploadedToday').and.returnValue(false);
    component.currentFile = undefined;
    fixture.detectChanges();

    const button = fixture.nativeElement.querySelector('[data-testid="upload-button"]') as HTMLButtonElement;
    expect(button.disabled).toBeTrue();
  });

  it('habilita el botón de subida si asUploadedToday() devuelve falso y hay un archivo', () => {
    spyOn(component, 'hasUploadedToday').and.returnValue(false);
    component.currentFile = new File([''], 'test.png', { type: 'image/png' });

    const button = fixture.nativeElement.querySelector('[data-testid="upload-button"]') as HTMLButtonElement;
    expect(button.disabled).toBeFalse();
  });
});