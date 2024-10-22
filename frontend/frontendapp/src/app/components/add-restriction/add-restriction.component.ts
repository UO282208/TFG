import { Component, OnInit } from '@angular/core';
import { AddRestrictionService } from '../../services/add-restriction/add-restriction.service';
import { ActivatedRoute, Router } from '@angular/router';
import { TokenService } from '../../services/token/token.service';
import { NewRestrictionRequest } from '../../models/NewRestrictionRequest';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-add-restriction',
  standalone: true,
  imports: [[CommonModule, FormsModule],],
  templateUrl: './add-restriction.component.html',
  styleUrl: './add-restriction.component.css'
})
export class AddRestrictionComponent implements OnInit {

  NewRestrictionRequest: NewRestrictionRequest = {
    transformers: 0,   
    expansionTanks: 0,
    radiators: 0, 
    connectionPoints: 0,   
    firewalls: 0,  
    startDate: new Date(),
    endDate: new Date(new Date().setDate(new Date().getDate() + 1)),
    shouldAppear: true};

  csId: number = 0;
  errorMsg: Array<String> = [];

  constructor(private addRestrictionService: AddRestrictionService, private router: Router, private route: ActivatedRoute, private tokenService: TokenService) { }

  ngOnInit(): void {
    if (!this.tokenService.isLoggedIn) {
      this.router.navigate(['/login']);
    }
    this.route.paramMap.subscribe(params => { this.csId = Number(params.get('csId'))});
  }

  onStartDateChange(event: Event) {
    const input = event.target as HTMLInputElement;
    this.NewRestrictionRequest.startDate = new Date(input.value + 'T02:00:00'); 
  }

  onEndDateChange(event: Event) {
    const input = event.target as HTMLInputElement;
    this.NewRestrictionRequest.endDate = new Date(input.value + 'T02:00:00'); 
  }

  onSubmit(): void {
    if (new Date(this.NewRestrictionRequest.endDate) <= new Date(this.NewRestrictionRequest.startDate)) {
      alert("End date must be after the start date.");
      return;
    }

    this.addRestrictionService.addRestriction(this.csId, this.NewRestrictionRequest).subscribe(
      (response) => {
        console.log('Restriction added succesfully', response);
        this.router.navigate(['/details', this.csId]);
      },
      (error) => {
        this.errorMsg = error.error.validationErrors;
      }
    );
  }

  cancel(){
    this.router.navigate(['/details', this.csId]);
  }
}
