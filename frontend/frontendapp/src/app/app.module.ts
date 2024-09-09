import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { AppComponent } from './app.component';

import { FileUploadService } from './file-upload.service';

@NgModule({
  declarations: [
    AppComponent,   
  ],
  imports: [
    BrowserModule,     
    HttpClientModule,  
    FormsModule,    
    CommonModule,  
  ],
  providers: [FileUploadService],
  bootstrap: [AppComponent],
})
export class AppModule { }