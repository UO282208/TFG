import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileUploadService {
  private apiBaseUrl = 'http://localhost:8080'
  private uploadUrl = '/api/uploadFile';
  private filesUrl = '/api/allFiles';

  constructor(private http: HttpClient) { }

  uploadFile(file: File): Observable<any> {
    const formData = new FormData();
    formData.append(file.name, file);
    return this.http.post(this.apiBaseUrl + this.uploadUrl, formData);
  }

  getFiles(): Observable<string[]> {
    return this.http.get<string[]>(this.apiBaseUrl + this.filesUrl);
  }
}