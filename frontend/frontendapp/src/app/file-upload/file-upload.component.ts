import { Component, OnInit } from '@angular/core';
import { FileUploadService } from '../file-upload.service';

@Component({
  selector: 'file-upload-component',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css']
})

export class FileUploadComponent implements OnInit {
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
