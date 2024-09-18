import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { ListFilesService } from '../../services/list-files/list-files.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-list-files',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './list-files.component.html',
  styleUrl: './list-files.component.css'
})
export class ListFilesComponent implements OnInit{

  fileInfos?: Observable<any>;

  constructor(private listFilesService: ListFilesService, private router: Router) { }

  goToFileUploadPage(): void{
    this.router.navigate(['']);
  }

  ngOnInit(): void {
    this.fileInfos = this.listFilesService.getFiles();
  }
}
