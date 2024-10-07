import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root'
})
export class ListUserCsService {

  private apiBaseUrl = 'http://localhost:8080'

  constructor(private http: HttpClient) { }
  
  getConstructionSites(token: string): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    
    return this.http.get(`${this.apiBaseUrl}/api/constructionSite/allConstructionSites`, { headers });
  }

  deleteConstructionSite(csId: number): Observable<any> {
    const params = new HttpParams().set('id', csId);
    return this.http.delete(`${this.apiBaseUrl}/api/constructionSite/deleteConstructionSite`, { params });
  }

}
