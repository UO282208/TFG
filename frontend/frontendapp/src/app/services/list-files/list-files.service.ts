import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ListFilesService {
  private apiBaseUrl = 'http://localhost:8080'

  constructor(private http: HttpClient) { }

  getFiles(): Observable<any> {
    return this.http.get(`${this.apiBaseUrl}/api/files/allFiles`);
  }
}
