import { HttpClient, HttpEvent, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root'
})
export class CsDetailsService {

  private apiBaseUrl = 'http://localhost:8080'

  constructor(private http: HttpClient) { }

  getConstructionSiteDetails(csId: number): Observable<any> {
    return this.http.get(`${this.apiBaseUrl}/api/constructionSite/details/${csId}`);
  }

  uploadFile(file: File, csId: string): Observable<HttpEvent<any>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    formData.append('csId', csId);
    const req = new HttpRequest('POST', `${this.apiBaseUrl}/api/files/uploadFile`, formData, {
      responseType: 'text',
    });

    return this.http.request(req);
  }
}
