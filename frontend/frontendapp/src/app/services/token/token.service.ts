import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  set token(token: string) {
    localStorage.setItem('token', token);
  }

  get token() {
    return localStorage.getItem('token') as string;
  }

  get userName(): string | null {
    const token = this.token;
    if (token) {
      const decodedToken = jwtDecode<any>(token);
      return decodedToken?.sub || null;
    }
    return null;
  }

  get isLoggedIn(): boolean {
    return !!this.token; 
  }

  logout() {
    localStorage.removeItem('token');
  }
}
