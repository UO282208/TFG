import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { AppComponent } from './app.component';

import { FileUploadService } from './services/file-upload/file-upload.service';
import { ListFilesService } from './services/list-files/list-files.service';
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
  providers: [FileUploadService, ListFilesService],
  bootstrap: [AppComponent],
})
export class AppModule { }