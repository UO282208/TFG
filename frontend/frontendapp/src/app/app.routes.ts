import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { ListUserCsComponent } from './components/list-user-cs/list-user-cs.component';
import { AddCsComponent } from './components/add-cs/add-cs.component';
import { CsDetailsComponent } from './components/cs-details/cs-details.component';
import { AddRestrictionComponent } from './components/add-restriction/add-restriction.component';
import { ModifyCsComponent } from './components/modify-cs/modify-cs.component';

export const routes: Routes = [
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
    {
        path: 'details/:csId/addRestriction', component: AddRestrictionComponent
    },
    {
        path: 'modify-cs/:csId', component: ModifyCsComponent
    },
    ];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {}