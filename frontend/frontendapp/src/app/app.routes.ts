import { RouterModule, Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { NgModule } from '@angular/core';
import { ListFilesComponent } from './components/list-files/list-files.component';
import { FileUploadComponent } from './components/file-upload/file-upload.component';

export const routes: Routes = [
    {
        path: '', component: FileUploadComponent
    },
    {
        path: 'list_files', component: ListFilesComponent
    }
    ];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {}