import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CsDetailsService } from '../../services/cs-details/cs-details.service';
import { ActivatedRoute, Router } from '@angular/router';
import { GetCsDetailsRequest } from '../../models/GetCsDetailsRequest';
import { HttpResponse } from '@angular/common/http';
import { TokenService } from '../../services/token/token.service';

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
    lastDayUploaded: undefined,
    restrictionsViolated: []
  }
  
  csId: number = 0

  currentFile?: File | null;
  message = '';

  constructor(private csDetailsService: CsDetailsService, private router: Router, private route: ActivatedRoute, private tokenService: TokenService) { }

  hasUploadedToday(): boolean{
    if (this.csDetails.lastDayUploaded) {
      const lastUploadedDate = new Date(this.csDetails.lastDayUploaded);
      const today = new Date();

      return (
          lastUploadedDate.getFullYear() === today.getFullYear() &&
          lastUploadedDate.getMonth() === today.getMonth() &&
          lastUploadedDate.getDate() === today.getDate()
      );
    }
    return false;
  }

  ngOnInit(): void {
    if (!this.tokenService.isLoggedIn) {
      this.router.navigate(['/login']);
    }
    this.currentFile = undefined;
    this.route.paramMap.subscribe(params => { this.csId = Number(params.get('csId'))});
    this.getDetails();
  } 

  getDetails(): void {
    this.csDetailsService.getConstructionSiteDetails(this.csId).subscribe(
      (data) => {
        this.csDetails = data;
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
    } else {
      this.currentFile = null;
      alert('Formato de archivo incorrecto. Porfavor seleccione una imagen (JPG, PNG, WEBP).');
    }
  }

  upload(): void {
    if (this.currentFile) {
      this.message = 'El archivo está siendo procesado, por favor espere entre 15 a 30 segundos antes de que se complete la operación.'
      this.csDetailsService.uploadFile(this.currentFile, String(this.csId)).subscribe({
        next: (event: any) => {
          if (event instanceof HttpResponse) {
            this.message = event.body.message;
          }
        },
        error: (err: any) => {
          if (err.error.sizeLimitError) {
            this.message = err.error.sizeLimitError;
          }
          else if (err.error && err.error.message) {
            this.message = "El archivo pesa más de 10MB";
          } else {
            this.message = 'No se ha podido subir el archivo';
          }
        },
        complete: () => {
          this.currentFile = null;
          this.getDetails();
        },
      });
    }
  }

  addRestriction() {
    this.router.navigate(['details', this.csId, 'addRestriction']);
  }

  goBack() {
    this.router.navigate(['/all-cs']);
  }
}
