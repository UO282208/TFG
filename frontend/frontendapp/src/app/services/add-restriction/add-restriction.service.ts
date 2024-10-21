import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { NewRestrictionRequest } from '../../models/NewRestrictionRequest';

@Injectable({
  providedIn: 'root'
})
export class AddRestrictionService {
  
  private apiBaseUrl = 'http://localhost:8080'

  constructor(private http: HttpClient) { }

  addRestriction(csdId: number, newRestrictionRequest: NewRestrictionRequest): Observable<any> {
    return this.http.post(`${this.apiBaseUrl}/api/constructionSite/details/${csdId}/addRestriction`, newRestrictionRequest);
  }
}
