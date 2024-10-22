import { Component } from '@angular/core';
import { LoginService } from '../../services/login/login.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthenticationRequest } from '../../models/AuthenticationRequest';
import { FormsModule } from '@angular/forms';
import { TokenService } from '../../services/token/token.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})

export class LoginComponent {

  errorMsg: Array<String> = [];
  authRequest: AuthenticationRequest = {email: '', password: ''};

  constructor (private loginService: LoginService, private router: Router, private tokenService: TokenService ) { }

  login() {
    this.errorMsg = [];
    this.loginService.login(this.authRequest).subscribe({
      next: (response) => {
        this.tokenService.token = response.token as string;
        this.router.navigate(['/all-cs']);
      },
      error: (error) => {
        if (error.error.validationErrors){
          this.errorMsg = error.error.validationErrors;
        } else {
          this.errorMsg.push(error.error.error);
        }
        
      }});
  }

  register() {
    this.router.navigate(['/register']);
  }

  cancel(){
    this.router.navigate(['']);
  }
}
