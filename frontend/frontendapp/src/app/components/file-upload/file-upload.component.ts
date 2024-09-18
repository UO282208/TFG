import { Component, OnInit } from '@angular/core';
import { FileUploadService } from '../../services/file-upload/file-upload.service';
import { CommonModule } from '@angular/common';
import { HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-file-upload',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css']
})

export class FileUploadComponent {
  currentFile?: File;
  message = '';

  constructor(private fileUploadService: FileUploadService, private router: Router) { }

  selectFile(event: any): void {
    this.currentFile = event.target.files.item(0);
  }

  upload(): void {
    if (this.currentFile) {
      this.fileUploadService.uploadFile(this.currentFile).subscribe({
        next: (event: any) => {
          if (event instanceof HttpResponse) {
            this.message = event.body.message;
            this.router.navigate(['/list_files']);
          }
        },
        error: (err: any) => {
          console.log(err);

          if (err.error && err.error.message) {
            this.message = err.error.message;
          } else {
            this.message = 'Could not upload the file!';
          }
        },
        complete: () => {
          this.currentFile = undefined;
        },
      });
    }
  }

  goToListFilesPage(): void
  {
    this.router.navigate(['/list_files']);
  }
}
