import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NewConstructionSiteRequest } from '../../models/NewConstructionSiteRequest';
import { AddCsService } from '../../services/add-cs/add-cs.service';
import { Router } from '@angular/router';
import { TokenService } from '../../services/token/token.service';

@Component({
  selector: 'app-add-cs',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-cs.component.html',
  styleUrl: './add-cs.component.css'
})
export class AddCsComponent {

  errorMsg: Array<String> = [];
  newConstructionSiteRequest: NewConstructionSiteRequest = {token: '', name: '', numOfWorkers: 0};

  constructor (private addCsService: AddCsService, private router: Router, private tokenService: TokenService ) { }

  add(): void {
    this.errorMsg = [];
    this.newConstructionSiteRequest.token = this.tokenService.token;
    this.addCsService.addConstructionSite(this.newConstructionSiteRequest).subscribe({
      next: (response) => {
        console.log('Construction site added succesfully', response);
        this.router.navigate(['/all-cs']);
      },
      error: (error) => {
        this.errorMsg = error.error.validationErrors;
      }});
    }
}
