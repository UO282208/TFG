import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { ModifyConstructionSiteRequest } from '../../models/ModifyConstructionSiteRequest';

@Injectable({
  providedIn: 'root'
})
export class ModifyCsService {

  private apiBaseUrl = 'http://localhost:8080'

  constructor(private http: HttpClient) { }

  modifyConstructionSite(csId: number, modifyConstructionSiteRequest: ModifyConstructionSiteRequest): Observable<any> {
    return this.http.post(`${this.apiBaseUrl}/api/constructionSite/modifyConstructionSite/${csId}`, modifyConstructionSiteRequest);
  }
}
