import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { NewConstructionSiteRequest } from '../../models/NewConstructionSiteRequest';

@Injectable({
  providedIn: 'root'
})
export class AddCsService {

  private apiBaseUrl = 'http://localhost:8080'

  constructor(private http: HttpClient) { }

  addConstructionSite(newConstructionSiteRequest: NewConstructionSiteRequest): Observable<any> {
    return this.http.post(`${this.apiBaseUrl}/api/constructionSite/addConstructionSite`, newConstructionSiteRequest);
  }
}
