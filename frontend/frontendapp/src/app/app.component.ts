import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FileUploadService } from './file-upload.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  standalone: true
})
export class AppComponent implements OnInit{
  message: string | undefined;
  files: string[] = [];
  selectedFile: File | null = null;

  constructor(private fileUploadService: FileUploadService) { }

  onFileChange(event: any) {
    if (event.target.files.length > 0) {
      this.selectedFile = event.target.files[0];
    }
  }

  onSubmit() {
    if (this.selectedFile) {
      this.fileUploadService.uploadFile(this.selectedFile).subscribe({
        next: () => {
          this.message = 'File uploaded successfully';
          this.fetchFiles();
        },
        error: () => {
          this.message = 'Failed to upload file';
        }
      });
    }
  }

  fetchFiles() {
    this.fileUploadService.getFiles().subscribe({
      next: (files: string[]) => {
        this.files = files;
      },
      error: () => {
        this.message = 'Failed to fetch files';
      }
    });
  }

  ngOnInit() {
    this.fetchFiles();
  }
}
