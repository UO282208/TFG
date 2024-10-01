import { Injectable } from '@angular/core';
import { AuthenticationRequest } from '../../models/AuthenticationRequest';
import { HttpClient, HttpEvent, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private apiBaseUrl = 'http://localhost:8080'

  constructor(private http: HttpClient) {}

  login(authRequest: AuthenticationRequest): Observable<{ token: string }> {
    return this.http.post<any>(`${this.apiBaseUrl}/api/auth/authenticate`, authRequest);
  }
}
