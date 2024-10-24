import { Component, OnInit } from '@angular/core';
import { ModifyConstructionSiteRequest } from '../../models/ModifyConstructionSiteRequest';
import { ModifyCsService } from '../../services/modify-cs/modify-cs.service';
import { ActivatedRoute, Router } from '@angular/router';
import { TokenService } from '../../services/token/token.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-modify-cs',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './modify-cs.component.html',
  styleUrl: './modify-cs.component.css'
})
export class ModifyCsComponent implements OnInit {

  errorMsg: Array<String> = [];
  modifyConstructionSiteRequest: ModifyConstructionSiteRequest = {name: '', numOfWorkers: 0};
  csId: number = 0

  constructor (private modifyCsService: ModifyCsService, private router: Router, private route: ActivatedRoute, private tokenService: TokenService ) { }

  ngOnInit(): void {
    if (!this.tokenService.isLoggedIn) {
      this.router.navigate(['/login']);
    }
    this.route.paramMap.subscribe(params => { this.csId = Number(params.get('csId'))});
  }

  modify(): void {
    this.errorMsg = [];
    this.modifyCsService.modifyConstructionSite(this.csId, this.modifyConstructionSiteRequest).subscribe({
      next: (response) => {
        this.router.navigate(['/all-cs']);
      },
      error: (error) => {
        this.errorMsg = error.error.validationErrors;
      }});
  }

  cancel(){
    this.router.navigate(['/all-cs']);
  }

}
