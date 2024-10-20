import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ListUserCsService } from '../../services/list-user-cs/list-user-cs.service';
import { Router } from '@angular/router';
import { TokenService } from '../../services/token/token.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-list-user-cs',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './list-user-cs.component.html',
  styleUrl: './list-user-cs.component.css'
})
export class ListUserCsComponent implements OnInit {

  public constructionSites: any[] = [];

  constructor(private listUserCsService: ListUserCsService, private router: Router, private tokenService: TokenService) { }

  ngOnInit(): void {
    if (!this.tokenService.isLoggedIn) {
      this.router.navigate(['/login']);
    }
    this.fetchConstructionSites();
  }

  fetchConstructionSites() {
    this.listUserCsService.getConstructionSites(this.tokenService.token).subscribe(
      (data) => {
        this.constructionSites = data;
        console.log('Construction sites:', this.constructionSites);
      },
      (error) => {
        console.error('Error fetching construction sites:', error);
      }
    );
  }

  add() {
    this.router.navigate(['/add-cs']);
  }

  delete(csId: number){
    const confirmed = confirm('Are you sure you want to delete this construction site?');

    if (confirmed) {
      this.listUserCsService.deleteConstructionSite(csId).subscribe(
        response => {
          console.log('Delete successful', response);
          window.location.reload();
        },
        error => {
          console.error('Delete failed', error);
        });
    }
  }

  viewDetails(csId: number){
    this.router.navigate(['/details', csId]);
  }
}
