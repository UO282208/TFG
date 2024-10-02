import { RouterModule, Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { NgModule } from '@angular/core';
import { ListFilesComponent } from './components/list-files/list-files.component';
import { FileUploadComponent } from './components/file-upload/file-upload.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { ListUserCsComponent } from './components/list-user-cs/list-user-cs.component';
import { AddCsComponent } from './components/add-cs/add-cs.component';

export const routes: Routes = [
    {
        path: 'upload_files', component: FileUploadComponent
    },
    {
        path: 'list_files', component: ListFilesComponent
    },
    {
        path: 'login', component: LoginComponent
    },
    {
        path: 'register', component: RegisterComponent
    },
    {
        path: 'all-cs', component: ListUserCsComponent
    },
    {
        path: 'add-cs', component: AddCsComponent
    },
    ];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {}