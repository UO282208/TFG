import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { ListFilesComponent } from './components/list-files/list-files.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { ListUserCsComponent } from './components/list-user-cs/list-user-cs.component';
import { AddCsComponent } from './components/add-cs/add-cs.component';
import { CsDetailsComponent } from './components/cs-details/cs-details.component';

export const routes: Routes = [
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
    {
        path: 'details/:csId', component: CsDetailsComponent
    },
    ];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {}