import { CommonModule } from '@angular/common';
import { Component, OnInit} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TokenService } from './services/token/token.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit{
  
  constructor(public tokenService: TokenService) {}

  ngOnInit(): void {
    this.tokenService.token = '';
  }

  logout() {
    this.tokenService.logout();
  }
}
