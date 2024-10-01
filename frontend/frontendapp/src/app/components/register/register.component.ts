import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RegisterRequest } from '../../models/RegisterRequest';
import { RegisterService } from '../../services/register/register.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  errorMsg: Array<String> = [];
  registerRequest: RegisterRequest = {name: '', email: '', password: ''};

  constructor (private registerService: RegisterService, private router: Router) { }

  register() {
    this.errorMsg = [];
    this.registerService.register(this.registerRequest).subscribe({
      next: (response) => {
        this.router.navigate(['/login']);
      },
      error: (error) => {
        console.log('Login failed', error);
        this.errorMsg = error.error;
      }});
  }

  login() {
    this.router.navigate(['/login']);
  }
}
