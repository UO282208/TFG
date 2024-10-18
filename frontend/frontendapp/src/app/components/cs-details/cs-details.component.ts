import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CsDetailsService } from '../../services/cs-details/cs-details.service';
import { ActivatedRoute, Router } from '@angular/router';
import { GetCsDetailsRequest } from '../../models/GetCsDetailsRequest';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-cs-details',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cs-details.component.html',
  styleUrl: './cs-details.component.css'
})
export class CsDetailsComponent implements OnInit{

  csDetails: GetCsDetailsRequest = {
    numberOfTransformers: 0,
    numberOfExpansionTanks: 0,
    numberOfRadiators: 0,
    numberOfConnectionPoints: 0,
    numberOfFirewalls: 0,
    lastDayUploaded: undefined
  }
  
  csId: number = 0

  currentFile?: File | null;
  message = '';
  todayUploaded = false;

  constructor(private csDetailsService: CsDetailsService, private router: Router, private route: ActivatedRoute) { }

  hasNotUploadedToday(): boolean{
    if (this.csDetails.lastDayUploaded != undefined)
      return this.csDetails.lastDayUploaded.getDay < new Date().getDay && 
        this.csDetails.lastDayUploaded.getMonth <= new Date().getMonth &&
        this.csDetails.lastDayUploaded.getFullYear <= new Date().getFullYear;
    return true;
  }

  ngOnInit(): void {
    this.todayUploaded = !this.hasNotUploadedToday();
    this.getDetails();
  } 

  getDetails(): void {
    this.route.paramMap.subscribe(params => { this.csId = Number(params.get('csId'))});
    this.csDetailsService.getConstructionSiteDetails(this.csId).subscribe(
      (data) => {
        this.csDetails = data;
        console.log('Construction sites:', this.csDetails);
      },
      (error) => {
        console.error('Error fetching construction sites:', error);
      }
    );
  }

  selectFile(event: any): void {
    const file = event.target.files.item(0);

    const allowedMimeTypes = ['image/jpeg', 'image/png', 'image/webp'];

    if (file && allowedMimeTypes.includes(file.type)) {
      this.currentFile = file;
      console.log('File selected:', this.currentFile);
    } else {
      this.currentFile = null;
      alert('Invalid file type. Please select an image (JPG, PNG, WEBP).');
      console.error('Invalid file type selected:', file?.type);
    }
  }

  upload(): void {
    if (this.currentFile) {
      this.message = 'File is being processed, please wait around 15 seconds before data is loaded'
      this.csDetailsService.uploadFile(this.currentFile, String(this.csId)).subscribe({
        next: (event: any) => {
          if (event instanceof HttpResponse) {
            this.message = event.body.message;
            this.todayUploaded = true;
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
          this.getDetails();
          this.todayUploaded = !this.hasNotUploadedToday();
        },
      });
    }
  }
}
