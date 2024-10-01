import { Injectable } from '@angular/core';
import { RegisterRequest } from '../../models/RegisterRequest';
import { Observable } from 'rxjs';
import { HttpClient, HttpEvent } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  private apiBaseUrl = 'http://localhost:8080'

  constructor(private http: HttpClient) { }

  register(registerRequest: RegisterRequest): Observable<HttpEvent<any>> {
    return this.http.post<any>(`${this.apiBaseUrl}/api/auth/register`, registerRequest);
  }
}
